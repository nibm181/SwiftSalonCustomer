package lk.xtracheese.swiftsalon.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_stylist_job")
public class StylistJob {

    @PrimaryKey
    int id;
    @ColumnInfo(name = "stylist_id")
    @SerializedName("stylist_id")
    int stylistId;
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    int jobId;
    @ColumnInfo(name = "salon_id")
    @SerializedName("salon_id")
    String salonId;
    String name;
    int duration;
    Float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StylistJob{" +
                "id=" + id +
                ", stylistId=" + stylistId +
                ", jobId=" + jobId +
                ", salonId='" + salonId + '\'' +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
