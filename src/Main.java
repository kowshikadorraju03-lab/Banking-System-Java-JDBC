import service.BankService;
import java.util.Scanner;
import gui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        new LoginFrame();
        Scanner sc = new Scanner(System.in);
        BankService bs = new BankService();

        int attempts = 0;
        int userId = -1;

        // LOGIN (max 3 attempts)
        while (attempts < 3) {

            System.out.print("Name: ");
            String name = sc.next();

            System.out.print("PIN: ");
            String pin = sc.next();

            userId = bs.login(name, pin);

            if (userId != -1) {
                System.out.println("Login successful!");
                break;
            }

            System.out.println("Wrong credentials!");
            attempts++;
        }

        if (userId == -1) {
            System.out.println("Account locked!");
            return;
        }

        // MAIN MENU
        while (true) {

            System.out.println("\n=================================================");
            System.out.println("                 MAIN MENU");
            System.out.println("=================================================");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Mini Statement");
            System.out.println("5. Transfer Money");
            System.out.println("6. Exit");
            System.out.println("=================================================");
            System.out.print("Enter Your Choice: ");

            int ch;

            // SAFE INPUT HANDLING (upgrade)
            try {
                ch = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (ch) {

                case 1 -> {
                    System.out.print("Enter amount to deposit: ");
                    double amount = sc.nextDouble();
                    bs.deposit(userId, amount);
                }

                case 2 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double amount = sc.nextDouble();
                    bs.withdraw(userId, amount);
                }

                case 3 -> bs.checkBalance(userId);

                case 4 -> bs.miniStatement(userId);

                case 5 -> {
                    System.out.print("Enter Receiver Account ID: ");
                    int receiverId = sc.nextInt();

                    System.out.print("Enter Amount: ");
                    double amount = sc.nextDouble();

                    bs.transferMoney(userId, receiverId, amount);
                }

                case 6 -> {
                    System.out.println("Thank you for using Banking System!");
                    sc.close();
                    System.exit(0);
                }

                default -> System.out.println("Invalid Choice!");
            }
        }
    }
}