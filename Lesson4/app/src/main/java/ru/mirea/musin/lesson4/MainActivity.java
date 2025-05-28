package ru.mirea.musin.lesson4;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import ru.mirea.musin.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MediaPlayer player;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private boolean isPrepared = false;

    private static final String KEY_POS     = "pos";
    private static final String KEY_PLAYING = "playing";

    private final Runnable uiUpdater = new Runnable() {
        @Override public void run() {
            if (isPrepared && player != null && player.isPlaying()) {
                int pos = player.getCurrentPosition();
                binding.seekBar.setProgress(pos);
                binding.tvCurrent.setText(millisToTime(pos));
            }
            uiHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        player = MediaPlayer.create(this, R.raw.demo);
        player.setOnPreparedListener(mp -> {
            isPrepared = true;
            binding.tvDuration.setText(millisToTime(mp.getDuration()));
            binding.seekBar.setMax(mp.getDuration());

            if (savedInstanceState != null) {
                int pos = savedInstanceState.getInt(KEY_POS, 0);
                mp.seekTo(pos);
                binding.seekBar.setProgress(pos);

                if (savedInstanceState.getBoolean(KEY_PLAYING, false)) {
                    mp.start();
                    binding.btnPlayPause.setImageResource(
                            android.R.drawable.ic_media_pause);
                }
            }
            startUiUpdates();
        });

        binding.btnPlayPause.setOnClickListener(v -> togglePlayPause());
        binding.btnPrev.setOnClickListener(v -> seekBy(-10_000));
        binding.btnNext.setOnClickListener(v -> seekBy(10_000));

        binding.seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar sb, int pos,
                                                  boolean fromUser) {
                        if (fromUser && isPrepared && player != null) {
                            player.seekTo(pos);
                        }
                        binding.tvCurrent.setText(millisToTime(pos));
                    }
                    @Override public void onStartTrackingTouch(SeekBar sb) {}
                    @Override public void onStopTrackingTouch(SeekBar sb) {}
                });
    }

    private void togglePlayPause() {
        if (!isPrepared || player == null) return;
        if (player.isPlaying()) {
            player.pause();
            binding.btnPlayPause.setImageResource(
                    android.R.drawable.ic_media_play);
        } else {
            player.start();
            binding.btnPlayPause.setImageResource(
                    android.R.drawable.ic_media_pause);
        }
    }

    private void seekBy(int delta) {
        if (!isPrepared || player == null) return;
        int newPos = player.getCurrentPosition() + delta;
        newPos = Math.max(0, Math.min(newPos, player.getDuration()));
        player.seekTo(newPos);
        binding.seekBar.setProgress(newPos);
        binding.tvCurrent.setText(millisToTime(newPos));
    }

    private void stopPlayback() {
        if (!isPrepared || player == null) return;
        player.pause();
        player.seekTo(0);
        binding.seekBar.setProgress(0);
        binding.tvCurrent.setText(millisToTime(0));
        binding.btnPlayPause.setImageResource(
                android.R.drawable.ic_media_play);
    }

    private void startUiUpdates() {
        uiHandler.post(uiUpdater);
    }

    private String millisToTime(int ms) {
        long min = TimeUnit.MILLISECONDS.toMinutes(ms);
        long sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;
        return String.format("%02d:%02d", min, sec);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isPrepared && player != null) {
            outState.putInt(KEY_POS, player.getCurrentPosition());
            outState.putBoolean(KEY_PLAYING, player.isPlaying());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        uiHandler.removeCallbacksAndMessages(null);

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
