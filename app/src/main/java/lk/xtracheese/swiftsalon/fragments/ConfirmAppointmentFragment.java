package lk.xtracheese.swiftsalon.fragments;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import lk.xtracheese.swiftsalon.adapter.JobAdapter;
import lk.xtracheese.swiftsalon.adapter.JobPriceAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.StylistJob;

public class ConfirmAppointmentFragment extends Fragment {

    private static final String TAG = "ConfirmAppointmentFragm";

    TextView txtSalon, txtHairStylistName, txtDate,txtContact, txtAddr, txtStylist, txtAmount;
    RecyclerView recyclerView;
    JobPriceAdapter jobPriceAdapter;
    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;

    float totPrice;

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        jobCalculate();
        txtSalon.setText(Common.currentSalon.getSalonName());
        txtAddr.setText(new StringBuilder(Common.currentSalon.getSalonAddress1())
                    .append(", "+Common.currentSalon.getSalonAddress2()));
        txtContact.setText(Common.currentSalon.getMobileNo());
        txtStylist.setText(Common.currentStylist.getName());
        txtHairStylistName.setText(new StringBuilder("Stylist: ")
                 .append(Common.currentStylist.getName()));
        txtDate.setText(new StringBuilder((Common.currentTimeSlot.getSlotTiming()))
                .append(" on ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));
        txtAmount.setText(new StringBuilder(String.valueOf(totPrice))
                .append(".00 Rs  "));

        initRecyclerView();
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
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container, false);
        txtSalon = itemView.findViewById(R.id.booking_confirm_salon_name);
        txtDate = itemView.findViewById(R.id.booking_confirm_date);
        txtStylist = itemView.findViewById(R.id.booking_confirm_hs_name);
        txtHairStylistName = itemView.findViewById(R.id.booking_confirm_hs_name);
        txtContact = itemView.findViewById(R.id.booking_confirm_contact);
        txtAddr = itemView.findViewById(R.id.booking_confirm_salon_addr);
        txtAmount = itemView.findViewById(R.id.booking_confirm_amount);
        recyclerView = itemView.findViewById(R.id.recycler_jobs);

        return itemView;

    }

    private void jobCalculate(){
            totPrice = 0;
            for(StylistJob stylistJob : Common.currentJob){
                totPrice = totPrice + stylistJob.getPrice();
                if(Common.currentPromotion != null){
                    for(Promotion promotion : Common.currentPromotion){
                        if(promotion.getJobId() == stylistJob.getJobId()){
                            totPrice = totPrice - promotion.getOffAmount();
                        }
                    }
                }

            }

    }

    void initRecyclerView(){
        jobPriceAdapter = new JobPriceAdapter(getActivity());
        jobPriceAdapter.submitList(Common.currentJob);
        recyclerView.setAdapter(jobPriceAdapter);
        recyclerView.setHasFixedSize(true);;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));

    }
}
