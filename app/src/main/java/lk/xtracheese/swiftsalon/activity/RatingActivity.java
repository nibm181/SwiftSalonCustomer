package lk.xtracheese.swiftsalon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDao;
import lk.xtracheese.swiftsalon.persistence.SwiftSalonDatabase;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.viewmodel.RatingViewModel;

public class RatingActivity extends AppCompatActivity {

    private static final String TAG = "RatingActivity";

    RatingViewModel viewModel;

    DialogService alertDialog;
    PicassoImageLoadingService picassoImageLoadingService;
    SwiftSalonDao swiftSalonDao;

    TextView txtStylist, txtSalon, txtAppointmentNo;
    RatingBar ratingBar;
    ImageView imageView;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Appointment appointment = getIntent().getParcelableExtra("appointment");
        viewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        picassoImageLoadingService = new PicassoImageLoadingService();
        swiftSalonDao = SwiftSalonDatabase.getInstance(this).getDao();
        alertDialog = new DialogService(this);

        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.imageView);
        btnSubmit = findViewById(R.id.btn_submit);
        txtStylist = findViewById(R.id.txt_stylist);
        txtSalon = findViewById(R.id.txt_salon);
        txtAppointmentNo = findViewById(R.id.txt_appointment_no);

        subscribeObservers();

        txtStylist.setText(appointment.getStylistName());
        txtAppointmentNo.setText("Appointment No. : "+appointment.getId());
        txtSalon.setText(appointment.getSalonName());
        picassoImageLoadingService.loadImageRound(appointment.getSalonImage(), imageView);

        btnSubmit.setOnClickListener(v -> {
            float ratingCount = ratingBar.getRating();
            if (ratingCount == 0) {
                alertDialog.errorDialog("Please rate first!");
            } else {
                addRatingApi(appointment.getId(), ratingCount);
            }

        });


    }

    public void subscribeObservers() {
        viewModel.getResponse().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    alertDialog.loadingDialog().show();
                    break;
                case SUCCESS:
                    if (resource.data.getStatus() == 1) {
                        alertDialog.dismissLoading();
                        alertDialog.ratingSMessage().setConfirmClickListener(sweetAlertDialog -> {
                            Intent intent = new Intent(RatingActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }).show();
                    }
                    break;
                case ERROR:
                    alertDialog.dismissLoading();
                    alertDialog.showToast(resource.message);
                    break;
            }
        });
    }

    public void addRatingApi(int id, float rating) {
        viewModel.addRatingApi(id, rating);
    }




}