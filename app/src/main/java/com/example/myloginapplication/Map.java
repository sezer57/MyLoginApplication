package com.example.myloginapplication;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myloginapplication.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.example.myloginapplication.databinding.ActivityMapBinding;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;



public class Map extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    Dialog myPop;

    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPop = new Dialog(this);


        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        LatLng ayasofya = new LatLng(41.008583, 28.980175);
        LatLng dolmabahce = new LatLng(41.0391643, 29.0004594);
        LatLng suleymaniye = new LatLng(41.016047, 28.9639711);
        LatLng topkapı = new LatLng(41.0115195, 28.9833789);
        LatLng rahimikoc = new LatLng(41.0426561, 28.9495399);


        googleMap.addMarker(new MarkerOptions().position(ayasofya).title("Ayasofya").snippet("ayasofya insanlar tarafından ziyaret camidir ///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(dolmabahce).title("Dolmabahce").snippet("Dolmabahce gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(suleymaniye).title("Süleymaniye").snippet("Süleymaniye gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));
        googleMap.addMarker(new MarkerOptions().position(topkapı).title("Topkapı").snippet("Topkapı gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));
        googleMap.addMarker(new MarkerOptions().position(rahimikoc).title("Rahmi Koç").snippet("Rahmi gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));



        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ayasofya));
        googleMap.setOnInfoWindowClickListener(this);

    }


    @Override
    public void onInfoWindowClick( Marker marker) {

        //bilgi+img // ile parse
        String[] parts = marker.getSnippet().split("///");

        String snippet = parts[0];
        String imgurl = parts[1];


        TextView txtclose,textView,textView1;
        myPop.setContentView(R.layout.popup);
        txtclose =(TextView) myPop.findViewById(R.id.txtclose);
        txtclose.setText("X");
        textView1 =(TextView) myPop.findViewById(R.id.title);
        textView1.setText(marker.getTitle());
        textView =(TextView) myPop.findViewById(R.id.text);
        textView.setText(snippet);


       new FetchImage(imgurl).start();




        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPop.dismiss();
            }
        });
        myPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myPop.show();
    }


    class FetchImage extends Thread {

        ImageView img;
        String URL;
        Bitmap bitmap;
    //    ImageView img;

        FetchImage(String URL) {

            this.URL = URL;

        }

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(Map.this);
                //    progressDialog.setMessage("Getting your pic....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            InputStream inputStream = null;
            try {
                inputStream = new URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    img=(ImageView) myPop.findViewById(R.id.img);
                    img.setImageBitmap(bitmap);

                }
            });


        }
    }

}