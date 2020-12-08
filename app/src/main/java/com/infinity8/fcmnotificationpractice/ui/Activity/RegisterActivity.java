package com.infinity8.fcmnotificationpractice.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infinity8.fcmnotificationpractice.databinding.ActivityRegisterBinding;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    final int PERMISSION_ALL = 1;
    private ImagePicker imagePicker;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.loginTextClick.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        String[] PERMISSIONS = {

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        binding.profilePic.setOnClickListener(v -> {
            if (!hasPermissions(RegisterActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                imagePicker.choosePicture(true /*show camera intents*/);
            }
        });
        imagePicker = new ImagePicker(RegisterActivity.this, /* activity non null*/
                null, /* fragment nullable*/
                imageUri -> UCrop.of(imageUri, getTempUri())
                        .withAspectRatio(1, 1)
                        .start(RegisterActivity.this));

        binding.registerButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.EmailId.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.EmailId.getText().toString()).matches()) {
                Toast.makeText(RegisterActivity.this, "Please Enter valid Email Address", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.passwordReg.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            } else if (binding.passwordReg.getText().toString().length() < 6) {
                Toast.makeText(RegisterActivity.this, "Please Enter 6 or more than digit password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.name.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else if (uri == null) {
                Toast.makeText(RegisterActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
            } else {
                register();
            }
        });

    }

    private void register() {
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().
                createUserWithEmailAndPassword(binding.EmailId.getText().toString(), binding.passwordReg.getText().toString())
                .addOnSuccessListener(authResult ->
                        upload())
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Failed.", Toast.LENGTH_SHORT).show();

                    Log.d("FailedData", e.getMessage());
                });

        binding.loginTextClick.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

    }

    void upload() {
        final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Temp/" + System.currentTimeMillis() + ".png");
        riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                binding.progressBar.setVisibility(View.GONE);
                Log.d("FailedData", "onFailure: " + exception.getMessage());
            }
        }).addOnSuccessListener(taskSnapshot ->
                riversRef.getDownloadUrl().addOnSuccessListener(this::insertUserInfo));
    }

    private void insertUserInfo(Uri uri) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {

                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("email", binding.EmailId.getText().toString());
                    map.put("name", binding.name.getText().toString());
                    map.put("password", binding.passwordReg.getText().toString());
                    map.put("image", uri.toString());
                    map.put("token", token);

                    FirebaseDatabase.getInstance().getReference()
                            .child("User")
                            .child(FirebaseAuth.getInstance().getUid())
                            .setValue(map)
                            .addOnSuccessListener(aVoid -> {
                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                binding.progressBar.setVisibility(View.GONE);
                                Log.i("dscgjdshv", "onFailure: " + e.getMessage());
                            });
                });


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissionsList, @NotNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    boolean b = true;
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            b = false;
                        }

                    }
                    if (b) {
                        imagePicker.choosePicture(true /*show camera intents*/);
                    }

                }
                return;
            }
        }
    }

    private Uri getTempUri() {
        String dri = Environment.getExternalStorageDirectory() + File.separator + "Temp";
        File dirFile = new File(dri);
        dirFile.mkdir();

        String file = dri + File.separator + "temp.png";
        File tempFile = new File(file);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            uri = UCrop.getOutput(data);
            binding.profilePic.setImageURI(null);
            binding.profilePic.setImageURI(uri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.i("dsjknjsdkn", "onActivityResult: " + cropError.getMessage());
        }
    }

}