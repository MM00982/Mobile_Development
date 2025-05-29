package ru.mirea.musin.mireaproject.ui.mic;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import java.util.Locale;

import ru.mirea.musin.mireaproject.BasePermissionFragment;
import ru.mirea.musin.mireaproject.R;

public class MicLevelFragment extends BasePermissionFragment {

    private static final int SR = 8000;     // sample rate (Hz)

    private AudioRecord rec;
    private boolean running;

    private ProgressBar bar;
    private TextView tv;

    @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle b) {
        return i.inflate(R.layout.fragment_mic_level, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        bar = v.findViewById(R.id.progress);
        tv  = v.findViewById(R.id.tvDb);

        // запросим permission и, если дали, запустим start()
        requestPermission(Manifest.permission.RECORD_AUDIO, this::start);
    }

    /** Старт записи после того, как пользователь выдал разрешение */
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private void start() {
        int buf = AudioRecord.getMinBufferSize(
                SR,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (buf == AudioRecord.ERROR || buf == AudioRecord.ERROR_BAD_VALUE) buf = SR;
        buf *= 2;

        rec = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SR,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                buf);

        rec.startRecording();
        running = true;

        final int finalBuf = buf;
        new Thread(() -> {
            short[] arr = new short[finalBuf];
            while (running) {
                int read = rec.read(arr, 0, arr.length);
                if (read <= 0) continue;

                long sum = 0;
                for (int i = 0; i < read; i++) sum += arr[i] * arr[i];

                double rms = Math.sqrt(sum / (double) read);
                double db  = 20 * Math.log10(rms);

                requireActivity().runOnUiThread(() -> {
                    int level = (int) Math.min(Math.max(db + 60, 0), 120);
                    bar.setProgress(level);
                    tv.setText(String.format(Locale.getDefault(), "%.0f dB", db));
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        running = false;
        if (rec != null) {
            rec.stop();
            rec.release();
        }
        super.onDestroyView();
    }
}