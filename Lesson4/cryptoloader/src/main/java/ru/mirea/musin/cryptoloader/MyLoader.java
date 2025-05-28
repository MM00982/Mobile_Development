package ru.mirea.musin.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {

    public static final String ARG_CRYPT = "crypt_text";
    public static final String ARG_KEY   = "crypt_key";

    private final byte[] cryptText;
    private final byte[] keyBytes;

    public MyLoader(@NonNull Context ctx, @Nullable Bundle args) {
        super(ctx);
        if (args == null) {
            throw new IllegalArgumentException("Bundle args must not be null");
        }
        cryptText = args.getByteArray(ARG_CRYPT);
        keyBytes  = args.getByteArray(ARG_KEY);
    }

    @Override protected void onStartLoading() { forceLoad(); }

    @Override public String loadInBackground() {
        SystemClock.sleep(3000);

        SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        return CryptoUtils.decryptMsg(cryptText, originalKey);
    }
}
