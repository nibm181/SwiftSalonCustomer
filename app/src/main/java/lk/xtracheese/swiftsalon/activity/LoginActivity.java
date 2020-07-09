package lk.xtracheese.swiftsalon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    Button btnLogin;

    String userMobNo;
    String userPassword;
    String url;
    String resMessage;
    Boolean resIsSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginMob = findViewById(R.id.edit_txt_mob_no);
        txtLoginPass = findViewById(R.id.edit_txt_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMobNo = txtLoginMob.getText().toString();
                userPassword = txtLoginPass.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                url = "http://192.168.56.1/SwiftSalon/CustomerLogin.php?userMobNo="+userMobNo+"&userPassword="+userPassword;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    resMessage = response.getString("message");
                                    resIsSuccess = response.getBoolean("success");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                requestQueue.add(jsonObjectRequest);
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);


            }
        });
    }
}
