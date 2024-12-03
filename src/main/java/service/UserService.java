package service;

import entities.User;
import exceptions.UserAlreadyExists;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    public static User loggedInUser;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    public void registerUser(String displayName, String email, String username, String password, String bio) throws SQLException , UserAlreadyExists {

            if (userRepository.isUsernameOrEmailTaken(username, email)) {
                throw new UserAlreadyExists("Username or email already exists");
            } else {
                String hashedPassword = hashPassword(password);
                User user = new User(displayName, email, username, hashedPassword, bio);
                userRepository.save(user);
                System.out.println("Registered successfully");
            }
    }

    public boolean login(String identifier, String password) throws SQLException {

        User user = userRepository.findByUsernameOrEmail(identifier);
        if (user != null && checkPassword(password, user.getPassword())) {
            loggedInUser= user ;
            System.out.println("logged in successfully");
            return true;
        }
        System.out.println("username or password doesn't match");
        return false;
    }
    public void logout() {
        loggedInUser = null;
        System.out.println("logged out successfully");
    }

    public void editUserProfile(int choice, String newValue) throws SQLException {
        switch (choice) {
            case 1:
                loggedInUser.setDisplayName(newValue);
                break;
            case 2:
                loggedInUser.setEmail(newValue);
                break;
            case 3:
                loggedInUser.setUsername(newValue);
                break;
            case 4:
                String hashedPassword = hashPassword(newValue);
                loggedInUser.setPassword(hashedPassword);
                break;
            case 5:
                loggedInUser.setBio(newValue);
            default:
                System.out.println("Invalid choice. Please try again.");
                return;
        }
        userRepository.updateUser(loggedInUser);
        System.out.println("Profile updated successfully.");
    }
    public User getLoggedInUser() {
        return loggedInUser;
    }
}