package com.fistandantilus.surprise.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.mvp.model.API;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneVerifyActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private EditText phoneView;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        Bundle extras = getIntent().getExtras();
        uid = extras.getString(getString(R.string.extra_key_user_uid));

        if (uid == null) {
            throw new IllegalStateException("UID is null");
        }

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
        verifyPhoneNumber();
    }

    private void verifyPhoneNumber() {
        UserData userData = Util.getUserDataPhomPreference(this);

        //TODO TEMPORARY! HERE MUST BE PHONE NUMBER VERIFICATION AND VALIDATION!!!
        String phoneNumber = phoneView.getText().toString();

        API
                .isPhoneNumberFree(this, phoneNumber)
                .subscribe(valid -> {
                    if (valid) {
                        userData.setPhoneNumber(phoneNumber);
                        database.getReference(Const.USERS_PATH).child(uid).child(Const.PHONE_PATH).setValue(phoneNumber).addOnCompleteListener(this, task -> done());
                    } else {
                        Toast.makeText(this, "This phone number is taken!", Toast.LENGTH_SHORT).show();
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
