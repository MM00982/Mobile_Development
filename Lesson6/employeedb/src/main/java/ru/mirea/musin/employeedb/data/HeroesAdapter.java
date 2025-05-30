package ru.mirea.musin.employeedb.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mirea.musin.employeedb.R;
import ru.mirea.musin.employeedb.data.Hero;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroViewHolder> {

    private final List<Hero> data;

    public HeroesAdapter(List<Hero> data) {
        this.data = data;
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUniverse, tvPower, tvStrength;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUniverse = itemView.findViewById(R.id.tvUniverse);
            tvPower = itemView.findViewById(R.id.tvPower);
            tvStrength = itemView.findViewById(R.id.tvStrength);
        }

        void bind(Hero h) {
            tvName.setText(h.name);
            tvUniverse.setText(h.universe);
            tvPower.setText(h.superPower);
            tvStrength.setText("Сила: " + h.strengthLevel);
        }
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
