package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class LookBookRepository {

    private static  LookBookRepository instance;
    private SwiftSalonDao swiftSalonDao;

    public static LookBookRepository getInstance(Context context) {
        if(instance == null){
            instance = new LookBookRepository(context);
        }
        return instance;
    }

    public  LookBookRepository(Context context){
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<List<LookBook>>> getLookBookApi() {
        return new NetworkBoundResource<List<LookBook>, GenericResponse<List<LookBook>>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<List<LookBook>> item) {
                if(item.getContent() != null){
                    LookBook[] lookBook = new LookBook[item.getContent().size()];
                    swiftSalonDao.insertLookBooks(item.getContent().toArray(lookBook));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<LookBook> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<LookBook>> loadFromDb() {
                return swiftSalonDao.getLookBooks();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<List<LookBook>>>> createCall() {
                return ServiceGenerator.getHomeApi().getLookBooks();
            }
        }.getAsLiveData();
    }
}
