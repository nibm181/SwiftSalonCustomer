package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    RecyclerView recyclerView;
    Session session;
    PicassoImageLoadingService picassoImageLoadingService;


    TextView  txtName, txtAddress, txtMobile, txtUsername;
    ImageView imageView;

    int salonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salon);

        session = new Session(ViewSalonActivity.this);
        selectStylistViewModel = new ViewModelProvider(this).get(SelectStylistViewModel.class);
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        alertDialog = new DialogService(this);
        salonId = getIntent().getIntExtra("salonId", 0);

        txtName = findViewById(R.id.txt_salon_name);
        txtAddress = findViewById(R.id.txt_salon_addr);
        txtMobile =findViewById(R.id.txt_salon_mob);
        recyclerView = findViewById(R.id.recycler_salon_stylist);
        txtUsername = findViewById(R.id.txt_user_name);
        imageView = findViewById(R.id.prof_user_pic);

        txtUsername.setText(session.getUsername());
        picassoImageLoadingService.loadImageRound(session.getUserImg(), imageView);


        initRecyclerView();

        selectSalon();

    }

    public void setSalonData(){
        txtName.setText(Common.currentSalon.getSalonName());
        txtMobile.setText(Common.currentSalon.getMobileNo());
        txtAddress.setText(new StringBuilder(Common.currentSalon.getSalonAddress1())
                .append(" "+Common.currentSalon.getSalonAddress2()));
    }

    public void selectSalon(){
        if(salonId != 0){
            subscribeObserversForSalon();
            subscribeObservers();
            getSalonApi(salonId);

        }else{
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
            if(listResource != null){
                if(listResource.data != null){
                    switch (listResource.status){
                        case LOADING:{
                            alertDialog.loadingDialog().show();
                            break;
                        }
                        case SUCCESS:{
                            alertDialog.dismissLoading();
                            stylistAdapter.submitList(listResource.data);
                        }
                        case ERROR:{
                            alertDialog.dismissLoading();
                            alertDialog.showToast(listResource.message);
                            stylistAdapter.submitList(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void getStylists() {selectStylistViewModel.stylistApi();}

    private void subscribeObserversForSalon(){
        selectStylistViewModel.getPromotionSalon().observe(this, salonResource -> {
            switch (salonResource.status) {
                case LOADING:
                    alertDialog.loadingDialog().show();
                    break;
                case ERROR:
                    alertDialog.dismissLoading();
                    alertDialog.showToast(salonResource.message);
                    break;
                case SUCCESS:
                    if (salonResource.data != null) {
                        alertDialog.dismissLoading();
                        Common.currentSalon = salonResource.data;
                        setSalonData();
                        getStylists();
                    }
                    break;

            }
        });
    }

    private void getSalonApi(int salonId) {
        selectStylistViewModel.salonApi(salonId);
    }

}