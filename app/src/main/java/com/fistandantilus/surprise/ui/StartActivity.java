package com.fistandantilus.surprise.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fistandantilus.surprise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText emailView;

    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initFirebaseFeatures();
        initUI();
    }

    private void initUI() {
        emailView = (EditText) findViewById(R.id.sign_in_nickname);
        passwordView = (EditText) findViewById(R.id.sing_in_password);
    }

    private void initFirebaseFeatures() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {

                }
            }

        };
    }

    public void onLoginClick(View view) {
        logUserIn();

        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }

    private void logUserIn() {

    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_left);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
