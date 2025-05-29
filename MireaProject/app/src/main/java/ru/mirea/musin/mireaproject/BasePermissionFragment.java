package ru.mirea.musin.mireaproject;

import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public abstract class BasePermissionFragment extends Fragment {

    private ActivityResultLauncher<String> permissionLauncher;
    private Runnable pendingAction;

    @Override
    public void onCreate(@Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        granted -> {
                            if (granted && pendingAction != null) {
                                pendingAction.run();
                            } else if (!granted) {
                                Toast.makeText(
                                        requireContext(),
                                        "Разрешение не выдано",
                                        Toast.LENGTH_SHORT).show();
                            }
                            pendingAction = null;
                        });
    }

    protected void requestPermission(String perm, Runnable actionIfGranted) {
        if (ContextCompat.checkSelfPermission(requireContext(), perm)
                == PackageManager.PERMISSION_GRANTED) {
            actionIfGranted.run();
        } else {
            pendingAction = actionIfGranted;
            permissionLauncher.launch(perm);
        }
    }
}
