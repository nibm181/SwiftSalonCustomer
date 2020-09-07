package lk.xtracheese.swiftsalon.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.work.ListenableWorker;

import java.io.IOException;

import lk.xtracheese.swiftsalon.Interface.GetDataService;
import lk.xtracheese.swiftsalon.activity.HomeActivity;
import lk.xtracheese.swiftsalon.activity.RatingActivity;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.network.RetrofitClientInstance;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.request.ServiceGenerator;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.NotificationHelper;
import lk.xtracheese.swiftsalon.util.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingViewModel extends AndroidViewModel {

    private static final String TAG = "RatingViewModel";

    private AppointmentRepository appointmentRepository;
    private MediatorLiveData<Resource<GenericResponse>> response = new MediatorLiveData<>();
    private boolean isFetching;


    public RatingViewModel(@NonNull Application application) {
        super(application);
        appointmentRepository = AppointmentRepository.getInstance(application);
    }

    public LiveData<Resource<GenericResponse>> getResponse(){return response;}

    public void addRatingApi(int id, float rating){
        if(!isFetching){
            Appointment objAppointment = new Appointment();
            objAppointment.setId(id);
            objAppointment.setRating(rating);
            executeFetch(objAppointment);
        }
    }

    public void executeFetch(Appointment objAppointment) {
        isFetching = true;

        final LiveData<Resource<GenericResponse>> repositorySource = appointmentRepository.addRating(objAppointment);

        response.addSource(repositorySource, new Observer<Resource<GenericResponse>>() {
            @Override
            public void onChanged(Resource<GenericResponse> genericResponseResource) {
                if (genericResponseResource != null) {
                    response.setValue(genericResponseResource);
                    if (genericResponseResource.status == Resource.Status.SUCCESS) {
                        isFetching = false;
                    } else if (genericResponseResource.status == Resource.Status.ERROR) {
                        isFetching = false;
                        response.removeSource(repositorySource);
                    }
                } else {
                    response.removeSource(repositorySource);
                }
            }
        });
    }

    public String getStylistImage(int id){
       String image =  appointmentRepository.getStylistImage(id);
       return image;
    }




}
