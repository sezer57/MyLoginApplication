package com.example.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG =       "serror";
    EditText username, password;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);



        TextView btn = findViewById(R.id.SignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        TextView btn2 = findViewById(R.id.loginbtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });



    }

    private void login() {

        final String usern = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        if (usern.isEmpty()) {
            username.setError("Username is required");
        }

        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        } else {


            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Login");
            loading.setMessage("Please wait....");
            loading.show();

            JSONObject js = new JSONObject();
            try {
                js.put(Constant.KEY_ROLL, usern);
                js.put(Constant.KEY_PASSWORD, pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, Constant.LOGIN_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                if (message.equals("Login Successful !")) {
                                    //Creating a shared preference
                                    username = findViewById(R.id.username);
                                    String a = username.getText().toString().trim();
                                    String token = response.getString("accessToken");
                                    String userid = response.getString("userId");
                                    SharedPreferences sp = MainActivity.this.getSharedPreferences("User", Context.MODE_PRIVATE);
                                    //Creating editor to store values to shared preferences
                                    SharedPreferences.Editor editor = sp.edit();
                                    //Adding values to editor
                                    editor.putString(Constant.ROLL_SHARED_username, a);
                                    //    editor.putString(Constant.ROLL_SHARED_token, token);
                                    //     editor.putString(Constant.ROLL_SHARED_userid, userid);
                                    //Saving values to editor
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                    Intent login = new Intent(MainActivity.this, Menu.class);
                                    startActivity(login);
                                    finish();
                                }
                                if (message.equals("Bad credentials")) {
                                    Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                }
                                if (message.equals("No value present")) {
                                    Toast.makeText(getApplicationContext(), "No value present", Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Registration Error !1" + e, Toast.LENGTH_LONG).show();
                                loading.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No value present", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            // Adding request to request queue
            Volley.newRequestQueue(this).add(jsonObjReq);

        }

    }

}