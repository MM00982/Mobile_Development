package ru.mirea.musin.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;

import ru.mirea.musin.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // если уже вошли – сразу в MainActivity
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            goMain();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> signIn());
        binding.btnRegister.setOnClickListener(v -> register());
    }

    private void signIn() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();
        if (!valid(email, pass)) return;

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) goMain();
                    else {
                        Log.w(TAG, "signIn", task.getException());
                        Toast.makeText(this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void register() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();
        if (!valid(email, pass)) return;

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) goMain();
                    else {
                        Log.w(TAG, "register", task.getException());
                        String msg = (task.getException() instanceof FirebaseAuthUserCollisionException)
                                ? "Account exists" : getString(R.string.auth_failed);
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean valid(String email, String pass) {
        if (TextUtils.isEmpty(email)) { binding.etEmail.setError("!"); return false; }
        if (TextUtils.isEmpty(pass))  { binding.etPassword.setError("!"); return false; }
        return true;
    }

    private void goMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
