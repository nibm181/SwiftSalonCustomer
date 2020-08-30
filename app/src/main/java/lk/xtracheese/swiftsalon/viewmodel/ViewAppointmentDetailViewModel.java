package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class ViewAppointmentDetailViewModel extends AndroidViewModel {

    private static final String TAG = "ViewAppointmentDetailVM";

    private AppointmentRepository appointmentRepository;
    private MediatorLiveData<Resource<List<AppointmentDetail>>> appointmentDetail = new MediatorLiveData<>();
    private MediatorLiveData<Resource<GenericResponse<Appointment>>> appointment = new MediatorLiveData<>();
    private boolean isFetching;

    public ViewAppointmentDetailViewModel(@NonNull Application application) {
        super(application);
        appointmentRepository = AppointmentRepository.getInstance(application);
    }

    public LiveData<Resource<List<AppointmentDetail>>> getAppointmentDetail(){return appointmentDetail;}
    public LiveData<Resource<GenericResponse<Appointment>>> getAppointment(){return appointment;}


    public void appointmentDetailApi(int appointmentId){
        if(!isFetching){
            executeFetch(appointmentId);
        }
    }

    private void executeFetch(int id) {
        isFetching = true;

        final LiveData<Resource<List<AppointmentDetail>>> repositorySource = appointmentRepository.getAppointmentDetail(id);

        appointmentDetail.addSource(repositorySource, new Observer<Resource<List<AppointmentDetail>>>() {
            @Override
            public void onChanged(Resource<List<AppointmentDetail>> resource) {
                if(resource != null){
                    appointmentDetail.setValue(resource);
                    if(resource.status == Resource.Status.SUCCESS) {
                        isFetching = false;
                    }
                    else if(resource.status == Resource.Status.ERROR) {
                        isFetching = false;
                        appointmentDetail.removeSource(repositorySource);
                    }
                }else{
                    appointmentDetail.removeSource(repositorySource);
                }
            }
        });
    }

    public LiveData<Resource<Appointment>> updateAppointment(){return appointment;}

    public void appointmentUpdateApi(Appointment objAppointment){
        if(!isFetching){
            executeUpdateFetch(objAppointment);
        }
    }

    private void executeUpdateFetch(Appointment objAppointment) {
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
