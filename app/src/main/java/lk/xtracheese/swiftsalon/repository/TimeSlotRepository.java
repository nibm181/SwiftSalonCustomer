package lk.xtracheese.swiftsalon.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkOnlyBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class TimeSlotRepository {

    private static final String TAG = "TimeSlotRepository";

    private  static TimeSlotRepository instance;

    private int userId;
    private Session session;

    public static TimeSlotRepository getInstance() {
        if(instance == null) {
            instance = new TimeSlotRepository();
        }
        return  instance;
    }


    public LiveData<Resource<GenericResponse<List<TimeSlot>>>> getTimeSlots(int stylistId, String date, String openTime, String closeTime, int duration){
        return new NetworkOnlyBoundResource<List<TimeSlot>, GenericResponse<List<TimeSlot>>>(AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<TimeSlot>>>> createCall() {
                return ServiceGenerator.getSalonApi().getTimeSlots(stylistId, date, openTime, closeTime, duration);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<TimeSlot>> item) {

            }
        }.getAsLiveData();
    }





}
