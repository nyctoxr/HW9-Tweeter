package repository;

import Tweeter.Datasource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesRepository {
    private static final String INSERT_SQL = """
        INSERT INTO likes(tweet_id, user_id, is_like)
        VALUES (?, ?, ?)
        ON CONFLICT (tweet_id, user_id) DO UPDATE SET is_like = EXCLUDED.is_like
        """;
    private static final String REMOVE_SQL = """
            DELETE FROM likes
            WHERE tweet_id = ? AND user_id = ?
            """;
    private static final String COUNT_LIKES = """
            SELECT COUNT(*) FROM likes
            WHERE tweet_id = ? AND is_like = true
            """;
    private static final String COUNT_DISLIKES = """
            SELECT COUNT(*) FROM likes
            WHERE tweet_id = ? AND is_like = false
            """;
    private static final String GET_LIKE_STATUS = """
            SELECT is_like FROM likes
            WHERE tweet_id = ? AND user_id = ?
            """;

    public void save(long tweetId, long userId, boolean isLike) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setLong(1, tweetId);
            statement.setLong(2, userId);
            statement.setBoolean(3, isLike);
            statement.executeUpdate();
        }
    }
    public void delete(int tweetId, int userId) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(REMOVE_SQL)) {
            statement.setLong(1, tweetId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }
    public int countLikes(long tweetid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(COUNT_LIKES)) {
            statement.setLong(1, tweetid);
            ResultSet rs = statement.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    public int countDislikes(long tweetid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(COUNT_DISLIKES)) {
            statement.setLong(1, tweetid);
            ResultSet rs = statement.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    public static boolean getLikeStatus(int tweetid, int userid) throws SQLException {
        try (var statement = Datasource.getConnection().prepareStatement(GET_LIKE_STATUS)) {
            statement.setLong(1, tweetid);
            statement.setInt(2, userid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_like");
                } else {
                    return false;
                }
            }
        }
    }
}
