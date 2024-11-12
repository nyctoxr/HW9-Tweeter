package repository;
import Tweeter.Datasource;
import entities.Tweet;
import entities.User;

import java.sql.SQLException;


public class TweetRepository {

    private static final String INSERT_SQL = """
            INSERT INTO tweets(content,user_id,created_at)
            VALUES (?,?,?)
            """;
    public Tweet save(Tweet tweet) throws SQLException {

        try (var statement = Datasource.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, tweet.getContent());
            statement.setInt(2, tweet.getUserId());
            statement.setTimestamp(3, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
            statement.execute();
        }
        return tweet;
    }


}