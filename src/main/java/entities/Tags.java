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

    public Tags(long id, String tag_name) {
        this.id = id;
        this.tag_name = tag_name;

    }
    public Tags(String tag_name) {
        this.tag_name = tag_name;
    }
}
