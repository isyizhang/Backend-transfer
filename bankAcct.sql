CREATE DATABASE bank;
USE bank;

CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      balance DOUBLE NOT NULL DEFAULT 0
);