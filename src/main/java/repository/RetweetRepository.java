package repository;

import Tweeter.Datasource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RetweetRepository {
    private static final String INSERT = """  
            INSERT INTO retweets(tweet_id, user_id, created_at)
            VALUES (?, ?, ?)
            """;
    public void addRetweet(long tweetId, long userId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(INSERT)) {
            statement.setLong(1, tweetId);
            statement.setLong(2, userId);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        }
    }









}
