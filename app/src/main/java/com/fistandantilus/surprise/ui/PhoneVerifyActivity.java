package com.fistandantilus.surprise.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneVerifyActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private EditText phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        initFirebaseFeatures();

        initUI();
    }

    private void initFirebaseFeatures() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void initUI() {
        phoneView = (EditText) findViewById(R.id.phone_verify_phone);
    }

    public void onVerifyClick(View view) {
        pushUserDataToFirebase();
    }

    private void pushUserDataToFirebase() {
        UserData userData = Util.getUserDataPhomPreference(this);

        //TODO TEMPORARY!
        String phoneNumber = phoneView.getText().toString();
        userData.setPhoneNumber(phoneNumber);

//        DatabaseReference reference = database.getReference(Const.USERS_PATH).child(userData.getEmail());
//        reference.setValue(userData);

        createUserAccount(userData);
    }

    private void createUserAccount(UserData userData) {

        firebaseAuth.createUserWithEmailAndPassword(userData.getEmail(), userData.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            done();
                        }

                    }
                });

    }

    private void done() {
        startActivity(new Intent(this, SignUpDoneActivity.class));
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_down, R.anim.transition_out_down);
        finish();
    }

}
