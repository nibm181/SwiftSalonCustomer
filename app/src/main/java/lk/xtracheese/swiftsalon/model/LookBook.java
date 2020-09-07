package lk.xtracheese.swiftsalon.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_look_book")
public class LookBook {

    @PrimaryKey
    private int id;
    private String image;

    @Override
    public String toString() {
        return "LookBook{" +
                "id=" + id +
                ", image='" + image + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
