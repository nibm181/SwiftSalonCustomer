package lk.xtracheese.swiftsalon.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;

import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.R;

public class ConfirmAppointmentFragment extends Fragment {

    TextView txtSalon, txtHairStylistName, txtDate,txtContact, txtAddr;

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;

    BroadcastReceiver confrimBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txtSalon.setText(Common.currentSalon.getSalonName());
        txtAddr.setText(Common.currentSalon.getSalonAddress());
        txtHairStylistName.setText(new StringBuilder("Stylist: ").
                append(Common.currentHairStylist.getName()));
        txtDate.setText(new StringBuilder((Common.currentTimeSlot.getSlotTiming()))
                .append(" on ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));
    }

    static ConfirmAppointmentFragment instance;
    public static ConfirmAppointmentFragment getInstance(){
        if(instance == null)
            instance = new ConfirmAppointmentFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display on confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confrimBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confrimBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container, false);
        txtSalon = itemView.findViewById(R.id.booking_confirm_salon_name);
        txtDate = itemView.findViewById(R.id.booking_confirm_date);
        txtHairStylistName = itemView.findViewById(R.id.booking_confirm_hs_name);
        txtContact = itemView.findViewById(R.id.booking_confirm_contact);
        txtAddr = itemView.findViewById(R.id.booking_confirm_salon_addr);

        return itemView;

    }
}
