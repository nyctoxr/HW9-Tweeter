package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Tweet {
    private int id;
    private String content;
    private int userId;
    private Date createdAt;
    private List<Tags> tags;


    public Tweet(int id,String content,int userId,Date createdAt,List<Tags> tags) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.tags = tags;

    }
    public Tweet(String content,int userId,Date createdAt,List<Tags> tags) {
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