package ru.mirea.musin.mireaproject.ui.network;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.*;
import ru.mirea.musin.mireaproject.R;
import ru.mirea.musin.mireaproject.network.*;

public class NetworkFragment extends Fragment {

    private PostAdapter adapter;

    @Override public View onCreateView(
            @NonNull LayoutInflater inf, ViewGroup c, Bundle b) {
        return inf.inflate(R.layout.fragment_network, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        RecyclerView rv = v.findViewById(R.id.rvPosts);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter();
        rv.setAdapter(adapter);
        loadPosts();
    }

    private void loadPosts() {
        ApiService.create().posts().enqueue(new Callback<List<Post>>() {
            @Override public void onResponse(
                    @NonNull Call<List<Post>> call, @NonNull Response<List<Post>> resp) {
                if (resp.isSuccessful() && resp.body() != null)
                    adapter.setData(resp.body());
                else
                    Toast.makeText(getContext(),
                            "Error: " + resp.code(), Toast.LENGTH_SHORT).show();
            }

            @Override public void onFailure(
                    @NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),
                        "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
