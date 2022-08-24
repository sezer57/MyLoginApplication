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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    TextView user,userBalance;
    Button logout, addMoneyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getBalance();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences sp = Profile.this.getSharedPreferences("User", Context.MODE_PRIVATE);
        //String userId = sp.getString(Constant.ROLL_SHARED_userid,"2");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = findViewById(R.id.user);
        userBalance = findViewById(R.id.balance);

        //SharedPreferences sp = Profile.this.getSharedPreferences("User", Context.MODE_PRIVATE);
        user.setText(sp.getString(Constant.ROLL_SHARED_username,"s"));

        addMoneyButton = findViewById(R.id.addMoneyButton);

        System.out.println(userBalance);
        addMoneyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                setAddMoneyButton();
                getBalance();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(getIntent());

                Toast.makeText(Profile.this, "Money Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });


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

    private void sleep(int milisn) throws InterruptedException
    {
        Thread.sleep(milisn);
    }

    public void getBalance()
    {
        SharedPreferences sp = Profile.this.getSharedPreferences("User", Context.MODE_PRIVATE);

        StringRequest jsonObjReq = new StringRequest(
                Request.Method.GET,
                Constant.GET_BALANCE +
                        sp.getString(Constant.ROLL_SHARED_userid,"2"),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try // TO DO
                        {
                            System.out.println(response);
//
                            userBalance.setText(response + " TL");

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "error" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               // Toast.makeText(getApplicationContext() "No value present"+error Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    public void setAddMoneyButton()
    {
        SharedPreferences sp = Profile.this.getSharedPreferences("User", Context.MODE_PRIVATE);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, Constant.GET_ADD_BALANCE+sp.getString(Constant.ROLL_SHARED_userid,"2"), null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try // TO DO
                        {
                            JSONObject balanceObject = response.getJSONObject(0);
                            long balance = balanceObject.getLong("balance");
                            userBalance.setText(String.valueOf(balance));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "error" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
//                Toast.makeText(getApplicationContext(),
//                        "No value present"+error,
//                        Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}
