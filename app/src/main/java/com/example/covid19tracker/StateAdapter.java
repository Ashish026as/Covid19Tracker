package com.example.covid19tracker;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    ArrayList<StateWise> stateWises = new ArrayList<>();

    public StateAdapter(ArrayList<StateWise> stateWises) {
        this.stateWises = stateWises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view,1);
        }
        else if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new ViewHolder(view,0);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(holder.getItemViewType()==TYPE_ITEM) {
            StateWise data = stateWises.get(position - 1);
            holder.state.setText(data.state);
            holder.confirmed.setText(data.confirmed+"\n"+ "+" + data.deltaconfirmed);
            holder.active.setText(data.active+"\n"+ "+" + data.deltaactive);
            holder.recovered.setText(data.recovered+"\n"+ "+" + data.deltarecovered);
            holder.deceased.setText(data.deaths+"\n"+ "+" + data.deltadeaths);
        }
        else if(holder.getItemViewType()==TYPE_HEADER){
            holder.h_state.setText("STATE/UT");
            holder.h_confirmed.setText("CNFD");
            holder.h_active.setText("ACTV");
            holder.h_recovered.setText("RCVD");
            holder.h_deceased.setText("DSCD");
        }
    }

    @Override
    public int getItemCount() {
        return stateWises.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView state,confirmed,active,recovered,deceased;
        TextView h_state,h_confirmed,h_active,h_recovered,h_deceased;

        public ViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            if(viewType == 1) {
                state = itemView.findViewById(R.id.stateTv);
                confirmed = itemView.findViewById(R.id.confirmedTv);
                active = itemView.findViewById(R.id.activeTv);
                recovered = itemView.findViewById(R.id.recoveredTv);
                deceased = itemView.findViewById(R.id.deceasedTv);
            }
            else if(viewType == 0){
                h_state = itemView.findViewById(R.id.h_state);
                 h_confirmed = itemView.findViewById(R.id.h_confirmed);
                  h_active = itemView.findViewById(R.id.h_active);
                   h_recovered = itemView.findViewById(R.id.h_recovered);
                    h_deceased = itemView.findViewById(R.id.h_deceased);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;

    }
}