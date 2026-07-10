package com.infosys.rewards.customerrewards.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardResponse {

    private Long customerId;

    private String customerName;

    private LocalDate fromDate;

    private LocalDate toDate;

    private List<TransactionResponse> transactions;

    private List<MonthlyRewardResponse> monthlyRewards;

    private Long totalRewardPoints;
}