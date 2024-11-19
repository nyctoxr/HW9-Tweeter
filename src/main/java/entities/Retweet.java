package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Retweet {
    private long id;
    private long tweetId;
    private long userId;
    private Date createdAt;

    public Retweet(long id, long tweetId, long userId, Date createdAt) {
        this.id = id;
        this.tweetId = tweetId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

}