package com.example.myloginapplication;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myloginapplication.Locations;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myloginapplication.databinding.ActivityMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Maps extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {
    public static final String KEY_User_Document1 = "doc1";
    ProgressDialog loading;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    Dialog myPop;
    List<String> mynamelist = new ArrayList<>();
    //---
    private static final String TAG = Maps.class.getSimpleName();
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    //--

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private byte[] b = {};

    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPop = new Dialog(this);


        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading = new ProgressDialog(this);
        loading.setTitle("CREATING");
        loading.setMessage("Please wait....");
        loading.show();
        List<String> mynamelist =getloc();

        Handler hndler= new Handler();

        hndler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(Maps.this::onMapReady);
                loading.dismiss();
            }
            // Kodların ne kadar süre sonra çalışacağını belirttik. Burada 1000 değeri ms (milisaniye)
        },5000);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.


        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));


///baslangıc

        JsonArrayRequest  jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, Constant.GET_LOCATIONS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray  response) {
                        try {
                            // String message = response.getString("id");



                            for (int i=0; i<response.length(); i++) {
                                JSONObject loc = response.getJSONObject(i);
                                Integer id = loc.getInt("id");
                                String name = loc.getString("name");
                                double longtitude,latitude;
                                longtitude = loc.getDouble("longtitude");
                                latitude = loc.getDouble("latitude");
                                Integer price = loc.getInt("price");
                                LatLng ayasofya = new LatLng(longtitude, latitude);

                                if (mynamelist.contains(name)){
                                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(ayasofya).title(name).snippet(price.toString()+"TL"));
                                 //   System.out.println("yeşil");
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(ayasofya));
                                //    Maps.this.wait(1000);
                                }
                                else{
                                    googleMap.addMarker(new MarkerOptions().flat(true).position(ayasofya).title(name).snippet(price.toString()+"TL///"+String.valueOf(id)));
                                //    System.out.println("kırmızı");
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(ayasofya));
                                }


                                //if else ile isim kontrol

                               // googleMap.addMarker(new MarkerOptions().flat(true).position(ayasofya).title(name).snippet(price.toString()+"TL"));

                                //  a.append(String.valueOf(id) + " "+name+" "+String.valueOf(longtitude)+" "+String.valueOf(latitude)+"\n\n");
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

        googleMap.setOnInfoWindowClickListener(this);

        googleMap.setOnInfoWindowClickListener(this);

    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        //ziyaret edilmiş

        TextView txtclose, textView, textView1, txtclose1, txtclose2;
        Button btnvisit, btnvisitok;
        if (!marker.isFlat()) {
            //bilgi+img // ile parse
         //   String[] parts = marker.getSnippet().split("///");

           // String snippet = parts[0];
           // String imgurl = parts[1];


            myPop.setContentView(R.layout.popup);
            txtclose = (TextView) myPop.findViewById(R.id.txtclose1);
            txtclose.setText("X");
            textView1 = (TextView) myPop.findViewById(R.id.title);
            textView1.setText(marker.getTitle());
            textView = (TextView) myPop.findViewById(R.id.text);
            textView.setText(marker.getSnippet());


        //    new FetchImage(imgurl).start();


            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPop.dismiss();
                }
            });
            myPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myPop.show();
        } else {
            //ziyaret edilmemiş
            ImageView img;
            myPop.setContentView(R.layout.popup1);
            String[] parts = marker.getSnippet().split("///");

             String snippet = parts[0];
             String loc_id = parts[1];

            textView1 = (TextView) myPop.findViewById(R.id.title);
            textView1.setText(marker.getTitle());
            textView = (TextView) myPop.findViewById(R.id.textview);
            textView.setText(snippet);
            btnvisit = (Button) myPop.findViewById(R.id.btnvisit);
            btnvisitok = (Button) myPop.findViewById(R.id.save);
            txtclose1 = (TextView) myPop.findViewById(R.id.txtclose1);
            txtclose1.setText("X");
            img = (ImageView) myPop.findViewById(R.id.imageView);
            img.setImageResource(R.drawable.ayasofya);


            btnvisitok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView1, txtclose2;
                    Button image, save;

                    myPop.setContentView(R.layout.popup2);
                    EditText commenttext;
                    image = (Button) myPop.findViewById(R.id.button);
                    save = (Button) myPop.findViewById(R.id.save);
                    textView1 = (TextView) myPop.findViewById(R.id.title);
                    textView1.setText(marker.getTitle());
                    txtclose2 = (TextView) myPop.findViewById(R.id.txtclose1);
                    txtclose2.setText("X");
                    commenttext = (EditText) myPop.findViewById(R.id.editcomment);




                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyStoragePermissions(Maps.this);
                            selectImage();

                        }

                    });

                    txtclose2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myPop.dismiss();
                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = commenttext.getText().toString();



                                sendhistory(str,loc_id);

                        }
                    });

                }
            });
            txtclose1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPop.dismiss();
                }
            });


            myPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myPop.show();

        }


    }



    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Maps.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");

                BitMapToString(thumbnail);
            }
        }
    }
    public byte[] BitMapToString(Bitmap userImage1) {
        ImageView imageView;
        imageView = (ImageView) myPop.findViewById(R.id.imageView3);
        imageView.setImageBitmap(userImage1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        b = baos.toByteArray();
       //Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
       //Log.w("aasd",Document_img1);
        SendDetail();
        return b;

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void SendDetail() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.SAVE_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("imageFile", new DataPart(imagename + ".png", b));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


//fotograf çekme urldan
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

                    progressDialog = new ProgressDialog(Maps.this);
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
                    img = (ImageView) myPop.findViewById(R.id.img);
                    img.setImageBitmap(bitmap);

                }
            });


        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
//hatavar
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                }
//            }
//        }
//        updateLocationUI();
//    }


    public List<String> getloc(){
        SharedPreferences sp = Maps.this.getSharedPreferences("User", Context.MODE_PRIVATE);

        JsonArrayRequest  jsonObjReq = new JsonArrayRequest(
                Request.Method.GET, Constant.GET_HISTORY+sp.getString(Constant.ROLL_SHARED_userid,"s"), null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray  response) {
                        try {
                            // String message = response.getString("id");

                            for (int i=0; i< response.length(); i++) {
                                JSONObject loc = response.getJSONObject(i);
                                String name = loc.getString("name");
                                mynamelist.add(name);
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


        return mynamelist;
    }

    private void sleep(int i) {
    }

    private void sendhistory(String comment,String locid){

        SharedPreferences sp = Maps.this.getSharedPreferences("User", Context.MODE_PRIVATE);


            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("SAVING");
            loading.setMessage("Please wait....");
            loading.show();

            JSONObject js = new JSONObject();
            try {
               js.put("location_id", locid);
                js.put("user_id", sp.getString(Constant.ROLL_SHARED_userid,"s"));
                js.put("comment", comment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, Constant.SEND_HISTORY, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                Intent masp = new Intent(Maps.this, Maps.class);
                                startActivity(masp);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "send Error !1" + e, Toast.LENGTH_LONG).show();

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

