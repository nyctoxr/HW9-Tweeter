package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Tweet {
    private long id;
    private String content;
    private long userId;
    private Date createdAt;
    private List<Tags> tags;
    private boolean isRetweet;
    private Long originalTweetId;

    public Tweet(long id,String content,long userId,Date createdAt,List<Tags> tags,boolean isRetweet,Long originalTweetId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.tags = tags;
        this.isRetweet = isRetweet;
        this.originalTweetId = originalTweetId;
    }
    public Tweet(String content,long userId,Date createdAt,List<Tags> tags) {
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.tags = tags;
    }


    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                '}';
    }
}