package service;

import entities.User;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    public static User loggedInUser;
    private final UserRepository userRepository;
    private Scanner scanner;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.scanner = new Scanner(System.in);
    }

    public void registerUser() throws SQLException {
        System.out.println("Please enter your display name: ");
        String displayname = scanner.nextLine();
        System.out.println("Please enter your email address: : ");
        String email = scanner.nextLine();
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Please enter your bio: ");
        String bio = scanner.nextLine();

        if(userRepository.isUsernameOrEmailTaken(username,email)) {
            System.out.println("Username or email already taken");
            registerUser();
        }
        else {
            User user = new User(displayname, email, username, password, bio);
            userRepository.save(user);
            System.out.println("registered successfully");
        }

    }

    public boolean login(String identifier, String password) throws SQLException {

        User user = userRepository.findByUsernameOrEmail(identifier);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser= user ;
            System.out.println("logged in successfully");
            return true;
        }
        System.out.println("username or password doesn't match");
        return false;
    }
    public void logout() throws SQLException {
        loggedInUser = null;
        System.out.println("logged out successfully");
    }

    public void editUserProfile() throws SQLException {
        boolean editing = true;

        while (editing) {
            System.out.println("What do you want to edit:");
            System.out.println("1. Display Name (current: " + loggedInUser.getDisplayName() + ")");
            System.out.println("2. Username (current: " + loggedInUser.getUsername() + ")");
            System.out.println("3. Bio (current: " + loggedInUser.getBio() + ")");
            System.out.println("4. Password");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new display name: ");
                    String newDisplayName = scanner.nextLine();
                    loggedInUser.setDisplayName(newDisplayName);
                    break;
                case 2:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    loggedInUser.setUsername(newUsername);
                    break;
                case 3:
                    System.out.print("Enter new bio: ");
                    String newBio = scanner.nextLine();
                    loggedInUser.setBio(newBio);
                    break;
                case 4:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    loggedInUser.setPassword(newPassword);
                    break;
                case 5:
                    editing = false;
                    continue;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            userRepository.updateUser(loggedInUser);
            System.out.println("Profile updated successfully. You can edit another field or exit.");
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}