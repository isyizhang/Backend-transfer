package com.swe266.bankapp.controller;


import com.swe266.bankapp.entity.User;
import com.swe266.bankapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        return userService.saveNewUser(user);
    }

    /**
     * Below are account related controllers.
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password) {
        return userService.logIn(username, password);
    }

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestParam String username, @RequestParam double amount) {
        return userService.deposit(username, amount);
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdraw(@RequestParam String username, @RequestParam double amount) {
        return userService.withdraw(username, amount);
    }
}
