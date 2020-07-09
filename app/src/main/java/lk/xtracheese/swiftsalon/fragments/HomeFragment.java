package lk.xtracheese.swiftsalon.fragments;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import lk.xtracheese.swiftsalon.adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.adapter.LooKBookAdapter;
import lk.xtracheese.swiftsalon.model.Banner;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.viewmodel.HomeViewModel;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    RecyclerView recylerLookBook;
    Slider bannerSlider;
    AlertDialog alertDialog;
    LooKBookAdapter lookBookAdapter;

    HomeViewModel viewModel;


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

        initRecyclerView();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //initialize slider
        Slider.init(new PicassoImageLoadingService());

        subscribeObservers();
        getSalonsApi();

        alertDialog.dismiss();

        return view;
    }

    private void getSalonsApi() {
        viewModel.bannerApi();
    }

    private void subscribeObservers() {
        viewModel.getBanners().observe(getViewLifecycleOwner(), listResource -> {
            if(listResource != null){
                if(listResource.data != null){
                    switch (listResource.status) {
                        case LOADING:{
                            break;
                        }
                        case SUCCESS:{
                            if(!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                            }
                        }

                        case ERROR:{
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            if(!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                            }
                        }
                    }
                }
            }
        });


        viewModel.getLookBooks().observe(getViewLifecycleOwner(), listResource -> {
            if(listResource != null){
                if(listResource.data != null){
                    switch (listResource.status) {
                        case LOADING:{
                            break;
                        }
                        case SUCCESS:{
                            lookBookAdapter.submitList(listResource.data);
                        }

                        case ERROR:{
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            lookBookAdapter.submitList(listResource.data);
                        }
                    }
                }
            }
        });

    }

    private void initRecyclerView() {
        lookBookAdapter = new LooKBookAdapter(getActivity());
        recylerLookBook.setAdapter(lookBookAdapter);
        recylerLookBook.setHasFixedSize(true);
        recylerLookBook.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }


//    void getPromotions() {
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        url = "http://10.0.2.2/SwiftSalon/PromotionBanner.php";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        int length = response.length();
//                        resImgAddress = new String[length];
//                        try {
//
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject salonPromotion = response.getJSONObject(i);
//
//                                // Get the current student (json object) data
//                                resImgAddress[i] = salonPromotion.getString("image");
//                            }
//
//                            //convert array to list
//                            for(String imgURL:resImgAddress){
//                                banners.add(new Banner(imgURL));
//                            }
//                            //set adapter on slider
//                            bannerSlider.setAdapter(new BannerSlideAdapter(banners));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.d("debug", error.getMessage());
//                    }
//                }
//
//
//        );
//
//        requestQueue.add(jsonArrayRequest);
//
//    }
//
//    void getLookBook(){
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        url = "http://10.0.2.2/SwiftSalon/LookBook.php";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        int length = response.length();
//                        resImgAddress = new String[length];
//                        try {
//
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject salonPromotion = response.getJSONObject(i);
//
//                                // Get the current student (json object) data
//                                resImgAddress[i] = salonPromotion.getString("image");
//                            }
//
//                            //convert array to list
//                            for(String imgURL:resImgAddress){
//                                lookBooks.add(new LookBook(imgURL));
//                            }
//                            //set adapter on recyclerview
//                            recylerLookBook.setHasFixedSize(true);
//                            recylerLookBook.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recylerLookBook.setAdapter(new LooKBookAdapter(getActivity(), lookBooks));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//        );
//
//        requestQueue.add(jsonArrayRequest);
//
//    }



}
