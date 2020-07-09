package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class TimeSlotRepository {

    private static final String TAG = "TimeSlotRepository";

    private  static TimeSlotRepository instance;
    private SwiftSalonDao swiftSalonDao;

    private int userId;
    private Session session;

    public static TimeSlotRepository getInstance(Context context) {
        if(instance == null) {
            instance = new TimeSlotRepository(context);
        }
        return  instance;
    }

    private TimeSlotRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }



}
