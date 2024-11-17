package service;

import repository.LikesRepository;

import java.sql.SQLException;
import java.util.Scanner;


public class LikeService {

    private final LikesRepository likesRepository;

    private final Scanner scanner;

    public LikeService() {

        likesRepository = new LikesRepository();

        scanner = new Scanner(System.in);

    }


    public void likeOrDislikeTweet(int tweetId, int userId) throws SQLException {
        boolean currentStatus = LikesRepository.GetLikeStatus(tweetId, userId);

        if (!currentStatus) {
            System.out.println("Do you want to like (L) or dislike (D) the tweet? (Enter 'L' or 'D'): ");
            String choice = scanner.nextLine();

            boolean isLike = choice.equalsIgnoreCase("L");

            if (choice.equalsIgnoreCase("L") || choice.equalsIgnoreCase("D")) {
                likesRepository.save(tweetId, userId, isLike);
                System.out.println("Tweet " + (isLike ? "liked" : "disliked") + " successfully.");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("You have already liked this tweet. Do you want to undo your like (U) or dislike (D) it? (Enter 'U' or 'D'): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("U")) {
                likesRepository.delete(tweetId, userId);
                System.out.println("Like removed successfully.");
            } else if (choice.equalsIgnoreCase("D")) {
                likesRepository.save(tweetId, userId, false);
                System.out.println("Tweet disliked successfully.");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
