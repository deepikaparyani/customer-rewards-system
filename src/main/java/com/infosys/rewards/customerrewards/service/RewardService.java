package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.response.RewardResponse;

import java.time.LocalDate;

public interface RewardService {
    RewardResponse calculateRewards(Long customerId,LocalDate fromDate,LocalDate toDate);
}
