package repository;

import Tweeter.Datasource;
import entities.Tags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepository {

    private static final String INSERT_TAG_SQL = """
            INSERT INTO tags(name, tweet_id)
            VALUES (?, ?)
            """;
    private static final String READ_TAGS_BY_TWEET_ID = """
            SELECT id, name, tweet_id
            FROM tags
            WHERE tweet_id = ?
            """;
    private static final String READ_ALL_TAGS = """
            SELECT id, name, tweet_id
            FROM tags
            """;
    private static final String READ_TAG_NAMES_BY_TWEET_ID = """
            SELECT name
            FROM tags
            WHERE tweet_id = ?
            """;

    public void save(Tags tag) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(INSERT_TAG_SQL)) {
            statement.setString(1, tag.getTag_name());
            statement.setLong(2, tag.getTweet_id());
            statement.executeUpdate();
        }
    }

    public List<Tags> getTagsByTweetId(long tweetId) throws SQLException {
        List<Tags> tags = new ArrayList<>();
        try (var statement = Datasource.getConnection().prepareStatement(READ_TAGS_BY_TWEET_ID)) {
            statement.setLong(1, tweetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int tagTweetId = resultSet.getInt("tweet_id");
                    tags.add(new Tags(id, name, tagTweetId));
                }
            }
        }
        return tags;
    }

    public List<Tags> getAllTags() throws SQLException {
        List<Tags> tags = new ArrayList<>();
        try (var statement = Datasource.getConnection().prepareStatement(READ_ALL_TAGS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int tagTweetId = resultSet.getInt("tweet_id");
                    tags.add(new Tags(id, name, tagTweetId));
                }
            }
        }
        return tags;
    }

public List<String> getTagNamesByTweetId(long tweetId) throws SQLException {
    List<String> tagNames = new ArrayList<>();
    try (var statement = Datasource.getConnection().prepareStatement(READ_TAG_NAMES_BY_TWEET_ID)) {
        statement.setInt(1, tweetId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                tagNames.add(name);
            }
        }
    }
    return tagNames;
}
}
