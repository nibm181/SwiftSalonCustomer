package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Stylist;
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

public class StylistRepository {

    private static final String TAG = "StylistRepository";

    private static StylistRepository instance;
    private SwiftSalonDao swiftSalonDao;



    public static StylistRepository getInstance(Context context) {
        if (instance == null) {
            instance = new StylistRepository(context);
        }
        return instance;
    }

    private StylistRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();

    }

    public LiveData<Resource<List<Stylist>>> getStylistsApi(int salonId) {
        return new NetworkBoundResource<List<Stylist>, GenericResponse<List<Stylist>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Stylist>> item) {
                Log.d(TAG, "saveCallResult: DATA: " + item.toString());
                if (item.getContent() != null) {
                    Stylist[] stylists = new Stylist[item.getContent().size()];
                    swiftSalonDao.insertStylists(item.getContent().toArray(stylists));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Stylist> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Stylist>> loadFromDb() {
                return swiftSalonDao.getStylists(salonId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Stylist>>>> createCall() {
                return ServiceGenerator.getSalonApi().getStylists(salonId);
            }
        }.getAsLiveData();
    }


}
