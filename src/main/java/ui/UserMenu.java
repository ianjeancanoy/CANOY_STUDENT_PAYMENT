package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;

/** Menu shown after login. Admins also get account management. */
public class UserMenu {

    private final User user;
    private final UserDao userDao;
    private final Scanner scanner;

    public UserMenu(User user, UserDao userDao, Scanner scanner) {
        this.user = user;
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show() {
        System.out.println("\nWelcome, " + user.username() + "!  Role: " + user.roles() + "  Status: " + user.status());
        if (user.status().equals("PENDING")) {
            System.out.println("(Your account is still PENDING approval.)");
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- " + user.roles() + " MENU ---");
            if (user.roles().equals("ADMIN")) {
                System.out.println("1. View all users");
                System.out.println("2. Activate / Deactivate a user");
            }
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Logged out. See you, " + user.username() + "!");
                loggedIn = false;
            } else if (user.roles().equals("ADMIN") && choice.equals("1")) {
                listUsers();
            } else if (user.roles().equals("ADMIN") && choice.equals("2")) {
                changeStatus();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void listUsers() {
        List<User> users = userDao.findAll();
        System.out.printf("%-4s %-20s %-10s %-10s%n", "ID", "USERNAME", "ROLE", "STATUS");
        for (User u : users) {
            System.out.printf("%-4d %-20s %-10s %-10s%n", u.id(), u.username(), u.roles(), u.status());
        }
    }

    private void changeStatus() {
        listUsers();
        System.out.print("Enter user ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }
        System.out.print("New status (ACTIVE / INACTIVE): ");
        String status = scanner.nextLine().trim().toUpperCase();
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            System.out.println("Invalid status.");
            return;
        }
        System.out.println(userDao.updateStatus(id, status) ? "Status updated." : "User not found.");
    }
}
