package com.swe266.bankapp.service;

import com.swe266.bankapp.entity.User;
import com.swe266.bankapp.repository.UserRepository;
import com.swe266.bankapp.request.TransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

import static com.swe266.bankapp.utils.ValidationUtil.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * save a new user for user register
     */
    public ResponseEntity saveNewUser(User user, HttpSession session) {
        String username = user.getUsername();
        String password = user.getPassword();
        Double balance = user.getBalance();
        session.setAttribute("currentUser", username);

        //validation
        if (!isValidUsername(username) || !isValidPassword(password)) {
            String errorMessage = "Invalid input. Please provide valid username and password.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (!isValidAmount(balance)) {
            String errorMessage = "Invalid input. Please provide valid initial balance.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (userRepository.existsUserByUsername(username)) {
            String errorMessage = "Username exists, please change a name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //register and login success
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * handle user login
     */
    public ResponseEntity logIn(User user, HttpSession session) {
        String username = user.getUsername();
        String password = user.getPassword();
        session.setAttribute("currentUser", username);

        // validation
        if (!isValidUsername(username) || !isValidPassword(password)) {
            String errorMessage = "Invalid input. Please provide valid username and password.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if(!userRepository.existsUserByUsername(username)){
            String errorMessage = "User Not Found";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        User userResult = userRepository.findUserByUsername(username);
        boolean passwordMatches = passwordEncoder.matches(password, userResult.getPassword());
        if (!passwordMatches) {
            String errorMessage = "Wrong Password";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
        // login success
        userResult.setPassword(null);
        return ResponseEntity.ok(userResult);
    }

    /**
     * Below are methods for account transactions
     */
    public ResponseEntity deposit(TransactionRequest request, HttpSession session) {
        String username = request.getUsername();
        Double amount = request.getAmount();

        if (!isValidUsername(username) || !userRepository.existsUserByUsername(username)) {
            String errorMessage = "Invalid input. Please provide a valid username";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (!isValidAmount(amount)) {
            String errorMessage = "Invalid input. Please provide valid amount.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            User updatedUser = userRepository.depositBalance(username, amount);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalStateException e) {
            String errorMessage = "Deposit Failed";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    public ResponseEntity withdraw(TransactionRequest request, HttpSession session) {
        String username = request.getUsername();
        Double amount = request.getAmount();

        if (!isValidUsername(username) || !userRepository.existsUserByUsername(username)) {
            String errorMessage = "Invalid input. Please provide a valid username";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        if (!isValidAmount(amount)) {
            String errorMessage = "Invalid input. Please provide valid amount.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        double currentBalance = userRepository.findBalanceByUsername(username);
        if (amount > currentBalance) {
            String errorMessage = "Insufficient balance for user '" + username + "'.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            User updatedUser = userRepository.withdrawBalance(username, amount);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalStateException e) {
            String errorMessage = "Withdraw Failed";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    public ResponseEntity checkBalance(HttpSession session){
        String username = (String) session.getAttribute("currentUser");
        if (!isValidUsername(username) || !userRepository.existsUserByUsername(username)) {
            String errorMessage = "Invalid input. Please provide a valid username";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        User user = userRepository.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

}
