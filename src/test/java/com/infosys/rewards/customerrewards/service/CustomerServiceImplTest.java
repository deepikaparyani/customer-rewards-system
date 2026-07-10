package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.request.CustomerRequest;
import com.infosys.rewards.customerrewards.dto.response.CustomerResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.exception.CustomerAlreadyExistsException;
import com.infosys.rewards.customerrewards.exception.CustomerNotFoundException;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.service.Impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldCreateCustomerSuccessfully() {

        CustomerRequest request = CustomerRequest.builder()
                .name("John Doe")
                .email("jone@test.com")
                .build();
        Customer customer= Customer.builder()
                .name("John Doe")
                .email("jone@test.com")
                .build();

        Customer savedCustomer = Customer.builder()
                .customerId(1L)
                .name("John Doe")
                .email("jone@test.com")
                .createdAt(LocalDateTime.now())
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .customerId(1L)
                .name("John Doe")
                .email("jone@test.com")
                .createdAt(savedCustomer.getCreatedAt())
                .build();

        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);

        when(modelMapper.map(request, Customer.class)).thenReturn(customer);

        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        when(modelMapper.map(savedCustomer,CustomerResponse.class)).thenReturn(response);

        CustomerResponse result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals(1L,result.getCustomerId());
        assertEquals("John Doe",result.getName());

        verify(customerRepository).existsByEmail(result.getEmail());
        verify(customerRepository).save(customer);
        verify(modelMapper,times(2)).map(any(),any());

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        CustomerRequest request = CustomerRequest.builder()
                .name("John Doe")
                .email("jone@test.com")
                .build();


        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);


        assertThrows(CustomerAlreadyExistsException.class,() -> customerService.createCustomer(request));

        verify(customerRepository,never()).save(any());
    }

    @Test
    void shouldReturnByCustomerId() {

        Customer customer = Customer.builder()
                .customerId(1L)
                .name("John Doe")
                .email("jone@test.com")
                .createdAt(LocalDateTime.now())
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .customerId(1L)
                .name("John Doe")
                .email("jone@test.com")
                .createdAt(customer.getCreatedAt())
                .build();


        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(modelMapper.map(customer, CustomerResponse.class)).thenReturn(response);

        CustomerResponse result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("John Doe",result.getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,() -> customerService.getCustomerById(100L));

        verify(customerRepository).findById(100L);
    }
}