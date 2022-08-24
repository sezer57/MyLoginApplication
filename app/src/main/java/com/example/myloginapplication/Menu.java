package com.example.myloginapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu extends AppCompatActivity
{

    private static List<String> mynamelist = new ArrayList<>();
    private static HashMap<String,String> mynameimagelist = new HashMap<String,String>();

    public static HashMap<String,String> getImageList()
    {
        if(mynameimagelist==null)
        {
            mynameimagelist= new HashMap<String,String>();

        }
        return mynameimagelist;
    }

    public static List<String> getNameList()
    {
        if(mynamelist==null)
        {
            mynamelist= new ArrayList<>();
        }
        return mynamelist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getloc();
        TextView button = findViewById(R.id.profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, Profile.class));
            }
        });

        TextView button2 = findViewById(R.id.mylocation);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, Maps.class));
            }
        });

        TextView button3 = findViewById(R.id.places);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, Locations.class));
            }
        });


    }

    public void getloc()
    {
        SharedPreferences sp = Menu.this.getSharedPreferences("User", Context.MODE_PRIVATE);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, Constant.GET_HISTORY +
                sp.getString(Constant.ROLL_SHARED_userid,"s"), null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray  response) {
                        try {
                            // String message = response.getString("id");

                            for (int i=0; i< response.length(); i++) {
                                JSONObject loc = response.getJSONObject(i);
                                String name = loc.getString("name");
                                String comment = loc.getString("comment");
                                String piclist = loc.getString("picByte");
                                System.out.println(name);
                                mynamelist.add(name);
                                mynamelist.add(comment);
                                mynameimagelist.put(name,piclist);

                                //    mynamelist.add(b);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No value present"+error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);

    }


}
