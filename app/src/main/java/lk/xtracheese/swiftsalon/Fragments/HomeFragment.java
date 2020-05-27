package lk.xtracheese.swiftsalon.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import lk.xtracheese.swiftsalon.Adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.Adapter.LooKBookAdapter;
import lk.xtracheese.swiftsalon.Model.Banner;
import lk.xtracheese.swiftsalon.Model.LookBook;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.Service.PicassoImageLoadingService;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recylerLookBook;
    Slider bannerSlider;

    AlertDialog alertDialog;

    String url;
    String[] resImgAddress;
    List<Banner> banners = new ArrayList<>();
    List<LookBook> lookBooks = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        alertDialog = new SpotsDialog.Builder().setContext(getContext()).build();
        alertDialog.show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bannerSlider = view.findViewById(R.id.banner_slider);
        recylerLookBook = view.findViewById(R.id.recycler_promotion_book);



        //initialize slider
        Slider.init(new PicassoImageLoadingService());
        //get image urls from server
        getPromotions();

        getLookBook();

        alertDialog.dismiss();
        return view;


    }

    void getPromotions() {

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        url = "http://192.168.56.1/SwiftSalon/PromotionBanner.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int length = response.length();
                        resImgAddress = new String[length];
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject salonPromotion = response.getJSONObject(i);

                                // Get the current student (json object) data
                                resImgAddress[i] = salonPromotion.getString("image");
                            }

                            //convert array to list
                            for(String imgURL:resImgAddress){
                                banners.add(new Banner(imgURL));
                            }
                            //set adapter on slider
                            bannerSlider.setAdapter(new BannerSlideAdapter(banners));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        );

        requestQueue.add(jsonArrayRequest);

    }

    void getLookBook(){
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        url = "http://192.168.56.1/SwiftSalon/LookBook.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int length = response.length();
                        resImgAddress = new String[length];
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject salonPromotion = response.getJSONObject(i);

                                // Get the current student (json object) data
                                resImgAddress[i] = salonPromotion.getString("image");
                            }

                            //convert array to list
                            for(String imgURL:resImgAddress){
                                lookBooks.add(new LookBook(imgURL));
                            }
                            //set adapter on recyclerview
                            recylerLookBook.setHasFixedSize(true);
                            recylerLookBook.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recylerLookBook.setAdapter(new LooKBookAdapter(getActivity(), lookBooks));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        );

        requestQueue.add(jsonArrayRequest);

    }



}
