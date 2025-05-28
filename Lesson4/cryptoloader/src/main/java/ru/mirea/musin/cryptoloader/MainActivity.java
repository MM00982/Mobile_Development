package ru.mirea.musin.cryptoloader;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import ru.mirea.musin.cryptoloader.databinding.ActivityMainBinding;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final int LOADER_ID = 1;
    private static final String TAG = "CryptoLoader";

    private ActivityMainBinding binding;

    private byte[] lastCrypt;
    private byte[] lastKeyBytes;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEncrypt.setOnClickListener(v -> {
            String phrase = binding.editPhrase.getText().toString();

            SecretKey key = CryptoUtils.generateKey();
            byte[] crypt = CryptoUtils.encryptMsg(phrase, key);

            lastCrypt    = crypt;
            lastKeyBytes = key.getEncoded();

            Bundle args = new Bundle();
            args.putByteArray(MyLoader.ARG_CRYPT, crypt);
            args.putByteArray(MyLoader.ARG_KEY, key.getEncoded());

            LoaderManager.getInstance(this)
                    .restartLoader(LOADER_ID, args, this);
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_ID) {
            Toast.makeText(this, "Создаём Loader " + id, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, args);
        }
        throw new IllegalArgumentException("Unknown loader id " + id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (loader.getId() == LOADER_ID) {
            Toast.makeText(this, "Расшифровано: " + data, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Decrypt result = " + data);
        }
    }

    @Override public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "Loader reset");
    }
}