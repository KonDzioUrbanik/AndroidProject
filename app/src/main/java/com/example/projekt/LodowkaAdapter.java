package com.example.projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekt.databinding.WierszLodowkiBinding; // Ważne!
import java.util.List;

public class LodowkaAdapter extends RecyclerView.Adapter<LodowkaAdapter.ViewHolder> {

    private List<Produkt> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener listener; // Do klikania

    public LodowkaAdapter(Context context, List<Produkt> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Interfejs do klikania w produkt (dla DetailsActivity)
    public interface OnItemClickListener {
        void onItemClick(Produkt produkt);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        // Obsługa kliknięcia
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(produkt);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // TO JEST KLUCZOWE - bez tego lista się nie odświeża
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