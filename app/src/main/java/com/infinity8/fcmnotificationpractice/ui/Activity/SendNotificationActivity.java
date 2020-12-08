package com.infinity8.fcmnotificationpractice.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.databinding.ActivitySendNotificationBinding;
import com.infinity8.fcmnotificationpractice.model.NotificationReq;
import com.infinity8.fcmnotificationpractice.model.NotificationResponse;
import com.infinity8.fcmnotificationpractice.network.NotificationRequest;
import com.infinity8.fcmnotificationpractice.network.RetrofitClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.infinity8.fcmnotificationpractice.network.Constants.BASE_URL;

public class SendNotificationActivity extends AppCompatActivity {

    ActivitySendNotificationBinding binding;
    DialogPlus dialogplus;
    private DatabaseReference reference;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendNotificationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Log.i("sdgrg", "onCreate: " + getIntent().getStringExtra("id"));

        reference = FirebaseDatabase.getInstance().getReference().child("Notification");

        id = getIntent().getStringExtra("id");

        String key1=getIntent().getStringExtra("key1");
        String key2=getIntent().getStringExtra("key2");

        Toast.makeText(this, "Key1: "+key1+"Key2: "+key2, Toast.LENGTH_SHORT).show();


        dialogplus = DialogPlus.newDialog(this)
                .setMargin(50, 0, 50, 0)
                .setContentHolder(new ViewHolder(R.layout.dialog_notification_type))
                .setGravity(Gravity.CENTER)
                .setExpanded(false)
                .create();

        LinearLayout layout = (LinearLayout) dialogplus.getHolderView();
        Button restApi = layout.findViewById(R.id.rest_api);
        Button cloud_function = layout.findViewById(R.id.cloud_funcation);

        restApi.setOnClickListener(v -> {
            dialogplus.dismiss();
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", binding.title.getText().toString());
            map.put("description", binding.description.getText().toString());
            map.put("id", id);
            reference.child(FirebaseAuth.getInstance().getUid())
                    .child("RestApi").push().setValue(map)
                    .addOnCompleteListener(task -> {
                        sentByRest();
                    });
        });

        cloud_function.setOnClickListener(v -> {
            dialogplus.dismiss();
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", binding.title.getText().toString());
            map.put("description", binding.description.getText().toString());
            map.put("id", id);
            reference.child(FirebaseAuth.getInstance().getUid()).child("CloudFunction").push().setValue(map).addOnCompleteListener(task ->
                    binding.progressBar.setVisibility(View.GONE));

        });

        binding.send.setOnClickListener(v -> {
            if (binding.title.getText().toString().length() == 0) {
                Toast.makeText(SendNotificationActivity.this, "Enter Title ...", Toast.LENGTH_SHORT).show();
            } else if (binding.description.getText().toString().length() == 0) {
                Toast.makeText(SendNotificationActivity.this, "Enter Description ...", Toast.LENGTH_SHORT).show();
            } else {
                dialogplus.show();
            }
        });
    }

    private void sentByRest() {

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        NotificationReq req = new NotificationReq(
                                snapshot.child("token").getValue().toString(),
                                new NotificationReq.Notification(binding.title.getText().toString(),
                                        binding.description.getText().toString(),
                                        "https://images.unsplash.com/photo-1470813740244-df37b8c1edcb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=751&q=80",
                                        "my_click")
                                ,new NotificationReq.Data_("value 1","value 2")
                        );
                        RetrofitClient.getRetrofit(BASE_URL)
                                .create(NotificationRequest.class)
                                .send(req)
                                .enqueue(new Callback<NotificationResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<NotificationResponse> call, @NotNull
                                            Response<NotificationResponse> response) {
                                        if (response.code() == 200) {
                                            Toast.makeText(SendNotificationActivity.this,
                                                    "Sent", Toast.LENGTH_SHORT).show();
                                        }

                                        Log.d("response", String.valueOf(response));

                                        binding.progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<NotificationResponse> call, @NotNull Throwable t) {
                                        Toast.makeText(SendNotificationActivity.this, "Failed"
                                                + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.i("hfbvjes", "onFailure: " + t.toString());
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}