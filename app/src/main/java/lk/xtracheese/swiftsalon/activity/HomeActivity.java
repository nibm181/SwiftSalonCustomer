package lk.xtracheese.swiftsalon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.xtracheese.swiftsalon.fragments.HomeFragment;
import lk.xtracheese.swiftsalon.fragments.ProfileFragment;
import lk.xtracheese.swiftsalon.fragments.SelectSalonFragment;
import lk.xtracheese.swiftsalon.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    
    public static final String CHANNEL_ID = "swift_salon";
    public static final String CHANNEL_NAME = "Swift Salon";

    BottomNavigationView bottomNavigationView;

    boolean isAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isAppointment = getIntent().getBooleanExtra("isAppointment", true);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        //to open home by default is else if
        //to open select salon if there is no appointment (data from no appointment activity)
        if(isAppointment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }else if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SelectSalonFragment()).commit();
        }



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_booking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SelectSalonFragment()).commit();
                        break;
                    case R.id.action_home:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        break;
                    case R.id.action_profile:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment()).commit();
                        break;
                }

                return true;
            }
        });


    }
    @Override
    public void onBackPressed() {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
    }

}
