package com.swe266.bankapp.repository;

import com.swe266.bankapp.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import static org.springframework.util.Assert.notNull;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        String sql = "INSERT INTO User (username, password, balance) VALUES ('" + user.getUsername() +
                "', '" + user.getPassword() + "', " + user.getBalance() + ")";
        jdbcTemplate.update(sql);
        return user;
    }

//    public User findById(Long id) {
//        String sql = "SELECT * FROM User WHERE id = ?";
//        Object[] params = {id};
//
//        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
//            User user = new User();
//            user.setId(rs.getLong("id"));
//            user.setUsername(rs.getString("username"));
//            user.setPassword(rs.getString("password"));
//            user.setBalance(rs.getBigDecimal("balance"));
//            return user;
//        });
//    }

    public User findUserByNameAndPassword(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = '"
                + username + "' AND password = '" + password + "'";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null when no matching user is found
        }
    }

    //Exist: True   if not: False
    public boolean existsUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = '" + username + "'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }


    /**
     * To add deposit to existing account.
     * @param username
     * @param amount
     */
    public void deposit(String username, double amount) {
        notNull(username);
        notNull(amount);

        if (existsUserByUsername(username)) {
            String depositSql = "UPDATE User SET balance = balance + ? WHERE username = ?";
            jdbcTemplate.update(depositSql, amount, username);
        } else {
            throw new IllegalArgumentException("User with username '" + username + "' does not exist.");
        }
    }

    /**
     * To check account balance for existing account.
     * @param username
     * @return
     */
    public double checkBalance(String username) {
        notNull(username);

        String sql = "SELECT balance FROM User WHERE username = ?";

        try {
            Double balance = jdbcTemplate.queryForObject(sql, Double.class, username);
            System.out.println("Balance for user '" + username + "': " + balance);
            return balance;
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("User with username '" + username + "' does not exist.");
        }
    }


    /**
     * To withdraw money from existing account.
     * @param username
     * @param amount
     */
    public void withdraw(String username, double amount) {
        notNull(username);
        notNull(amount);

        if (existsUserByUsername(username)) {
            double currentBalance = checkBalance(username);
            double newBalance = currentBalance - amount;

            if (newBalance >= 0) {
                String withdrawSql = "UPDATE User SET balance = ? WHERE username = ?";
                jdbcTemplate.update(withdrawSql, newBalance, username);
                System.out.println("Withdrawal of " + amount + " from user '" + username + "' successful.");
            } else {
                throw new IllegalArgumentException("Insufficient balance for user '" + username + "'.");
            }
        } else {
            throw new IllegalArgumentException("User with username '" + username + "' does not exist.");
        }
    }


    /**
     * To support user login.
     * @param username
     * @param password
     * @return
     */
    public User logIn(String username, String password) {
        notNull(username);
        notNull(password);

        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            }, username, password);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

}
