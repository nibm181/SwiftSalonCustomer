package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.repository.UserRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class CreateAccountViewModel extends AndroidViewModel {

    private static final String TAG = "CreateAccountViewModel";

    private UserRepository userRepository;
    private MediatorLiveData<Resource<GenericResponse<User>>> user = new MediatorLiveData<>();
    private boolean isFetching;

    public CreateAccountViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }

    public LiveData<Resource<GenericResponse<User>>> getUser(){return user;}

    public void registerUserApi(User objUser){
        if(!isFetching){
            executeFetch(objUser);
        }
    }

    private void executeFetch(User objUser) {
        isFetching = true;

        final LiveData<Resource<GenericResponse<User>>> repositorySource = userRepository.registerUserApi(objUser);

        user.addSource(repositorySource, new Observer<Resource<GenericResponse<User>>>() {
            @Override
            public void onChanged(Resource<GenericResponse<User>> resource) {
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
            }
        });
    }
}
