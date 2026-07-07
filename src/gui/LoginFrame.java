package gui;

import service.BankService;

import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private BankService bs = new BankService();

    private int loginAttempts = 0;

    public LoginFrame() {

        setTitle("Banking System");

        ImageIcon appIcon = new ImageIcon("resources/icons/bank.png");
        setIconImage(appIcon.getImage());
        setSize(420, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new java.awt.Color(240,248,255));

        setLayout(null);
        ImageIcon logo = new ImageIcon("resources/icons/bank.png");
        System.out.println("Logo Width = " + logo.getIconWidth());
        Image img = logo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setBounds(165, 10, 60, 60);
        add(logoLabel);
        // Title
        JLabel title = new JLabel("BANKING MANAGEMENT SYSTEM");
        title.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        title.setBounds(40, 75, 320, 30);
        add(title);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        userLabel.setBounds(50, 130, 100, 25);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 130, 150, 25);
        add(userField);

        // PIN
        JLabel pinLabel = new JLabel("PIN:");
        userLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        pinLabel.setBounds(50, 170, 100, 25);
        add(pinLabel);

        JPasswordField pinField = new JPasswordField();
        pinField.setBounds(150, 170, 150, 25);
        add(pinField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        loginButton.setBounds(145, 220, 100, 35);
        add(loginButton);

        // Login Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = userField.getText();
                String pin = new String(pinField.getPassword());

                int userId = bs.login(username, pin);

                if (userId != -1) {

                    JOptionPane.showMessageDialog(null, "Login Successful!");

                    dispose(); // Close Login Window

                    new DashboardFrame(userId); // FIXED

                } else {

                    loginAttempts++;

                    if (loginAttempts >= 3) {

                        JOptionPane.showMessageDialog(
                                null,
                                "Too many incorrect login attempts.\nApplication will now close.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE
                        );

                        System.exit(0);

                    } else {

                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid Username or PIN!\nAttempts Remaining: " + (3 - loginAttempts),
                                "Login Failed",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }

            }
        });

        setVisible(true);
    }
}

