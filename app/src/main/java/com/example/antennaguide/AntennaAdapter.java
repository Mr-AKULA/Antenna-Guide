package com.example.antennaguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AntennaAdapter extends RecyclerView.Adapter<AntennaAdapter.ViewHolder> {
    private List<Antenna> antennas;
    private OnAntennaClickListener listener;

    public interface OnAntennaClickListener {
        void onAntennaClick(Antenna antenna);
    }

    public AntennaAdapter(List<Antenna> antennas, OnAntennaClickListener listener) {
        this.antennas = antennas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_antenna, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Antenna antenna = antennas.get(position);
        holder.nameTextView.setText(antenna.getName());
        holder.descTextView.setText(antenna.getShortDesc());
        holder.iconImageView.setImageResource(antenna.getIconResId());
        holder.cardView.setOnClickListener(v -> listener.onAntennaClick(antenna));
    }

    @Override
    public int getItemCount() {
        return antennas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView iconImageView;
        TextView nameTextView;
        TextView descTextView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            iconImageView = itemView.findViewById(R.id.antenna_icon);
            nameTextView = itemView.findViewById(R.id.antenna_name);
            descTextView = itemView.findViewById(R.id.antenna_short_desc);
        }
    }
}