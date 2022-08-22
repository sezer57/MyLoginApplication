package com.example.myloginapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    TextView user;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user=findViewById(R.id.user)    ;

        SharedPreferences sp = Profile.this.getSharedPreferences("User", Context.MODE_PRIVATE);
        user.setText(sp.getString(Constant.ROLL_SHARED_username,"s"));

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Profile.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
            }
        });

        TextView btn = findViewById(R.id.backtomenu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Menu.class));
            }
        });


    }

}
