package com.example.myloginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login, register;
    private EditText editusername, editemail, editpassword, editconfirmpassword;

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

    }

}