package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.repository.StylistRepository;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.AppExecutor;
import lk.xtracheese.swiftsalon.util.NetworkBoundResource;
import lk.xtracheese.swiftsalon.util.Resource;

public class SelectStylistViewModel extends AndroidViewModel {

    private MediatorLiveData<Resource<List<Stylist>>> stylists = new MediatorLiveData<>();
    private StylistRepository repository;
    private boolean isFetching;

    public SelectStylistViewModel(@NonNull Application application) {
        super(application);
        repository = StylistRepository.getInstance(application);
    }

    public LiveData<Resource<List<Stylist>>> getStylist() {
        return  stylists;
    }

    public void stylistApi(){
        if(!isFetching) {
            executeFetch();
        }
    }

    private void executeFetch() {
        isFetching =true;
        int salonId = Integer.parseInt(Common.currentSalon.getSalID());
        final LiveData<Resource<List<Stylist>>> repositorySource = repository.getStylistsApi(salonId);

        stylists.addSource(repositorySource, new Observer<Resource<List<Stylist>>>() {
            @Override
            public void onChanged(Resource<List<Stylist>> listResource) {
                if(listResource != null) {
                    stylists.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS) {
                        isFetching = false;
                    }
                    else if(listResource.status == Resource.Status.ERROR) {
                        isFetching = false;
                        stylists.removeSource(repositorySource);
                    }
                }
                else {
                    stylists.removeSource(repositorySource);
                }
            }
        });

    }
}
