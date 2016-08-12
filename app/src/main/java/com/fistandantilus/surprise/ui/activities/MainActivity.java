package com.fistandantilus.surprise.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.mvp.main.MainView;
import com.fistandantilus.surprise.mvp.model.UsersManager;

public class MainActivity extends AppCompatActivity implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsersManager
                .getUserDataByUid("0sXcR4NiJfUort23RlBj8Z8FJza2")
                .doOnNext(userData -> Log.d("USER", userData.toString()))
                .doOnError(error -> Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show())
                .take(5)
                .subscribe(userData -> Toast.makeText(this, "Retrieved user data for name \"" + userData.getName() + "\"", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showFriendsList() {

    }

    @Override
    public void showWallpapersList() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void showSettings() {

    }
}
