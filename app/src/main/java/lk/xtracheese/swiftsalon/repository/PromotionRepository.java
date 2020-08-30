package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class PromotionRepository {

    private static final String TAG = "BannerRepository";
    private static PromotionRepository instance;
    private SwiftSalonDao swiftSalonDao;

    public static PromotionRepository getInstance(Context context){
        if(instance == null){
            instance = new PromotionRepository(context);
        }
        return instance;
    }

    public PromotionRepository(Context context){
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<List<Promotion>>> getBannersApi(){
        return  new NetworkBoundResource<List<Promotion>, GenericResponse<List<Promotion>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<Promotion>> item) {
                swiftSalonDao.deletePromotions();
                if(item.getContent() != null) {
                    Promotion[] promotions = new Promotion[item.getContent().size()];
                    swiftSalonDao.insertPromotions(item.getContent().toArray(promotions));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Promotion> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Promotion>> loadFromDb() {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                return swiftSalonDao.getPromotions(calendar.getTimeInMillis());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<Promotion>>>> createCall() {
                return ServiceGenerator.getHomeApi().getBanners();
            }
        }.getAsLiveData();
    }

}
