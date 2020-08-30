package lk.xtracheese.swiftsalon.request;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AppointmentApi {

    // get an appointment by id
    @GET("Appointment")
    LiveData<ApiResponse<GenericResponse<Appointment>>> getAppointment(@Query("id") int id);

    // get all appointments
    @GET("Appointment")
    LiveData<ApiResponse<GenericResponse<List<Appointment>>>> getAllAppointments(@Query("customer_id") int customerId);

    // get new appointments
    @GET("Appointment")
    LiveData<ApiResponse<GenericResponse<List<Appointment>>>> getAppointmentsByStatus(@Query("salon_id") int salonId, @Query("status") String status);

    @GET("Appointment_details")
    LiveData<ApiResponse<GenericResponse<List<AppointmentDetail>>>> getAppointmentDetail(@Query("appointment_id") int appointmentId);

    @GET("Appointment?status=pending")
    Call<GenericResponse<List<Appointment>>> getWorkerNewAppointments(@Query("salon_id") int salon_id);

    @POST("Appointment")
    LiveData<ApiResponse<GenericResponse<Appointment>>> addAppointment(@Body Appointment appointment);

    @PUT("Appointment")
    LiveData<ApiResponse<GenericResponse<Appointment>>> updateAppointment(@Body Appointment appointment);


}
