package Tweeter;

import entities.User;
import repository.LikesRepository;
import repository.TagRepository;
import repository.TweetRepository;
import repository.UserRepository;
import service.TweetService;
import service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static UserRepository userRepository = new UserRepository();
    static UserService userService = new UserService(userRepository);
    static TweetService tweetService = new TweetService(new TweetRepository(), new LikesRepository(),new TagRepository());
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
                System.out.print("Enter email or username to login: ");
                String identifier = scanner.nextLine();
                System.out.print("Enter password: ");
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
                    tweetService.displayUserTweets(user.getId());
                    System.out.println("       ------       ");
                    tweetsMenu(user);
                    break;
                case 4:
                    userService.editUserProfile();
                    break;
                case 5:
                    userService.logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void retweetMenu(User user) throws SQLException {
        System.out.print("Enter the tweet ID to retweet or type 'exit' to exit: ");
        String tweetIdToRetweet = scanner.nextLine();
        if (tweetIdToRetweet.equals("exit")) {
            accountMenu(userService.getLoggedInUser());
        }
        else {
            scanner.nextLine();
            tweetService.checkTweetExist(user, Integer.parseInt(tweetIdToRetweet));
        }
    }


    public static void tweetsMenu(User user) throws SQLException {
        while (true) {
            System.out.println("1. Retweet");
            System.out.println("2. Edit a tweet");
            System.out.println("3. Delete a tweet");
            System.out.println("4. Back to main menu");
            System.out.print("Please Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tweetService.displayUserTweets(user.getId());
                    break;
                case 2:
                    tweetService.displayUserTweets(user.getId());
                    System.out.print("Enter the tweet ID you want to edit: ");
                    int tweetIdToEdit = scanner.nextInt();
                    scanner.nextLine();
                    tweetService.editUserTweets(user.getId(), tweetIdToEdit);
                    break;
                case 3:
                    tweetService.displayUserTweets(user.getId());
                    System.out.print("Enter the tweet ID you want to delete: ");
                    int tweetIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    tweetService.deleteTweet(tweetIdToDelete, user.getId());
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


}