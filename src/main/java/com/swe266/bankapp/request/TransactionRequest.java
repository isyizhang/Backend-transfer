package com.swe266.bankapp.request;

import lombok.Data;

@Data
public class TransactionRequest {
    String username;
    Double amount;
}
