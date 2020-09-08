package lk.xtracheese.swiftsalon.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.BookingActivity;
import lk.xtracheese.swiftsalon.activity.ViewSalonActivity;
import lk.xtracheese.swiftsalon.adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.adapter.LooKBookAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.HomeViewModel;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    HomeViewModel viewModel;
    Session session;
    DialogService dialogService;
    LooKBookAdapter lookBookAdapter;

    RecyclerView recyclerLookBook;
    Slider bannerSlider;
    TextView txtUserName;
    ImageView imageView;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        session = new Session(getContext());
        dialogService = new DialogService(getContext());

        bannerSlider = view.findViewById(R.id.banner_slider);
        recyclerLookBook = view.findViewById(R.id.recycler_promotion_book);
        imageView = view.findViewById(R.id.home_user_pic);
        txtUserName = view.findViewById(R.id.txt_user_name);
        txtUserName.setText(session.getUsername());

        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImageRound(session.getUserImg(), imageView);


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
                                Common.currentPromotion = listResource.data;
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                                bannerSlider.setInterval(5000);
                                bannerSlider.setOnSlideClickListener(new OnSlideClickListener() {
                                    @Override
                                    public void onSlideClick(int position) {
                                        Log.d(TAG, "onSlideClick: Clicked "+position);
                                        Intent intent =new Intent(getContext(), ViewSalonActivity.class);
                                        intent.putExtra("salonId", Common.currentPromotion.get(position).getSalonId());
                                        startActivity(intent);

                                    }
                                });
                            } else {
                                bannerSlider.setVisibility(View.GONE);
                            }
                            break;
                        }

                        case ERROR: {
                            dialogService.showToast(listResource.message);
                            if (!listResource.data.isEmpty()) {
                                //set adapter on slider
                                bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                                bannerSlider.setInterval(5000);
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
                            initRecyclerView();
                            lookBookAdapter.submitList(listResource.data);
                            break;
                        }

                        case ERROR: {
                            dialogService.showToast(listResource.message);
                            if (!listResource.data.isEmpty()) {
                                initRecyclerView();
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
