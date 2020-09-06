package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.util.Session;

public class ViewAppointmentViewModel extends AndroidViewModel {

    private static final String TAG = "ViewAppointmentViewMode";

    Session session;


    private AppointmentRepository appointmentRepository;
    private MediatorLiveData<Resource<List<Appointment>>> appointments = new MediatorLiveData<>();
    private boolean isFetching;

    public ViewAppointmentViewModel(@NonNull Application application){
        super(application);
        session = new Session(application);
        appointmentRepository = AppointmentRepository.getInstance(application);
    }

    public LiveData<Resource<List<Appointment>>> getAppointments(){return appointments;}

    public void appointmentApi(){
        if(!isFetching){
            executeFetch();
        }
    }

    private void executeFetch() {
        isFetching = true;

        final LiveData<Resource<List<Appointment>>> repositorySource = appointmentRepository.getAppointments(session.getUserId());

        appointments.addSource(repositorySource, listResource -> {
            if(listResource != null){
                appointments.setValue(listResource);
                if(listResource.status == Resource.Status.SUCCESS) {
                    isFetching = false;
                }
                else if(listResource.status == Resource.Status.ERROR) {
                    isFetching = false;
                    appointments.removeSource(repositorySource);
                }
            }else{
                appointments.removeSource(repositorySource);
            }
        });
    }
}
