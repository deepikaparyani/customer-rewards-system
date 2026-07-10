package com.infosys.rewards.customerrewards.service.Impl;

import com.infosys.rewards.customerrewards.dto.response.MonthlyRewardResponse;
import com.infosys.rewards.customerrewards.dto.response.RewardResponse;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.entity.Transaction;
import com.infosys.rewards.customerrewards.exception.CustomerNotFoundException;
import com.infosys.rewards.customerrewards.exception.InvalidDateRangeException;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.repository.TransactionRepository;
import com.infosys.rewards.customerrewards.service.RewardService;
import com.infosys.rewards.customerrewards.util.RewardCalculator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Tag(
        name = "Customer APIs",
        description = "APIs for managing customers"
)
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardCalculator rewardCalculator;

    @Override
    public RewardResponse calculateRewards(Long customerId,
                                           LocalDate fromDate,
                                           LocalDate toDate) {
        log.info("Calculating rewards for customer with id {} between {} and {}", customerId,fromDate,toDate);

        if (fromDate == null || toDate == null) {
            throw new InvalidDateRangeException("From and To date are required");
        }
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDateRangeException("From date cannot be after To date");
        }

        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer with id " + customerId + " does not exist"));

        List<Transaction> transactions = transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customerId,fromDate,toDate);

        List<TransactionResponse> transactionResponses = transactions.stream().map(transaction -> {
            Long reward = rewardCalculator.calculateReward(transaction.getAmount());
            return TransactionResponse.builder()
                    .transactionId(transaction.getTransactionId())
                    .amount(transaction.getAmount())
                    .transactionDate(transaction.getTransactionDate())
                    .rewardPoints(reward)
                    .build();
        }).toList();

        Map<Month,Long> monthlyRewardMap = transactionResponses.stream().collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().getMonth(),
                        Collectors.summingLong(TransactionResponse::getRewardPoints)));

        List<MonthlyRewardResponse> monthlyReward = monthlyRewardMap.entrySet().stream().sorted(Comparator.comparingInt(entry -> entry.getKey().getValue())).map(entry ->
                MonthlyRewardResponse.builder().month(entry.getKey().getDisplayName( TextStyle.FULL,Locale.ENGLISH)).rewardPoints(entry.getValue()).build()).toList();

        Long totalRewardsPoints = transactionResponses.stream().mapToLong(TransactionResponse::getRewardPoints).sum();

        return RewardResponse.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getName())
                .fromDate(fromDate)
                .toDate(toDate)
                .transactions(transactionResponses)
                .monthlyRewards(monthlyReward)
                .totalRewardPoints(totalRewardsPoints)
                .build();
    }
}
