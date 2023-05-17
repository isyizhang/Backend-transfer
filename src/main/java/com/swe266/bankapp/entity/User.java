package com.swe266.bankapp.entity;

import lombok.Data;

@Data

public class User {
    private Long id;
    private String username;
    private String password;
    private double balance;
}
