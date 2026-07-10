package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.request.CustomerRequest;
import com.infosys.rewards.customerrewards.dto.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(Long customerId);

}