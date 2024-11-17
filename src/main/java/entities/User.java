package entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class User {
    private int id;
    private String displayName;
    private String email;
    private String username;
    private String password;
    private String bio;

    public User(int id, String displayName, String email, String username, String password, String bio) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
    }
    public User(String displayName, String email, String username, String password, String bio) {
        this.displayName = displayName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
    }

}