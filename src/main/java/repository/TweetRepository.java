package repository;

import Tweeter.Datasource;
import entities.Tweet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TweetRepository {

    private static final String INSERT_SQL = """
            INSERT INTO tweets(content,user_id,created_at,retweet_id)
            VALUES (?,?,?,?)
            RETURNING id
            """;

    private static final String READ_ALL_TWEETS = """
            Select * from tweets
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
    private static final String FIND_TWEETS_BY_USER_ID = """ 
            SELECT *
            FROM tweets AS t
            LEFT JOIN likes AS l ON t.id=l.tweet_id
            WHERE t.user_id = ?
            """;


    private final List<Tweet> tweets = new ArrayList<>();

    public Tweet save(Tweet tweet) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, tweet.getContent());
            statement.setInt(2, tweet.getUserId());
            statement.setTimestamp(3, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
            if (tweet.getRetweetId() != null) {
                statement.setLong(4, tweet.getRetweetId());
            }else{
                    statement.setNull(4, java.sql.Types.INTEGER);
                }
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    tweet.setId(resultSet.getInt("id"));
                }
            }
        }
        return tweet;
    }

    public List<Tweet> getAllTweets() throws SQLException {
        if (tweets.isEmpty()) {
            try (var statement = Datasource.getConnection().prepareStatement(READ_ALL_TWEETS);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Tweet tweet = new Tweet(resultSet.getInt("id"),
                            resultSet.getString("content"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("created_at"),
                            new ArrayList<>(),
                            resultSet.getInt("retweet_id")
                    );
                    tweets.add(tweet);
                }
            }
        }
        return tweets;
    }

    public List<Tweet> findTweetsByUserId(int userId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(FIND_TWEETS_BY_USER_ID)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Tweet tweet = new Tweet(
                            resultSet.getInt("id"),
                            resultSet.getString("content"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("created_at"),
                            new ArrayList<>(),
                            resultSet.getInt("retweet_id")
                    );
                    tweets.add(tweet);
                }
            }
        }
        return tweets;
    }


    public Tweet getTweetByTweetId(long tweetId) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(GET_TWEET_BY_TWEET_ID)) {
            statement.setLong(1, tweetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    int userId = resultSet.getInt("user_id");
                    Date createdAt = resultSet.getTimestamp("created_at");
                    Integer retweetId = resultSet.getInt("retweet_id");
                    if (resultSet.wasNull()) {
                        retweetId = null;
                    }

                    return new Tweet(id, content, userId, createdAt, new ArrayList<>(),retweetId);
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

