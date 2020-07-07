package lk.xtracheese.swiftsalon.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;

public class TimeSlot implements Parcelable {
   @SerializedName("slot_timing")
   String slotTiming;
   String status;

   public TimeSlot() {
   }

   protected TimeSlot(Parcel in) {
      slotTiming = in.readString();
      status = in.readString();
   }

   public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
      @Override
      public TimeSlot createFromParcel(Parcel in) {
         return new TimeSlot(in);
      }

      @Override
      public TimeSlot[] newArray(int size) {
         return new TimeSlot[size];
      }
   };

   public String getSlotTiming() {
      return slotTiming;
   }

   public void setSlotTiming(String slotTiming) {
      this.slotTiming = slotTiming;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(slotTiming);
      dest.writeString(status);
   }
}
