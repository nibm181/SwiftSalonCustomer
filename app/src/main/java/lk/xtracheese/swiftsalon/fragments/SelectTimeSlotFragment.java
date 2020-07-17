package lk.xtracheese.swiftsalon.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import lk.xtracheese.swiftsalon.adapter.TimeSlotAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.viewmodel.SelectJobViewModel;
import lk.xtracheese.swiftsalon.viewmodel.SelectTimeSlotViewModel;

public class SelectTimeSlotFragment extends Fragment {

    private static final String TAG = "SelectTimeSlotFragment";
    
    RecyclerView recyclerTimeSlots;
    HorizontalCalendarView horizontalCalendarView;
    SimpleDateFormat simpleDateFormat;
    SelectTimeSlotViewModel viewModel;
    TimeSlotAdapter timeSlotAdapter;


    LocalBroadcastManager localBroadcastManager;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1); // add next date
            String date = simpleDateFormat.format(calendar.getTime());
            Common.currentDate = calendar;

            subscribeObservers();
            getTimeSlotsApi(Common.currentStylist.getId(), date, Common.currentSalon.getOpenTime(), Common.currentSalon.getCloseTime(), jobCalculate());

        }
    };



    static SelectTimeSlotFragment instance;
    public static SelectTimeSlotFragment getInstance(){
        if(instance == null)
            instance = new SelectTimeSlotFragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_JOB_SELECTED));

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View itemView = inflater.inflate(R.layout.fragment_booking_step_three,container, false);
        recyclerTimeSlots = itemView.findViewById(R.id.recycler_time_slot);

        viewModel = new ViewModelProvider(this).get(SelectTimeSlotViewModel.class);

        initRecyclerView();
        initCalender(itemView);
        return itemView;

    }

    private void initCalender(View itemView) {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 15); //2 days left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calenderView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.currentDate = date;
                    String sDate = simpleDateFormat.format(date.getTime());
                    subscribeObservers();
                    getTimeSlotsApi(Common.currentStylist.getId(), sDate, Common.currentSalon.getOpenTime(), Common.currentSalon.getCloseTime(), jobCalculate() );

                }
            }
        });
    }

    private void initRecyclerView() {
        timeSlotAdapter = new TimeSlotAdapter(getActivity());
        recyclerTimeSlots.setAdapter(timeSlotAdapter);
        recyclerTimeSlots.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerTimeSlots.setLayoutManager(gridLayoutManager);
        recyclerTimeSlots.addItemDecoration(new SpacesitemDecoration(2));

        //calender

    }

    private int jobCalculate(){

        int totDuration = 0;

        if(Common.currentJob != null){
            int jobSize = Common.currentJob.size();
            Log.d(TAG, "jobCalculate: JOB SIZE " +jobSize);
            for(int i=0; i<jobSize ;i++){
                totDuration = totDuration + Common.currentJob.get(i).getDuration();
            }
            Log.d(TAG, "jobCalculate: Duration " +totDuration);
        }
        return totDuration;
    }

    private void getTimeSlotsApi(int stylistId, String date, String openTime, String closeTime, int duration ){
        viewModel.timeSlotsApi(stylistId, date, openTime, closeTime, duration);
    }

    private void subscribeObservers(){
        viewModel.getTimeSlots().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                Log.d(TAG, "subscribeObservers: RESOURCE: " + listResource.message);
                if(listResource.data != null){

                    switch (listResource.status){
                        case LOADING:{
                            break;
                        }

                        case ERROR:{
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                        }
                        case SUCCESS:{
                            if(listResource.data.getStatus() == 1){
                                timeSlotAdapter.submitList(listResource.data.getContent());
                            }

                        }
                    }
                }
            }
        });
    }

}
