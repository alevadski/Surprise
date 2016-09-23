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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.mvp.main.MainPresenter;
import com.fistandantilus.surprise.mvp.main.MainPresenterImpl;
import com.fistandantilus.surprise.mvp.main.MainView;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;
import com.fistandantilus.surprise.ui.fragments.FriendsFragment;
import com.fistandantilus.surprise.ui.fragments.WallpapersFragment;

import rx.functions.Func0;

public class MainActivity extends AppCompatActivity implements MainView, FriendsFragmentInteractor {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private MainPresenter mainPresenter;
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
    public void showWallpapersList(String friendUID) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, WallpapersFragment.newInstance(friendUID), "FRAGMENT TAG")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showLogout() {
        mainPresenter.logout();

        startActivity(new Intent(this, StartActivity.class));
        finishAffinity();
    }

    @Override
    public void showSettings() {
        Toast.makeText(this, "Setting not implemented yet...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attachPresenter() {
        mainPresenter = new MainPresenterImpl(this);
    }

    @Override
    public void detachPresenter() {
        mainPresenter = null;
    }

    private void confirmLogout() {
        showConfirmDialog(getString(R.string.confirm_quit_title), getString(R.string.quit_confirm_message),
                getString(R.string.yes), getString(R.string.no), () -> {
                    showLogout();
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
        showWallpapersList(friendUID);
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
