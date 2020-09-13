package lk.xtracheese.swiftsalon.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.Job;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.model.User;

@Database(entities = {Appointment.class, AppointmentDetail.class, Salon.class, Stylist.class, StylistJob.class, LookBook.class, Promotion.class, Job.class, User.class}, version = 24)
@TypeConverters({Converter.class})
public abstract class SwiftSalonDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "swiftsalon";

    private static SwiftSalonDatabase instance;

    public static SwiftSalonDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SwiftSalonDatabase.class,
                    DATABASE_NAME
            )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract SwiftSalonDao getDao();

}
