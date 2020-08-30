package lk.xtracheese.swiftsalon.request;

import androidx.lifecycle.LiveData;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.request.response.ApiResponse;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import retrofit2.http.GET;

public interface HomeApi {
    @GET("Look_book")
    LiveData<ApiResponse<GenericResponse<List<LookBook>>>> getLookBooks();

    @GET("Promotion")
    LiveData<ApiResponse<GenericResponse<List<Promotion>>>> getBanners();
}
