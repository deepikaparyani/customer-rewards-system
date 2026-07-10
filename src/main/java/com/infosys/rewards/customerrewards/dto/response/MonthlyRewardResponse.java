package com.infosys.rewards.customerrewards.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyRewardResponse {

    private String month;

    private Long rewardPoints;
}
