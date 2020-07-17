package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkOnlyBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private static UserRepository instance;
    private SwiftSalonDao swiftSalonDao;

    public static UserRepository getInstance(Context context){
        if(instance == null){
            instance = new UserRepository(context);
        }

        return instance;
    }

    private UserRepository(Context context) {
        swiftSalonDao = SwiftSalonDatabase.getInstance(context).getDao();
    }

    public LiveData<Resource<GenericResponse<User>>> getLoginApi(String mobileNo, String password){
        return new NetworkOnlyBoundResource<User, GenericResponse<User>> (AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<User>>> createCall() {
                return ServiceGenerator.getUserApi().login(mobileNo, password);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<User> item) {
                swiftSalonDao.insertUser(item.getContent());
            }
        }.getAsLiveData();
    }
}
