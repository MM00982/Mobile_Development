package ru.mirea.musin.notebook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    private static final String LOG_TAG = "Notebook";

    private EditText etFileName;
    private EditText etQuote;
    private Button btnSave, btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileName = findViewById(R.id.etFileName);
        etQuote    = findViewById(R.id.etQuote);
        btnSave    = findViewById(R.id.btnSave);
        btnLoad    = findViewById(R.id.btnLoad);

        checkPermissions();

        btnSave.setOnClickListener(v -> writeFile());
        btnLoad.setOnClickListener(v -> readFile());

        populateDemoQuotes();
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void writeFile() {
        if (!isExternalStorageWritable()) {
            toast("Внешнее хранилище недоступно для записи");
            return;
        }

        String name  = etFileName.getText().toString().trim();
        String text  = etQuote.getText().toString();

        if (name.isEmpty()) {
            toast("Введите название файла");
            return;
        }

        File dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, name + ".txt");

        try {
            if (!dir.exists() && !dir.mkdirs()) {
                toast("Не удалось создать директорию");
                return;
            }

            OutputStreamWriter writer =
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            writer.write(text);
            writer.close();

            toast("Сохранено: " + file.getName());
            Log.d(LOG_TAG, "saved to " + file.getAbsolutePath());

        } catch (Exception e) {
            toast("Ошибка записи: " + e.getMessage());
            Log.e(LOG_TAG, "write error", e);
        }
    }

    private void readFile() {
        if (!isExternalStorageReadable()) {
            toast("Внешнее хранилище недоступно для чтения");
            return;
        }

        String name = etFileName.getText().toString().trim();
        if (name.isEmpty()) {
            toast("Введите название файла");
            return;
        }

        File dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, name + ".txt");

        if (!file.exists()) {
            toast("Файл не найден");
            return;
        }

        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            etQuote.setText(sb.toString().trim());
            toast("Загружено");
            Log.d(LOG_TAG, "read " + file.getName());

        } catch (Exception e) {
            toast("Ошибка чтения: " + e.getMessage());
            Log.e(LOG_TAG, "read error", e);
        }
    }

    private void populateDemoQuotes() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File f1  = new File(dir, "einstein.txt");
        File f2  = new File(dir, "tesla.txt");

        if (f1.exists() && f2.exists()) return;

        try {
            if (isExternalStorageWritable()) {
                writeTextToFile(f1, "«Воображение важнее знаний.» — А. Эйнштейн");
                writeTextToFile(f2, "«Если ты хочешь найти тайны Вселенной, думай в терминах энергии, частоты и вибрации.» — Н. Тесла");
                Log.d(LOG_TAG, "demo quotes written");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "demo write failed", e);
        }
    }

    private void writeTextToFile(File file, String text) throws Exception {
        File dir = file.getParentFile();
        if (!dir.exists()) dir.mkdirs();

        OutputStreamWriter writer =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(text);
        writer.close();
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            toast("Без разрешения приложение не сможет сохранять файлы");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}