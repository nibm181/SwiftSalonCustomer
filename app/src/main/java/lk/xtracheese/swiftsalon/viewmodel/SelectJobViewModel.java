package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.repository.StylistJobRepository;
import lk.xtracheese.swiftsalon.util.Resource;

public class SelectJobViewModel extends AndroidViewModel {

    private static final String TAG = "SelectJobViewModel";

    private MediatorLiveData<Resource<List<StylistJob>>> stylistJobs = new MediatorLiveData<>();
    private StylistJobRepository repository;

    private boolean isFetching;

    public SelectJobViewModel(@NonNull Application application) {
        super(application);
        repository = StylistJobRepository.getInstance(application);
    }

    public LiveData<Resource<List<StylistJob>>> getStylistJobs(){return stylistJobs;}

    public void StylistJobApi(){
        if(!isFetching){
            executeFetch();
        }
    }

    private void executeFetch() {
        isFetching = true;

        final LiveData<Resource<List<StylistJob>>> repositorySource = repository.getStylistJobsApi();

        stylistJobs.addSource(repositorySource, new Observer<Resource<List<StylistJob>>>() {
            @Override
            public void onChanged(Resource<List<StylistJob>> listResource) {
                if(listResource != null){
                    stylistJobs.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS){
                        isFetching = false;
                    }
                    else if(listResource.status == Resource.Status.ERROR){
                        isFetching = false;
                        stylistJobs.removeSource(repositorySource);
                    }
                }
                else {
                    stylistJobs.removeSource(repositorySource);
                }
            }
        });
    }
}
