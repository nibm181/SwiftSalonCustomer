package lk.xtracheese.swiftsalon.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import android.app.Application;


import java.util.HashMap;

import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.repository.UserRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class EditUserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private MediatorLiveData<Resource<GenericResponse<User>>> user = new MediatorLiveData<>();
    private MediatorLiveData<Resource<GenericResponse>> verifyResponse = new MediatorLiveData<>();
    private MediatorLiveData<Resource<GenericResponse>> confirmResponse = new MediatorLiveData<>();

    private boolean isFetching;

    public EditUserViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(application);
    }

    public LiveData<Resource<GenericResponse<User>>> updateCustomer() {
        return user;
    }
    public LiveData<Resource<GenericResponse>> verifyPassword() {
        return verifyResponse;
    }
    public LiveData<Resource<GenericResponse>> confirmPassword() {
        return confirmResponse;
    }

    public void updateApi(User objUser) {
        if (!isFetching) {
            executeUpdate(objUser);
        }
    }

    public void verifyApi(String password) {
        if (!isFetching) {
            HashMap<Object, Object> data = new HashMap<>();
            data.put("current_password", password);
            data.put("id", 1);
            executeVerify(data);
        }
    }

    public void confirmApi(String password) {
        if (!isFetching) {
            HashMap<Object, Object> data = new HashMap<>();
            data.put("new_password", password);
            data.put("id", 1);
            executeConfirm(data);
        }
    }

    private void executeUpdate(User objUser){
        isFetching = true;

        final LiveData<Resource<GenericResponse<User>>> repositorySource = repository.updateCustomerApi(objUser);

        user.addSource(repositorySource, resource -> {
            if (resource != null) {
                user.setValue(resource);
                if (resource.status == Resource.Status.SUCCESS) {
                    isFetching = false;
                } else if (resource.status == Resource.Status.ERROR) {
                    isFetching = false;
                    user.removeSource(repositorySource);
                }
            } else {
                user.removeSource(repositorySource);
            }
        });
    }

    private void executeVerify(HashMap<Object, Object> data) {
        isFetching = true;

        final LiveData<Resource<GenericResponse>> repositorySource = repository.verifyPasswordApi(data);

        verifyResponse.addSource(repositorySource, resource -> {
            if (resource != null) {
                verifyResponse.setValue(resource);
                if (resource.status == Resource.Status.SUCCESS) {
                    isFetching = false;
                } else if (resource.status == Resource.Status.ERROR) {
                    isFetching = false;
                    verifyResponse.removeSource(repositorySource);
                }
            } else {
                verifyResponse.removeSource(repositorySource);
            }
        });
    }

    private void executeConfirm(HashMap<Object, Object> data) {
        isFetching = true;

        final LiveData<Resource<GenericResponse>> repositorySource = repository.confirmPasswordApi(data);

        confirmResponse.addSource(repositorySource, resource -> {
            if (resource != null) {
                confirmResponse.setValue(resource);
                if (resource.status == Resource.Status.SUCCESS) {
                    isFetching = false;
                } else if (resource.status == Resource.Status.ERROR) {
                    isFetching = false;
                    confirmResponse.removeSource(repositorySource);
                }
            } else {
                confirmResponse.removeSource(repositorySource);
            }
        });
    }

}
