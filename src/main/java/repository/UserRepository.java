package repository;

import Tweeter.Datasource;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String INSERT_SQL = """
            INSERT INTO users(displayname,email,username,password,bio)
            VALUES (?,?,?,?,?)
            """;
    private static final String AUTH_REG = """
            SELECT COUNT(*) FROM users(username,password)
            WHERE username = ? OR email = ?
            """;
    private static final String AUTH_LOGIN = """
            SELECT COUNT(*) FROM users(username,password)
            WHERE username = ? OR email = ?
            """;
    private static final String READ_USERS = """
            SELECT * FROM users
            """;

    public User save(User user) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.execute();
        }
        return user;
    }

    public boolean isUsernameOrEmailTaken(String username, String email) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(AUTH_REG)) {
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public User findByUsernameOrEmail(String identifire) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(AUTH_LOGIN)) {
            statement.setString(1, identifire);
            statement.setString(2, identifire);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"), // اگر شناسه کاربر وجود دارد
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

    public static List<User> findAll() throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(READ_USERS)) {
            ResultSet rs = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                int userid = rs.getInt("id");
                String displayname = rs.getString("displayname");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String bio = rs.getString("bio");
                users.add(new User(userid, displayname, email, username, password, bio));
            }
            return new ArrayList<>(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}