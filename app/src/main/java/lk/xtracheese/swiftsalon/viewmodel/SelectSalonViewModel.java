package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.repository.SalonRepository;
import lk.xtracheese.swiftsalon.util.Resource;

public class SelectSalonViewModel extends AndroidViewModel {
    private static final String TAG = "SelectSalonViewModel";

    private MediatorLiveData<Resource<List<Salon>>> salons = new MediatorLiveData<>();
    private SalonRepository repository;

    private boolean isFetching;

    public SelectSalonViewModel(@NonNull Application application) {
        super(application);
        repository = SalonRepository.getInstance(application);
    }

    public LiveData<Resource<List<Salon>>> getSalons() {
        return salons;
    }

    public void salonsApi(String searchText) {
        if(!isFetching) {
            executeFetch(searchText);
        }
    }

    private void executeFetch(String searchText) {
        isFetching = true;

        final LiveData<Resource<List<Salon>>> repositorySource = repository.getSalonsApi(searchText);

        salons.addSource(repositorySource, new Observer<Resource<List<Salon>>>() {
            @Override
            public void onChanged(Resource<List<Salon>> listResource) {
                if(listResource != null) {
                    salons.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS) {
                        isFetching = false;
                    }
                    else if(listResource.status == Resource.Status.ERROR) {
                        isFetching = false;
                        salons.removeSource(repositorySource);
                    }
                }
                else {
                    salons.removeSource(repositorySource);
                }
            }
        });
    }

}
