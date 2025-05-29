package ru.mirea.musin.mireaproject.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import ru.mirea.musin.mireaproject.BasePermissionFragment;
import ru.mirea.musin.mireaproject.R;

import java.io.File;
import java.io.IOException;

public class CameraNoteFragment extends BasePermissionFragment {

    private static final String KEY_URI = "captured_uri";
    private Uri photoUri;

    private ImageView iv;
    private EditText et;

    @Override public android.view.View onCreateView(
            @NonNull android.view.LayoutInflater i,
            android.view.ViewGroup c, Bundle b) {
        return i.inflate(R.layout.fragment_camera_note, c, false);
    }

    @Override public void onViewCreated(@NonNull android.view.View v,@Nullable Bundle s) {
        iv = v.findViewById(R.id.ivPhoto);
        et = v.findViewById(R.id.etNote);

        v.findViewById(R.id.btnShot).setOnClickListener(btn ->
                requestPermission(android.Manifest.permission.CAMERA, this::openCamera));

        if (s!=null && s.containsKey(KEY_URI)) {
            photoUri = Uri.parse(s.getString(KEY_URI));
            iv.setImageURI(photoUri);
        }
    }

    @Override public void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        if (photoUri!=null) out.putString(KEY_URI, photoUri.toString());
    }

    /* ---------- камера ---------- */

    private void openCamera() {
        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cam.resolveActivity(requireContext().getPackageManager()) == null) {
            Toast.makeText(getContext(), "Камера недоступна", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            File f = File.createTempFile("cam_", ".jpg", requireContext().getCacheDir());
            photoUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName()+".provider", f);
            cam.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(cam);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка файла", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
                if (res.getResultCode()==Activity.RESULT_OK && photoUri!=null) {
                    iv.setImageURI(photoUri);
                    Snackbar.make(requireView(),
                            "Фото сохранено, напишите заметку!", Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Отменено", Toast.LENGTH_SHORT).show();
                }
            });
}
