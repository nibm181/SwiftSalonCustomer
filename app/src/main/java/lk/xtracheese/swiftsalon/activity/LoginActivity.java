package lk.xtracheese.swiftsalon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText txtEmail, txtPwd;
    private RelativeLayout btnLogin;
    private TextView btnRegister, txtLogin;
    private ProgressBar prgLogin;
    private SweetAlertDialog dialog;
    private Session session;

    DialogService dialogService;

    private ConstraintLayout constraintLayout;
    private ConstraintSet currentConstraintSet;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(getApplicationContext());
        txtEmail = findViewById(R.id.txt_uname);
        txtPwd = findViewById(R.id.txt_pwd);
        btnLogin = findViewById(R.id.btn_login);
        txtLogin = findViewById(R.id.btn_login_text);
        prgLogin = findViewById(R.id.btn_login_progress);
        btnRegister = findViewById(R.id.btn_register);

        constraintLayout = findViewById(R.id.login_parent_layout);
        currentConstraintSet = new ConstraintSet();
        currentConstraintSet.clone(constraintLayout);

        dialog = new SweetAlertDialog(LoginActivity.this);
        dialogService = new DialogService(this);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        subscribeObserver();

        btnLogin.setEnabled(false);

        //enable or disable login button
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtEmail.getText().toString().trim().isEmpty() || txtPwd.getText().toString().trim().isEmpty()) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //enable or disable login button
        txtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtEmail.getText().toString().trim().isEmpty() || txtPwd.getText().toString().trim().isEmpty()) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginApi();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        //move the layout when keyboard appears
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.txt_uname, ConstraintSet.TOP, R.id.textView2, ConstraintSet.BOTTOM, 380);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    currentConstraintSet.applyTo(constraintLayout);
                }
            }
        });

        txtPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.txt_uname, ConstraintSet.TOP, R.id.textView2, ConstraintSet.BOTTOM, 380);
                constraintSet.applyTo(constraintLayout);
            }
        });
    }

    private void subscribeObserver() {
        viewModel.getUser().observe(this, resource -> {
            Log.d(TAG, "onChanged: resource: " + resource.status);
            switch (resource.status) {

                case LOADING: {
                    Log.d(TAG, "onChanged: LOADING");

                    showProgressBar(true);
                    break;
                }

                case ERROR: {
                    Log.d(TAG, "onChanged: ERROR");
                    Log.d(TAG, "onChanged: ERROR MSG: " + resource.data);

                    showProgressBar(false);

                    if (resource.data != null) {
                        dialogService.errorDialog(resource.data.getMessage()).show();
                    } else {
                       dialogService.oopsErrorDialog();

                    }
                    break;
                }

                case SUCCESS: {
                    Log.d(TAG, "onChanged: SUCCESS");

                    if (resource.data.getStatus() == 1) {

                        if (resource.data.getContent() != null) {
                            session.setUserId(resource.data.getContent().getId());
                            session.setSignedIn(true);

                            //viewModel.updateToken();

                            Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            showProgressBar(false);
                            dialogService.oopsErrorDialog();

                        }

                    } else {
                        dialogService.errorDialog("Incorrect mobile no. or password").show();
                    }
                    break;
                }

            }
        });
    }

    private void loginApi() {
        if (isOnline()) {
            String email = txtEmail.getText().toString().trim();
            String password = txtPwd.getText().toString();
            viewModel.loginApi(email, password);
        } else {
            showProgressBar(false);
            dialog.setTitleText("Check your connection and try again.")
                    .show();
        }

    }

    private void showProgressBar(boolean show) {
        if (show) {
            txtLogin.setVisibility(View.GONE);
            prgLogin.setVisibility(View.VISIBLE);
        } else {
            txtLogin.setVisibility(View.VISIBLE);
            prgLogin.setVisibility(View.GONE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
