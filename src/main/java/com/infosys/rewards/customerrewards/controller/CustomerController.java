package com.infosys.rewards.customerrewards.controller;

import com.infosys.rewards.customerrewards.constants.ApiConstants;
import com.infosys.rewards.customerrewards.dto.request.CustomerRequest;
import com.infosys.rewards.customerrewards.dto.response.ApiResponse;
import com.infosys.rewards.customerrewards.dto.response.CustomerResponse;
import com.infosys.rewards.customerrewards.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CUSTOMERS)
@Tag(
        name = "Customer APIs",
        description = "APIs for managing customers"
)
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Create Customer",
            description = "Creates a new customer."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer created successfully!")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(
            summary = "Get Customer",
            description = "Fetch customer details using customer ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<CustomerResponse>builder()
                        .success(true)
                        .message("Customer retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
