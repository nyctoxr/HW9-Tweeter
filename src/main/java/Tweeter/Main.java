package Tweeter;

import entities.User;
import repository.LikesRepository;
import repository.TagRepository;
import repository.TweetRepository;
import repository.UserRepository;
import service.LikeService;
import service.TweetService;
import service.UserService;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static UserRepository userRepository = new UserRepository();
    static UserService userService = new UserService(userRepository);
    static TweetService tweetService = new TweetService(new TweetRepository(), new LikesRepository(),new TagRepository());
    static LikeService likeService = new LikeService();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        while (true) {
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.print("Please Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("Please enter your display name: ");
                String displayName = scanner.nextLine();
                System.out.println("Please enter your email address: ");
                String email = scanner.nextLine();
                System.out.println("Please enter your username: ");
                String username = scanner.nextLine();
                System.out.println("Please enter your password: ");
                String password = scanner.nextLine();
                System.out.println("Please enter your bio: ");
                String bio = scanner.nextLine();

                userService.registerUser(displayName, email, username, password, bio);
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
                    System.out.println("Enter tweet ID for Reaction or type 'skip' to continue ");
                    String reaction = scanner.nextLine();
                    if (!reaction.equalsIgnoreCase("skip")) {
                        try {
                            int tweetId = Integer.parseInt(reaction);
                            System.out.println("Do you want to like (L) or dislike (D) the tweet? (Enter 'L' or 'D'): ");
                            String likeChoice = scanner.nextLine();
                            likeService.likeOrDislikeTweet(tweetId, user.getId(), likeChoice);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid tweet ID.");
                        }
                    }
                    retweetMenu(user);
                    break;
                case 2:
                    System.out.println("Please enter your tweet content: ");
                    String content = scanner.nextLine();
                    if (content.length() <= 280) {
                        List<String> tagNames = new ArrayList<>();
                        System.out.println("Please enter tags (one at a time, type 'done' to finish): ");
                        while (true) {
                            String tagName = scanner.nextLine();
                            if (tagName.equalsIgnoreCase("done")) {
                                break;
                            }
                            tagNames.add(tagName);
                        }

                       tweetService.postTweet(content, tagNames);
                    }

                    break;
                case 3:
                    tweetService.displayUserTweets(user.getId());
                    System.out.println("       ------       ");
                    tweetsMenu(user);
                    break;
                case 4:
                    boolean editing = true;
                    while (editing) {
                        System.out.println("What do you want to edit:");
                        System.out.println("1. Display Name (current: " + user.getDisplayName() + ")");
                        System.out.println("2. Email (current: " + user.getEmail() + ")");
                        System.out.println("3. Username (current: " + user.getUsername() + ")");
                        System.out.println("4. Bio (current: " + user.getBio() + ")");
                        System.out.println("5. Password");
                        System.out.println("6. Exit");

                        int editChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (editChoice == 6) {
                            editing = false;
                            continue;
                        }

                        System.out.print("Enter new value: ");
                        String newValue = scanner.nextLine();
                        userService.editUserProfile(editChoice, newValue);
                    }
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
        } else {
            try {
                int tweetId = Integer.parseInt(tweetIdToRetweet);
                tweetService.checkTweetExist(user, tweetId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid tweet ID.");
            }
        }
    }

    public static void tweetsMenu(User user) throws SQLException {
        while (true) {

            System.out.println("1. Edit a tweet");
            System.out.println("2. Delete a tweet");
            System.out.println("3. Back to main menu");
            System.out.print("Please Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tweetService.displayUserTweets(user.getId());
                    System.out.print("Enter the tweet ID you want to edit: ");
                    int tweetIdToEdit = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter the new content for the tweet: ");
                    String newContent = scanner.nextLine();
                    tweetService.editUserTweets(user.getId(), tweetIdToEdit, newContent);
                    break;
                case 2:
                    tweetService.displayUserTweets(user.getId());
                    System.out.print("Enter the tweet ID you want to delete: ");
                    int tweetIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    tweetService.deleteTweet(tweetIdToDelete, user.getId());
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}