package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Arrays;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.util.Resource;
import lk.xtracheese.swiftsalon.viewmodel.EditUserViewModel;

import static lk.xtracheese.swiftsalon.util.Constants.EMAIL_REGEX;
import static lk.xtracheese.swiftsalon.util.Constants.MOBILE_NUMBER_REGEX;


public class EditUserProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    public static final String INPUT_FIRST_NAME = "fist_name";
    public static final String INPUT_LAST_NAME = "last_name";
    public static final String INPUT_EMAIL = "email";
    public static final String INPUT_MOBILE = "mobile";
    public static final String INPUT_PASSWORD = "password";

//    public static final int GENTS = 0;
//    public static final int UNISEX = 1;
//    public static final int LADIES = 2;

    private String edit;
    private User user;
    private SimpleDateFormat dateFormat;
    private EditUserViewModel viewModel;
    private DialogService alertDialog;

    private ImageButton btnBack;
    private TextView txtTitle, txtTitle2, txtOpenTime, txtCloseTime, txtSave, txtWarning;
    private EditText txtEdit, txtEdit2;
    private RelativeLayout btnSave;
    private LinearLayout layoutTime;
    private ProgressBar prgSave;

    private boolean isPasswordVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        btnBack = findViewById(R.id.btn_back);
        txtTitle = findViewById(R.id.txt_title);
        txtTitle2 = findViewById(R.id.txt_title2);
        txtEdit = findViewById(R.id.txt_edit);
        txtEdit2 = findViewById(R.id.txt_edit2);
        txtWarning = findViewById(R.id.txt_warning);
        btnSave = findViewById(R.id.btn_save);
        txtSave = findViewById(R.id.btn_save_text);
        prgSave = findViewById(R.id.btn_save_progress);

        dateFormat = new SimpleDateFormat("HH:mm");
        alertDialog = new DialogService(EditUserProfileActivity.this);
        viewModel = new ViewModelProvider(this).get(EditUserViewModel.class);

        btnBack.setOnClickListener(v -> finish());

        getIncomingContent();
        subscribeObservers();
        disableSave();

        btnSave.setOnClickListener(v -> {
            if (validate()) {
                updateApi();
            }
        });
    }

    private void getIncomingContent() {
        if (getIntent().hasExtra("user") && getIntent().hasExtra("edit")) {

            user = getIntent().getParcelableExtra("user");
            edit = getIntent().getStringExtra("edit");
            Log.d(TAG, "getIncomingContent: EDIT: " + edit);

            if (edit.equals(INPUT_FIRST_NAME)) {
                txtEdit.requestFocus();
                txtTitle.setText("First Name");
                txtEdit.setText(user.getFirstName());
                txtEdit.setHint("Enter fist name");
                txtEdit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                txtEdit.setSelection(txtEdit.getText().length());
            } else if (edit.equals(INPUT_LAST_NAME)) {
                txtEdit.requestFocus();
                txtTitle.setText("Last Name");
                txtEdit.setText(user.getLastName());
                txtEdit.setHint("Enter last name");
                txtEdit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                txtEdit.setSelection(txtEdit.getText().length());
            } else if (edit.equals(INPUT_EMAIL)) {
                txtEdit.requestFocus();
                txtTitle.setText("Email");
                txtEdit.setText(user.getEmail());
                txtEdit.setHint("Enter email");
                txtEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                txtEdit.setSelection(txtEdit.getText().length());
            } else if (edit.equals(INPUT_MOBILE)) {
                txtEdit.requestFocus();
                txtTitle.setText("Mobile Number");
                txtEdit.setText(user.getMobileNo());
                txtEdit.setHint("Enter mobile");
                txtEdit.setInputType(InputType.TYPE_CLASS_PHONE);
                txtEdit.setSelection(txtEdit.getText().length());
            }
            else if (edit.equals(INPUT_PASSWORD)) {
                txtEdit.requestFocus();
                txtTitle.setText("Verify Password");
                btnSave.setEnabled(false);
                txtEdit.setHint("Enter current password");
                txtEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                txtEdit.setTransformationMethod(new PasswordTransformationMethod());
                txtSave.setText("Verify");
            }
        }
    }

    private void showConfirmPasswordLayout() {
        txtTitle.setText("Confirm Password");
        txtEdit.setText("");
        txtEdit.setHint("Enter new password");
        txtEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtEdit.setTransformationMethod(new PasswordTransformationMethod());
        txtEdit2.setVisibility(View.VISIBLE);
        txtEdit2.setHint("Confirm new password");
        txtEdit2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtEdit2.setTransformationMethod(new PasswordTransformationMethod());
        txtSave.setText("Confirm");
        txtEdit.requestFocus();

        disableSave();
    }

    private void disableSave() {
        if (Arrays.asList(INPUT_FIRST_NAME,INPUT_LAST_NAME, INPUT_EMAIL, INPUT_MOBILE, INPUT_PASSWORD).contains(edit)) {
            txtEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtEdit.getText().toString().trim().isEmpty()) {
                        btnSave.setEnabled(false);
                    } else {
                        btnSave.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private boolean validate() {
        txtWarning.setVisibility(View.GONE);
        String text = txtEdit.getText().toString();

        if (edit.equals(INPUT_EMAIL) && !text.matches(EMAIL_REGEX)) {
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Enter a valid email address");
            return false;
        } else if (edit.equals(INPUT_MOBILE) && !text.matches(MOBILE_NUMBER_REGEX)) {
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Enter a valid mobile number");
            return false;
        } else if (edit.equals(INPUT_PASSWORD) && isPasswordVerified) {
            if (text.length() < 8) {
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Password should be at least 8 characters");
                return false;
            } else if (!text.equals(txtEdit2.getText().toString().trim())) {
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("The confirm password does not match new password");
                return false;
            }
        }

        return true;
    }

    private void subscribeObservers() {
        viewModel.updateCustomer().observe(this, new Observer<Resource<GenericResponse<User>>>() {
            @Override
            public void onChanged(Resource<GenericResponse<User>> resource) {

                if (resource != null) {

                    switch (resource.status) {

                        case LOADING: {
                            Log.d(TAG, "onChanged: LOADING");
                            showProgressBar(true);
                            break;
                        }

                        case ERROR: {
                            Log.d(TAG, "onChanged: ERROR");
                            showProgressBar(false);
                            alertDialog.showToast(resource.message);
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged: SUCCESS");

                            if (resource.data.getStatus() == 1) {

                                if (resource.data.getContent() != null) {
                                    alertDialog.showToast("Successfully updated");
                                    finish();
                                } else {
                                    showProgressBar(false);
                                    alertDialog.showToast(resource.message);
                                }

                            } else {
                                showProgressBar(false);
                                alertDialog.showToast(resource.data.getMessage());
                            }
                            break;
                        }

                    }

                }
            }
        });

        viewModel.verifyPassword().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING: {
                        Log.d(TAG, "onChanged: LOADING");
                        showProgressBar(true);
                        break;
                    }

                    case ERROR: {
                        Log.d(TAG, "onChanged: ERROR");
                        showProgressBar(false);
                        alertDialog.showToast(resource.message);
                        break;
                    }

                    case SUCCESS: {
                        Log.d(TAG, "onChanged: SUCCESS");

                        if (resource.data.getStatus() == 1) {
                            isPasswordVerified = true;
                            showConfirmPasswordLayout();
                        } else {
                            isPasswordVerified = false;
                            alertDialog.errorDialog("Incorrect password");
                        }
                        showProgressBar(false);
                        break;
                    }
                }
            }
        });
        viewModel.confirmPassword().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING: {
                        Log.d(TAG, "onChanged: LOADING");
                        showProgressBar(true);
                        break;
                    }

                    case ERROR: {
                        Log.d(TAG, "onChanged: ERROR");
                        showProgressBar(false);
                        alertDialog.showToast(resource.message);
                        break;
                    }

                    case SUCCESS: {
                        Log.d(TAG, "onChanged: SUCCESS");

                        if (resource.data.getStatus() == 1) {
                            alertDialog.showToast("Successfully updated");
                            finish();
                        } else {
                            showProgressBar(false);
                            alertDialog.errorDialog("Incorrect password");
                        }
                        break;
                    }
                }
            }
        });
    }

    private void updateApi() {
        String text = txtEdit.getText().toString().trim();

        if(isOnline()) {
            switch (edit) {

                case INPUT_FIRST_NAME: {
                    user.setFirstName(text);
                    break;
                } case INPUT_LAST_NAME: {
                    user.setLastName(text);
                    break;
                } case INPUT_EMAIL: {
                    user.setEmail(text);
                    break;
                } case INPUT_MOBILE: {
                    user.setMobileNo(text);
                    break;
                }

                case INPUT_PASSWORD:{
                    if (isPasswordVerified) {
                        viewModel.confirmApi(text);
                    } else {
                        viewModel.verifyApi(text);
                    }
                    break;
                }
            }

            Log.d(TAG, "onCreate: CUSTOMER: " + user.toString());
            viewModel.updateApi(user);
        }
        else {
            alertDialog.showToast("Check your connection and try again.");
        }

    }

    private void showProgressBar(boolean show) {
        if (show) {
            txtSave.setVisibility(View.GONE);
            prgSave.setVisibility(View.VISIBLE);
        } else {
            txtSave.setVisibility(View.VISIBLE);
            prgSave.setVisibility(View.GONE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}