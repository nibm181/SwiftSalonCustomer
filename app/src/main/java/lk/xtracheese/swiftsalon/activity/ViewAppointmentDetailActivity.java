package lk.xtracheese.swiftsalon.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.AppointmentAdapter;
import lk.xtracheese.swiftsalon.adapter.AppointmentDetailAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentDetailViewModel;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentViewModel;

import static lk.xtracheese.swiftsalon.util.Resource.Status.LOADING;

public class ViewAppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "ViewAppointmentDetailAc";

    DialogService alertDialog;

    ViewAppointmentDetailViewModel viewAppointmentDetailViewModel;
    AppointmentDetailAdapter appointmentDetailAdapter;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView txtSalon, txtStylistName, txtDate, txtStatus;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_detail);
        imageView = findViewById(R.id.view_appointment_detail_prof_user_pic);
        txtSalon = findViewById(R.id.view_appointment_detail_salon_name);
        txtDate = findViewById(R.id.view_appointment_detail_date);
        txtStatus = findViewById(R.id.view_appointment_detail_status);
        txtStylistName = findViewById(R.id.view_appointment_detail_stylist_name);
        btnCancel = findViewById(R.id.view_appointment_detail_cancel_btn);
        recyclerView = findViewById(R.id.recycler_view_appointment_detail);

        btnCancel.setVisibility(View.GONE);
        alertDialog = new DialogService(this);

        viewAppointmentDetailViewModel = new ViewModelProvider(this).get(ViewAppointmentDetailViewModel.class);

        setData();

        String userImageURL = "http://10.0.2.2/SwiftSalon/user_images/user.jpeg";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);

        initRecyclerView();
        subscribeObservers();
        getAppointmentDetail();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check is he online!!!

            }
        });
    }

    private void initRecyclerView() {
        appointmentDetailAdapter = new AppointmentDetailAdapter(this);
        recyclerView.setAdapter(appointmentDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAppointmentDetail() {viewAppointmentDetailViewModel.appointmentDetailApi(Common.currentAppointment.getId());}

    private void subscribeObservers() {
        viewAppointmentDetailViewModel.getAppointmentDetail().observe(this, listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            break;
                        }
                        case SUCCESS: {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "subscribeObservers: DATA: " + listResource.data.toString());
                            appointmentDetailAdapter.submitList(listResource.data);
                            break;
                        }
                        case ERROR: {
                            Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
                            appointmentDetailAdapter.submitList(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }

    void setData(){
        txtSalon.setText(Common.currentAppointment.getSalonName());
        txtStylistName.setText(Common.currentAppointment.getStylistName());
        txtStatus.setText("status : "+Common.currentAppointment.getStatus());
        txtDate.setText(new StringBuilder("on "+Common.currentAppointment.getDate())
            .append(" at "+Common.currentAppointment.getTime()));

        if(Common.currentAppointment.getStatus().equals("pending")){
            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    private void updateAppointmentApi() {
        viewAppointmentDetailViewModel.appointmentUpdateApi(Common.currentAppointment);
    }

    private void subscribeObserversForUpdate() {
        viewAppointmentDetailViewModel.getAppointment().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    alertDialog.loadingDialog().show();
                    break;
                case ERROR:
                    alertDialog.dismissLoading();
                    alertDialog.oopsErrorDialog();
                    break;
                case SUCCESS:
                    if (resource.data.getStatus() == 1) {
                        if (resource.data.getContent() != null) {
                            alertDialog.dismissLoading();
                            alertDialog.successAppointmentDialog().show();
                        }
                    }
                    break;

            }
        });
    }

}