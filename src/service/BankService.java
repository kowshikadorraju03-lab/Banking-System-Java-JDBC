package service;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BankService {

    // Login
    public int login(String name, String pin) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT account_id FROM accounts WHERE name=? AND pin=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("account_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Deposit
    public void deposit(int id, double amount) {

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, amount);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                log(id, id, id, "DEPOSIT", amount);

            } else {
                System.out.println("Deposit Failed!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Withdraw
    public void withdraw(int id, double amount) {

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String check = "SELECT balance FROM accounts WHERE account_id=?";
            PreparedStatement ps1 = con.prepareStatement(check);

            ps1.setInt(1, id);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                double balance = rs.getDouble("balance");

                if (balance >= amount) {

                    String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id=?";
                    PreparedStatement ps2 = con.prepareStatement(sql);

                    ps2.setDouble(1, amount);
                    ps2.setInt(2, id);

                    ps2.executeUpdate();

                    log(id, id, id, "WITHDRAW", amount);

                } else {
                    System.out.println("Insufficient Balance!");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Balance Check
    public void checkBalance(int id) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT balance FROM accounts WHERE account_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n=================================");
                double balance = rs.getDouble("balance");

                System.out.println("\n======================================");
                System.out.println("         ACCOUNT BALANCE");
                System.out.println("======================================");
                System.out.printf("Available Balance : ₹%.2f%n", balance);
                System.out.println("======================================");
                System.out.println("---------------------------------");
                System.out.println("Available Balance : ₹" + balance);
                System.out.println("=================================");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBalance(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT balance FROM accounts WHERE account_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return 0;
    }

    // Mini Statement
    public void miniStatement(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT transaction_id, type, amount, date " +
                    "FROM transactions WHERE account_id=? " +
                    "ORDER BY date DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n======================================================================");
            System.out.printf("%-8s %-22s %-12s %-20s%n",
                    "TxnID", "Type", "Amount", "Date & Time");
            System.out.println("----------------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.printf("%-8d %-22s ₹%-10.2f %-20s%n",
                        rs.getInt("transaction_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date"));
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

            System.out.println("======================================================================");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Transfer Money
    public void transferMoney(int senderId, int receiverId, double amount) {

        if (amount <= 0) {
            System.out.println("Invalid Amount!");
            return;
        }

        if (senderId == receiverId) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            // Check sender balance
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_id=?");

            ps1.setInt(1, senderId);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                System.out.println("Sender account not found!");
                return;
            }

            double balance = rs.getDouble("balance");

            if (balance < amount) {
                System.out.println("Insufficient Balance!");
                return;
            }

            // Check receiver account
            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT account_id FROM accounts WHERE account_id=?");

            ps2.setInt(1, receiverId);

            ResultSet rs2 = ps2.executeQuery();

            if (!rs2.next()) {
                System.out.println("Receiver account not found!");
                return;
            }

            // Deduct sender balance
            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE accounts SET balance=balance-? WHERE account_id=?");

            ps3.setDouble(1, amount);
            ps3.setInt(2, senderId);

            ps3.executeUpdate();

            // Add receiver balance
            PreparedStatement ps4 = con.prepareStatement(
                    "UPDATE accounts SET balance=balance+? WHERE account_id=?");

            ps4.setDouble(1, amount);
            ps4.setInt(2, receiverId);

            ps4.executeUpdate();

            // Log transactions
            log(senderId, senderId, receiverId, "TRANSFER SENT", amount);
            log(receiverId, senderId, receiverId, "TRANSFER RECEIVED", amount);

            System.out.println("\n-----------------------------------");
            System.out.println("Transfer Successful!");
            System.out.println("Transferred Amount : ₹" + amount);
            System.out.println("Receiver Account ID : " + receiverId);
            System.out.println("-----------------------------------");
            System.out.println("Amount transferred successfully.");
            System.out.println("-----------------------------------");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ResultSet getMiniStatement(int userId) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT transaction_id, type, amount, date " +
                    "FROM transactions WHERE account_id=? " +
                    "ORDER BY date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Transaction Log
    private void log(int accountId, int fromAccount, int toAccount, String type, double amount) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO transactions(account_id, from_account, to_account, type, amount) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, accountId);
            ps.setInt(2, fromAccount);
            ps.setInt(3, toAccount);
            ps.setString(4, type);
            ps.setDouble(5, amount);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}