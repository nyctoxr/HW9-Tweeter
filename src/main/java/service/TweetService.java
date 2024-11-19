package service;


import Tweeter.Main;
import entities.Tags;
import entities.Tweet;
import entities.User;
import repository.TagRepository;
import repository.TweetRepository;
import repository.LikesRepository;
import repository.UserRepository;


import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static service.UserService.loggedInUser;

public class TweetService {
    TweetRepository tweetRepository = new TweetRepository();
    private final LikesRepository likesRepository;
    private UserService userService;
    private final Scanner scanner;
    private final TagRepository tagRepository;
    private LikeService likeService;
    private UserRepository userRepository;
    private final List<Tweet> tweets;

    public TweetService(TweetRepository tweetRepository, LikesRepository likesRepository, UserService userService, TagRepository tagRepository) {
        this.tweetRepository = tweetRepository;
        this.likesRepository = likesRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
        this.scanner = new Scanner(System.in);
        this.likeService = new LikeService();
        this.userRepository = new UserRepository();
        this.tweets = new ArrayList<>();

    }

    public void postTweet() throws SQLException {
        System.out.println("Please enter your tweet content: ");
        String content = scanner.nextLine();
        if (content.length() <= 280) {
            List<Tags> tags = new ArrayList<>();
            List<Tags> availableTags = tagRepository.getAllTags();

            System.out.println("Available tags:");
            for (Tags tag : availableTags) {
                System.out.println(tag.getTag_name());
            }

            System.out.println("Please enter tags (one at a time, type 'done' to finish): ");

            while (true) {
                String tagName = scanner.nextLine();
                if (tagName.equalsIgnoreCase("done")) {
                    break;
                }


                addTagIfValid(tags, availableTags, tagName);
            }

            Tweet tweet = new Tweet(content, loggedInUser.getId(), new Date(), tags);
            tweet = tweetRepository.save(tweet);
            for (Tags tag : tags) {
                tag.setTweet_id(tweet.getId());
                tagRepository.save(tag);
            }
        } else {
            System.out.println("Tweet has to be less than 280 characters!");
            Main.accountMenu(loggedInUser);
        }
    }

    private void addTagIfValid(List<Tags> tags, List<Tags> availableTags, String tagName) {
        boolean tagExists = false;

        for (Tags tag : tags) {
            if (tag.getTag_name().equalsIgnoreCase(tagName)) {
                tagExists = true;
                break;
            }
        }


        for (Tags tag : availableTags) {
            if (tag.getTag_name().equalsIgnoreCase(tagName)) {
                if (!tagExists) {
                    tags.add(tag);
                    System.out.println("Tag " + tagName + " added to your tweet.");
                }
                tagExists = true;
                break;
            }
        }

        if (!tagExists) {
            System.out.println("Tag does not exist. Please enter a valid tag.");
        }
    }

    public void displayAllTweets() throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            List<String> tags = tagRepository.getTagNamesByTweetId((int) tweet.getId());

            int likes = likesRepository.countLikes(tweet.getId());
            int dislikes = likesRepository.countDislikes(tweet.getId());
            User user = userRepository.findById(tweet.getUserId());
            System.out.println("\n\n"+"Tweet ID: " + tweet.getId() +  ", Content: " + tweet.getContent() + "  , Tags: " + tags + "\n UserName: " + user.getUsername()  + ", Likes: " + likes + ", Dislikes: " + dislikes + "\n Created At: " + tweet.getCreatedAt()) ;
        }
        System.out.println("Enter tweet ID for Reaction: ");
        int tweetid = scanner.nextInt();
        likeService.likeOrDislikeTweet(tweetid, (int) loggedInUser.getId());
    }

public void displayUserTweets(int userId) throws SQLException {
    List<Tweet> userTweets = tweetRepository.getTweetByUserId(userId);
    for (Tweet tweet : userTweets) {
        List<String > tags = tagRepository.getTagNamesByTweetId((int) tweet.getId());
        int likes = likesRepository.countLikes(tweet.getId());
        int dislikes = likesRepository.countDislikes(tweet.getId());
        System.out.println("\n"+"Tweet ID: " + tweet.getId() +  ", Content: " + tweet.getContent() + "  , Tags: " + tags  + "\nLikes: " + likes + ", Dislikes: " + dislikes + "\n Created At: " + tweet.getCreatedAt());
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
            tweetRepository.deleteTweet(tweetId);
            System.out.println("Tweet deleted successfully.");
        } else {
            System.out.println("You are not authorized to delete this tweet.");
        }
    }
}