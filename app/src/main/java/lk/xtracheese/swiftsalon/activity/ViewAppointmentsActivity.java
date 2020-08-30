package lk.xtracheese.swiftsalon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.AppointmentAdapter;
import lk.xtracheese.swiftsalon.adapter.AppointmentDetailAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.viewmodel.ViewAppointmentViewModel;

public class ViewAppointmentsActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = "ViewAppointmentsAc";

    RecyclerView recyclerView;
    ViewAppointmentViewModel viewAppointmentViewModel;
    AppointmentAdapter appointmentAdapter;

    DialogService alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

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
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            break;
                        }
                        case SUCCESS: {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "subscribeObservers: DATA: " + listResource.data.toString());
                            appointmentAdapter.submitList(listResource.data);
                            break;
                        }
                        case ERROR: {
                            Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
                            appointmentAdapter.submitList(listResource.data);
                            break;
                        }
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
        Common.currentAppointment = appointment;
        startActivity(intent);

    }
}