package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.repository.UserRepository;
import lk.xtracheese.swiftsalon.util.Resource;

public class UserProfileViewModel extends AndroidViewModel {
    private UserRepository repository;
    private MediatorLiveData<Resource<User>> user = new MediatorLiveData<>();


    private boolean isFetching;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(application);

    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }

    public void getCustomerApi(int id) {
        if(!isFetching) {
            executeCustomerApi(id);
        }
    }

    public void executeCustomerApi(int id) {
        isFetching = true;

        final LiveData<Resource<User>> repositorySource = repository.getCustomerApi(id);

        user.addSource(repositorySource, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> customerResource) {
                if(customerResource != null) {
                    user.setValue(customerResource);

                    isFetching = false;

                    if(customerResource.status == Resource.Status.ERROR) {
                        user.removeSource(repositorySource);
                    }
                }
                else {
                    user.removeSource(repositorySource);
                }
            }
        });

    }

//    public void clearToken() {
//        int id = session.getSalonId();
//
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        Data.Builder data = new Data.Builder();
//        data.putString("token", "0");
//        data.putInt("id", id);
//
//        WorkRequest request = new OneTimeWorkRequest.Builder(RefreshTokenWorker.class)
//                .setInputData(data.build())
//                .setConstraints(constraints)
//                .build();
//
//        workManager.enqueue(request);
//    }
}
