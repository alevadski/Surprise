package com.fistandantilus.surprise.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fistandantilus.surprise.R;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    private EditText nicknameView;
    private EditText passwordView;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();

        initUI();
    }

    private void initUI() {
        nicknameView = (EditText) findViewById(R.id.sign_in_nickname);
        passwordView = (EditText) findViewById(R.id.sing_in_password);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_left);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
