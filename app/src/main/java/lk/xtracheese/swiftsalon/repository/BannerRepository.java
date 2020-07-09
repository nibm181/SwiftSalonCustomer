package lk.xtracheese.swiftsalon.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Banner;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class BannerRepository {

    private static final String TAG = "BannerRepository";
    private static BannerRepository instance;
    private SwiftSalonDao swiftSalonDao;

    public static BannerRepository getInstance(Context context){
        if(instance == null){
            instance = new BannerRepository(context);
        }
        return instance;
    }

    public BannerRepository(Context context){
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<List<Banner>>> getBannersApi(){
        return  new NetworkBoundResource<List<Banner>, GenericResponse<List<Banner>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Banner>> item) {
                if(item.getContent() != null) {
                    Banner[] banners = new Banner[item.getContent().size()];
                    swiftSalonDao.insertBanners(item.getContent().toArray(banners));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Banner> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Banner>> loadFromDb() {
                return swiftSalonDao.getBanners();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Banner>>>> createCall() {
                return ServiceGenerator.getHomeApi().getBanners();
            }
        }.getAsLiveData();
    }

}
