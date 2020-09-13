package lk.xtracheese.swiftsalon.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_stylist")
public class Stylist implements Parcelable {

    @PrimaryKey
    private int id;
    private String name;
    private String gender;
    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String img;
    private int status;
    @SerializedName("salon_id")
    @ColumnInfo(name = "salon_id")
    private int salID;

    @SerializedName("rating")
    @ColumnInfo(name = "rating")
    private float rating;

    public Stylist() {
    }

    protected Stylist(Parcel in) {
        name = in.readString();
        gender = in.readString();
        id = in.readInt();
        img = in.readString();
        status = in.readInt();
        salID = in.readInt();
        rating = in.readFloat();
    }

    public static final Creator<Stylist> CREATOR = new Creator<Stylist>() {
        @Override
        public Stylist createFromParcel(Parcel in) {
            return new Stylist(in);
        }

        @Override
        public Stylist[] newArray(int size) {
            return new Stylist[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSalID() {
        return salID;
    }

    public void setSalID(int salID) {
        this.salID = salID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeInt(id);
        dest.writeString(img);
        dest.writeInt(status);
        dest.writeInt(salID);
        dest.writeFloat(rating);
    }

    @Override
    public String toString() {
        return "Stylist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", img='" + img + '\'' +
                ", status=" + status +
                ", salID=" + salID +
                ", rating=" + rating +
                '}';
    }
}
