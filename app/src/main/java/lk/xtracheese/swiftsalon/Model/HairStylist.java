package lk.xtracheese.swiftsalon.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HairStylist implements Parcelable {

    private String name;
    private String gender;
    private int id;
    private String img;
    private boolean status;
    private int salID;
    private long rating;

    public HairStylist() {
    }

    protected HairStylist(Parcel in) {
        name = in.readString();
        gender = in.readString();
        id = in.readInt();
        img = in.readString();
        status = in.readByte() != 0;
        salID = in.readInt();
        rating = in.readLong();
    }

    public static final Creator<HairStylist> CREATOR = new Creator<HairStylist>() {
        @Override
        public HairStylist createFromParcel(Parcel in) {
            return new HairStylist(in);
        }

        @Override
        public HairStylist[] newArray(int size) {
            return new HairStylist[size];
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSalID() {
        return salID;
    }

    public void setSalID(int salID) {
        this.salID = salID;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
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
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(salID);
        dest.writeLong(rating);
    }
}
