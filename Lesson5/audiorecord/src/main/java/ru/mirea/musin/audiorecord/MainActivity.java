package ru.mirea.musin.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import ru.mirea.musin.audiorecord.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "AudioRecordDemo";

    private String recordFilePath;
    private MediaRecorder recorder;
    private MediaPlayer player;

    private boolean isRecording = false;
    private boolean isPlaying   = false;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts
                    .RequestMultiplePermissions(), result -> {
                boolean granted = true;
                for (Boolean ok : result.values()) granted &= ok;
                if (!granted) finish();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button recordButton = binding.recordButton;
        Button playButton   = binding.playButton;
        playButton.setEnabled(false);

        recordFilePath = new File(
                getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audiorecordtest.3gp").getAbsolutePath();

        askPermissions();

        recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
                recordButton.setText("Остановить запись");
                playButton.setEnabled(false);
            } else {
                stopRecording();
                recordButton.setText("Начать запись");
                playButton.setEnabled(true);
            }
            isRecording = !isRecording;
        });

        playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                startPlaying();
                playButton.setText("Остановить воспроизведение");
                recordButton.setEnabled(false);
            } else {
                stopPlaying();
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
            }
            isPlaying = !isPlaying;
        });
    }

    private void askPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        boolean allGranted = true;
        for (String p : permissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, p)
                    == PackageManager.PERMISSION_GRANTED;
        }
        if (!allGranted) permissionLauncher.launch(permissions);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed: " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (recorder == null) return;
        try {
            recorder.stop();
        } catch (RuntimeException ignored) {}
        recorder.release();
        recorder = null;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mp -> {
                binding.playButton.performClick();
            });
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed: " + e.getMessage());
        }
    }

    private void stopPlaying() {
        if (player == null) return;
        player.release();
        player = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) stopRecording();
        if (player   != null) stopPlaying();
    }
}