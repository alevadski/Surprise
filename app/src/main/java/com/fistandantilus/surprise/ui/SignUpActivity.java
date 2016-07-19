package com.fistandantilus.surprise.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.DateOfBirth;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.DatePickerInteractor;
import com.fistandantilus.surprise.tools.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements DatePickerInteractor, OnCompleteListener, OnFailureListener {

    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private FrameLayout loadingLayout;
    private Button dateView;

    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    private DateOfBirth dateOfBirth;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initFirebaseFeatures();

        initUI();
    }

    private void initFirebaseFeatures() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void initUI() {
        nameView = (EditText) findViewById(R.id.sign_up_name);
        emailView = (EditText) findViewById(R.id.sign_up_email);
        passwordView = (EditText) findViewById(R.id.sign_up_password);
        passwordConfirmView = (EditText) findViewById(R.id.sign_up_password_confirm);
        dateView = (Button) findViewById(R.id.sign_up_set_date_of_birth);

        loadingLayout = (FrameLayout) findViewById(R.id.sign_up_loading_layout);

        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    public void onConfirmSignUpClick(View view) {
        loadingLayout.setVisibility(View.VISIBLE);

        userData = getAndValidateUserInput();

        if (userData == null) {
            loadingLayout.setVisibility(View.GONE);
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(userData.getEmail(), userData.getPassword())
                .addOnCompleteListener(this, this)
                .addOnFailureListener(this, this);
    }

    private UserData getAndValidateUserInput() {
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!TextUtils.equals(password, passwordConfirm)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Password`s length must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (dateOfBirth == null) {
            Toast.makeText(this, "Set your date of birth", Toast.LENGTH_SHORT).show();
            return null;
        }

        Point screenSize = getScreenResolution();

        UserData userData = new UserData(name, email, password, screenSize.x, screenSize.y);
        userData.setPassword(Util.md5(userData.getPassword()));
        userData.setDateOfBirth(dateOfBirth);


        return userData;
    }

    private void setPhoneNumber(String uid) {
        loadingLayout.setVisibility(View.GONE);
        Util.pushUserDataIntoPreferences(this, userData);

        Intent intent = new Intent(this, PhoneVerifyActivity.class);
        intent.putExtra(getString(R.string.extra_key_user_uid), uid);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_in_up, R.anim.transition_out_up);
    }

    @Override
    public void onComplete(@NonNull Task task) {

        if (task.isSuccessful()) {

            try {
                AuthResult result = (AuthResult) task.getResult(AuthResult.class);

                FirebaseUser user = result.getUser();

                DatabaseReference reference = database.getReference(Const.USERS_PATH).child(user.getUid());
                reference.setValue(userData);

                setPhoneNumber(user.getUid());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(int day, int month, int year) {
        dateOfBirth = new DateOfBirth(day, month, year);
        dateView.setText(day + "." + month + "." + year);
    }

    public void onSetDateOfBirthClick(View view) {
        DialogFragment newFragment = new DatePickerDialog();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private Point getScreenResolution() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_right);
    }
}
