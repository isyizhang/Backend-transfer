package com.swe266.bankapp.controller;


import com.swe266.bankapp.entity.User;
import com.swe266.bankapp.request.TransactionRequest;
import com.swe266.bankapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user, HttpSession session) {
        return userService.saveNewUser(user, session);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user, HttpSession session) {
        return userService.logIn(user, session);
    }

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestBody TransactionRequest depositRequest, HttpSession session) {
        return userService.deposit(depositRequest, session);
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdraw(@RequestBody TransactionRequest withdrawRequest, HttpSession session) {
        return userService.withdraw(withdrawRequest, session);
    }

    @GetMapping("/check")
    public ResponseEntity checkBalance(HttpSession session){
        return userService.checkBalance(session);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect";
    }

}
