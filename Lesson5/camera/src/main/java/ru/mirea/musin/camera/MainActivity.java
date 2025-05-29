package ru.mirea.musin.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mirea.musin.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMS = new String[]{
            Manifest.permission.CAMERA
    };
    private Uri photoUri;
    private File photoFile;

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && photoFile.exists()) {
                            binding.imageView.setImageBitmap(
                                    BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
                        } else {
                            Toast.makeText(this, "Фото не сделано", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnTakePhoto.setOnClickListener(v -> {
            if (allPermissionsGranted()) {
                launchCamera();
            } else {
                requestPermissions(REQUIRED_PERMS, REQUEST_CODE_PERMISSIONS);
            }
        });
    }

    private boolean allPermissionsGranted() {
        for (String perm : REQUIRED_PERMS) {
            if (ContextCompat.checkSelfPermission(this, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void launchCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String fileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            photoFile = File.createTempFile(
                    fileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не удалось создать файл для фото", Toast.LENGTH_SHORT).show();
            return;
        }

        String authority = getApplicationContext().getPackageName() + ".fileprovider";
        photoUri = FileProvider.getUriForFile(this, authority, photoFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        takePictureLauncher.launch(intent);
    }@Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                launchCamera();
            } else {
                Toast.makeText(this,
                        "Без разрешений CAMERA и STORAGE невозможно сделать фото",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}