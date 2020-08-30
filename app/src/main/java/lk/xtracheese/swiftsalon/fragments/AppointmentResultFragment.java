package lk.xtracheese.swiftsalon.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.Appointment;


public class AppointmentResultFragment extends Fragment {

    static AppointmentResultFragment instance;

    TextView txtAppointmentStatus;


    public AppointmentResultFragment() {
        // Required empty public constructor
    }


    public static AppointmentResultFragment getInstance(){
        if(instance == null)
            instance = new AppointmentResultFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_appointment_result, container, false);
        txtAppointmentStatus = itemView.findViewById(R.id.appointment_result_status);
        txtAppointmentStatus.setText("Your appointment has been successfully requested to Salon. Appointment wil be confirmed once salon accepts your appointment request");


        return itemView;
    }
}