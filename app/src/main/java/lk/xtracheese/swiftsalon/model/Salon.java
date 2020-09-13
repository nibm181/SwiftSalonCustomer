package lk.xtracheese.swiftsalon.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_salon")
public class Salon implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int salID;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String salonName;

    @ColumnInfo(name = "addr1")
    @SerializedName("addr1")
    private String salonAddress1;

    @ColumnInfo(name = "addr2")
    @SerializedName("addr2")
    private String salonAddress2;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ColumnInfo(name = "open_time")
    @SerializedName("open_time")
    private String openTime;

    @ColumnInfo(name = "close_time")
    @SerializedName("close_time")
    private String closeTime;

    @SerializedName("image")
    private String image;

    @ColumnInfo(name = "mobile_no")
    @SerializedName("mobile_no")
    private String mobileNo;

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    private float rating;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    private String latitude;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public void setSalonAddress1(String salonAddress1) {
        this.salonAddress1 = salonAddress1;
    }

    public void setSalonAddress2(String salonAddress2) {
        this.salonAddress2 = salonAddress2;
    }

    public void setSalID(int salID) {
        this.salID = salID;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }



    public Salon() {
    }

    protected Salon(Parcel in) {
        salonName = in.readString();
        salonAddress1 = in.readString();
        salonAddress2 = in.readString();
        salID = in.readInt();
        openTime = in.readString();
        closeTime = in.readString();
        image = in.readString();
        mobileNo = in.readString();
        rating = in.readFloat();
        type = in.readString();
        longitude = in.readString();
        latitude = in.readString();
    }

    public static final Creator<Salon> CREATOR = new Creator<Salon>() {
        @Override
        public Salon createFromParcel(Parcel in) {
            return new Salon(in);
        }

        @Override
        public Salon[] newArray(int size) {
            return new Salon[size];
        }
    };

    public String getSalonName() {
        return salonName;
    }

    public String getSalonAddress1() {
        return salonAddress1;
    }

    public String getSalonAddress2() {
        return salonAddress2;
    }

    public int getSalID() {
        return salID;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getImage() {
        return image;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(salonName);
        dest.writeString(salonAddress1);
        dest.writeString(salonAddress2);
        dest.writeInt(salID);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeString(image);
        dest.writeString(mobileNo);
        dest.writeFloat(rating);
        dest.writeString(type);
        dest.writeString(longitude);
        dest.writeString(latitude);
    }

    @Override
    public String toString() {
        return "Salon{" +
                "salonName='" + salonName + '\'' +
                ", salonAddress1='" + salonAddress1 + '\'' +
                ", salonAddress2='" + salonAddress2 + '\'' +
                ", salID='" + salID + '\'' +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", image='" + image + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}