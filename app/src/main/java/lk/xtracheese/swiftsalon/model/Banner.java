package lk.xtracheese.swiftsalon.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_promotion")
public class Banner {

    @PrimaryKey
    private int id;

    @SerializedName("job_id")
    @ColumnInfo(name = "job_id")
    private int jobId;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", image='" + image + '\'' +
                '}';
    }
}
