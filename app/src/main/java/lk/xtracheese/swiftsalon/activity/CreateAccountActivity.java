package lk.xtracheese.swiftsalon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.util.Session;
import lk.xtracheese.swiftsalon.viewmodel.CreateAccountViewModel;

import static lk.xtracheese.swiftsalon.util.Constants.MOBILE_NUMBER_REGEX;
import static lk.xtracheese.swiftsalon.util.Constants.NAME_REGEX;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    EditText txtFirstName, txtLastName, txtMobileNo, txtPassword, txtRePassword;
    String firstName, lastName, mobileNo, password, rePassword;
    Button btnRegister;

    User objUser;

    DialogService alertDialog;
    SweetAlertDialog sweetAlertDialog;

    CreateAccountViewModel viewModel;

    boolean isValidated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        viewModel = new CreateAccountViewModel(getApplication());
        alertDialog = new DialogService(this);
        sweetAlertDialog = alertDialog.loadingDialog();

        isValidated = false;

        txtFirstName = findViewById(R.id.ca_first_name);
        txtLastName = findViewById(R.id.ca_last_name);
        txtMobileNo = findViewById(R.id.ca_mobile_no);
        txtPassword = findViewById(R.id.ca_password);
        txtRePassword = findViewById(R.id.ca_repassword);
        btnRegister =findViewById(R.id.btn_register);

        subscribeObservers();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    setUserData();
                    registerUserApi(objUser);
                }
            }
        });

    }

    public void registerUserApi(User user){
        Log.d(TAG, "registerUserApi: USER: " + user.toString());
        viewModel.registerUserApi(user);
    }

    private void subscribeObservers() {
        viewModel.getUser().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    sweetAlertDialog.show();
                    break;
                case ERROR:{
                    sweetAlertDialog.dismiss();
                    alertDialog.showToast(resource.message);
                    break;
                }

                case SUCCESS:{
                    sweetAlertDialog.dismiss();
                    if (resource.data.getStatus() == 1) {
                        if (resource.data.getContent() != null) {
                            alertDialog.showToast("Account Created Successfully!");
                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        alertDialog.errorDialog("Mobile Number already registered. Please try Login").show();
                    }
                    break;
                }

            }
        });
    }

    void setUserData(){
        objUser = new User();
        objUser.setFirstName(firstName);
        objUser.setLastName(lastName);
        objUser.setMobileNo(mobileNo);
        objUser.setPassword(password);
    }

     public boolean validate(){
         firstName = txtFirstName.getText().toString().trim();
         lastName = txtLastName.getText().toString().trim();
         mobileNo = txtMobileNo.getText().toString();
         password = txtPassword.getText().toString().trim();
         rePassword = txtRePassword.getText().toString().trim();

        if(firstName.isEmpty()) {
            alertDialog.errorDialog("Please enter first name").show();
            return false;
        }else if(!firstName.matches(NAME_REGEX)){
            alertDialog.errorDialog("Please enter valid first name").show();
            return false;
        }else if(lastName.isEmpty()){
            alertDialog.errorDialog("Please enter last name").show();
            return false;
        }else if(!lastName.matches(NAME_REGEX)){
            alertDialog.errorDialog("Please enter valid last name").show();
            return false;
        }else if(mobileNo.isEmpty()){
             alertDialog.errorDialog("Please enter mobile number").show();
            return false;
        }else if(!mobileNo.matches(MOBILE_NUMBER_REGEX)){
            alertDialog.errorDialog("Please enter valid mobile number").show();
            return false;
        }else if(password.isEmpty()) {
            alertDialog.errorDialog("Please enter password").show();
            return false;
        }else if (!isValidPassword(password))    {
            alertDialog.errorDialog("Password should contain capital letter and symbol").show();
            return false;
        }else if(rePassword.isEmpty()){
             alertDialog.errorDialog("Please re-type password").show();
            return false;
        }else if(!password.equals(rePassword)){
            alertDialog.errorDialog("Please check password, Password doesn't match").show();
            Log.d(TAG, "validate: "+password + " " +rePassword );
            return false;
        }else{
            return true;
         }
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}