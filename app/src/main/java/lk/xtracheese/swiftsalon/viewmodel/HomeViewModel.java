package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Banner;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.repository.BannerRepository;
import lk.xtracheese.swiftsalon.repository.LookBookRepository;
import lk.xtracheese.swiftsalon.repository.SalonRepository;
import lk.xtracheese.swiftsalon.util.Resource;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeViewModel";
    private MediatorLiveData<Resource<List<LookBook>>> lookbooks = new MediatorLiveData<>();
    private MediatorLiveData<Resource<List<Banner>>> banners = new MediatorLiveData<>();

    private LookBookRepository lookBookRepository;
    private BannerRepository bannerRepository;

    private boolean isFetching;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        lookBookRepository = LookBookRepository.getInstance(application);
        bannerRepository = BannerRepository.getInstance(application);
    }

    public LiveData<Resource<List<Banner>>> getBanners(){return banners;}

    public LiveData<Resource<List<LookBook>>> getLookBooks(){return lookbooks;}

    public void bannerApi(){
        if(!isFetching){
            executeFetch();
        }
    }

    private void executeFetch(){
        //isFetching =true;

        final LiveData<Resource<List<Banner>>> bannerRepositorySource = bannerRepository.getBannersApi();

        banners.addSource(bannerRepositorySource, new Observer<Resource<List<Banner>>>() {

            @Override
            public void onChanged(Resource<List<Banner>> listResource) {
                if(listResource != null){
                    banners.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS){
                        isFetching = false;
                    }
                    else if(listResource.status == Resource.Status.ERROR){
                        isFetching = false;
                        banners.removeSource(bannerRepositorySource);
                    }
                }
                else {
                    banners.removeSource(bannerRepositorySource);
                }
            }
        });

        final LiveData<Resource<List<LookBook>>> lookBookRepositorySource = lookBookRepository.getLookBookApi();

        lookbooks.addSource(lookBookRepositorySource, new Observer<Resource<List<LookBook>>>() {
            @Override
            public void onChanged(Resource<List<LookBook>> listResource) {
                if(listResource != null){
                    lookbooks.setValue(listResource);
                    if(listResource.status == Resource.Status.SUCCESS){
                        isFetching = false;
                    }
                    else if(listResource.status == Resource.Status.ERROR){
                        isFetching = false;
                        lookbooks.removeSource(lookBookRepositorySource);
                    }
                }
                else {
                    lookbooks.removeSource(lookBookRepositorySource);
                }
            }
        });
    }

}
