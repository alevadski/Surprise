package com.fistandantilus.surprise.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneVerifyActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private EditText phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        database = FirebaseDatabase.getInstance();

        initUI();
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

        DatabaseReference reference = database.getReference(Const.USERS_PATH).child(userData.getNickname());
        reference.setValue(userData);

        done();
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
