package service;


import Tweeter.Main;
import entities.Tweet;
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
    private final List<Tweet> tweets;

    public TweetService(TweetRepository tweetRepository, LikesRepository likesRepository, UserService userService) {
        this.tweetRepository = tweetRepository;
        this.likesRepository = likesRepository;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
        this.tweets = new ArrayList<>();
    }
    public void postTweet() throws SQLException {
        System.out.println("please enter your tweet content: ");
        String content = scanner.nextLine();
        if (content.length() <= 280) {
            Tweet tweet = new Tweet(content, loggedInUser.getId(), new Date());
            tweetRepository.save(tweet);
            tweets.add(tweet);
        }
        else {
            System.out.println("tweet has to be less than 280 characters!");
            Main.accountMenu(loggedInUser);
        }
    }

    public void displayAllTweets() throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            int likes = likesRepository.countLikes(tweet.getId());
            int dislikes = likesRepository.countDislikes(tweet.getId());
            System.out.println("Tweet ID: " + tweet.getId() + ", User ID: " + tweet.getUserId() + ", Content: " + tweet.getContent() + ", Created At: " + tweet.getCreatedAt() + ", Likes: " + likes + ", Dislikes: " + dislikes);
        }
    }

    public void displayUserTweets(int userId) throws SQLException {
        List<Tweet> allTweets = tweetRepository.getAllTweets();
        for (Tweet tweet : allTweets) {
            if (tweet.getUserId() == userId) {
                int likes = likesRepository.countLikes(tweet.getId());
                int dislikes = likesRepository.countDislikes(tweet.getId());
                System.out.println("Tweet ID: " + tweet.getId() + ", Content: " + tweet.getContent() + ", Created At: " + tweet.getCreatedAt() + ", Likes: " + likes + ", Dislikes: " + dislikes);
            }
        }
    }
}