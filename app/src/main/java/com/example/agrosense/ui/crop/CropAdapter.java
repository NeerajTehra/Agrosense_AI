package com.example.agrosense.ui.crop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.entity.Crop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private final List<Crop> crops;
    private final OnCropClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public interface OnCropClickListener {
        void onCropClick(Crop crop);
    }

    public CropAdapter(List<Crop> crops, OnCropClickListener listener) {
        this.crops = crops;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crop, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = crops.get(position);
        holder.bind(crop, listener, dateFormat);
    }

    @Override
    public int getItemCount() {
        return crops.size();
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCropVariety, tvFieldName, tvPlantingDate, tvStatus;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCropVariety = itemView.findViewById(R.id.tvCropVariety);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvPlantingDate = itemView.findViewById(R.id.tvPlantingDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(Crop crop, OnCropClickListener listener, SimpleDateFormat dateFormat) {
            tvCropVariety.setText(crop.variety);
            tvFieldName.setText("Field ID: " + crop.fieldId); // Placeholder
            tvPlantingDate.setText("Planting: " + dateFormat.format(new Date(crop.plantingDate)));
            tvStatus.setText(crop.status);
            
            itemView.setOnClickListener(v -> listener.onCropClick(crop));
        }
    }
}
