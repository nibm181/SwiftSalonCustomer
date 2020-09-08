package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
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

    public LiveData<Resource<List<Appointment>>> getAppointments(int userId){
        return new NetworkBoundResource<List<Appointment>, GenericResponse<List<Appointment>>>(AppExecutor.getInstance()){

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Appointment>> item) {
                if(item.getContent() != null){
                    Appointment[] appointments = new Appointment[item.getContent().size()];
                    swiftSalonDao.insertAppointments(item.getContent().toArray(appointments));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Appointment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Appointment>> loadFromDb() {
                return swiftSalonDao.getAppointments(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Appointment>>>> createCall() {
                return ServiceGenerator.getAppointmentApi().getAllAppointments(userId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<AppointmentDetail>>> getAppointmentDetail(int appointmentId) {
        return new NetworkBoundResource<List<AppointmentDetail>, GenericResponse<List<AppointmentDetail>>>(AppExecutor.getInstance()) {


            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<AppointmentDetail>> item) {
                if (item.getContent() != null) {
                    AppointmentDetail[] appointmentDetails = new AppointmentDetail[item.getContent().size()];
                    swiftSalonDao.insertAppointmentDetail(item.getContent().toArray(appointmentDetails));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<AppointmentDetail> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<AppointmentDetail>> loadFromDb() {
                return swiftSalonDao.getAppointmentDetail(appointmentId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<AppointmentDetail>>>> createCall() {
                return ServiceGenerator.getAppointmentApi().getAppointmentDetail(appointmentId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<GenericResponse<Appointment>>> updateAppointment(Appointment appointment){
        return new NetworkOnlyBoundResource<Appointment, GenericResponse<Appointment>>(AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<Appointment>>> createCall() {
                return ServiceGenerator.getAppointmentApi().updateAppointment(appointment);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<Appointment> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.updateAppointment(item.getContent());
                }
            }
        }.getAsLiveData();

    }

    public LiveData<Resource<GenericResponse>> addRating(Appointment appointment){
        return new NetworkOnlyBoundResource<GenericResponse, GenericResponse>(AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse>> createCall() {
                return ServiceGenerator.getAppointmentApi().addRating(appointment);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse item) {
            }
        }.getAsLiveData();

    }

}
