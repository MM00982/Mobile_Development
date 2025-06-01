package ru.mirea.musin.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Определяем внешний IP, парсим город/регион/координаты,
 * по координатам получаем текущую погоду от open-meteo.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvIp, tvCity, tvRegion, tvCoords, tvWeather;
    private static final String TAG = "HttpURLDemo";

    /* ipinfo без токена – лимит 1000 запросов/день. При необходимости
       можно добавить "?token=ВАШ_ТОКЕН" и снять ограничение. */
    private static final String IPINFO_URL = "https://ipinfo.io/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFetch = findViewById(R.id.btnFetch);
        tvIp     = findViewById(R.id.tvIp);
        tvCity   = findViewById(R.id.tvCity);
        tvRegion = findViewById(R.id.tvRegion);
        tvCoords = findViewById(R.id.tvCoords);
        tvWeather= findViewById(R.id.tvWeather);

        btnFetch.setOnClickListener(v -> startLoading());
    }

    /* --------------------------------------------------- */
    /* Проверяем наличие интернета                         */
    /* --------------------------------------------------- */
    private void startLoading() {
        if (isOnline()) {
            new DownloadIpTask().execute(IPINFO_URL);
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnected();
    }

    /* --------------------------------------------------- */
    /* AsyncTask №1  ─ IP-запрос                           */
    /* --------------------------------------------------- */
    private class DownloadIpTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvIp.setText("Загружаем IP…");
            tvWeather.setText("");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, "IP download error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null || !result.trim().startsWith("{")) {
                tvIp.setText("Ошибка IP-сервиса:\n" + result);
                return;
            }

            try {
                JSONObject json = new JSONObject(result);

                String ip      = json.optString("ip");
                String city    = json.optString("city");
                String region  = json.optString("region");
                String loc     = json.optString("loc");   // "lat,lon"

                tvIp.setText("IP: " + ip);
                tvCity.setText("Город: " + city);
                tvRegion.setText("Регион: " + region);
                tvCoords.setText("Координаты: " + loc);

                /* loc → lat,lon → запускаем 2-ю задачу */
                String[] parts = loc.split(",");
                if (parts.length == 2) {
                    double lat = Double.parseDouble(parts[0]);
                    double lon = Double.parseDouble(parts[1]);
                    new WeatherTask().execute(lat, lon);
                } else {
                    tvWeather.setText("Координаты не найдены");
                }

            } catch (JSONException | NumberFormatException e) {
                tvIp.setText("Не удалось разобрать JSON\n" + e.getMessage());
                Log.e(TAG, "Parse IP JSON error", e);
            }
        }
    }

    /* --------------------------------------------------- */
    /* AsyncTask №2  ─ Погода open-meteo                   */
    /* --------------------------------------------------- */
    private class WeatherTask extends AsyncTask<Double, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvWeather.setText("Загружаем погоду…");
        }

        @Override
        protected String doInBackground(Double... coords) {
            double lat = coords[0];
            double lon = coords[1];

            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude="
                    + lat + "&longitude=" + lon + "&current_weather=true";

            try {
                return downloadUrl(weatherUrl);
            } catch (IOException e) {
                Log.e(TAG, "Weather download error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null || !result.trim().startsWith("{")) {
                tvWeather.setText("Ошибка погоды:\n" + result);
                return;
            }

            try {
                JSONObject json = new JSONObject(result);
                JSONObject current = json.getJSONObject("current_weather");

                double temp  = current.getDouble("temperature");
                double wind  = current.getDouble("windspeed");
                String summary = String.format("Температура: %.1f °C\nВетер: %.1f км/ч",
                        temp, wind);

                tvWeather.setText(summary);

            } catch (JSONException e) {
                tvWeather.setText("Не удалось разобрать JSON погоды");
                Log.e(TAG, "Parse weather JSON error", e);
            }
        }
    }

    /* --------------------------------------------------- */
    /* Универсальный метод скачивания текста              */
    /* --------------------------------------------------- */
    private String downloadUrl(String address) throws IOException {
        InputStream in = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);

            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP " + code + " – "
                        + conn.getResponseMessage());
            }

            in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int r;
            while ((r = in.read()) != -1) bos.write(r);

            return bos.toString(StandardCharsets.UTF_8.name());

        } finally {
            if (in != null) in.close();
        }
    }
}