package lk.xtracheese.swiftsalon.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.HashMap;

import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.NetworkOnlyBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;
import okhttp3.MultipartBody;

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

    public LiveData<Resource<GenericResponse<User>>> registerUserApi(User user){
        return new NetworkOnlyBoundResource<User, GenericResponse<User>> (AppExecutor.getInstance()){

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<User>>> createCall() {
                return ServiceGenerator.getUserApi().addCustomer(user);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<User> item) {
                if(item.getContent() != null)
                    swiftSalonDao.insertUser(item.getContent());
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<User>> getCustomerApi(int id) {
        return new NetworkBoundResource<User, GenericResponse<User>>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GenericResponse<User> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.insertUser(item.getContent());
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
//                if(data == null) {
//                    return true;
//                }
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return swiftSalonDao.getUser(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<User>>> createCall() {
                return ServiceGenerator.getUserApi().getCustomer(id);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<GenericResponse<User>>> updateCustomerApi(User user) {
        return new NetworkOnlyBoundResource<User, GenericResponse<User>>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<User>>> createCall() {
                return ServiceGenerator.getUserApi().updateCustomer(user);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<User> item) {
                if(item.getContent() != null) {
                    swiftSalonDao.insertUser(item.getContent());
                }
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<GenericResponse<User>>> updateCustomerImageApi(int customerId, MultipartBody.Part image) {
        return new NetworkOnlyBoundResource<User, GenericResponse<User>>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<User>>> createCall() {
                return ServiceGenerator.getUserApi().updateCustomerImage(customerId, image);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse<User> item) {
                if (item.getContent() != null) {
                    swiftSalonDao.insertUser(item.getContent());
                }
            }

        }.getAsLiveData();

    }
    public LiveData<Resource<GenericResponse>> verifyPasswordApi(HashMap<Object, Object> data) {
        return new NetworkOnlyBoundResource<Object, GenericResponse>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse>> createCall() {
                return ServiceGenerator.getUserApi().verifyPassword(data);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse item) {
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<GenericResponse>> confirmPasswordApi(HashMap<Object, Object> data) {
        return new NetworkOnlyBoundResource<Object, GenericResponse>(AppExecutor.getInstance()) {
            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse>> createCall() {
                return ServiceGenerator.getUserApi().confirmPassword(data);
            }

            @Override
            protected void saveCallResult(@NonNull GenericResponse item) {
            }
        }.getAsLiveData();
    }
}
