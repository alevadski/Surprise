package com.fistandantilus.surprise.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>, OnFailureListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


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
        passwordView = (EditText) findViewById(R.id.sign_in_password);
    }

    private void initFirebaseFeatures() {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onLoginClick(View view) {
        logUserIn();
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }

    private void logUserIn() {

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }

        password = Util.md5(password);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this)
                .addOnFailureListener(this, this);
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

    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Toast.makeText(StartActivity.this, "You logged as user " + user.getUid(), Toast.LENGTH_SHORT).show();

                database.getReference(Const.USERS_PATH).child(user.getUid()).child("online").setValue(true);

                goToMainActivity();
            }
        }
    };

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if (!task.isSuccessful()) {
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
