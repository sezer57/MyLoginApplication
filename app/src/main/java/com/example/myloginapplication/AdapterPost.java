package com.example.myloginapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.HolderPost> {
    private Context context;
    private ArrayList<ModelMaps>mapsArrayList;

    public AdapterPost(Context context, ArrayList<ModelMaps> mapsArrayList) {
        this.context = context;
        this.mapsArrayList = mapsArrayList;
    }

    @NonNull
    @Override
    public HolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.map_locs,parent,false);

        return new HolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPost holder, int position) {

                ModelMaps model =mapsArrayList.get(position);
                  Integer id= model.getId();
                String title = model.getName();
                Integer price= model.getPrice();
                Double longtitude =model.getLongtitude();
                Double latitude = model.getLatitude();

                holder.title.setText(title);
                holder.price.setText(String.valueOf(price)+"TL");
                 holder.id.setText(String.valueOf(id));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+longtitude+","+latitude+"");


                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                context.startActivity(mapIntent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return mapsArrayList.size();
    }

    class HolderPost extends RecyclerView.ViewHolder{
        TextView id,title,price;
        Button button;
        public HolderPost (@NonNull View itemView){
            super(itemView);


            button = itemView.findViewById(R.id.button2);
            title = itemView.findViewById(R.id.titleloc);
            price = itemView.findViewById(R.id.price);
            id = itemView.findViewById(R.id.id);

        }
    }
}