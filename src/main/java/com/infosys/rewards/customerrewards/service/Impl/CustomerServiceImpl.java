package com.infosys.rewards.customerrewards.service.Impl;

import com.infosys.rewards.customerrewards.dto.request.CustomerRequest;
import com.infosys.rewards.customerrewards.dto.response.CustomerResponse;
import com.infosys.rewards.customerrewards.entity.Customer;
import com.infosys.rewards.customerrewards.exception.CustomerAlreadyExistsException;
import com.infosys.rewards.customerrewards.exception.CustomerNotFoundException;
import com.infosys.rewards.customerrewards.repository.CustomerRepository;
import com.infosys.rewards.customerrewards.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {

        log.info("Creating customer with email : {}", request.getEmail());
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + request.getEmail() + " already exists");
        }

        Customer customer =  modelMapper.map(request, Customer.class);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully  with Id : {}", savedCustomer.getCustomerId());

        return modelMapper.map(savedCustomer, CustomerResponse.class);
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        log.info("Fetching customer with Id : {}", customerId);
        Customer customer=  customerRepository.findById(customerId).orElseThrow(() ->{
            log.error("Customer not found with id: {}", customerId);
            return new CustomerNotFoundException("Customer with id " + customerId + " not found");
        });

        return modelMapper.map(customer, CustomerResponse.class);
    }
}
