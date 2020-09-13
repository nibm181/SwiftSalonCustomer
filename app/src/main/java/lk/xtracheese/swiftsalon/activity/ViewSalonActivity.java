package lk.xtracheese.swiftsalon.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.HairStylistAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.SelectStylistViewModel;

public class ViewSalonActivity extends AppCompatActivity {

    private static final String TAG = "ViewSalonActivity";

    SelectStylistViewModel selectStylistViewModel;

    HairStylistAdapter stylistAdapter;
    DialogService alertDialog;
    SweetAlertDialog sweetAlertDialog;
    RecyclerView recyclerView;
    Session session;
    PicassoImageLoadingService picassoImageLoadingService;


    TextView txtName, txtAddress, txtMobile, txtUsername, txtType, txtOpenTime;
    ImageView imageView;

    int salonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salon);

        session = new Session(ViewSalonActivity.this);
        selectStylistViewModel = new ViewModelProvider(this).get(SelectStylistViewModel.class);
        alertDialog = new DialogService(this);
        sweetAlertDialog = alertDialog.loadingDialog();
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        salonId = getIntent().getIntExtra("salonId", 0);

        txtName = findViewById(R.id.txt_salon_name);
        txtAddress = findViewById(R.id.txt_salon_addr);
        txtMobile = findViewById(R.id.txt_salon_mob);
        recyclerView = findViewById(R.id.recycler_salon_stylist);
        txtUsername = findViewById(R.id.txt_user_name);
        txtOpenTime = findViewById(R.id.txt_salon_open_time);
        txtType = findViewById(R.id.txt_salon_type);
        imageView = findViewById(R.id.prof_user_pic);

        txtUsername.setText(session.getUsername());
        picassoImageLoadingService.loadImageRound(session.getUserImg(), imageView);


        initRecyclerView();

        selectSalon();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sweetAlertDialog.dismissWithAnimation();
    }

    public void setSalonData() {
        if (Common.currentSalon != null) {
            txtName.setText(Common.currentSalon.getSalonName());
            txtMobile.setText(Common.currentSalon.getMobileNo());
            txtAddress.setText(new StringBuilder(Common.currentSalon.getSalonAddress1())
                    .append(" " + Common.currentSalon.getSalonAddress2()));
            txtType.setText("Type: " + Common.currentSalon.getType());
            txtOpenTime.setText("Open Time: " + Common.currentSalon.getOpenTime() + " - " + Common.currentSalon.getCloseTime());
        } else {
            alertDialog.showToast("Network error!");
        }

    }


    public void selectSalon() {
        if (salonId != 0) {
            subscribeObserversForSalon();
            subscribeObservers();
            getSalonApi(salonId);

        } else {
            setSalonData();
            subscribeObservers();
            getStylists();
        }
    }

    private void initRecyclerView() {
        stylistAdapter = new HairStylistAdapter(this);
        recyclerView.setAdapter(stylistAdapter);
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeObservers() {

        selectStylistViewModel.getStylist().observe(this, listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }
                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        alertDialog.showToast(listResource.message);
                        stylistAdapter.submitList(listResource.data);
                        break;
                    }
                    case SUCCESS: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (listResource.data != null) {
                            stylistAdapter.submitList(listResource.data);
                        }
                        break;

                    }
                }
            }
        });
    }

    private void getStylists() {
        selectStylistViewModel.stylistApi();
    }

    private void subscribeObserversForSalon() {
        selectStylistViewModel.getPromotionSalon().observe(this, salonResource -> {
            if (salonResource != null) {
                switch (salonResource.status) {
                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }
                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        alertDialog.showToast(salonResource.message);
                        break;
                    }
                    case SUCCESS: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (salonResource.data != null) {
                            Common.currentSalon = salonResource.data;
                            setSalonData();
                            getStylists();
                        }
                        break;
                    }
                }
            }


        });
    }

    private void getSalonApi(int salonId) {
        selectStylistViewModel.salonApi(salonId);
    }

}