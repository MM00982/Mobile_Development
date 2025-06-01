package ru.mirea.musin.firebaseauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.musin.firebaseauth.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.createAccountButton.setOnClickListener(v ->
                createAccount(
                        binding.fieldEmail.getText().toString().trim(),
                        binding.fieldPassword.getText().toString().trim()));

        binding.signInButton.setOnClickListener(v ->
                signIn(
                        binding.fieldEmail.getText().toString().trim(),
                        binding.fieldPassword.getText().toString().trim()));

        binding.signOutButton.setOnClickListener(v -> signOut());

        binding.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    // ───────────────────────────────────────── Lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    // ───────────────────────────────────────── Auth helpers
    private boolean validateForm() {
        boolean valid = true;

        String email = binding.fieldEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.setError("Required.");
            valid = false;
        } else {
            binding.fieldEmail.setError(null);
        }

        String password = binding.fieldPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            binding.fieldPassword.setError("Required.");
            valid = false;
        } else {
            binding.fieldPassword.setError(null);
        }
        return valid;
    }

    // ───────────────────────────────────────── Create account
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        handleException(task.getException());
                        updateUI(null);
                    }
                });
    }

    // ───────────────────────────────────────── Sign in
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        handleException(task.getException());
                        binding.statusTextView.setText(R.string.auth_failed);
                        updateUI(null);
                    }
                });
    }

    // ───────────────────────────────────────── Sign out
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    // ───────────────────────────────────────── Email verification
    private void sendEmailVerification() {
        binding.verifyEmailButton.setEnabled(false);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    binding.verifyEmailButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ───────────────────────────────────────── UI update
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.statusTextView.setText(getString(
                    R.string.emailpassword_status_fmt,
                    user.getEmail(),
                    user.isEmailVerified()));
            binding.detailTextView.setText(getString(
                    R.string.firebase_status_fmt,
                    user.getUid()));

            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.emailPasswordFields.setVisibility(View.GONE);

            binding.signedInButtons.setVisibility(View.VISIBLE);
            binding.verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText(null);

            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.emailPasswordFields.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.GONE);
        }
    }

    // ───────────────────────────────────────── Nice error messages
    private void handleException(Exception e) {
        Log.w(TAG, "Auth error", e);
        if (e instanceof FirebaseAuthWeakPasswordException) {
            binding.fieldPassword.setError("Password is too weak");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            binding.fieldEmail.setError("Bad e-mail format or wrong password");
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            binding.fieldEmail.setError("Account already exists");
        } else if (e instanceof FirebaseAuthInvalidUserException) {
            binding.fieldEmail.setError("No such user");
        }
        Toast.makeText(this, R.string.error_sign_in_failed, Toast.LENGTH_SHORT).show();
    }
}