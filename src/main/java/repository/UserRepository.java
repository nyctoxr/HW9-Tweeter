package repository;

import Tweeter.Datasource;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String INSERT_SQL = """
            INSERT INTO users(displayname, email, username, password, bio)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String AUTH_REG = """
            SELECT COUNT(*) FROM users
            WHERE username = ? OR email = ?
            """;
    private static final String AUTH_LOGIN = """
            SELECT * FROM users
            WHERE username = ? OR email = ?
            """;
    private static final String FIND_BY_USER_ID = """
            SELECT * FROM users
            WHERE id = ?
            """;
    private static final String EDIT_USER = """  
            UPDATE users
            SET displayname = ?,
                email=? ,
                username = ?,
                password = ?,
                bio = ?
            WHERE id = ?
            """;

    public User save(User user) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, user.getDisplayName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getBio());
            statement.execute();
        }
        return user;
    }

    public boolean isUsernameOrEmailTaken(String username, String email) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(AUTH_REG)) {
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public User findByUsernameOrEmail(String identifier) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(AUTH_LOGIN)) {
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("displayname"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("bio")
                );
            }
        }
        return null;
    }
    public User findById(long userId) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(FIND_BY_USER_ID)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("displayname"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("bio")
                );
            }
        }
        return null;
    }

    public void updateUser(User user) throws SQLException {
        try (PreparedStatement statement = Datasource.getConnection().prepareStatement(EDIT_USER)) {
            statement.setString(1, user.getDisplayName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getBio());

            statement.setInt(6, user.getId());
            statement.executeUpdate();
        }
    }
}