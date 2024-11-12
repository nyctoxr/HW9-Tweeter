package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Likes {
    private int id;
    private int tweet_id;
    private int user_id;
    private boolean isLike;

    public Likes() {
    }
    public Likes(int tweet_id, int user_id, boolean isLike) {
        this.tweet_id = tweet_id;
        this.user_id = user_id;
        this.isLike = isLike;
    }

}
