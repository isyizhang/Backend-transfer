package com.swe266.bankapp.repository;

import com.swe266.bankapp.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

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


}
