package com.sophie.miller.portablecloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sophie.miller.portablecloset.monitoring.Firebase;
import com.sophie.miller.portablecloset.utils.Notifications;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_INTERNET = 5;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        Firebase.create(this);
        Firebase.logToFirebase("user opened the app", FirebaseAnalytics.Event.APP_OPEN);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                Notifications.makeToast(this, "Sorry, we cannot load the login without internet permission.");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSIONS_REQUEST_INTERNET);
            }
        } else {
            //todo sign in
        }
    }

}
