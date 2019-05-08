package com.example.ju.finedust;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ju.finedust.Item.ItemHourlyForecast;

import java.util.ArrayList;

public class AdapterHourlyForecast extends RecyclerView.Adapter<AdapterHourlyForecast.ItemViewHolder> {

    ArrayList<ItemHourlyForecast> itemHourlyForecastArrayList;
    private Context context;

    public AdapterHourlyForecast(Context context) {
        this.itemHourlyForecastArrayList = new ArrayList<>();
        this.itemHourlyForecastArrayList.clear();
        this.context = context;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_maindustlevel_time, parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemHourlyForecast itemHourlyForecast = itemHourlyForecastArrayList.get(position);
        holder.currentTime.setText(itemHourlyForecast.getCurrentTime());
        Glide.with(context).load(itemHourlyForecast.getResultcolor()).into(holder.resultcolor);
        holder.result.setText(itemHourlyForecast.getResult());
    }

    @Override
    public int getItemCount() {
        return itemHourlyForecastArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView currentTime;
        ImageView resultcolor;
        TextView result;
        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            currentTime = (TextView)itemView.findViewById(R.id.MainrecyclerView_item_Time_tv);
            resultcolor = (ImageView)itemView.findViewById(R.id.MainrecyclerView_item_Image_iv);
            result = (TextView)itemView.findViewById(R.id.MainrecyclerView_item_dustleveltext_tv);

        }
    }

    public void add(String dataTime, String pm10Value){
        itemHourlyForecastArrayList.add(new ItemHourlyForecast(dataTime,pm10Value));
        this.notifyDataSetChanged();
}
}
