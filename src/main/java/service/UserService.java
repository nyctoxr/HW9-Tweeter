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
    public User getLoggedInUser() {
        return loggedInUser;
    }

}