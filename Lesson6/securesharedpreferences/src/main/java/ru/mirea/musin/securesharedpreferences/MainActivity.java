package ru.mirea.musin.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "secret_shared_prefs";
    private static final String KEY_SECURE = "secure";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView poetImage = findViewById(R.id.poetImage);
        TextView  poetName  = findViewById(R.id.poetName);

        poetImage.setImageResource(R.drawable.poet_image);
        poetName.setText(getString(R.string.poet_name));

        savePoetNameSecurely(poetName.getText().toString());
    }

    private void savePoetNameSecurely(String name) {
        try {
            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    FILE_NAME,
                    mainKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            securePrefs.edit().putString(KEY_SECURE, name).apply();

            String readBack = securePrefs.getString(KEY_SECURE, "-");

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
