package repository;

import Tweeter.Datasource;
import entities.Tweet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TweetRepository {

    private static final String INSERT_SQL = """
            INSERT INTO tweets(content,user_id,created_at)
            VALUES (?,?,?)
            """;

    private static final String READ_ALL_TWEETS = """
            SELECT * FROM tweets
            LEFT JOIN likes ON tweets.id=likes.tweet_id
            GROUP BY tweets.id ,tweets.user_id,tweets.created_at,likes.id
            """;
    private static final String GET_TWEETS_BY_USER_ID = """
            SELECT * FROM tweets
            LEFT JOIN likes ON tweets.id=likes.tweet_id
            WHERE tweets.user_id=?
            """;
    private static final String UPDATE_SQL = """
            UPDATE tweets SET content = ? WHERE id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM tweets WHERE id = ?
            """;
    private static final String GET_TWEET_BY_TWEET_ID = """
            SELECT * FROM tweets
            LEFT JOIN likes ON tweets.id=likes.tweet_id
            WHERE tweets.id=?
            """;

    private final List<Tweet> tweets = new ArrayList<>();

    public Tweet save(Tweet tweet) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, tweet.getContent());
            statement.setLong(2, tweet.getUserId());
            statement.setTimestamp(3, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
            statement.execute();
        }
        return tweet;
    }

    public List<Tweet> getAllTweets() throws SQLException {
        if (tweets.isEmpty()) {
            try (var statement = Datasource.getConnection().prepareStatement(READ_ALL_TWEETS);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    long userId = resultSet.getInt("user_id");
                    java.util.Date createdAt = resultSet.getTimestamp("created_at");
                    Tweet tweet = new Tweet(id, content, userId, createdAt, new ArrayList<>());
                    tweets.add(tweet);
                }
            }
        }
        return tweets;
    }

    public List<Tweet> getTweetByUserId(long userId) throws SQLException {
        List<Tweet> userTweets = new ArrayList<>();
        try (var statement = Datasource.getConnection().prepareStatement(GET_TWEETS_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long tweetId = resultSet.getLong("id");
                    String content = resultSet.getString("content");
                    long tweetUserId = resultSet.getLong("user_id");
                    java.util.Date createdAt = resultSet.getTimestamp("created_at");
                    Tweet tweet = new Tweet(tweetId, content, tweetUserId, createdAt, new ArrayList<>());
                    userTweets.add(tweet);
                }
            }
        }
        return userTweets;
    }

    public Tweet getTweetByTweetId(long tweetId) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(GET_TWEET_BY_TWEET_ID)) {
            statement.setLong(1, tweetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String content = resultSet.getString("content");
                    long userId = resultSet.getLong("user_id");
                    Date createdAt = resultSet.getTimestamp("created_at");
                    return new Tweet(id, content, userId, createdAt, new ArrayList<>());
                }
            }
        }
        return null;
    }

    public void updateTweet(Tweet tweet) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, tweet.getContent());
            statement.setLong(2, tweet.getUserId());
            statement.executeUpdate();
        }
    }
    public void deleteTweet(long tweetid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(DELETE_SQL)) {
            statement.setLong(1, tweetid);
            statement.executeUpdate();
        }
    }
}

