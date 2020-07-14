package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import lk.xtracheese.swiftsalon.R;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText txtLoginMob;
    TextInputEditText txtLoginPass;
    RelativeLayout btnLogin;
    String userMobNo;
    String userPassword;
    String url;
    String resMessage;
    Boolean resIsSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
//        txtLoginMob = findViewById(R.id.txt_uname);
//        txtLoginPass = findViewById(R.id.txt_pwd);
//        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
