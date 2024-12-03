package service;
import repository.LikesRepository;
import java.sql.SQLException;
public class LikeService {

    private final LikesRepository likesRepository;

    public LikeService() {
        likesRepository = new LikesRepository();
    }


    public void likeOrDislikeTweet(int tweetId, int userId, String choice) throws SQLException {
        boolean currentStatus = LikesRepository.getLikeStatus(tweetId, userId);

        if (!currentStatus) {
            boolean isLike = choice.equalsIgnoreCase("L");

            if (choice.equalsIgnoreCase("L") || choice.equalsIgnoreCase("D")) {
                likesRepository.save(tweetId, userId, isLike);
                System.out.println("Tweet " + (isLike ? "liked" : "disliked") + " successfully.");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } else {
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
