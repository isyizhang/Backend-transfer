-- Database: MySQL

CREATE DATABASE IF NOT EXISTS bank;
USE bank;

-- Drop the 'user' table if it exists
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(127) NOT NULL,
    password VARCHAR(127) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0
);
