package lk.xtracheese.swiftsalon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import lk.xtracheese.swiftsalon.Adapter.HairStylistAdapter;
import lk.xtracheese.swiftsalon.Adapter.SalonAdapter;
import lk.xtracheese.swiftsalon.Adapter.SearchViewAdapter;
import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.Common.NonSwipeViewPager;
import lk.xtracheese.swiftsalon.Interface.GetDataService;
import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.Model.Salon;
import lk.xtracheese.swiftsalon.Network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog alertDialog;

    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btnPrevStep;
    Button btnNxtStep;

    String salonID;


    //broadcast receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.currentSalon = intent.getParcelableExtra(Common.KEY_SALON_STORE);
            btnNxtStep.setEnabled(true);
            salonID = Common.currentSalon.getSalID();
            setButtonColor();
        }
    };



    private void loadBarberBySalon(String salID) {
        alertDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<HairStylist>> call = service.getHairstylist();
        call.enqueue(new Callback<List<HairStylist>>() {
            @Override
            public void onResponse(Call<List<HairStylist>> call, retrofit2.Response<List<HairStylist>> response) {
                //send broadcast to booking step2frangment to load recycler
                Intent intent = new Intent(Common.KEY_HAIR_STYLIST_LOAD_DONE);
                intent.putParcelableArrayListExtra(Common.KEY_HAIR_STYLIST_LOAD_DONE, (ArrayList<? extends Parcelable>) response.body());
                localBroadcastManager.sendBroadcast(intent);

                alertDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<HairStylist>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong ...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("debug", t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        alertDialog = new SpotsDialog.Builder().setContext(this).build();

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btnPrevStep = findViewById(R.id.btn_prev_step);
        btnNxtStep =  findViewById(R.id.btn_nxt_step);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setButtonColor();

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

                if(position == 0){
                    btnPrevStep.setEnabled(false);
                }
                else {
                    btnPrevStep.setEnabled(true);
                }

                setButtonColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNxtStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.step < 3 || Common.step == 0){
                    Common.step++;
                    if(Common.step == 1){ //after choosing the salon
                        if(Common.currentSalon != null)
                            loadBarberBySalon(Common.currentSalon.getSalID());
                    }
                    viewPager.setCurrentItem(Common.step);
                }
            }
        });
        btnPrevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Common.step == 3 || Common.step > 0){
                        Common.step--;
                        viewPager.setCurrentItem(Common.step);
                    }

            }
        });
    }



    private void setButtonColor() {
        if(btnPrevStep.isEnabled()){
            btnPrevStep.setBackgroundResource(R.color.disable);
        }
        else{
            btnPrevStep.setBackgroundResource(R.color.grey);
        }
        if(btnNxtStep.isEnabled()){
            btnNxtStep.setBackgroundResource(R.color.disable);
        }
        else{
            btnNxtStep.setBackgroundResource(R.color.grey);
        }
    }

    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Salon");
        stepList.add("Barber");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }



}
