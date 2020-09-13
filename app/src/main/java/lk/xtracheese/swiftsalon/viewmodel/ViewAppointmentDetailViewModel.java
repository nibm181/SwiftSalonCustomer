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
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.repository.AppointmentRepository;
import lk.xtracheese.swiftsalon.repository.SalonRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;

public class ViewAppointmentDetailViewModel extends AndroidViewModel {

    private static final String TAG = "ViewAppointmentDetailVM";

    private AppointmentRepository appointmentRepository;
    private SalonRepository salonRepository;
    private MediatorLiveData<Resource<List<AppointmentDetail>>> appointmentDetail = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Salon>> salon = new MediatorLiveData<>();
    private MediatorLiveData<Resource<GenericResponse<Appointment>>> appointment = new MediatorLiveData<>();
    private boolean isFetching;
    private boolean isFetchingForSalon;

    public ViewAppointmentDetailViewModel(@NonNull Application application) {
        super(application);
        appointmentRepository = AppointmentRepository.getInstance(application);
        salonRepository = SalonRepository.getInstance(application);
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

    public LiveData<Resource<Salon>> getSalon() {return salon;}

    public void getSalonApi(int salonId){
        if(!isFetchingForSalon){
            executeSalonFetch(salonId);
        }
    }

    public void executeSalonFetch(int salonId){
        isFetchingForSalon = true;

        final LiveData<Resource<Salon>> repositorySource = salonRepository.getSalonApi(salonId);

        appointmentDetail.addSource(repositorySource, new Observer<Resource<Salon>>() {

            @Override
            public void onChanged(Resource<Salon> salonResource) {
                if(salonResource != null){
                    salon.setValue(salonResource);
                    if(salonResource.status == Resource.Status.SUCCESS) {
                        isFetchingForSalon = false;
                    }
                    else if(salonResource.status == Resource.Status.ERROR) {
                        isFetchingForSalon = false;
                        salon.removeSource(repositorySource);
                    }
                }else{
                    salon.removeSource(repositorySource);
                }
            }
        });

    }

    public LiveData<Resource<GenericResponse<Appointment>>> updateAppointment(){return appointment;}

    public void appointmentUpdateApi(Appointment objAppointment){
        if(!isFetching){
            executeUpdateFetch(objAppointment);
        }
    }

    private void executeUpdateFetch(Appointment objAppointment) {
        isFetching = true;

        final LiveData<Resource<GenericResponse<Appointment>>> repositorySource = appointmentRepository.updateAppointment(objAppointment);

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
