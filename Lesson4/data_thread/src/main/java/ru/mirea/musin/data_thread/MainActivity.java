package ru.mirea.musin.data_thread;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;
import ru.mirea.musin.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvInfo.setText("");

        final Runnable runn1 = () -> log("runn1 (runOnUiThread)");
        final Runnable runn2 = () -> log("runn2 (View.post)");
        final Runnable runn3 = () -> log("runn3 (View.postDelayed)");

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);

                TimeUnit.SECONDS.sleep(1);

                binding.tvInfo.postDelayed(runn3, 2_000);

                binding.tvInfo.post(runn2);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void log(String msg) {
        runOnUiThread(() -> {
            binding.tvInfo.append(msg + "\n");
            binding.logScroll.post(() -> binding.logScroll.fullScroll(View.FOCUS_DOWN));
        });
    }
}
