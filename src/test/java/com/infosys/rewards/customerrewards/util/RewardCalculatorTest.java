package com.infosys.rewards.customerrewards.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private RewardCalculator rewardCalculator;

    @BeforeEach
    void setUp() {
        rewardCalculator = new RewardCalculator();
    }

    @ParameterizedTest(name = "Amount = {0}, Expected Reward = {1}")
    @CsvSource({
            "0,0",
            "30,0",
            "50,0",
            "75,25",
            "99.99,49",
            "100,50",
            "100.01,50",
            "120,90",
            "120.75,91",
            "150,150",
            "250,350"
    })
    @DisplayName("Should calculate reward points for multiple purchase amounts")
    void shouldCalculateRewardForDifferentPurchaseAmounts(double amount, long expectedReward) {

        long actualReward = rewardCalculator.calculateReward(
                BigDecimal.valueOf(amount));

        assertEquals(expectedReward, actualReward);
    }

    @Test
    @DisplayName("Should return zero reward for negative amount")
    void shouldReturnZeroForNegativeAmount() {

        long reward = rewardCalculator.calculateReward(
                BigDecimal.valueOf(-100));

        assertEquals(0L, reward);
    }
}