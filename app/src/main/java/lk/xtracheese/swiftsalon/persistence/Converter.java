package lk.xtracheese.swiftsalon.persistence;

import androidx.room.TypeConverter;

import java.sql.Time;

public class Converter {

    @TypeConverter
    public static String fromTime(Time time) {
        return time == null ? null : time.toString();
    }

    @TypeConverter
    public static Time fromString(String time) {
        return time == null ? null : Time.valueOf(time);
    }
}
