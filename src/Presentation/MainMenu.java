package Presentation;

import Controller.*;
import Model.Admin;
import Model.Developer;
import Model.Game;
import Model.User;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final AccountController accountController;
    private final GameController gameController;
    private final AdminController adminController;
    private final DeveloperController developerController;
    private final CustomerController customerController;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(AccountController accountController, GameController gameController,
                    AdminController adminController, DeveloperController developerController, CustomerController customerController) {
        this.accountController = accountController;
        this.gameController = gameController;
        this.adminController = adminController;
        this.developerController = developerController;
        this.customerController = customerController;
    }

    public void start() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> handleSignUp();
                case 2 -> handleLogIn();
                case 3 -> exitApp();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }


    private void handleSignUp() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (accountController.signUp(username, email, password)) {
            System.out.println("Sign-up successful!");
        } else {
            System.out.println("Sign-up failed! Email may already be in use.");
        }
    }

    private void handleLogIn() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (accountController.logIn(email, password)) {
            System.out.println("Login successful!");
            User loggedInUser = accountController.getLoggedInUser();
            if ("Admin".equals(loggedInUser.getRole())) {
                Admin loggedInAdmin = (Admin) loggedInUser;
                AdminMenu adminMenu = new AdminMenu(adminController, gameController, this, loggedInAdmin); // Transmit gameController și MainMenu
                adminMenu.start();
            } else if ("Developer".equals(loggedInUser.getRole())) {
                Developer loggedInDeveloper = (Developer) loggedInUser;
                DeveloperMenu developerMenu = new DeveloperMenu(developerController, gameController, this, loggedInDeveloper); // Aici creezi DeveloperMenu
                developerMenu.start();
            } else {
                System.out.println("Unknown role. Returning to Main Menu.");
            }
        } else {
            System.out.println("Login failed! Invalid email or password.");
        }
    }

    public void handleLogOut() {
        try {
            boolean success = accountController.logOut();
            if (success) {
                System.out.println("You have been logged out successfully.");
            } else {
                System.out.println("No user is currently logged in.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while logging out: " + e.getMessage());
        }
    }


    public void handleViewAllGames() {
        try {
            List<Game> games = gameController.getAllGames();
            if (games.isEmpty()) {
                System.out.println("No games available.");
            } else {
                System.out.println("Available Games:");
                for (Game game : games) {
                    System.out.println(game);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving games: " + e.getMessage());
        }
    }

    public void handleViewGame(GameController gameController) {
        try {
            System.out.print("Enter Game ID: ");
            int gameId = scanner.nextInt();
            scanner.nextLine(); // Consumă linia rămasă

            Game game = gameController.getGameById(gameId);
            if (game != null) {
                System.out.println("Game Details: ");
                System.out.println(game);
            } else {
                System.out.println("Game with ID " + gameId + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Please enter a valid Game ID.");
        }
    }

    public void handleDeleteAccount() {
        try {
            System.out.print("Are you sure you want to delete your account? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                boolean success = accountController.deleteAccount();
                if (success) {
                    System.out.println("Your account has been successfully deleted.");
                    System.out.println("Returning to Main Menu...");
                } else {
                    System.out.println("Failed to delete your account. Please try again.");
                }
            } else {
                System.out.println("Account deletion canceled.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void exitApp() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }

}

