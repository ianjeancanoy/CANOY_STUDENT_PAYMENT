package ui;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import java.util.Optional;
import java.util.Scanner;

/** Main menu: Register, Login, Exit. */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=====================================");
            System.out.println(" STUDENT TUITION PAYMENT RESERVATION");
            System.out.println("=====================================");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- Register Account ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        String role = null;
        while (role == null) {
            System.out.println("Select role: 1) STUDENT  2) TEACHER");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            if (c.equals("1")) role = "STUDENT";
            else if (c.equals("2")) role = "TEACHER";
            else System.out.println("Invalid role.");
        }

        boolean ok = userDao.register(username, PasswordUtil.hash(password), role);
        if (ok) {
            System.out.println("Account created! Status: PENDING!");
        } else {
            System.out.println("Username already exists. Try another one.");
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> result = userDao.login(username, PasswordUtil.hash(password));
        if (result.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }

        User user = result.get();
        if (user.status().equals("INACTIVE")) {
            System.out.println("This account is INACTIVE. Contact the administrator.");
            return;
        }

        new UserMenu(user, userDao, scanner).show();
    }
}
