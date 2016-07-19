package com.fistandantilus.surprise.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.tools.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onBackPressed() {
        database.getReference(Const.USERS_PATH).child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue(false);
        firebaseAuth.signOut();
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_left);
    }
}
