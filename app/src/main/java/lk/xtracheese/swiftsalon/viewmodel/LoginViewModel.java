package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.work.WorkManager;

import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.repository.UserRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    private UserRepository repository;
    private MediatorLiveData<Resource<GenericResponse<User>>> user = new MediatorLiveData<>();

    private Session session;
    private WorkManager workManager;
    private boolean isFetching;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(application);
        session = new Session(application);
        workManager = WorkManager.getInstance(application);
    }

    public LiveData<Resource<GenericResponse<User>>> getUser() {
        return user;
    }

    public void loginApi(String email, String password) {
        if (!isFetching) {
            executeLogin(email, password);
        }
    }

    private void executeLogin(String mobileNo, String password) {
        isFetching = true;

        final LiveData<Resource<GenericResponse<User>>> repositorySource = repository.getLoginApi(mobileNo, password);

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

//    public void updateToken() {
//
//        // TODO: Implement this method to send token to your app server.
//        int id = session.getUserId();
//
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        FirebaseInstanceId.getInstance().getInstanceId().
//                addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        Log.d(TAG, "onComplete: TOKEN: " + token);
//
//                        Data.Builder data = new Data.Builder();
//                        data.putString("token", token);
//                        data.putInt("id", id);
//
//                        Log.d(TAG, "onComplete: 101");
//                        WorkRequest request = new OneTimeWorkRequest.Builder(RefreshTokenWorker.class)
//                                .setInputData(data.build())
//                                .setConstraints(constraints)
//                                .build();
//
//                        workManager.enqueue(request);
//                    }
//                });
//
//    }
}