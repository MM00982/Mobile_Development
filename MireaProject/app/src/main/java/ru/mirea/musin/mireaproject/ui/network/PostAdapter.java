package ru.mirea.musin.mireaproject.ui.network;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.musin.mireaproject.network.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.VH> {

    private final List<Post> data = new ArrayList<>();

    public void setData(List<Post> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v) { super(v); tv = (TextView) v; }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView t = new TextView(parent.getContext());
        int pad = (int) (16 * parent.getResources().getDisplayMetrics().density);
        t.setPadding(pad, pad, pad, pad);
        t.setTextSize(16f);
        return new VH(t);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tv.setText(data.get(position).title);
    }

    @Override
    public int getItemCount() { return data.size(); }
}
