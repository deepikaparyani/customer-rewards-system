package com.infosys.rewards.customerrewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.rewards.customerrewards.dto.request.CustomerRequest;
import com.infosys.rewards.customerrewards.dto.response.CustomerResponse;
import com.infosys.rewards.customerrewards.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void shouldCreatedCustomerSuccessfully() throws Exception {

        CustomerRequest request= CustomerRequest.builder()
                .name("John Doe")
                .email("john@test.com")
                .build();

        CustomerResponse response= CustomerResponse.builder()
                .customerId(1L)
                .name("John Doe")
                .email("john@test.com")
                .createdAt(LocalDateTime.now())
                .build();

        when(customerService.createCustomer(any(CustomerRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Customer created successfully!"))
                .andExpect(jsonPath("$.data.customerId").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@test.com"));

    }

    @Test
    void shouldReturnCustomerById() throws Exception {

        CustomerResponse response =
                CustomerResponse.builder()
                        .customerId(1L)
                        .name("John Doe")
                        .email("john@test.com")
                        .createdAt(LocalDateTime.now())
                        .build();

        when(customerService.getCustomerById(
                eq(1L))).thenReturn(response);


        mockMvc.perform(get("/api/v1/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.customerId").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

}
