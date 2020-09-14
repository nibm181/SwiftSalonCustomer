package lk.xtracheese.swiftsalon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.UserProfileViewModel;

public class UserProfileActivity extends AppCompatActivity {

    PicassoImageLoadingService picassoImageLoadingService;
    private ImageButton btnBack, editImage;
    private LinearLayout btnFirstName, btnLastName, btnEmail, btnMobile, btnPassword;
    private TextView txtFirstName, txtLastName, txtEmail, txtMobile;
    private ImageView imgCustomer;
    private Session session;
    private DialogService dialog;
    SweetAlertDialog sweetAlertDialog;
    private UserProfileViewModel viewModel;

    private User user;

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        btnBack = findViewById(R.id.btn_back);
        editImage = findViewById(R.id.btn_edit_image);
        btnFirstName = findViewById(R.id.btn_first_name);
        btnLastName = findViewById(R.id.btn_last_name);
        btnEmail = findViewById(R.id.btn_email);
        btnMobile = findViewById((R.id.btn_mobile));
        btnPassword = findViewById(R.id.btn_password);
        txtFirstName = findViewById(R.id.txt_first_name);
        txtLastName = findViewById(R.id.txt_last_name);
        txtEmail = findViewById(R.id.txt_email);
        txtMobile = findViewById(R.id.txt_mobile);
        imgCustomer = findViewById(R.id.img_customer);

        session = new Session(this);
        picassoImageLoadingService = new PicassoImageLoadingService();
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        dialog = new DialogService(UserProfileActivity.this);
        sweetAlertDialog = dialog.loadingDialog();

        subscribeObservers();
        customerApi();

        btnFirstName.setOnClickListener(v -> {
            Intent editProfile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
            editProfile.putExtra("user", getUser());
            editProfile.putExtra("edit", EditUserProfileActivity.INPUT_FIRST_NAME);

            startActivity(editProfile);
        });
        btnLastName.setOnClickListener(v -> {
            Intent editProfile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
            editProfile.putExtra("user", getUser());
            editProfile.putExtra("edit", EditUserProfileActivity.INPUT_LAST_NAME);

            startActivity(editProfile);
        });
        btnEmail.setOnClickListener(v -> {
            Intent editProfile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
            editProfile.putExtra("user", getUser());
            editProfile.putExtra("edit", EditUserProfileActivity.INPUT_EMAIL);

            startActivity(editProfile);
        });
        btnMobile.setOnClickListener(v -> {
            Intent editProfile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
            editProfile.putExtra("user", getUser());
            editProfile.putExtra("edit", EditUserProfileActivity.INPUT_MOBILE);

            startActivity(editProfile);
        });
        btnPassword.setOnClickListener(v -> {
            Intent editProfile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
            editProfile.putExtra("user", getUser());
            editProfile.putExtra("edit", EditUserProfileActivity.INPUT_PASSWORD);

            startActivity(editProfile);
        });
        imgCustomer.setOnClickListener(v -> {
            Intent editImage = new Intent(UserProfileActivity.this, ImageActivity.class);
            editImage.putExtra("user", getUser());

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, (View) imgCustomer, "image");

            startActivity(editImage, options.toBundle());
        });

        btnBack.setVisibility(View.GONE);


    }

    private void subscribeObservers() {
        viewModel.getUser().observe(this, customerResource -> {
            if (customerResource != null) {
                switch (customerResource.status) {

                    case LOADING: {
                        sweetAlertDialog.show();
                        break;
                    }

                    case ERROR: {
                        sweetAlertDialog.dismiss();
                        dialog.showToast(customerResource.message);
                        setUser(customerResource.data);
                        setValue(customerResource.data);
                        break;
                    }

                    case SUCCESS: {
                        sweetAlertDialog.dismiss();
                        if (customerResource.data != null) {
                            setUser(customerResource.data);
                            setValue(customerResource.data);

                        }
                        break;
                    }
                }
            }
        });
    }

    private void customerApi() {
        viewModel.getCustomerApi(session.getUserId());
    }

    private void setValue(User user) {
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtMobile.setText(user.getMobileNo());
        picassoImageLoadingService.loadImageRound(user.getImage(), imgCustomer);
        session.setUserImg(user.getImage());
        session.setUserName(user.getFirstName());
    }
}