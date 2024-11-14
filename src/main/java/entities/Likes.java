package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Likes {
    private long id;
    private long tweet_id;
    private long user_id;
    private boolean isLike;

    public Likes() {
    }
    public Likes(long tweet_id, long user_id, boolean isLike) {
        this.tweet_id = tweet_id;
        this.user_id = user_id;
        this.isLike = isLike;
    }

}
