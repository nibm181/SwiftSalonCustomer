package lk.xtracheese.swiftsalon.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

@Entity(tableName = "tbl_appointment")
public class Appointment implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("appointment_id")
    private int id;

    @ColumnInfo(name = "salon_id")
    @SerializedName("salon_id")
    private int salonId;

    @ColumnInfo(name = "customer_id")
    @SerializedName("customer_id")
    private int customerId;

    @ColumnInfo(name = "stylist_id")
    @SerializedName("stylist_id")
    private int stylistId;

    @ColumnInfo(name = "date")
    @SerializedName("date")
    private String date;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    private String time;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    @ColumnInfo(name = "salon_name")
    @SerializedName("salon_name")
    private String salonName;

    @ColumnInfo(name = "stylist_name")
    @SerializedName("stylist_name")
    private String stylistName;

    @ColumnInfo(name = "addr1")
    @SerializedName("addr1")
    private String addr1;

    @ColumnInfo(name = "addr2")
    @SerializedName("addr2")
    private String addr2;

    @ColumnInfo(name = "mobile_no")
    @SerializedName("mobile_no")
    private String mobileNo;

    @ColumnInfo(name = "modified_on")
    @SerializedName("modified_on")
    private String modifiedOn; //timestamp

    @Ignore
    @SerializedName("job_ids")
    private int[] jobIds;

    public Appointment() {
    }

    protected Appointment(Parcel in) {
        id = in.readInt();
        salonId = in.readInt();
        customerId = in.readInt();
        stylistId = in.readInt();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        salonName = in.readString();
        stylistName = in.readString();
        addr1 = in.readString();
        addr2 = in.readString();
        mobileNo = in.readString();
        modifiedOn = in.readString();
        jobIds = in.createIntArray();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalonId() {
        return salonId;
    }

    public void setSalonId(int salonId) {
        this.salonId = salonId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public int[] getJobIds() {
        return jobIds;
    }

    public void setJobIds(int[] jobIds) {
        this.jobIds = jobIds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(salonId);
        dest.writeInt(customerId);
        dest.writeInt(stylistId);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(salonName);
        dest.writeString(stylistName);
        dest.writeString(addr1);
        dest.writeString(addr2);
        dest.writeString(mobileNo);
        dest.writeString(modifiedOn);
        dest.writeIntArray(jobIds);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", salonId=" + salonId +
                ", customerId=" + customerId +
                ", stylistId=" + stylistId +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", salonName='" + salonName + '\'' +
                ", stylistName='" + stylistName + '\'' +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", modifiedOn='" + modifiedOn + '\'' +
                ", jobIds=" + Arrays.toString(jobIds) +
                '}';
    }
}
