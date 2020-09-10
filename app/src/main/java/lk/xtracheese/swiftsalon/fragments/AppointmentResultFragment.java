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
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Appointment;


public class AppointmentResultFragment extends Fragment {

    static AppointmentResultFragment instance;

    TextView txtAppointmentStatus, txtMobNo, txtSalon, txtStylist, txtDate, txtAddr;


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
        txtMobNo = itemView.findViewById(R.id.view_appointment_detail_contact);
        txtAddr = itemView.findViewById(R.id.view_appointment_detail_salon_addr);
        txtDate = itemView.findViewById(R.id.view_appointment_detail_date);
        txtSalon = itemView.findViewById(R.id.view_appointment_detail_salon_name);
        txtStylist = itemView.findViewById(R.id.view_appointment_detail_hs_name);

        txtAppointmentStatus.setText("Your appointment has been successfully requested to Salon. Appointment wil be confirmed once salon accepts your appointment request");
        txtStylist.setText(Common.currentAppointment.getSalonName());
        txtSalon.setText(Common.currentStylist.getName());
        txtMobNo.setText(Common.currentSalon.getMobileNo());
        txtAddr.setText(Common.currentSalon.getSalonAddress1()+" "+Common.currentSalon.getSalonAddress2());
        txtDate.setText(Common.currentAppointment.getDate());

        return itemView;
    }
}