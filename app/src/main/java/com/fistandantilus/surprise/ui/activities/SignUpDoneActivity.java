package com.fistandantilus.surprise.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fistandantilus.surprise.R;

public class SignUpDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_done);
    }

    public void onContinueClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
        overridePendingTransition(R.anim.transition_in_down, R.anim.transition_out_down);
        finish();
    }
}
