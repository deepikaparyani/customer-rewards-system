package com.infosys.rewards.customerrewards.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long transactionId;

    private BigDecimal amount;

    private LocalDate transactionDate;

    private Long rewardPoints;
}
