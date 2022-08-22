package com.example.myloginapplication;

import android.content.Context;
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
                String title = model.getName();
                Integer price= model.getPrice();
                Double longtitude =model.getLongtitude();
                Double latitude = model.getLatitude();

                holder.title.setText(title);
                holder.price.setText(price);

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
        }
    }
}