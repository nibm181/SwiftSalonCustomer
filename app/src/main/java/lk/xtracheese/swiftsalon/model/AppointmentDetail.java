package lk.xtracheese.swiftsalon.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_appointment_detail")
public class AppointmentDetail {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "appointment_id")
    @SerializedName("appointment_id")
    private int appointmentId;

    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    private int jobId;

    @ColumnInfo(name = "job_name")
    @SerializedName("job_name")
    private String jobName;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    private float price;

    public AppointmentDetail() {
    }

    public AppointmentDetail(int id, int appointmentId, int jobId, String jobName, float price) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AppointmentDetail{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", price=" + price +
                '}';
    }
}
