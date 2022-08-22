package com.example.myloginapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myloginapplication.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Maps extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {
    public static final String KEY_User_Document1 = "doc1";

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    Dialog myPop;


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


        //ziyaret edilmemiş
        googleMap.addMarker(new MarkerOptions().flat(true).position(ayasofya).title("Ayasofya").snippet("ayasofya insanlar tarafından ziyaret camidir "));
        googleMap.addMarker(new MarkerOptions().flat(true).position(topkapı).title("Topkapı").snippet("Topkapı gezilcek yerlerden \nbiridir istanbulda"));
        googleMap.addMarker(new MarkerOptions().flat(true).position(rahimikoc).title("Rahmi Koç").snippet("Rahmi gezilcek yerlerden \nbiridir istanbulda"));
        googleMap.setOnInfoWindowClickListener(this);

        //ziyaret edilmiş
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(dolmabahce).title("Dolmabahce").snippet("Dolmabahce gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(suleymaniye).title("Süleymaniye").snippet("Süleymaniye gezilcek yerlerden \nbiridir istanbulda///https://upload.wikimedia.org/wikipedia/commons/2/29/Ayasofya%2C_%C4%B0stanbul%2C_T%C3%BCrkiye.jpg"));


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ayasofya));
        googleMap.setOnInfoWindowClickListener(this);

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        TextView txtclose, textView, textView1, txtclose1, txtclose2;
        Button btnvisit, btnvisitok;
        if (!marker.isFlat()) {
            //bilgi+img // ile parse
            String[] parts = marker.getSnippet().split("///");

            String snippet = parts[0];
            String imgurl = parts[1];


            myPop.setContentView(R.layout.popup);
            txtclose = (TextView) myPop.findViewById(R.id.txtclose1);
            txtclose.setText("X");
            textView1 = (TextView) myPop.findViewById(R.id.title);
            textView1.setText(marker.getTitle());
            textView = (TextView) myPop.findViewById(R.id.text);
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
        } else {

            ImageView img;
            myPop.setContentView(R.layout.popup1);
            textView1 = (TextView) myPop.findViewById(R.id.title);
            textView1.setText(marker.getTitle());
            textView = (TextView) myPop.findViewById(R.id.textview);
            textView.setText(marker.getSnippet());
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

                    image = (Button) myPop.findViewById(R.id.button);
                    save = (Button) myPop.findViewById(R.id.save);
                    textView1 = (TextView) myPop.findViewById(R.id.title);
                    textView1.setText(marker.getTitle());
                    txtclose2 = (TextView) myPop.findViewById(R.id.txtclose1);
                    txtclose2.setText("X");
                    ImageView imageView;
                    imageView = (ImageView) myPop.findViewById(R.id.imageView3);
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

                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



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
}

