package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Tweet {
    private int id;
    private String content;
    private int userId;
    private Date createdAt;

    public Tweet(int id,String content,int userId,Date createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }
    public Tweet(String content,int userId,Date createdAt) {
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
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