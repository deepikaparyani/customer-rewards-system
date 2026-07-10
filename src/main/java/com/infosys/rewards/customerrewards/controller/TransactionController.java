package com.infosys.rewards.customerrewards.controller;

import com.infosys.rewards.customerrewards.constants.ApiConstants;
import com.infosys.rewards.customerrewards.dto.request.TransactionRequest;
import com.infosys.rewards.customerrewards.dto.response.ApiResponse;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;
import com.infosys.rewards.customerrewards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.CUSTOMERS)
@RequiredArgsConstructor
@Tag(
        name = "Transaction APIs",
        description = "APIs for managing customer transactions"
)
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Create Transaction using customer Id",
            description = "Creates a new transaction using customer Id."
    )
    @PostMapping("/{customerId}/transactions")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(@Valid @RequestBody TransactionRequest request, @PathVariable Long customerId) {
        TransactionResponse response = transactionService.addTransaction(customerId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<TransactionResponse>builder()
                        .success(true)
                        .message("Transaction added successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @Operation(
            summary = "Get Transaction using CustomerId ",
            description = "Fetch transaction details using customer ID."
    )
    @GetMapping("/{customerId}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>>
    getTransactionsByCustomerId(
            @PathVariable Long customerId) {

        List<TransactionResponse> response =
                transactionService
                        .getTransactionsByCustomerId(customerId);

        ApiResponse<List<TransactionResponse>> apiResponse =
                ApiResponse.<List<TransactionResponse>>builder()
                        .success(true)
                        .message("Transactions fetched successfully.")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}