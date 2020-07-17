package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkOnlyBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class AppointmentRepository {

    private static final String TAG = "AppointmentRepository";

    private  static AppointmentRepository instance;

    SwiftSalonDao swiftSalonDao;

    public static AppointmentRepository getInstance(Context context){
        if(instance == null){
            instance = new AppointmentRepository(context);
        }
        return instance;
    }

    public AppointmentRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<GenericResponse<Appointment>>> setAppointmentApi(Appointment appointment){
        return new NetworkOnlyBoundResource<Appointment, GenericResponse<Appointment>>(AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<Appointment>>> createCall() {
                return ServiceGenerator.getAppointmentApi().addAppointment(appointment);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<Appointment> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.insertAppointment(item.getContent());
                }
            }
        }.getAsLiveData();

    }

}
