package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.response.RewardResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.entity.Transaction;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.repository.TransactionRepository;
import com.infosys.rewards.customerrewards.service.Impl.RewardServiceImpl;
import com.infosys.rewards.customerrewards.util.RewardCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RewardServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Spy
    private RewardCalculator rewardCalculator;

    @Test
    void ShouldCalculateRewardsSuccessfully() {

    Customer customer = Customer.builder()
            .customerId(1L)
            .name("John Doe")
            .build();

    Transaction t1 = Transaction.builder()
            .transactionId(1L)
            .amount(BigDecimal.valueOf(120))
            .transactionDate(LocalDate.of(2026,1,10))
            .customer(customer)
            .build();


    Transaction t2 = Transaction.builder()
            .transactionId(2L)
            .amount(BigDecimal.valueOf(80))
            .transactionDate(LocalDate.of(2026,1,20))
            .customer(customer)
            .build();

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(eq(1L),any(LocalDate.class),any(LocalDate.class))).thenReturn(List.of(t1,t2));

    RewardResponse response= rewardService.calculateRewards(1L,LocalDate.of(2026,1,1),LocalDate.of(2026,1,30));

    assertNotNull(response);
    assertEquals(120L,response.getTotalRewardPoints());
    assertEquals(2,response.getTransactions().size());
    assertEquals(1,response.getMonthlyRewards().size());

    verify(customerRepository).findById(1L);
    verify(transactionRepository).findByCustomerCustomerIdAndTransactionDateBetween(anyLong(),any(),any());



    }

}
