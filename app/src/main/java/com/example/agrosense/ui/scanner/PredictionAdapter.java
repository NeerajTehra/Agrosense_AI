package com.example.agrosense.ui.scanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.entity.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {

    private final List<Prediction> predictions;
    private final OnPredictionClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    public interface OnPredictionClickListener {
        void onPredictionClick(Prediction prediction);
    }

    public PredictionAdapter(List<Prediction> predictions, OnPredictionClickListener listener) {
        this.predictions = predictions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prediction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prediction p = predictions.get(position);
        holder.bind(p, listener, dateFormat);
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiseaseName, tvCropInfo, tvDate, tvSeverity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            tvCropInfo = itemView.findViewById(R.id.tvCropInfo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
        }

        void bind(Prediction p, OnPredictionClickListener listener, SimpleDateFormat df) {
            String title = p.isHealthy ? "Healthy" : "Infected";
            if (p.detectedPlant != null && !p.detectedPlant.equals("Unknown")) {
                title += " (" + p.detectedPlant + ")";
            }
            tvDiseaseName.setText(title);
            tvCropInfo.setText("Scan ID: #" + p.id);
            tvDate.setText(df.format(new Date(p.timestamp)));
            tvSeverity.setText(p.severity);

            if (p.isHealthy) {
                tvSeverity.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.leaf_green_light));
                tvSeverity.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.leaf_green));
            } else {
                tvSeverity.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.error_red));
                tvSeverity.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            }

            itemView.setOnClickListener(v -> listener.onPredictionClick(p));
        }
    }
}
