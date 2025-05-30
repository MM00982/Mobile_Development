package ru.mirea.musin.mireaproject.ui.filelab;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.VH> {

    private final List<FileItem> data = new ArrayList<>();

    public void add(FileItem fi) {
        data.add(0, fi);
        notifyItemInserted(0);
    }


    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v) {
            super(v);
            tv = (TextView) v;
        }
    }

    @NonNull
    @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView t = new TextView(parent.getContext());
        t.setTextSize(18f);
        int pad = (int) (8 * parent.getResources().getDisplayMetrics().density);
        t.setPadding(pad, pad, pad, pad);
        return new VH(t);
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) {
        holder.tv.setText(data.get(pos).getName());
    }

    @Override public int getItemCount() { return data.size(); }
}
