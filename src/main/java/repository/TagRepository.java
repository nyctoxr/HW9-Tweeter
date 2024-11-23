package repository;

import Tweeter.Datasource;
import entities.Tags;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepository {

    private static final String INSERT_TAG_SQL = """
            INSERT INTO tags(name)
            VALUES (?)
            RETURNING id
            """;
    private static final String READ_ALL_TAGS = """
            SELECT id, name
            FROM tags
            """;
    private static final String READ_TAG_NAMES_BY_TWEET_ID = """
            SELECT tags.* FROM tags
            JOIN tweet_tags ON tags.id = tweet_tags.tag_id
            WHERE tweet_tags.tweet_id = ?
            """;
    private static final String FIND_TAG_BY_NAME= """
            SELECT *
            FROM tags
            WHERE name = ?
            """;

    public static Tags save(Tags tag) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(INSERT_TAG_SQL)) {
            statement.setString(1, tag.getTag_name());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    tag.setId(rs.getLong(1));
                }
            }
            return tag;
        }
    }

    public List<Tags> getAllTags() throws SQLException {
        List<Tags> tags = new ArrayList<>();
        try (var statement = Datasource.getConnection().prepareStatement(READ_ALL_TAGS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    tags.add(new Tags(id, name));
                }
            }
        }
        return tags;
    }

    public Tags findTagByName(String name) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(FIND_TAG_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Tags(resultSet.getInt("id"), resultSet.getString("name"));
                }
            }
        }
        return null;
    }
}
