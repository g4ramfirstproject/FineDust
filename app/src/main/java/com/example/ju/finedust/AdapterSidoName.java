package com.example.ju.finedust;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterSidoName extends RecyclerView.Adapter<AdapterSidoName.HolderSidoName> {

    String[] sidoNames;
    Context parentContext;

    AdapterSidoName(String[] sidoNames, Context parentContext){
        this.sidoNames = sidoNames;
        this.parentContext = parentContext;
    }

    @NonNull
    @Override
    public HolderSidoName onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.views_search_fine_dust_sido_name,viewGroup,false);
        return new HolderSidoName(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSidoName holderSidoName, final int i) {
        holderSidoName.sidoNameTextView.setText(sidoNames[i]);
        final int pos = i;
        holderSidoName.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentContext,CityNamesInSido.class);
                intent.putExtra("sidoName",sidoNames[pos]);
                parentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sidoNames.length;
    }

    class HolderSidoName extends RecyclerView.ViewHolder {

        TextView sidoNameTextView;

        HolderSidoName(@NonNull View itemView) {
            super(itemView);
            sidoNameTextView = itemView.findViewById(R.id.SidoNameTextView);
        }
    }
}
