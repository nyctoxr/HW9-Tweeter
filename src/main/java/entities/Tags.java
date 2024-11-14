package entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Tags {
    private long id;
    private String tag_name;
    private long tweet_id;

    public Tags(long id, String tag_name, long tweet_id) {
        this.id = id;
        this.tag_name = tag_name;
        this.tweet_id = tweet_id;
    }
    public Tags(String tag_name, long tweet_id) {
        this.tag_name = tag_name;
        this.tweet_id = tweet_id;
    }
}
