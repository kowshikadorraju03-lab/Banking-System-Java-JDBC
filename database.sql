CREATE DATABASE IF NOT EXISTS banking_db;
USE banking_db;

CREATE TABLE accounts (
                          account_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100),
                          pin VARCHAR(10),
                          balance DOUBLE DEFAULT 0
);

CREATE TABLE transactions (
                              transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                              account_id INT,
                              type VARCHAR(20),
                              amount DOUBLE,
                              date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);