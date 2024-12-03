package repository;

import Tweeter.Datasource;
import entities.Tags;

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
            SELECT name
            FROM tags
            JOIN tweet_tags ON tags.id = tweet_tags.tag_id
            WHERE tweet_tags.tweet_id = ?
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
                    Tags tag = new Tags(resultSet.getString("name"));
                    tags.add(tag);
                }
            }
        }
        return tags;
    }
}