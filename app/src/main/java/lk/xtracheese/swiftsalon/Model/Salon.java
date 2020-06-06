package lk.xtracheese.swiftsalon.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Salon implements Parcelable {

    @SerializedName("salonName")
    private String salonName;
    @SerializedName("salonAddr1")
    private String salonAddress;
    @SerializedName("salonID")
    private String salID;

    public Salon() {
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public void setSalonAddress(String salonAddress) {
        this.salonAddress = salonAddress;
    }

    public String getSalID() {
        return salID;
    }

    public void setSalidID(String salID) {
        this.salID = salID;
    }

    protected Salon(Parcel in) {
        salonName = in.readString();
        salonAddress = in.readString();
        salID = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(salonName);
        dest.writeString(salonAddress);
        dest.writeString(salID);
    }
}
