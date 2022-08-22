package com.example.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locations extends AppCompatActivity {
    TextView textView;
    RecyclerView recyclerView;
    ArrayList<ModelMaps> mapsArrayList;
    AdapterPost adapterPost;
    ProgressDialog progressDialog;
    ProgressDialog loading;
    List<String> alloc = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        getloc();






        //     recyclerView=findViewById(R.id.mapsrc);

        //    mapsArrayList = new ArrayList<>();
        //  mapsArrayList.clear();



    }
    public void getloc(){

        JsonArrayRequest  jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, Constant.GET_LOCATIONS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray  response) {
                        try {
                           // String message = response.getString("id");

                            for (int i=0; i<10; i++) {
                                JSONObject actor = response.getJSONObject(i);
                                String name = actor.getString("name");
                                alloc.add(name);
                                System.out.println(alloc);
                            }

                          //  textView.setText(message);
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

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

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);


    }

    }
