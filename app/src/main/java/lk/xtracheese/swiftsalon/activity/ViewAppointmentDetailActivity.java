package lk.xtracheese.swiftsalon.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.impl.constraints.controllers.NetworkNotRoamingController;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.AppointmentAdapter;
import lk.xtracheese.swiftsalon.adapter.AppointmentDetailAdapter;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentDetailViewModel;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentViewModel;

import static lk.xtracheese.swiftsalon.util.Resource.Status.LOADING;

public class ViewAppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "ViewAppointmentDetailAc";

    DialogService alertDialog;
    PicassoImageLoadingService picassoImageLoadingService;
    Session session;
    Appointment appointment;
    ViewAppointmentDetailViewModel viewAppointmentDetailViewModel;
    AppointmentDetailAdapter appointmentDetailAdapter;

    RecyclerView recyclerView;
    TextView txtSalon, txtStylistName, txtDate, txtStatus;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_detail);

        appointment = getIntent().getParcelableExtra("appointment");
        session = new Session(this);

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

        picassoImageLoadingService = new PicassoImageLoadingService();

        initRecyclerView();
        subscribeObservers();
        subscribeObserversForUpdate();
        getAppointmentDetail();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check is he online!!!
                if(isOnline()){
                    appointment.setStatus("cancelled");
                    updateAppointmentApi();
                }
            }
        });
    }

    private void initRecyclerView() {
        appointmentDetailAdapter = new AppointmentDetailAdapter(this);
        recyclerView.setAdapter(appointmentDetailAdapter);
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAppointmentDetail() {viewAppointmentDetailViewModel.appointmentDetailApi(appointment.getId());}

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
        txtSalon.setText(appointment.getSalonName());
        txtStylistName.setText(appointment.getStylistName());
        txtStatus.setText("status : "+appointment.getStatus());
        txtDate.setText(new StringBuilder("on "+appointment.getDate())
            .append(" at "+appointment.getTime()));

        if(appointment.getStatus().equals("pending")){
            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    private void updateAppointmentApi() {
        viewAppointmentDetailViewModel.appointmentUpdateApi(appointment);
    }

    private void subscribeObserversForUpdate() {
        viewAppointmentDetailViewModel.updateAppointment().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    alertDialog.loadingDialog().show();
                    break;
                case ERROR:
                    alertDialog.dismissLoading();
                    alertDialog.oopsErrorDialog().show();
                    break;
                case SUCCESS:
                    if (resource.data.getStatus() == 1) {
                        if (resource.data.getContent() != null) {
                            txtStatus.setText("status : cancelled");
                            btnCancel.setVisibility(View.GONE);
                            alertDialog.dismissLoading();
                            alertDialog.appointmentCancelled().show();
                        }
                    }
                    break;

            }
        });
    }

    public boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }

}