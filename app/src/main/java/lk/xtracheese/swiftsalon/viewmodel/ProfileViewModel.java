package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.xtracheese.swiftsalon.Interface.GetDataService;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = "ProfileViewModel";

    List<Appointment> appointments;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        appointments = new ArrayList<>();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void getAppointmentApi(int id){
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Appointment>> call = getDataService.getAppointment(id);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {

            }
        });
    }
}
