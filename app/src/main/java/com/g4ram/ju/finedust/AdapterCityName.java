package com.g4ram.ju.finedust;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g4ram.ju.finedust.R;

import java.util.List;

public class AdapterCityName extends RecyclerView.Adapter<AdapterCityName.HolderCityName> {

    List<GetInfoFromApi.AreaInfoRetrofit> listAreaInfo;
    Context parentContext;
    AdapterCityName(List<GetInfoFromApi.AreaInfoRetrofit> areainfo, Context parentContext){
        this.listAreaInfo = areainfo;
        this.parentContext = parentContext;
    }

    @NonNull
    @Override
    public HolderCityName onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.views_city_name,viewGroup,false);
        return new HolderCityName(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCityName.HolderCityName viewHolder, final int i) {
        viewHolder.cityNameTextView.setText(listAreaInfo.get(i).getCityName());
        viewHolder.cityNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentContext, EachCityDustInfo.class);
                intent.putExtra("dustInfo",listAreaInfo.get(i));
                parentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAreaInfo.size();
    }

    class HolderCityName extends RecyclerView.ViewHolder{
        TextView cityNameTextView;
        public HolderCityName(@NonNull View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
        }
    }
}
