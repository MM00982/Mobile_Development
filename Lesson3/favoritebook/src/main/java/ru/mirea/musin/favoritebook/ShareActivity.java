package ru.mirea.musin.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView tvBook = findViewById(R.id.textViewBook);
        TextView tvQuote = findViewById(R.id.textViewQuote);
        EditText editBook = findViewById(R.id.editBook);
        EditText editQuote = findViewById(R.id.editQuote);
        Button buttonSend = findViewById(R.id.buttonSend);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quote = extras.getString(MainActivity.QUOTES_KEY);
            tvBook.setText("Книга: " + bookName);
            tvQuote.setText("Цитата: " + quote);
        }

        buttonSend.setOnClickListener(v -> {
            String userBook = editBook.getText().toString().trim();
            String userQuote = editQuote.getText().toString().trim();
            String result = "Название книги: " + userBook + ". " +
                    "Цитата: " + userQuote;

            Intent data = new Intent();
            data.putExtra(MainActivity.USER_MESSAGE, result);
            setResult(Activity.RESULT_OK, data);
            finish();
        });
    }
}