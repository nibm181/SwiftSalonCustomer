package lk.xtracheese.swiftsalon.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.BookingActivity;
import lk.xtracheese.swiftsalon.adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.adapter.LooKBookAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.HomeViewModel;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    Session session;

    RecyclerView recyclerLookBook;
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        session = new Session(getContext());
        bannerSlider = view.findViewById(R.id.banner_slider);
        recyclerLookBook = view.findViewById(R.id.recycler_promotion_book);
        imageView = view.findViewById(R.id.home_user_pic);

        String userImageURL = "http://10.0.2.2/swiftsalon-api/uploads/user_images/user.png";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);

        initRecyclerView();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //initialize slider
        Slider.init(new PicassoImageLoadingService());

        subscribeObservers();
        getHomeApi();

        return view;
    }

    private void getHomeApi() {
        viewModel.bannerApi();
    }

    private void subscribeObservers() {
        viewModel.getBanners().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            break;
                        }
                        case SUCCESS: {
                            if (!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                                Common.currentPromotion = listResource.data;
                            } else {
                                bannerSlider.setVisibility(View.GONE);
                            }
                            break;
                        }

                        case ERROR: {
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            if (!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                                Common.currentPromotion = listResource.data;
                            } else {
                                bannerSlider.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                }
            }
        });


        viewModel.getLookBooks().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            break;
                        }
                        case SUCCESS: {
                            lookBookAdapter.submitList(listResource.data);
                            break;
                        }

                        case ERROR: {
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            if (!listResource.data.isEmpty()) {
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
        recyclerLookBook.setAdapter(lookBookAdapter);
        recyclerLookBook.setHasFixedSize(true);
        recyclerLookBook.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }


}
