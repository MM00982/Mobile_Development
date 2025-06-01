package ru.mirea.musin.timeservice;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.musin.timeservice.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final String HOST = "time.nist.gov";
    private static final int    PORT = 13;

    private static final DateTimeFormatter DATE_IN  =
            DateTimeFormatter.ofPattern("yy-MM-dd");
    private static final DateTimeFormatter TIME_IN  =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_OUT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());
    private static final DateTimeFormatter TIME_OUT =
            DateTimeFormatter.ofPattern("HH:mm:ss",  Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> new GetTimeTask().execute());
    }

    private class GetTimeTask extends AsyncTask<Void, Void, ZonedDateTime> {

        @Nullable
        @Override
        protected ZonedDateTime doInBackground(Void... params) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(HOST, PORT), 3000);
                socket.setSoTimeout(3000);

                BufferedReader reader = SocketUtils.getReader(socket);

                reader.readLine();
                String line = reader.readLine();

                if (line == null || line.trim().isEmpty()) return null;

                String[] parts = line.split("\\s+");
                if (parts.length < 3) return null;

                LocalDate  dateUtc = LocalDate.parse(parts[1], DATE_IN);
                LocalTime  timeUtc = LocalTime.parse(parts[2], TIME_IN);
                LocalDateTime ldt  = LocalDateTime.of(dateUtc, timeUtc);

                return ldt.atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(ZoneId.of("Europe/Moscow"));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(@Nullable ZonedDateTime mskDateTime) {
            if (mskDateTime == null) {
                Toast.makeText(MainActivity.this,
                        "Не удалось получить время с сервера. Попробуйте позже.",
                        Toast.LENGTH_LONG).show();
                binding.textViewDate.setText("--.--.----");
                binding.textViewTime.setText("--:--:--");
                return;
            }

            binding.textViewDate.setText(mskDateTime.format(DATE_OUT));
            binding.textViewTime.setText(mskDateTime.format(TIME_OUT));
        }
    }
}