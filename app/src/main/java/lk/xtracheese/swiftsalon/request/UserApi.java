package lk.xtracheese.swiftsalon.request;

import androidx.lifecycle.LiveData;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @FormUrlEncoded
    @POST("Cus_login")
    LiveData<ApiResponse<GenericResponse<User>>> login(@Field("identifier") String mobileNo, @Field("password") String password);
}
