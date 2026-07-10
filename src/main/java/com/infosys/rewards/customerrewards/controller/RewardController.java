package com.infosys.rewards.customerrewards.controller;

import com.infosys.rewards.customerrewards.constants.ApiConstants;
import com.infosys.rewards.customerrewards.dto.response.ApiResponse;
import com.infosys.rewards.customerrewards.dto.response.RewardResponse;
import com.infosys.rewards.customerrewards.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CUSTOMERS)
@Tag(
        name = "Reward APIs",
        description = "Reward calculation APIs"
)
public class RewardController {
    private final RewardService rewardService;

    @Operation(
            summary = "Calculate Rewards",
            description = "Calculates reward points for a customer between two dates."
    )
    @GetMapping("/{customerId}/rewards")
    public ResponseEntity<ApiResponse<RewardResponse>> calculateRewards(@PathVariable Long customerId,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
                                                                        ){
        RewardResponse response = rewardService.calculateRewards(customerId, fromDate, toDate);

        return ResponseEntity.ok(
                ApiResponse.<RewardResponse>builder()
                        .success(true)
                        .message("Rewards calculated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()

        );
    }

}
