package lk.xtracheese.swiftsalon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.AppointmentDetailAdapter;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentDetailViewModel;

import static lk.xtracheese.swiftsalon.util.Constants.STATUS_CANCELED;

public class ViewAppointmentDetailActivity extends AppCompatActivity {

    private static final String TAG = "ViewAppointmentDetailAc";


    Session session;
    Appointment appointment;
    Salon salon;

    DialogService alertDialog;
    SweetAlertDialog sweetAlertDialog;
    PicassoImageLoadingService picassoImageLoadingService;
    ViewAppointmentDetailViewModel viewAppointmentDetailViewModel;
    AppointmentDetailAdapter appointmentDetailAdapter;

    RecyclerView recyclerView;
    TextView txtSalon, txtStylistName, txtDate, txtStatus;
    Button btnCancel, btnNav;

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
        btnNav = findViewById(R.id.view_appointment_detail_nav);

        btnCancel.setVisibility(View.GONE);
        alertDialog = new DialogService(this);
        sweetAlertDialog = alertDialog.loadingDialog();

        viewAppointmentDetailViewModel = new ViewModelProvider(this).get(ViewAppointmentDetailViewModel.class);

        setData();

        picassoImageLoadingService = new PicassoImageLoadingService();

        initRecyclerView();
        subscribeObserversForAppointmentDetails();
        subscribeObserversForUpdate();
        subscribeObserversForSalon();
        getAppointmentDetailApi();
        getSalonApi();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check is he online!!!
                if (isOnline()) {
                    appointment.setStatus(STATUS_CANCELED);
                    updateAppointmentApi();
                } else {
                    alertDialog.notConnected().show();
                }
            }
        });

        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salon != null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + salon.getLatitude() + "," + salon.getLongitude()));
                    startActivity(intent);
                } else {
                    getSalonApi();
                    alertDialog.showToast("Salon not loaded");
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

    private void getAppointmentDetailApi() {
        viewAppointmentDetailViewModel.appointmentDetailApi(appointment.getId());
    }

    private void subscribeObserversForAppointmentDetails() {
        viewAppointmentDetailViewModel.getAppointmentDetail().observe(this, listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            sweetAlertDialog = alertDialog.loadingDialog();
                            sweetAlertDialog.show();
                            break;
                        }
                        case SUCCESS: {
                            sweetAlertDialog.dismissWithAnimation();
                            appointmentDetailAdapter.submitList(listResource.data);
                            break;
                        }
                        case ERROR: {
                            sweetAlertDialog.dismissWithAnimation();
                            Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
                            appointmentDetailAdapter.submitList(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }


    private void updateAppointmentApi() {
        viewAppointmentDetailViewModel.appointmentUpdateApi(appointment);
    }

    private void subscribeObserversForUpdate() {
        viewAppointmentDetailViewModel.updateAppointment().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        sweetAlertDialog.show();
                        break;
                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        alertDialog.showToast(resource.message);
                        break;
                    }
                    case SUCCESS: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (resource.data != null) {
                            if (resource.data.getStatus() == 1) {
                                txtStatus.setText("status : cancelled");
                                btnCancel.setVisibility(View.GONE);
                                alertDialog.appointmentCancelled().show();
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    private void getSalonApi() {
        viewAppointmentDetailViewModel.getSalonApi(appointment.getSalonId());
    }

    private void subscribeObserversForSalon() {
        viewAppointmentDetailViewModel.getSalon().observe(this, salonResource -> {
            if (salonResource != null) {
                if (salonResource.data != null) {
                    switch (salonResource.status) {
                        case LOADING: {
                            sweetAlertDialog = alertDialog.loadingDialog();
                            sweetAlertDialog.show();
                            break;
                        }
                        case ERROR: {
                            sweetAlertDialog.dismissWithAnimation();
                            alertDialog.showToast(salonResource.message + " from salon api");
                            break;
                        }
                        case SUCCESS: {
                            sweetAlertDialog.dismissWithAnimation();
                            salon = salonResource.data;
                            break;
                        }
                    }
                }
            }
        });
    }

    void setData() {
        txtSalon.setText(appointment.getSalonName());
        txtStylistName.setText(appointment.getStylistName());
        txtStatus.setText("status : " + appointment.getStatus());
        txtDate.setText(new StringBuilder("on " + appointment.getDate())
                .append(" at " + appointment.getTime()));

        if (appointment.getStatus().equals("pending")) {
            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}