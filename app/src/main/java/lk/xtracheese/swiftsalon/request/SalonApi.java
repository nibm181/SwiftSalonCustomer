package lk.xtracheese.swiftsalon.request;

import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Job;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SalonApi {

    @GET("Salon")
    LiveData<ApiResponse<GenericResponse<Salon>>> getSalon(@Path("id") int id);

    @GET("Salon")
    LiveData<ApiResponse<GenericResponse<List<Salon>>>> getSalons();

    @FormUrlEncoded
    @POST("Login")
    LiveData<ApiResponse<GenericResponse<Salon>>> login(@Field("identifier") String email, @Field("password") String password);

    @PUT("Salon")
    LiveData<ApiResponse<GenericResponse<Salon>>> updateSalon(@Body Salon salon);

    @FormUrlEncoded
    @PUT("Salon")
    Call<ResponseBody> updateToken(@Field("id") int id, @Field("token") String token);

    @GET("Stylist_job")
    LiveData<ApiResponse<GenericResponse<List<StylistJob>>>> getStylistJobs(@Query("stylist_id") int stylistId);

    @GET("Job")
    LiveData<ApiResponse<GenericResponse<Job>>> getJob(@Query("id") int id);


    @GET("Stylist")
    LiveData<ApiResponse<GenericResponse<List<Stylist>>>> getStylists(@Query("salon_id") int salonId);

    @PUT("Stylist")
    LiveData<ApiResponse<GenericResponse<Stylist>>> updateStylist(@Body Stylist stylist);
}
