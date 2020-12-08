package com.infinity8.fcmnotificationpractice.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.databinding.ActivityLoginBinding;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ActivityLoginBinding binding;
    String SLoginType;
    private static final int RC_SIGN_IN = 7;
    private final String TAG = "RegistrationActivity";
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(LoginActivity.this);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        /*AppEventsLogger.activateApp(LoginActivity.this);*/
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("User");
        binding.googleSignin.setOnClickListener(v -> signIn());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mCallbackManager = CallbackManager.Factory.create();
        binding.loginButtonFb.setReadPermissions("email", "public_profile");
        binding.registerTextClick.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        binding.loginButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.email.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                Toast.makeText(LoginActivity.this, "Please Enter valid Email Address", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.password.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            } else if (binding.password.getText().toString().length() < 6) {
                Toast.makeText(LoginActivity.this, "Please Enter 6 or more than digit password", Toast.LENGTH_SHORT).show();
            } else {
                login();
            }
        });


        binding.fbCustomeButton.setOnClickListener(v -> {
            if (v.getId() == R.id.fb_custome_button) {
                binding.loginButtonFb.performClick();
                binding.loginButtonFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFaceBookToken(loginResult);

                        // Toast.makeText(SignInActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("onCacnel", "Cancel Login");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("onError", String.valueOf(error));
                    }
                });
            }
        });

    }

    private void login() {
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {

                        return;
                    }
                    String token = task.getResult();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                            .addOnSuccessListener(authResult -> {

                                Map<String, Object> map = new HashMap<>();
                                map.put("token", token);
                                FirebaseDatabase.getInstance()
                                        .getReference().child("User")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(map)
                                        .addOnCompleteListener(task1 -> {
                                            binding.progressBar.setVisibility(View.GONE);
                                            Toast.makeText(LoginActivity.this, "Login Successfull.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            finish();
                                        });


                            })
                            .addOnFailureListener(e -> {
                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                            });
                });


    }


    private void signIn() {
        SLoginType = "Email";
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        String photo = String.valueOf(user.getPhotoUrl());

                        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String email1 = String.valueOf(snapshot.child("email").getValue());
                                assert email != null;
                                if (email.equals(email1)) {
                                    Toast.makeText(LoginActivity.this, "Welcome" + name, Toast.LENGTH_SHORT).show();
                                } else {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("email", email);
                                    map.put("name", name);
                                    map.put("password", "NAN");
                                    map.put("image", photo);
                                    map.put("token",idToken);
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(map)
                                            .addOnSuccessListener(aVoid -> {
                                                binding.progressBar.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                binding.progressBar.setVisibility(View.GONE);
                                                Log.d("FailedData", "onFailure: " + e.getMessage());
                                                Toast.makeText(LoginActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }

                    // ...
                });
    }

    private void handleFaceBookToken(LoginResult loginResult) {

        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {

                FirebaseUser currentuser = mAuth.getCurrentUser();

                if (currentuser != null) {
                    Toast.makeText(LoginActivity.this, currentuser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                        Log.d("LoginActivity", response.toString());
                        try {
                            String email = object.getString("email");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String auth_uid = object.getString("id");
                            Toast.makeText(LoginActivity.this, first_name, Toast.LENGTH_LONG).show();
                            Log.d("loginSucces", first_name);
                            Log.d("loginSucces", email);

                            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String email1 = String.valueOf(snapshot.child("email").getValue());
                                    if (email.equals(email1)) {
                                        Toast.makeText(LoginActivity.this, "Welcome" + first_name + " " + last_name, Toast.LENGTH_SHORT).show();
                                    } else {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("email", email);
                                        map.put("name", first_name + " " + last_name);
                                        map.put("password", "NAN");
                                        map.put("image", "http://graph.facebook.com/" + auth_uid + "/picture?type=square");
                                        map.put("token",loginResult.getAccessToken().getToken());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(map)
                                                .addOnSuccessListener(aVoid -> {
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(LoginActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    Log.d("FailedData", "onFailure: " + e.getMessage());
                                                    Toast.makeText(LoginActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("first_name", first_name);
                            intent.putExtra("last_name", last_name);
                            intent.putExtra("auth_uid", auth_uid);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email,first_name,last_name");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                Toast.makeText(LoginActivity.this, "Login with Facebook Suceefully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Erorro", Toast.LENGTH_LONG).show();
            }
        });

    }

}