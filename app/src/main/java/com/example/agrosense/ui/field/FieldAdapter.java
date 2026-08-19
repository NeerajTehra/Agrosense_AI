package com.example.agrosense.ui.field;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrosense.R;
import com.example.agrosense.data.entity.Field;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    private final List<Field> fields;
    private final OnFieldClickListener listener;

    public interface OnFieldClickListener {
        void onFieldClick(Field field);
    }

    public FieldAdapter(List<Field> fields, OnFieldClickListener listener) {
        this.fields = fields;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.bind(field, listener);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFieldName, tvFarmName, tvCropType, tvArea;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvFarmName = itemView.findViewById(R.id.tvFarmName);
            tvCropType = itemView.findViewById(R.id.tvCropType);
            tvArea = itemView.findViewById(R.id.tvArea);
        }

        void bind(Field field, OnFieldClickListener listener) {
            tvFieldName.setText(field.name);
            tvFarmName.setText("Field Management");
            tvCropType.setText("Crop: " + field.cropType);
            tvArea.setText(field.area + " Acres");
            
            itemView.setOnClickListener(v -> listener.onFieldClick(field));
        }
    }
}
