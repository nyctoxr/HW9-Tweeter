package service;

import repository.RetweetRepository;

import java.sql.SQLException;

public class RetweetService {
    private final RetweetRepository retweetRepository;
    public RetweetService(RetweetRepository retweetRepository) {
        this.retweetRepository = retweetRepository;
    }

    public void retweet(long tweetId, long userId) throws SQLException {
        retweetRepository.addRetweet(tweetId, userId);
    }

}
