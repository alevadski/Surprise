package com.fistandantilus.surprise.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.fistandantilus.surprise.MyService;
import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.UserData;
import com.fistandantilus.surprise.tools.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

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
        nicknameView = (EditText) findViewById(R.id.nickname_edittext);
        passwordView = (EditText) findViewById(R.id.password_edittext);
    }

    public void onLoginClick(View view) {

//        String nickname = nicknameView.getText().toString();
//        String password = passwordView.getText().toString();
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(getString(R.string.preference_nickname_key), nickname);
//        editor.putString(getString(R.string.preference_password_key), Util.md5(password));
//        editor.apply();
//
//        DatabaseReference reference = database.getReference(nickname);
//
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//        UserData userData = new UserData(nickname, Util.md5(password), width, height);
//        reference.setValue(userData);
//
//        Intent serviceIntent = new Intent(this, MyService.class);
//        startService(serviceIntent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onSignUpClick(View view) {

    }
}
