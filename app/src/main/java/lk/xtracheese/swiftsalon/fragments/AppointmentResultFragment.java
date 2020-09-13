package lk.xtracheese.swiftsalon.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Appointment;


public class AppointmentResultFragment extends Fragment {

    static AppointmentResultFragment instance;

    TextView txtAppointmentStatus, txtMobNo, txtSalon, txtStylist, txtDate, txtAddr;

    LocalBroadcastManager localBroadcastManager;

    BroadcastReceiver AppointmentRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };



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
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(AppointmentRequest, new IntentFilter(Common.KEY_APPOINTMENT_SENT));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(AppointmentRequest);
        super.onDestroy();
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


        return itemView;
    }

    void setData(){



        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(Common.currentAppointment.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String date = String.valueOf(cal.get(Calendar.DATE));
        String month = (new SimpleDateFormat("MMM").format(cal.getTime()));
        String year = String.valueOf(cal.get(Calendar.YEAR));

        txtAppointmentStatus.setText("Your appointment has been successfully requested to Salon. Appointment wil be confirmed once salon accepts your appointment request");
        txtStylist.setText(Common.currentStylist.getName());
        txtSalon.setText(Common.currentSalon.getSalonName());
        txtMobNo.setText(Common.currentSalon.getMobileNo());
        txtAddr.setText(Common.currentSalon.getSalonAddress1()+" "+Common.currentSalon.getSalonAddress2());
        txtDate.setText(date+" "+month+" "+year);
    }
}