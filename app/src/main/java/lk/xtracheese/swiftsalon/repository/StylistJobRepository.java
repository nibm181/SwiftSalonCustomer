package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Job;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class StylistJobRepository {

    private static final String TAG = "JobRepository";

    private static StylistJobRepository instance;
    private SwiftSalonDao swiftSalonDao;

    private int userId;
    private Session session;

    public static StylistJobRepository getInstance(Context context) {
        if (instance == null) {
            instance = new StylistJobRepository(context);
        }
        return instance;
    }

    private StylistJobRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public  LiveData<Resource<List<StylistJob>>> getStylistJobsApi(){
        return new NetworkBoundResource<List<StylistJob>, GenericResponse<List<StylistJob>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<StylistJob>> item) {

                if(item.getContent() != null) {
                    swiftSalonDao.deleteStylistJobs(Common.currentStylist.getSalID());
                    StylistJob[] stylistJobs = new StylistJob[item.getContent().size()];
                    swiftSalonDao.insertStylistJobs(item.getContent().toArray(stylistJobs));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StylistJob> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<StylistJob>> loadFromDb() {
                return swiftSalonDao.getStylistJobs(Common.currentStylist.getId());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<StylistJob>>>> createCall() {
                return ServiceGenerator.getSalonApi().getStylistJobs(Common.currentStylist.getId());
            }
        }.getAsLiveData();
    }


}
