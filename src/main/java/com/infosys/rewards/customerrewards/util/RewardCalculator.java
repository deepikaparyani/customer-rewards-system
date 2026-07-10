package com.infosys.rewards.customerrewards.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Utility class responsible for calculating
 * reward points based on transaction amount.
 */
@Component
public class RewardCalculator {
    /**
     * Calculates reward points.
     *
     * @param amount purchase amount
     * @return reward points
     */

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public long calculateReward(BigDecimal amount) {

        if (amount == null || amount.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (amount.compareTo(HUNDRED) <= 0) {
            return amount.subtract(FIFTY).longValue();
        }

        return 50 +
                amount.subtract(HUNDRED)
                        .multiply(BigDecimal.valueOf(2))
                        .longValue();
    }


}