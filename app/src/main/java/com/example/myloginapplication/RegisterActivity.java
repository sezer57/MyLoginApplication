package com.example.myloginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login, register;
    private EditText editusername, editemail, editpassword, editconfirmpassword;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editusername = (EditText) findViewById(R.id.username);
        editemail = (EditText) findViewById(R.id.email);
        editpassword = (EditText) findViewById(R.id.password);
        editconfirmpassword = (EditText) findViewById(R.id.confirmpassword);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
        }
    }

    private void registerUser(){

        String username = editusername.getText().toString().trim();
        String email = editemail.getText().toString().trim();
        String password = editpassword.getText().toString().trim();
        String confirmpassword = editconfirmpassword.getText().toString().trim();

        if (username.isEmpty()){
            editusername.setError("Username is required");
        }
        if (email.isEmpty()){
            editemail.setError("Email is required");
        }
        if (password.isEmpty()){
            editpassword.setError("Password is required");
            editpassword.requestFocus();
            return;
        }
        if (confirmpassword.isEmpty()){
            editconfirmpassword.setError("Password is required");
            editconfirmpassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editemail.setError("Invalid email");
            editemail.requestFocus();
            return;
        }

        else{

            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Login");
            loading.setMessage("Please wait....");
            loading.show();

            JSONObject js = new JSONObject();
            try {
                js.put("username", username);
                js.put("name", username);
                js.put("balance", "500");
                js.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, Constant.SIGNUP_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                if (message.equals("User successfully registered.")) {


                                    Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                    Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(login);
                                    finish();
                                }
                                if (message.equals("Username already in use.")) {
                                    Toast.makeText(getApplicationContext(), "Username already in use.", Toast.LENGTH_LONG).show();
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

