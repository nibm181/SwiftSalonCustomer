package lk.xtracheese.swiftsalon.request;

import androidx.lifecycle.LiveData;

import java.util.HashMap;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("Customer")
    LiveData<ApiResponse<GenericResponse<User>>> addCustomer(@Body User user);

    @FormUrlEncoded
    @POST("Cus_login")
    LiveData<ApiResponse<GenericResponse<User>>> login(@Field("identifier") String mobileNo, @Field("password") String password);

    @GET("Customer/{id}")
    LiveData<ApiResponse<GenericResponse<User>>> getCustomer(@Path("id") int id);

    @PUT("Customer")
    LiveData<ApiResponse<GenericResponse<User>>> updateCustomer(@Body User user);

    @Multipart
    @POST("Upload")
    LiveData<ApiResponse<GenericResponse<User>>> updateCustomerImage(@Part("customer_id") int customer_id, @Part MultipartBody.Part image);


    @PUT("Customer")
    LiveData<ApiResponse<GenericResponse>> verifyPassword(@Body HashMap<Object, Object> data);

    @PUT("Customer")
    LiveData<ApiResponse<GenericResponse>> confirmPassword(@Body HashMap<Object, Object> data);

    @PUT("Customer")
    Call<ResponseBody> updateToken(@Body User user);
}
