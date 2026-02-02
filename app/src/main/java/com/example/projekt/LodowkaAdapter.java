package com.example.projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekt.databinding.WierszLodowkiBinding;
import java.util.List;

public class LodowkaAdapter extends RecyclerView.Adapter<LodowkaAdapter.ViewHolder> {

    private List<Produkt> mData;
    private LayoutInflater mInflater;

    // Listenery dla kliknięć
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener; // NOWY

    public LodowkaAdapter(Context context, List<Produkt> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Interfejsy
    public interface OnItemClickListener {
        void onItemClick(Produkt produkt);
    }
    public interface OnDeleteClickListener { // NOWY
        void onDeleteClick(Produkt produkt);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) { // NOWY
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WierszLodowkiBinding binding = WierszLodowkiBinding.inflate(mInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produkt produkt = mData.get(position);
        holder.binding.tvPolka.setText(produkt.polka);
        holder.binding.tvProdukt.setText(produkt.nazwa);

        // Kliknięcie w cały wiersz (szczegóły)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(produkt);
        });

        // Kliknięcie w przycisk usuwania (NOWE)
        holder.binding.btnUsun.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteClick(produkt);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDane(List<Produkt> noweDane) {
        this.mData = noweDane;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final WierszLodowkiBinding binding;
        ViewHolder(WierszLodowkiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}