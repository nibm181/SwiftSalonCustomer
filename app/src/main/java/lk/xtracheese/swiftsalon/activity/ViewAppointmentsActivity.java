package lk.xtracheese.swiftsalon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.AppointmentAdapter;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentViewModel;

public class ViewAppointmentsActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = "ViewAppointmentsAc";

    RecyclerView recyclerView;
    ViewAppointmentViewModel viewAppointmentViewModel;
    AppointmentAdapter appointmentAdapter;

    DialogService alertDialog;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        alertDialog = new DialogService(this);

        if (!isOnline()) {
            alertDialog.notConnected().show();
        }

        alertDialog = new DialogService(this);
        sweetAlertDialog = alertDialog.loadingDialog();
        recyclerView = findViewById(R.id.recycler_view_appointments);
        viewAppointmentViewModel = new ViewModelProvider(this).get(ViewAppointmentViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getAppointments();
    }


    private void getAppointments() {
        viewAppointmentViewModel.appointmentApi();
    }

    private void subscribeObservers() {
        viewAppointmentViewModel.getAppointments().observe(this, listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }
                    case SUCCESS: {
                        if (listResource.data != null) {
                            sweetAlertDialog.dismissWithAnimation();
                            appointmentAdapter.submitList(listResource.data);
                        }
                        break;
                    }
                    case ERROR: {
                        sweetAlertDialog.dismissWithAnimation();
                        if (listResource.data.isEmpty()) {
                            Intent intent1 = new Intent(ViewAppointmentsActivity.this, NoAppointmentsActivity.class);
                            startActivity(intent1);
                        } else {
                            alertDialog.showToast(listResource.message);
                            appointmentAdapter.submitList(listResource.data);
                        }
                        break;
                    }
                }
            }


        });
    }

    private void initRecyclerView() {
        appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        Appointment appointment = appointmentAdapter.getSelectedAppointment(position);
        Intent intent = new Intent(ViewAppointmentsActivity.this, ViewAppointmentDetailActivity.class);
        intent.putExtra("appointment", appointment);
        startActivity(intent);

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}