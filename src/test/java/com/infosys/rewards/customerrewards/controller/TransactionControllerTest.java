package com.infosys.rewards.customerrewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.rewards.customerrewards.dto.request.TransactionRequest;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;
import com.infosys.rewards.customerrewards.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        TransactionRequest request =
                TransactionRequest.builder()
                        .amount(BigDecimal.valueOf(120))
                        .transactionDate(LocalDate.now())
                        .build();

        TransactionResponse response =
                TransactionResponse.builder()
                        .transactionId(1L)
                        .amount(BigDecimal.valueOf(120))
                        .transactionDate(LocalDate.now())
                        .rewardPoints(90L)
                        .build();

        when(transactionService.addTransaction(
                eq(1L),
                any(TransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/customers/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())              // <-- move here
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.rewardPoints").value(90));
    }
}