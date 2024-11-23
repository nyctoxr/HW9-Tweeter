package repository;

import Tweeter.Datasource;
import entities.Tags;
import entities.Tweet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TweetTagsRepository {

    private static final String INSERT= """
            INSERT INTO tweet_tags (tweet_id, tag_id)
            VALUES (?, ?)
            """;

    private static final String DELETE= """
            DELETE FROM tweet_tags
            WHERE tweet_id = ? AND tag_id = ?
            """;
    private static final String FIND_TAGS_BY_TWEET_ID= """
            SELECT tags.*
            FROM tags
            JOIN tweet_tags ON tags.id = tweet_tags.tag_id
            WHERE tweet_tags.tweet_id = ?
            """;
    private static final String FIND_TWEETS_BY_TAG_ID= """
            SELECT tweets.*
            FROM tweets
            JOIN tweet_tags ON tweets.id = tweet_tags.tweet_id
            WHERE tweet_tags.tag_id = ?
            """;
    private static final String CHECK_ASSOCIATION= """
            SELECT COUNT(*)
            FROM tweet_tags
            WHERE tweet_id = ? AND tag_id = ?
            """;

    public void associateTagWithTweet(int tagId, int tweetId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(INSERT)) {
            statement.setInt(1, tweetId);
            statement.setInt(2, tagId);
            statement.executeUpdate();
        }
    }

    public void removeTagsFromTweet(int tweetId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(DELETE)) {
            statement.setInt(1, tweetId);
            statement.executeUpdate();
        }
    }


        public List<Tags> findTagsByTweetId(int tweetId) throws SQLException {
        List<Tags> tags = new ArrayList<>();
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(FIND_TAGS_BY_TWEET_ID)) {
            statement.setInt(1, tweetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Tags tag = new Tags(resultSet.getInt("id"), resultSet.getString("name"));
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public List<Tweet> findTweetsByTagId(int tagId) throws SQLException {
        List<Tweet> tweets = new ArrayList<>();
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(FIND_TWEETS_BY_TAG_ID)) {
            statement.setInt(1, tagId);
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

    public boolean isTagAssociatedWithTweet(int tagId, int tweetId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(CHECK_ASSOCIATION)) {
            statement.setInt(1, tweetId);
            statement.setInt(2, tagId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}