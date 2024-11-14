package Tweeter;

import entities.User;
import entities.Tweet;
import repository.LikesRepository;
import repository.TagRepository;
import repository.TweetRepository;
import repository.UserRepository;
import service.TweetService;
import service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static UserRepository userRepository = new UserRepository();
    static UserService userService = new UserService(userRepository);
    static TweetService tweetService = new TweetService(new TweetRepository(), new LikesRepository(), userService, new TagRepository());
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        while (true) {
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.print("Please Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                userService.registerUser();
            } else if (choice == 2) {
                System.out.print("ایمیل یا نام کاربری: ");
                String identifier = scanner.nextLine();
                System.out.print("رمز عبور: ");
                String password = scanner.nextLine();
                if (userService.login(identifier, password)) {
                    accountMenu(userService.getLoggedInUser());
                }
            }
        }
    }

    public static void accountMenu(User user) throws SQLException {
        while (true) {
            System.out.println("1. View all tweets");
            System.out.println("2. Post a tweet");
            System.out.println("3. View your tweets");
            System.out.println("4. Edit your profile");
            System.out.println("5. Logout");
            System.out.print("Please Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tweetService.displayAllTweets();
                    break;
                case 2:
                    tweetService.postTweet();
                    break;
                case 3:
                    tweetService.displayUserTweets((int) user.getId());
                    break;
                case 4:

                    System.out.println("Edit your profile");
                    break;
                case 5:
                    userService.logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}