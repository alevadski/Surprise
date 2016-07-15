package com.fistandantilus.surprise.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText nicknameView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private EditText phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();

    }

    private void initUI() {
        nicknameView = (EditText) findViewById(R.id.sign_up_nickname);
        passwordView = (EditText) findViewById(R.id.sign_up_password);
        passwordConfirmView = (EditText) findViewById(R.id.sign_up_password_confirm);
        phoneView = (EditText) findViewById(R.id.sign_up_phone);
    }

    public void onConfirmSignUpClick(View view) {

        UserData userData = getAndValidateUserInput();

        if (userData != null) {
            signUserUp(userData);
        } else {
            Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUserUp(UserData userData) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference(Const.USERS_PATH).child(userData.getNickname());

        userData.setPassword(Util.md5(userData.getPassword()));

        reference.setValue(userData);

        onBackPressed();
    }

    private UserData getAndValidateUserInput() {
        String nickname = nicknameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();
        String phone = phoneView.getText().toString();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordConfirm) || TextUtils.isEmpty(phone)
                || !TextUtils.equals(password, passwordConfirm)) {
            return null;
        }

        return new UserData(nickname, password, phone);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }
}
