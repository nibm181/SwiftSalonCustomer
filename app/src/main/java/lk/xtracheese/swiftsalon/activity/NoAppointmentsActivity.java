package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lk.xtracheese.swiftsalon.R;

public class NoAppointmentsActivity extends AppCompatActivity {

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_appointments);
        btnAdd = findViewById(R.id.btn_add_appointment);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(NoAppointmentsActivity.this, HomeActivity.class);
            intent.putExtra("isAppointment", false);
            startActivity(intent);
        });
    }
}