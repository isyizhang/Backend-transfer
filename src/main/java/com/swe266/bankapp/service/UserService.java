package com.swe266.bankapp.service;

import com.swe266.bankapp.entity.User;
import com.swe266.bankapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidPassword(String password) {
        String pattern = "^[_\\-\\.0-9a-z]{1,127}$";
        return password.matches(pattern);
    }

    public boolean isValidUsername(String username) {
        String pattern = "^[_\\-\\.0-9a-z]{1,127}$";
        return username.matches(pattern);
    }

    public boolean isValidInitialDeposit(double input) {
        int fractional = (int) (input * 100) % 100;

        if (input < 0.00 || input > 4294967295.99) {
            return false;
        }

        // Check if fractional amount is exactly two digits
        if (fractional < 0 || fractional > 99) {
            return false;
        }

        return true;
    }

    public ResponseEntity saveNewUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        Double balance = user.getBalance();

        if (!isValidUsername(username) || !isValidPassword(password)) {
            String errorMessage = "Invalid input. Please provide valid username and password.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (!isValidInitialDeposit(balance)) {
            String errorMessage = "Invalid input. Please provide valid initial balance.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (userRepository.existsUserByUsername(username)) {
            String errorMessage = "Username exists, please change a name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Below are methods for account transactions
     */
    public ResponseEntity logIn(String username, String password) {
        User user = userRepository.findUserByNameAndPassword(username, password);
        if (user == null) {
            String errorMessage = "Invalid username or password.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }

        return ResponseEntity.ok(user);
    }

    public ResponseEntity deposit(String username, double amount) {
        if (!isValidUsername(username) || amount <= 0) {
            String errorMessage = "Invalid input. Please provide a valid username and a positive amount for deposit.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        userRepository.deposit(username, amount);
        String successMessage = "Deposit of " + amount + " for user '" + username + "' successful.";
        return ResponseEntity.ok(successMessage);
    }

    public ResponseEntity withdraw(String username, double amount) {
        if (!isValidUsername(username) || amount <= 0) {
            String errorMessage = "Invalid input. Please provide a valid username and a positive amount for withdrawal.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        double currentBalance = userRepository.checkBalance(username);
        if (amount > currentBalance) {
            String errorMessage = "Insufficient balance for user '" + username + "'.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        userRepository.withdraw(username, amount);
        String successMessage = "Withdrawal of " + amount + " from user '" + username + "' successful.";
        return ResponseEntity.ok(successMessage);
    }

}
