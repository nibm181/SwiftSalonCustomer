package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.HairStylistAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.viewmodel.SelectStylistViewModel;

public class ViewSalonActivity extends AppCompatActivity {

    private static final String TAG = "ViewSalonActivity";

    TextView  txtName, txtAddress, txtMobile;

    RecyclerView recyclerView;
    SelectStylistViewModel selectStylistViewModel;
    HairStylistAdapter stylistAdapter;

    DialogService alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salon);

        txtName = findViewById(R.id.txt_salon_name);
        txtAddress = findViewById(R.id.txt_salon_addr);
        txtMobile =findViewById(R.id.txt_salon_mob);

        txtName.setText(Common.currentSalon.getSalonName());
        txtMobile.setText(Common.currentSalon.getMobileNo());
        txtAddress.setText(new StringBuilder(Common.currentSalon.getSalonAddress1())
                .append(" "+Common.currentSalon.getSalonAddress2()));

        recyclerView = findViewById(R.id.recycler_salon_stylist);
        selectStylistViewModel = new ViewModelProvider(this).get(SelectStylistViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getStylists();
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
                            break;
                        }
                        case SUCCESS:{
                            stylistAdapter.submitList(listResource.data);
                        }
                        case ERROR:{
                            stylistAdapter.submitList(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void getStylists() {selectStylistViewModel.stylistApi();}

}