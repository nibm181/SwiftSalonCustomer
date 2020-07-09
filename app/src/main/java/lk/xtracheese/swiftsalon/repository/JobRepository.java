package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Job;
import lk.xtracheese.swiftsalon.model.Salon;
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

public class JobRepository {

    private static final String TAG = "JobRepository";

    private static JobRepository instance;
    private SwiftSalonDao swiftSalonDao;

    private int userId;
    private Session session;

    public static JobRepository getInstance(Context context) {
        if (instance == null) {
            instance = new JobRepository(context);
        }
        return instance;
    }

    private JobRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public  LiveData<Resource<List<Job>>> getJobsApi(){
        return new NetworkBoundResource<List<Job>, GenericResponse<List<Job>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Job>> item) {
                if(item.getContent() != null) {
                    Job[] jobs = new Job[item.getContent().size()];
                    swiftSalonDao.insertJobs(item.getContent().toArray(jobs));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Job> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Job>> loadFromDb() {
                return swiftSalonDao.getJobs(Common.currentStylist.getId());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Job>>>> createCall() {
                return ServiceGenerator.getSalonApi().getJobs(Common.currentStylist.getId());
            }
        }.getAsLiveData();
    }


}
