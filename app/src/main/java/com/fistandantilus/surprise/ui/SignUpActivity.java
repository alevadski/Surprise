package com.fistandantilus.surprise.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText nicknameView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private FrameLayout loadingLayout;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();

        initUI();
    }

    private void initUI() {
        nicknameView = (EditText) findViewById(R.id.sign_up_nickname);
        passwordView = (EditText) findViewById(R.id.sign_up_password);
        passwordConfirmView = (EditText) findViewById(R.id.sign_up_password_confirm);
        loadingLayout = (FrameLayout) findViewById(R.id.sign_up_loading_layout);

        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    public void onConfirmSignUpClick(View view) {
        getAndValidateUserInput();
    }

    private void pushUserData(UserData userData) {
        userData.setPassword(Util.md5(userData.getPassword()));

        Point screenSize = getScreenResolution();
        userData.setScreenWidth(screenSize.x);
        userData.setScreenHeight(screenSize.y);

        Util.pushUserDataIntoPreferences(this, userData);

        verifyPhoneNumber();
    }

    private void verifyPhoneNumber() {
        loadingLayout.setVisibility(View.GONE);
        startActivity(new Intent(this, PhoneVerifyActivity.class));
        overridePendingTransition(R.anim.transition_in_up, R.anim.transition_out_up);
    }

    private void getAndValidateUserInput() {
        String nickname = nicknameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(password, passwordConfirm)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingLayout.setVisibility(View.VISIBLE);

        final UserData userData = new UserData(nickname, password, null);

        final DatabaseReference reference = database.getReference(Const.USERS_PATH).child(userData.getNickname());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(UserData.class) != null) {
                    Toast.makeText(SignUpActivity.this, "Nickname \"" + userData.getNickname() + "\" is taken. Try another.", Toast.LENGTH_SHORT).show();
                } else {
                    pushUserData(userData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }

    private Point getScreenResolution() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
