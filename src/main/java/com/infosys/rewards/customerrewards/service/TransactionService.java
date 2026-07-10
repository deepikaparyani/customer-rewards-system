package com.infosys.rewards.customerrewards.service;

import com.infosys.rewards.customerrewards.dto.request.TransactionRequest;
import com.infosys.rewards.customerrewards.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse addTransaction(
            Long customerId,
            TransactionRequest request);

    List<TransactionResponse> getTransactionsByCustomerId(
            Long customerId);

}