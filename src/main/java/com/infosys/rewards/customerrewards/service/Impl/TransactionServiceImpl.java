package com.infosys.rewards.customerrewards.service.Impl;

import com.infosys.rewards.customerrewards.dto.request.TransactionRequest;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.entity.Transaction;
import com.infosys.rewards.customerrewards.exception.CustomerNotFoundException;
import com.infosys.rewards.customerrewards.exception.TransactionNotFoundException;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.repository.TransactionRepository;
import com.infosys.rewards.customerrewards.service.TransactionService;
import com.infosys.rewards.customerrewards.util.RewardCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardCalculator rewardCalculator;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponse addTransaction(Long customerId, TransactionRequest request) {
        log.info("Adding transaction to customer : {}" , customerId);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            log.error("Customer with id: {} not found", customerId);
            return new CustomerNotFoundException("Customer not found with id: " + customerId);
        });
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setCustomer(customer);

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionResponse response = modelMapper.map(savedTransaction, TransactionResponse.class);
        response.setRewardPoints(rewardCalculator.calculateReward(savedTransaction.getAmount()));
        return response;
    }

    @Override
    public List<TransactionResponse> getTransactionsByCustomerId(
            Long customerId) {

        log.info("Fetching transactions for customerId : {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + customerId));

        List<Transaction> transactions =
                transactionRepository.findByCustomerCustomerId(customerId);

        if (transactions.isEmpty()) {

            throw new TransactionNotFoundException(
                    "No transactions found for customer id : "
                            + customerId);
        }

        return transactions.stream()
                .map(transaction -> {

                    TransactionResponse response =
                            modelMapper.map(
                                    transaction,
                                    TransactionResponse.class);

                    response.setRewardPoints(
                            rewardCalculator.calculateReward(
                                    transaction.getAmount()));

                    return response;

                })
                .toList();
    }
}
