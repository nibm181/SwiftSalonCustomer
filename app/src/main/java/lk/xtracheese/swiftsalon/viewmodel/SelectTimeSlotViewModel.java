package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.repository.TimeSlotRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class SelectTimeSlotViewModel extends AndroidViewModel {

    private static final String TAG = "SelectTimeSlotViewModel";

    MediatorLiveData<Resource<GenericResponse<List<TimeSlot>>>> timeSlots = new MediatorLiveData<>();
    private TimeSlotRepository timeSlotRepository;


    public SelectTimeSlotViewModel(@NonNull Application application) {
        super(application);
        timeSlotRepository = TimeSlotRepository.getInstance();
    }

    public LiveData<Resource<GenericResponse<List<TimeSlot>>>>  getTimeSlots(){return timeSlots;}

    public void timeSlotsApi(int stylistId, String date, String openTime, String closeTime, int duration ) {

        final LiveData<Resource<GenericResponse<List<TimeSlot>>>> repositorySource = timeSlotRepository.getTimeSlotsApi(stylistId, date, openTime, closeTime, duration);

        timeSlots.addSource(repositorySource, new Observer<Resource<GenericResponse<List<TimeSlot>>>>() {
            @Override
            public void onChanged(Resource<GenericResponse<List<TimeSlot>>> genericResponseResource) {
                if(genericResponseResource != null){
                    timeSlots.setValue(genericResponseResource);
                    if(genericResponseResource.status == Resource.Status.ERROR){
                        timeSlots.removeSource(repositorySource);
                    }
                }
                else{
                    timeSlots.removeSource(repositorySource);
                }
            }
        });
    }
}
