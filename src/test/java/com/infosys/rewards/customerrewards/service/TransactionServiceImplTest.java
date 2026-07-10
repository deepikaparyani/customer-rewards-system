package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.request.TransactionRequest;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.entity.Transaction;
import com.infosys.rewards.customerrewards.exception.CustomerNotFoundException;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.repository.TransactionRepository;
import com.infosys.rewards.customerrewards.service.Impl.TransactionServiceImpl;
import com.infosys.rewards.customerrewards.util.RewardCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RewardCalculator rewardCalculator;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void ShouldAddTransactionSuccessfully(){

        Long customerId = 1L;
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(120))
                .transactionDate(LocalDate.now())
                .build();

        Customer customer = Customer.builder()
                .customerId(customerId)
                .name("John Doe")
                .build();
        
        Transaction transaction= Transaction.builder()
                .amount(BigDecimal.valueOf(120))
                .transactionDate(LocalDate.now())
                .customer(customer)
                .build();

        Transaction savedTransaction = Transaction.builder()
                .transactionId(1L)
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate())
                .customer(customer)
                .build();

        TransactionResponse response = TransactionResponse.builder()
                .transactionId(1L)
                .amount(BigDecimal.valueOf(120))
                .transactionDate(request.getTransactionDate())
                .build();
        response.setRewardPoints(
                rewardCalculator.calculateReward(savedTransaction.getAmount()));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(request, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(modelMapper.map(savedTransaction,TransactionResponse.class)).thenReturn(response);
        when(rewardCalculator.calculateReward(BigDecimal.valueOf(120))).thenReturn(90L);

        TransactionResponse result = transactionService.addTransaction(customerId, request);

        assertNotNull(result);
        assertEquals(1L,result.getTransactionId());
        assertEquals(90L,result.getRewardPoints());

        verify(customerRepository).findById(customerId);
        verify(transactionRepository).save(transaction);
        verify(rewardCalculator, times(2))
                .calculateReward(BigDecimal.valueOf(120));
    }

    @Test
    void ShouldThrowCustomerNotFoundException(){

        when(customerRepository.findById(100L)).thenReturn(Optional.empty());
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(120))
                .transactionDate(LocalDate.now())
                .build();

        assertThrows(CustomerNotFoundException.class,() -> transactionService.addTransaction(100L,request)  );
        verify(transactionRepository,never()).save(any());

    }
}
