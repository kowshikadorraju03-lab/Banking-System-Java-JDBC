package gui;

import service.BankService;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    int userId;
    BankService bs = new BankService();
    JLabel balanceLabel;
    private void showReceipt(String type, double amount, double before, double after) {

        JOptionPane.showMessageDialog(null,
                "================ RECEIPT ================\n" +
                        "Transaction : " + type + "\n" +
                        "Amount      : ₹" + amount + "\n" +
                        "Account ID  : " + userId + "\n" +
                        "Prev Balance: ₹" + before + "\n" +
                        "New Balance : ₹" + after + "\n" +
                        "Status      : SUCCESS\n" +
                        "========================================");
    }
    public DashboardFrame(int userId) {

        this.userId = userId;
        setTitle("Banking Dashboard");
        setSize(650, 450);
        setLocationRelativeTo(null);   // Centers the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem aboutItem = new JMenuItem("About");

        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
        getContentPane().setBackground(new Color(230, 240, 255));

        // HEADER
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 70, 140));
        header.setBounds(0, 0, 650, 60);
        header.setLayout(null);
        add(header);
        ImageIcon logo = new ImageIcon("resources/icons/bank.png");
        Image img = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setBounds(20, 10, 40, 40);

        header.add(logoLabel);
        JLabel title = new JLabel("🏦 BANKING MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(140, 15, 400, 30);
        header.add(title);

        JLabel welcome = new JLabel("Welcome, User");
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        welcome.setBounds(250, 80, 200, 25);
        add(welcome);
        balanceLabel = new JLabel("");
        balanceLabel.setVisible(false);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(0, 70, 140));
        balanceLabel.setBounds(250, 105, 250, 25);


        updateBalance();
        // 💰 DEPOSIT
        JButton depositBtn = new JButton("➕ Deposit");
        depositBtn.setFont(new Font("Arial", Font.BOLD, 14));
        depositBtn.setBackground(new Color(76, 175, 80));
        depositBtn.setForeground(Color.WHITE);
        depositBtn.setFocusPainted(false);
        depositBtn.setBounds(80, 130, 180, 40);
        add(depositBtn);
        depositBtn.addActionListener(e -> {

            try {

                String input = JOptionPane.showInputDialog(this, "Enter deposit amount:");

                if (input == null) return;

                double amount = Double.parseDouble(input);

                if (amount <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0.",
                            "Invalid Amount",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                double before = bs.getBalance(userId);

                bs.deposit(userId, amount);

                updateBalance();

                double after = bs.getBalance(userId);

                showReceipt("DEPOSIT", amount, before, after);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid amount.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
        // 💸 WITHDRAW
        JButton withdrawBtn = new JButton("➖ Withdraw");
        withdrawBtn.setFont(new Font("Arial", Font.BOLD, 14));
        withdrawBtn.setBackground(new Color(244, 67, 54));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFocusPainted(false);
        withdrawBtn.setBounds(330, 130, 180, 40);
        add(withdrawBtn);


        withdrawBtn.addActionListener(e -> {

            try {

                String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");

                if (input == null) return;

                double amount = Double.parseDouble(input);

                if (amount <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0.",
                            "Invalid Amount",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                double before = bs.getBalance(userId);

                bs.withdraw(userId, amount);

                updateBalance();

                double after = bs.getBalance(userId);

                showReceipt("WITHDRAW", amount, before, after);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid amount.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
        // 📊 BALANCE
        JButton balanceBtn = new JButton("💲 Check Balance");
        balanceBtn.setFont(new Font("Arial", Font.BOLD, 14));
        balanceBtn.setBackground(new Color(33, 150, 243));
        balanceBtn.setForeground(Color.WHITE);
        balanceBtn.setFocusPainted(false);
        balanceBtn.setBounds(80, 200, 180, 40);
        balanceLabel.setVisible(true);
        updateBalance();
        add(balanceBtn);

        balanceBtn.addActionListener(e -> {
            double bal = bs.getBalance(userId);
            JOptionPane.showMessageDialog(null, "Current Balance: ₹" + bal);
            addHoverEffect(balanceBtn);
        });

        // 💳 TRANSFER
        JButton transferBtn = new JButton("⇄ Transfer Money");
        transferBtn.setFont(new Font("Arial", Font.BOLD, 14));
        transferBtn.setBackground(new Color(255, 152, 0));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFocusPainted(false);
        transferBtn.setBounds(330, 200, 180, 40);
        add(transferBtn);

        transferBtn.addActionListener(e -> {

            try {

                String idStr = JOptionPane.showInputDialog(this, "Enter Receiver Account ID:");

                if (idStr == null || idStr.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Receiver Account ID is required.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                String amtStr = JOptionPane.showInputDialog(this, "Enter Amount:");

                if (amtStr == null) return;

                int receiverId = Integer.parseInt(idStr);

                double amount = Double.parseDouble(amtStr);

                if (amount <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0.",
                            "Invalid Amount",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                double before = bs.getBalance(userId);

                bs.transferMoney(userId, receiverId, amount);

                updateBalance();

                double after = bs.getBalance(userId);

                JOptionPane.showMessageDialog(
                        this,
                        "Transfer Successful!\n\n" +
                                "Receiver Account : " + receiverId +
                                "\nAmount : ₹" + amount +
                                "\nPrevious Balance : ₹" + before +
                                "\nCurrent Balance : ₹" + after,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid numeric values.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        // 📜 STATEMENT
        JButton statementBtn = new JButton("📄 Mini Statement");
        statementBtn.setFont(new Font("Arial", Font.BOLD, 14));
        statementBtn.setBackground(new Color(156, 39, 176));
        statementBtn.setForeground(Color.WHITE);
        statementBtn.setFocusPainted(false);
        statementBtn.setBounds(80, 270, 180, 40);
        add(statementBtn);

        statementBtn.addActionListener(e -> {
            new StatementFrame(userId);
            addHoverEffect(statementBtn);
        });

        // 🚪 LOGOUT
        JButton logoutBtn = new JButton("⏻ Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(96, 125, 139));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBounds(330, 270, 180, 40);
        add(logoutBtn);
        addHoverEffect(logoutBtn);

        logoutBtn.addActionListener(e -> {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {

                dispose();

                new LoginFrame();
            }

        });
        aboutItem.addActionListener(e -> {

            JOptionPane.showMessageDialog(

                    this,

                    "🏦 Banking Management System\n\n" +
                            "Version : 1.0\n\n" +
                            "Developed By:\n" +
                            "Kowshika Dorraju\n\n" +
                            "Technology:\n" +
                            "• Java\n" +
                            "• Swing\n" +
                            "• JDBC\n" +
                            "• MySQL",

                    "About",

                    JOptionPane.INFORMATION_MESSAGE);

        });
        JLabel statusBar = new JLabel(
                "Logged In Account : " + userId +
                        "     |     Status : Connected     |     Banking Management System v1.0");

        statusBar.setBounds(10, 380, 620, 25);
        statusBar.setFont(new Font("Arial", Font.PLAIN, 12));
        statusBar.setForeground(Color.DARK_GRAY);

        add(statusBar);
        logoutItem.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        exitItem.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        aboutItem.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    """
                    🏦 BANKING MANAGEMENT SYSTEM
        
                    Version : 1.0
        
                    Developed By:
                    Kowshika Dorraju
        
                    Technologies Used:
                    • Java
                    • Swing
                    • JDBC
                    • MySQL
        
                    Features:
                    • Login Authentication
                    • Deposit & Withdraw
                    • Balance Enquiry
                    • Money Transfer
                    • Mini Statement
                    • Export CSV
        
                    © 2026 Banking Management System
                    """,
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);

        });
        setVisible(true);
    }
    private void addHoverEffect(JButton button) {

        Color original = button.getBackground();

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(original.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(original);
            }
        });
    }
    private void updateBalance() {

        double balance = bs.getBalance(userId);

        balanceLabel.setText("Available Balance: ₹" + balance);
    }
}