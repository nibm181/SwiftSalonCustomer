package lk.xtracheese.swiftsalon.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import lk.xtracheese.swiftsalon.Adapter.TimeSlotAdapter;
import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.Common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.Model.TimeSlot;
import lk.xtracheese.swiftsalon.R;

public class SelectTimeSlotFragment extends Fragment {

    RecyclerView recyclerTimeSlots;
    HorizontalCalendarView horizontalCalendarView;
    SimpleDateFormat simpleDateFormat;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0); // add current date

            ArrayList<TimeSlot> timeSlotsArrayList = intent.getParcelableArrayListExtra(Common.KEY_TIME_SLOT_LOAD_DONE);

            //initialize adapter
             TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(getContext(), timeSlotsArrayList);
            recyclerTimeSlots.setAdapter(timeSlotAdapter);

            loadAvailableTimeSlots(Common.currentHairStylist.getId(),
                    simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeSlots(int hairStylistID, String date) {

    }

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
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_TIME_SLOT_LOAD_DONE));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

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

        unbinder = ButterKnife.bind(this, itemView);

        initView(itemView);
        return itemView;

    }

    private void initView(View itemView) {
        recyclerTimeSlots.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerTimeSlots.setLayoutManager(gridLayoutManager);
        recyclerTimeSlots.addItemDecoration(new SpacesitemDecoration(2));

        //calender
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
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

                    //after getting the api set a function to load time slots for different dates
                }
            }
        });
    }

}
