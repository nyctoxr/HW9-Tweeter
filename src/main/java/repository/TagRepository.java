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
