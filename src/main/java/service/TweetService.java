package service;


import Tweeter.Main;
import entities.Tags;
import entities.Tweet;
import repository.TagRepository;
import repository.TweetRepository;
import repository.LikesRepository;
import service.UserService;

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
    private final List<Tweet> tweets;

    public TweetService(TweetRepository tweetRepository, LikesRepository likesRepository, UserService userService, TagRepository tagRepository) {
        this.tweetRepository = tweetRepository;
        this.likesRepository = likesRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
        this.scanner = new Scanner(System.in);
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

            System.out.println("Please enter tags (one at a time, type 'done' to finish, or 'add' to add a new tag): ");

            while (true) {
                String tagName = scanner.nextLine();
                if (tagName.equalsIgnoreCase("done")) {
                    break;
                }

                if (tagName.equalsIgnoreCase("add")) {
                    System.out.println("Please enter the new tag name: ");
                    String newTagName = scanner.nextLine();
                    Tags newTag = new Tags(newTagName, 0);
                    tagRepository.save(newTag);
                    tags.add(newTag);
                    continue;
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
                }
                tagExists = true;
                break;
            }
        }

        if (!tagExists) {
            System.out.println("Tag does not exist. Please enter a valid tag or type 'add' to add a new tag.");
        }
    }

    public void displayAllTweets() throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            List<Tags> tags = tagRepository.getTagsByTweetId(tweet.getId());
            tweet.setTags(tags);
            int likes = likesRepository.countLikes(tweet.getId());
            int dislikes = likesRepository.countDislikes(tweet.getId());
            System.out.println("Tweet ID: " + tweet.getId() + ", User ID: " + tweet.getUserId() + ", Content: " + tweet.getContent() + ", Created At: " + tweet.getCreatedAt() + ", Likes: " + likes + ", Dislikes: " + dislikes + ", Tags: " + tweet.getTags());
        }
    }

    public void displayUserTweets(int userId) throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            if (tweet.getUserId() == userId) {
                List<Tags> tags = tagRepository.getTagsByTweetId(tweet.getId());
                tweet.setTags(tags);
                int likes = likesRepository.countLikes(tweet.getId());
                int dislikes = likesRepository.countDislikes(tweet.getId());
                System.out.println("Tweet ID: " + tweet.getId() + ", Content: " + tweet.getContent() + ", Created At: " + tweet.getCreatedAt() + ", Likes: " + likes + ", Dislikes: " + dislikes + ", Tags: " + tweet.getTags());
            }
        }
    }
}