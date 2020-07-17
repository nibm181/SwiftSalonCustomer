package lk.xtracheese.swiftsalon.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.SearchViewAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.NonSwipeViewPager;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.viewmodel.AppointmentViewModel;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";

    LocalBroadcastManager localBroadcastManager;
    DialogService alertDialog;
    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btnPrevStep;
    Button btnNxtStep;

    AppointmentViewModel viewModel;


    //broadcast receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if (step == 1)
                btnNxtStep.setEnabled(true);

            else if (step == 2) {
                if (Common.currentJob != null && !Common.currentJob.isEmpty()) {
                    btnNxtStep.setEnabled(true);
                } else {
                    btnNxtStep.setEnabled(false);
                }
            } else if (step == 3)
                btnNxtStep.setEnabled(true);

            setButtonColor();
        }
    };


    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        alertDialog = new DialogService(this);

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btnPrevStep = findViewById(R.id.btn_prev_step);
        btnNxtStep = findViewById(R.id.btn_nxt_step);

        viewModel = new AppointmentViewModel(getApplication());

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        subscribeObservers();
        setupStepView();
        setButtonColor();

        hidePreviousButton(false);

        //view
        viewPager.setAdapter(new SearchViewAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); //for limiting the pages, in this case i have 4 fragments
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //show steps
                stepView.go(position, true);

                if (position == 0) {
                    btnPrevStep.setEnabled(false);
                } else if (position == 3) {
                    btnNxtStep.setEnabled(true);
                } else {
                    btnPrevStep.setEnabled(true);
                    btnNxtStep.setEnabled(false);
                }

                setButtonColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        loadHairStylistBySalon(Common.currentSalon.getSalID());

        btnNxtStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.step < 4 || Common.step == 0) {
                    Common.step++;
                    if (Common.step == 1) { //after choosing the hair stylist
                        hidePreviousButton(true);
                        viewPager.setCurrentItem(Common.step);
//                        if (Common.currentSalon != null)
//                            loadHairStylistBySalon(Common.currentSalon.getSalID());
                    } else if (Common.step == 2) { //choose job
                        if (Common.currentJob != null && !Common.currentJob.isEmpty()) {
                            Intent intent = new Intent(Common.KEY_JOB_SELECTED);
                            localBroadcastManager.sendBroadcast(intent);
                        }
                        viewPager.setCurrentItem(Common.step);

                    } else if (Common.step == 3) { //confirm
                        if (Common.currentTimeSlot != null)
                            confirmBookingSetData();
                        viewPager.setCurrentItem(Common.step);
                    } else if (Common.step == 4) {
                        saveData();
                    }

                }
            }
        });

        btnPrevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.step == 3) {
                    Common.step--;
                    btnNxtStep.setText("Next");
                    viewPager.setCurrentItem(Common.step);
                } else if (Common.step > 0) {
                    Common.step--;
                    if (Common.step == 0) {
                        hidePreviousButton(false);
                    }
                    viewPager.setCurrentItem(Common.step);
                }


            }
        });
    }

    private void confirmBookingSetData() {
        //send broadcast to fragment step four
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
        //change button text
        btnNxtStep.setText("Confrim");
    }


    private void hidePreviousButton(boolean isVisible) {
        if (isVisible) {
            btnPrevStep.setVisibility(View.VISIBLE);
        } else {
            btnPrevStep.setVisibility(View.INVISIBLE);
        }

    }

    private void setButtonColor() {
        if (btnPrevStep.isEnabled()) {
            btnPrevStep.setBackgroundResource(R.drawable.nxt_prev_btn__enabled_background);
            btnPrevStep.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnPrevStep.setBackgroundResource(R.drawable.nxt_prev_btn_disabled_background);
            btnPrevStep.setTextColor(getResources().getColor(R.color.dark_dark_purple));
        }
        if (btnNxtStep.isEnabled()) {
            btnNxtStep.setBackgroundResource(R.drawable.nxt_prev_btn__enabled_background);
            btnNxtStep.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnNxtStep.setBackgroundResource(R.drawable.nxt_prev_btn_disabled_background);
            btnNxtStep.setTextColor(getResources().getColor(R.color.dark_dark_purple));
        }
    }

    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Hair Stylist");
        stepList.add("Jobs");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }


    private void getAppointmentApi(Appointment appointment) {
        viewModel.appointmentApi(appointment);
    }

    private void subscribeObservers() {
        viewModel.getAppointment().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    alertDialog.loadingDialog().show();
                    break;
                case ERROR:
                    alertDialog.dismissLoading();
                    alertDialog.oopsErrorDialog();
                    break;
                case SUCCESS:
                    if (resource.data.getStatus() == 1) {
                        if (resource.data.getContent() != null) {
                            alertDialog.dismissLoading();
                            alertDialog.successAppointmentDialog().show();
                            finish();
                        }
                    }
                    break;

            }
        });
    }

    private void saveData() {
        Appointment appointment = new Appointment();

        appointment.setCustomerId(1);
        appointment.setDate(Common.currentTimeSlot.getSlotTiming());
        appointment.setSalonId(Common.currentSalon.getSalID());
        appointment.setStylistId(Common.currentStylist.getId());
        appointment.setDate(Common.currentDate.getTime().toString());
        appointment.setTime(Common.currentTimeSlot.getSlotTiming());

        int[] jobIds = new int[Common.currentJob.size()];
        for(int i = 0; i < Common.currentJob.size(); i++) {
            jobIds[i] = Common.currentJob.get(i).getJobId();
        }
        appointment.setJobIds(jobIds);

        getAppointmentApi(appointment);
    }

}
