package ru.mirea.musin.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String STUDENT_NUMBER = "18";
    private static final String GROUP = "БСБО-09-22";

    private EditText etInput;
    private Button btnShowToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput      = findViewById(R.id.etInput);
        btnShowToast = findViewById(R.id.btnShowToast);

        btnShowToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountToast();
            }
        });
    }

    private void showCountToast() {
        String text = etInput.getText().toString();
        int length = text.length();

        String message = "СТУДЕНТ № " + STUDENT_NUMBER +
                " ГРУППА "   + GROUP +
                "  Количество символов – " + length;

        Toast toast = Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_LONG
        );

        toast.show();
    }
}