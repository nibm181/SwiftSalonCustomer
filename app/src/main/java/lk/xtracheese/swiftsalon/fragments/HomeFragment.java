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
import android.widget.ImageView;
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
    ImageView imageView;

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
        imageView = view.findViewById(R.id.home_user_pic);

        String userImageURL = "http://10.0.2.2/SwiftSalon/user_images/user.jpeg";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);

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
                            break;
                        }

                        case ERROR:{
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            if(!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                            }
                            break;
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
                            break;
                        }

                        case ERROR:{
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            if(!listResource.data.isEmpty()){
                                lookBookAdapter.submitList(listResource.data);
                            }
                            break;
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

    


}
