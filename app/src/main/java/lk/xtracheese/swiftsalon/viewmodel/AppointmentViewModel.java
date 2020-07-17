package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class AppointmentViewModel extends AndroidViewModel {

    private static final String TAG = "AppointmentViewModel";

    private AppointmentRepository appointmentRepository;
    private MediatorLiveData<Resource<GenericResponse<Appointment>>> appointment = new MediatorLiveData<>();
    private boolean isFetching;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        appointmentRepository = AppointmentRepository.getInstance(application);
    }

    public LiveData<Resource<GenericResponse<Appointment>>> getAppointment(){return appointment;}

    public void appointmentApi(Appointment objAppointment){
        if(!isFetching){
            executeFetch(objAppointment);
        }
    }

    private void executeFetch(Appointment objAppointment) {
        isFetching = true;

        final LiveData<Resource<GenericResponse<Appointment>>> repositorySource = appointmentRepository.setAppointmentApi(objAppointment);

        appointment.addSource(repositorySource, new Observer<Resource<GenericResponse<Appointment>>>() {
            @Override
            public void onChanged(Resource<GenericResponse<Appointment>> resource) {
                if (resource != null) {
                    appointment.setValue(resource);
                    if (resource.status == Resource.Status.SUCCESS) {
                        isFetching = false;
                    } else if (resource.status == Resource.Status.ERROR) {
                        isFetching = false;
                        appointment.removeSource(repositorySource);
                    }
                } else {
                    appointment.removeSource(repositorySource);
                }
            }
        });
    }
}
