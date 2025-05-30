package ru.mirea.musin.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG  = "HistoryDate";
    private static final String FILE_NAME = "history_date.txt";

    private EditText etInput;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.etInput);
        tv      = findViewById(R.id.tv);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String text = etInput.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Введите текст перед сохранением", Toast.LENGTH_SHORT).show();
                    return;
                }
                writeToFile(text);
                etInput.setText("");
                Toast.makeText(MainActivity.this,
                        "Сохранено во внутренний файл " + FILE_NAME,
                        Toast.LENGTH_SHORT).show();
            }
        });

        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) { }
                tv.post(new Runnable() {
                    @Override public void run() {
                        tv.setText(getTextFromFile());
                    }
                });
            }
        }).start();
    }

    private void writeToFile(String data) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextFromFile() {
        try (FileInputStream fin = openFileInput(FILE_NAME)) {
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            Log.e(LOG_TAG, "read error", e);
            return "Файл пока не создан.";
        }
    }
}