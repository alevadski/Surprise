package com.fistandantilus.surprise.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;
import com.fistandantilus.surprise.ui.fragments.FriendsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FriendsFragmentInteractor {

    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 111;
    public static final String FRIENDS_FRAGMENT_TAG = "friends_fragment";
    public static final String WALLPAPERS_FRAGMENT_TAG = "wallpapers_fragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private String userUID;
    private UserData userData;

    private FrameLayout mainContainer;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestNeededPermission();
    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userUID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference reference = database.getReference(Const.USERS_PATH).child(userUID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initUI() {
        bindViews();

        fragmentManager = getSupportFragmentManager();

        FriendsFragment friendsFragment = (FriendsFragment) fragmentManager.findFragmentByTag(FRIENDS_FRAGMENT_TAG);

        if (friendsFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_container, new FriendsFragment(), FRIENDS_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    private void bindViews() {
        mainContainer = (FrameLayout) findViewById(R.id.main_container);
    }

    private void signOut() {
        String dialogTitle = getString(R.string.confirm_quit_title);
        String dialogMessage = getString(R.string.quit_confirm_message);
        String yes = getString(R.string.yes);
        String no = getString(R.string.no);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference(Const.USERS_PATH).child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue(false);
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, StartActivity.class));
                        finishAffinity();
                    }
                })
                .setNegativeButton(no, null)
                .setCancelable(true);

        alertBuilder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        signOut();
    }

    @Override
    public void onFriendSelected(String friendUID) {
        Toast.makeText(this, friendUID, Toast.LENGTH_SHORT).show();
    }
}