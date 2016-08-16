package com.fistandantilus.surprise.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.mvp.main.MainPresenter;
import com.fistandantilus.surprise.mvp.main.MainPresenterImpl;
import com.fistandantilus.surprise.mvp.main.MainView;
import com.fistandantilus.surprise.mvp.model.API;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;
import com.fistandantilus.surprise.ui.fragments.FriendsFragment;
import com.fistandantilus.surprise.ui.fragments.WallpapersFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainView, FriendsFragmentInteractor {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private MainPresenter presenter;
    private FragmentManager fragmentManager;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        showFriendsList();
    }

    private void initUI() {
        fragmentManager = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager.addOnBackStackChangedListener(this::onBackStackChanged);
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachPresenter();
        checkPermissions();

        testAPI();

    }

    private void testAPI() {
        API
                .getAllContactsID(this)
                .flatMap(contactID -> API.getPhoneNumbersFromContactByID(this, contactID))
                .flatMap(API::getUserUIDByPhoneNumber)
                .filter(userUID -> userUID != null && !userUID.isEmpty())
                .flatMap(API::getUserDataByUid)
                .filter(userData -> userData != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userData -> Log.d("FRIENDS", userData.toString()));
    }


    @Override
    public void showFriendsList() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, new FriendsFragment(), "FRAGMENT TAG")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showWallpapersList() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, new WallpapersFragment(), "FRAGMENT TAG")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        presenter.logout();

        startActivity(new Intent(this, StartActivity.class));
        finishAffinity();
    }

    @Override
    public void showSettings() {
        Toast.makeText(this, "Setting not implemented yet...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attachPresenter() {
        presenter = new MainPresenterImpl(this);
    }

    @Override
    public void detachPresenter() {
        presenter = null;
    }

    private void confirmLogout() {
        showConfirmDialog(getString(R.string.confirm_quit_title), getString(R.string.quit_confirm_message),
                getString(R.string.yes), getString(R.string.no), () -> {
                    logout();
                    return null;
                });
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else confirmLogout();
    }

    private void showConfirmDialog(String title, String message, String positiveCase, String negativeCase, Func0 workToDo) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveCase, (dialog, which) -> workToDo.call())
                .setNegativeButton(negativeCase, null)
                .setCancelable(true);

        alertBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_main_settings:
                showSettings();
                break;
        }

        return false;
    }

    @Override
    public void onFriendSelected(String friendUID) {
        Log.d("MAIN", "onFriendSelected");
        showWallpapersList();
    }

    private void onBackStackChanged() {

        Fragment fragment = fragmentManager.findFragmentByTag("FRAGMENT TAG");

        if (fragment == null) return;

        if (fragment instanceof FriendsFragment) {
            setTitle(R.string.choose_friend);
        }

        if (fragment instanceof WallpapersFragment) {
            setTitle(R.string.choose_wallpaper);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachPresenter();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Granted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
