package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.NetworkOnlyBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class SalonRepository {

    private static final String TAG = "SalonRepository";

    private static SalonRepository instance;
    private SwiftSalonDao swiftSalonDao;

    public static SalonRepository getInstance(Context context) {
        if(instance == null) {
            instance = new SalonRepository(context);
        }
        return instance;
    }

    public SalonRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<GenericResponse<Salon>>> getLoginApi(String email, String password) {
        return new NetworkOnlyBoundResource<Salon, GenericResponse<Salon>>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<Salon>>> createCall() {
                return ServiceGenerator.getSalonApi().login(email, password);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<Salon> item) {
                swiftSalonDao.insertSalon(item.getContent());
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<Salon>> getSalonApi(int id) {
        return new NetworkBoundResource<Salon, GenericResponse<Salon>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<Salon> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.insertSalon(item.getContent());
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Salon data) {
                if(data == null) {
                    return true;
                }
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Salon> loadFromDb() {
                return swiftSalonDao.getSalon(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<Salon>>> createCall() {
                return ServiceGenerator.getSalonApi().getSalon(id);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Salon>>> getSalonsApi() {
        return new NetworkBoundResource<List<Salon>, GenericResponse<List<Salon>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Salon>> item) {
                if(item.getContent() != null) {
                    Salon[] salon = new Salon[item.getContent().size()];
                    swiftSalonDao.insertSalons(item.getContent().toArray(salon));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Salon> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Salon>> loadFromDb() {
                return swiftSalonDao.getSalons();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Salon>>>> createCall() {
                return ServiceGenerator.getSalonApi().getSalons();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<GenericResponse<Salon>>> updateSalonApi(Salon salon) {
        return new NetworkOnlyBoundResource<Salon, GenericResponse<Salon>>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<Salon>>> createCall() {
                return ServiceGenerator.getSalonApi().updateSalon(salon);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<Salon> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.insertSalon(item.getContent());
                }
            }

        }.getAsLiveData();
    }
}
