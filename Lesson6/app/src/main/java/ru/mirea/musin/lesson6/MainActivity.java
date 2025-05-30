package ru.mirea.musin.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "mirea_settings";
    private SharedPreferences prefs;

    private EditText editGroup, editNumber, editFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- Widgets
        editGroup  = findViewById(R.id.editGroup);
        editNumber = findViewById(R.id.editNumber);
        editFilm   = findViewById(R.id.editFilm);
        Button btnSave = findViewById(R.id.btnSave);

        //-- SharedPreferences
        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadPrefs();                 // отображаем сохранённые данные

        btnSave.setOnClickListener(v -> savePrefs());
    }

    private void loadPrefs() {
        editGroup.setText (prefs.getString ("GROUP",  ""));
        editNumber.setText(String.valueOf(
                prefs.getInt("NUMBER", 0)));
        editFilm.setText  (prefs.getString ("FILM",   ""));
    }

    private void savePrefs() {
        String group  = editGroup .getText().toString();
        int    number = 0;
        try { number = Integer.parseInt(editNumber.getText().toString()); }
        catch (NumberFormatException ignored) { }

        String film   = editFilm  .getText().toString();

        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("GROUP",  group);
        ed.putInt   ("NUMBER", number);
        ed.putString("FILM",   film);
        ed.apply();                     //-- асинхронно сохранить

        // (по желанию) тост-уведомление
        // Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show();
    }
}