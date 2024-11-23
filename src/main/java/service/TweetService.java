package service;


import Tweeter.Main;
import entities.Tags;
import entities.Tweet;
import entities.User;
import repository.*;


import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static Tweeter.Main.retweetMenu;
import static service.UserService.loggedInUser;

public class TweetService {
    private final TweetRepository tweetRepository;
    private final TagRepository tagRepository;
    private final TweetTagsRepository tweetTagsRepository;
    private final LikesRepository likesRepository;
    private final Scanner scanner;
    private final LikeService likeService;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, LikesRepository likesRepository, TagRepository tagRepository) {
        this.tweetRepository = tweetRepository;
        this.likesRepository = likesRepository;
        this.tagRepository = tagRepository;
        this.tweetTagsRepository = new TweetTagsRepository();
        this.scanner = new Scanner(System.in);
        this.likeService = new LikeService();
        this.userRepository = new UserRepository();
    }

    public void postTweet() throws SQLException {
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
            Tweet tweet = new Tweet( content, loggedInUser.getId(), new Date(), new ArrayList<>(),null);
            tweet = tweetRepository.save(tweet);
            for (String tagName : tagNames) {
                Tags tag = tagRepository.findTagByName(tagName);
                if (tag == null) {
                    tag = new Tags(tagName);
                    tagRepository.save(tag);
                }
                tweetTagsRepository.associateTagWithTweet((int) tag.getId(), tweet.getId());
            }
        } else {
            System.out.println("Tweet has to be less than 280 characters!");
            Main.accountMenu(loggedInUser);
        }
    }

    public void displayAllTweets() throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            List<Tags> tags = tweetTagsRepository.findTagsByTweetId(tweet.getId());
            int likes = likesRepository.countLikes(tweet.getId());
            int dislikes = likesRepository.countDislikes(tweet.getId());
            User user = userRepository.findById(tweet.getUserId());

            System.out.println("\n\n" + "Tweet ID: " + tweet.getId() +
                    ", Content: " + tweet.getContent() +
                    ", Tags: " + tags +
                    "\nUser Name: " + user.getUsername() +
                    ", Likes: " + likes +
                    ", Dislikes: " + dislikes +
                    "\nCreated At: " + tweet.getCreatedAt());
            if (tweet.getRetweetId() != null) {
                System.out.println("This is a retweet of Tweet ID: " + tweet.getRetweetId());
            }
        }
        System.out.println("Enter tweet ID for Reaction or type 'skip' to continue ");
        String reaction = scanner.nextLine();
        if (reaction.equalsIgnoreCase("skip")) {
            retweetMenu(loggedInUser);
        }
        else {
            likeService.likeOrDislikeTweet(Integer.parseInt(reaction), loggedInUser.getId());
            retweetMenu(loggedInUser);
        }

    }


public void displayUserTweets(int userId) throws SQLException {
    List<Tweet> userTweets = tweetRepository.findTweetsByUserId(userId);
    for (Tweet tweet : userTweets) {
        List<Tags> tags = tweetTagsRepository.findTagsByTweetId(tweet.getId());
        tweet.setTags(tags);
        int likes = likesRepository.countLikes(tweet.getId());
        int dislikes = likesRepository.countDislikes(tweet.getId());
        System.out.println("\n\nTweet ID: " + tweet.getId() +
                ", Content: " + tweet.getContent() +
                ", Tags: " + tags +
                "\nLikes: " + likes +
                ", Dislikes: " + dislikes +
                "\nCreated At: " + tweet.getCreatedAt());
        if (tweet.getRetweetId() != null) {
            System.out.println("This is a retweet of Tweet ID: " + tweet.getRetweetId());
        }
    }
}

    public void editUserTweets(int userId, int tweetId) throws SQLException {
        Tweet userTweet = tweetRepository.getTweetByTweetId(userId);
        if (userTweet.getId() == tweetId) {
            System.out.println("Enter new content for the tweet: ");
            String newContent = scanner.nextLine();
            userTweet.setContent(newContent);
            tweetRepository.updateTweet(userTweet);
            System.out.println("Tweet updated successfully.");
        } else {
            System.out.println("You are not authorized to edit this tweet.");
        }
    }
    public void deleteTweet(int tweetId, int userId) throws SQLException {
        Tweet tweet = tweetRepository.getTweetByTweetId(tweetId);
        if (tweet != null && tweet.getUserId() == userId) {
            tweetTagsRepository.removeTagsFromTweet(tweetId);
            tweetRepository.deleteTweet(tweetId);
            System.out.println("Tweet deleted successfully.");
        } else {
            System.out.println("You are not authorized to delete this tweet.");
        }
    }
    public void retweet(int originalTweetId, int userId) throws SQLException {

        Tweet originalTweet = tweetRepository.getTweetByTweetId(originalTweetId);
        if (originalTweet == null) {
            throw new SQLException("Original tweet not found.");
        }

        if (originalTweet.getRetweetId() != null) {
            throw new SQLException("Cannot retweet a retweet.");
        }

        List<Tags> originalTags = tweetTagsRepository.findTagsByTweetId(originalTweetId);
        Tweet retweet = new Tweet(
                originalTweet.getContent(),
                userId,
                new Date(),
                originalTags,
                originalTweet.getId()
        );
        tweetRepository.save(retweet);

        for (Tags tag : originalTags) {
            tweetTagsRepository.associateTagWithTweet((int) tag.getId(), retweet.getId());
        }
        System.out.println("Retweet successful.");
    }

    public void checkTweetExist(User user, int tweetIdToRetweet) throws SQLException {
        Tweet tweet = tweetRepository.getTweetByTweetId(tweetIdToRetweet);
        if (tweet != null && tweet.getUserId() != user.getId()) {
            retweet(tweetIdToRetweet, user.getId());
        } else {
            System.out.println("Invalid tweet selected.");
        }
    }
}