package com.fistandantilus.surprise.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fistandantilus.surprise.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_left);
    }
}
