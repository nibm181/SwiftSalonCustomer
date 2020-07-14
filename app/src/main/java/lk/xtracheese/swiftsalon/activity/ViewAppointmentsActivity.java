package lk.xtracheese.swiftsalon.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;

public class ViewAppointmentsActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        imageView = findViewById(R.id.view_appointment_user_pic);

        String userImageURL = "http://10.0.2.2/SwiftSalon/user_images/user.jpeg";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);
    }
}