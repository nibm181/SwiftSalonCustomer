package lk.xtracheese.swiftsalon.Interface;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("Appointment")
    Call<List<Appointment>> getAppointment(@Query("customer_id") int customerId);
}
