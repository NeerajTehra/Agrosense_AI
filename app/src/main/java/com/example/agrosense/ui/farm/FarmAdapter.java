package com.example.agrosense.ui.farm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.entity.Farm;

import java.util.List;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {

    private final List<Farm> farms;
    private final OnFarmClickListener listener;

    public interface OnFarmClickListener {
        void onFarmClick(Farm farm);
    }

    public FarmAdapter(List<Farm> farms, OnFarmClickListener listener) {
        this.farms = farms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm, parent, false);
        return new FarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {
        Farm farm = farms.get(position);
        holder.bind(farm, listener);
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    static class FarmViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFarmName, tvFarmLocation, tvTotalArea, tvFieldCount;

        public FarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFarmName = itemView.findViewById(R.id.tvFarmName);
            tvFarmLocation = itemView.findViewById(R.id.tvFarmLocation);
            tvTotalArea = itemView.findViewById(R.id.tvTotalArea);
            tvFieldCount = itemView.findViewById(R.id.tvFieldCount);
        }

        void bind(Farm farm, OnFarmClickListener listener) {
            tvFarmName.setText(farm.name);
            tvFarmLocation.setText(farm.location);
            tvTotalArea.setText("Area: " + farm.totalArea + " Acres");
            tvFieldCount.setText("Tap for details"); 
            
            itemView.setOnClickListener(v -> listener.onFarmClick(farm));
        }
    }
}
