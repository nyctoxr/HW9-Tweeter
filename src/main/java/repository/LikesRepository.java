package repository;

import Tweeter.Datasource;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesRepository {
    private static final String INSERT_SQL = """
            INSERT INTO likes(tweet_id,user_id,is_like)
            VALUES (?,?,?) ON DUPLICATE KEY UPDATE is_like="true"
            """;
    private static final String REMOVE_SQL = """
            DELETE FROM likes 
            WHERE tweet_id=? AND user_id=?
            """;
    private static final String COUNT_LIKES = """
            SELECT COUNT(*) FROM likes
            WHERE tweet_id=? AND is_like="true" 
            """;
    private static final String COUNT_DISLIKES = """
            SELECT COUNT(*) FROM likes
            WHERE tweet_id=? AND is_like="false" 
            """;

    public User save(User user) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.execute();
        }
        return user;
    }
    public void delete(int tweetid,int userid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(REMOVE_SQL)) {
            statement.setInt(1, tweetid);
            statement.setInt(2, userid);
            statement.executeUpdate();
        }
    }
    public int countLikes(int tweetid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(COUNT_LIKES)) {
            statement.setInt(1, tweetid);
            ResultSet rs = statement.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    public int countDislikes(int tweetid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(COUNT_DISLIKES)) {
            statement.setInt(1, tweetid);
            ResultSet rs = statement.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
