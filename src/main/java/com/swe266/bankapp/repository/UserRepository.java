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

    public User findUserByNameAndPassword(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = '"
                + username + "' AND password = '" + password + "'";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                //user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null when no matching user is found
        }
    }

    public boolean existsUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = '" + username + "'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                //user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null when no matching user is found
        }
    }

    /**
     * To add deposit to existing account.
     * @param username
     * @param amount
     */
    public User depositBalance(String username, double amount) {
        notNull(username);
        notNull(amount);
        String depositSql = "UPDATE User SET balance = balance + ? WHERE username = ?";
        int rowsUpdated = jdbcTemplate.update(depositSql, amount, username);
        if (rowsUpdated > 0) {
            return findUserByUsername(username);
        } else {
            throw new IllegalStateException("Failed to update balance for user with username '" + username + "'.");
        }
    }

    /**
     * To check account balance for existing account.
     * @param username
     * @return
     */
    public double findBalanceByUsername(String username) {
        notNull(username);
        String sql = "SELECT balance FROM User WHERE username = ?";

        try {
            Double balance = jdbcTemplate.queryForObject(sql, Double.class, username);
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
    public User withdrawBalance(String username, double amount) {
        notNull(amount);
        double currentBalance = findBalanceByUsername(username);
        double newBalance = currentBalance - amount;

        String withdrawSql = "UPDATE User SET balance = ? WHERE username = ?";
        int rowsUpdated = jdbcTemplate.update(withdrawSql, newBalance, username);
        if (rowsUpdated > 0) {
            return findUserByUsername(username);
        } else {
            throw new IllegalStateException("Failed to update balance for user with username '" + username + "'.");
        }
    }

}
