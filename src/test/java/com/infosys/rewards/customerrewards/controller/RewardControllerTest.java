package com.infosys.rewards.customerrewards.controller;

import com.infosys.rewards.customerrewards.dto.response.RewardResponse;
import com.infosys.rewards.customerrewards.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RewardController.class)
@AutoConfigureMockMvc(addFilters = false)
class RewardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RewardService rewardService;

    @Test
    void shouldCalculateRewardSuccessfully() throws Exception {

        RewardResponse response =
                RewardResponse.builder()
                        .customerId(1L)
                        .customerName("John Doe")
                        .fromDate(LocalDate.of(2026,1,1))
                        .toDate(LocalDate.of(2026,3,31))
                        .totalRewardPoints(120L)
                        .build();

        when(rewardService.calculateRewards(
                1L,
                LocalDate.of(2026,1,1),
                LocalDate.of(2026,3,31)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/1/rewards")
                        .param("fromDate","2026-01-01")
                        .param("toDate","2026-03-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalRewardPoints")
                        .value(120));
    }
}