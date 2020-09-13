package lk.xtracheese.swiftsalon.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.ViewSalonActivity;
import lk.xtracheese.swiftsalon.adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.adapter.LooKBookAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.HomeViewModel;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideChangeListener;
import ss.com.bannerslider.event.OnSlideClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    HomeViewModel viewModel;
    Session session;

    DialogService dialogService;
    SweetAlertDialog sweetAlertDialog;

    LooKBookAdapter lookBookAdapter;
    RecyclerView recyclerLookBook;
    Slider bannerSlider;
    TextView txtUserName, txtPromotionDesc, getTxtPromotionDuration;
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
        sweetAlertDialog = dialogService.loadingDialog();

        bannerSlider = view.findViewById(R.id.banner_slider);
        recyclerLookBook = view.findViewById(R.id.recycler_promotion_book);
        imageView = view.findViewById(R.id.home_user_pic);
        txtPromotionDesc = view.findViewById(R.id.txt_home_promotion_desc);
        getTxtPromotionDuration = view.findViewById(R.id.txt_home_promotion_duration);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        sweetAlertDialog.dismissWithAnimation();
    }

    private void getHomeApi() {
        viewModel.bannerApi();
    }

    private void subscribeObservers() {
        viewModel.getBanners().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }
                    case SUCCESS: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (!listResource.data.isEmpty()) {
                            Common.currentPromotion = listResource.data;
                            //set adapter on slider
                            bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                            bannerSlider.setInterval(5000);
                            setPromotionData(0);
                            bannerSlider.setOnSlideClickListener(new OnSlideClickListener() {
                                @Override
                                public void onSlideClick(int position) {
                                    if (position >= 0) {
                                        Intent intent = new Intent(getContext(), ViewSalonActivity.class);
                                        intent.putExtra("salonId", Common.currentPromotion.get(position).getSalonId());
                                        startActivity(intent);
                                    }
                                }

                            });
                            bannerSlider.setSlideChangeListener(new OnSlideChangeListener() {
                                @Override
                                public void onSlideChange(int position) {
                                    setPromotionData(position);
                                }
                            });
                        } else {
                            noPromotion();
                        }
                        break;
                    }

                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        dialogService.showToast(listResource.message);
                        if (!listResource.data.isEmpty()) {
                            //set adapter on slider
                            bannerSlider.setAdapter(new BannerSlideAdapter(listResource.data));
                            bannerSlider.setInterval(5000);
                            setPromotionData(0);
                            Common.currentPromotion = listResource.data;
                        } else {
                            noPromotion();
                        }
                        break;
                    }
                }
            }
        });


        viewModel.getLookBooks().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }
                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        dialogService.showToast(listResource.message);
                        if (!listResource.data.isEmpty()) {
                            initRecyclerView();
                            lookBookAdapter.submitList(listResource.data);
                        }
                        break;
                    }
                    case SUCCESS: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (listResource.data != null) {
                            initRecyclerView();
                            lookBookAdapter.submitList(listResource.data);
                        }
                        break;
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

    private void setPromotionData(int pos) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            String startDate = formatter.format(Common.currentPromotion.get(pos).getStartDate());
            String endDate = formatter.format(Common.currentPromotion.get(pos).getEndDate());
            txtPromotionDesc.setText(Common.currentPromotion.get(pos).getDescription());
            getTxtPromotionDuration.setText("from " + startDate + " to " + endDate);

    }

    private void noPromotion() {
        bannerSlider.setVisibility(View.GONE);
        txtPromotionDesc.setVisibility(View.GONE);
        getTxtPromotionDuration.setVisibility(View.GONE);
    }
}
