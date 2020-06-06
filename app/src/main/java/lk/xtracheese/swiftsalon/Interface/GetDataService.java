package lk.xtracheese.swiftsalon.Interface;

import java.util.List;

import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.Model.Salon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("Salon")
    Call<List<Salon>> getSalon();

    @GET("Salon")
    Call<List<Salon>> getSearchSalon(@Query("searchTxt") String SearchTxt);

    @GET("HairStylist")
    Call<List<HairStylist>> getHairstylist();
}
