package com.fistandantilus.surprise;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {

    private String currentLink;
    private UserData userData;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("SERVICE", "ON START COMMAND");


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("SERVICE", "SERVICE STARTED!");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String nickname = preferences.getString(getString(R.string.preference_nickname_key), null);
        Log.d("SERVICE", "NICKNAME = " + nickname);


        DatabaseReference reference = database.getReference(nickname);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                currentLink = userData.getWallpaper();
                Log.d("SERVICE", "DATA CHANGE: " + currentLink);
                new Thread(operateLink).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SERVICE", "CANCELED!");
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setWallpaper(String link) {
        Log.d("SERVICE", "SETTING WALLPAPER!");

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.d("SERVICE", "WINDOW SIZE:");
        Log.d("SERVICE", "WIDTH = " + width);
        Log.d("SERVICE", "HEIGHT = " + height);


        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap image = Glide.with(this).load(link).asBitmap().into(width, height).get();

            Log.d("SERVICE", "BITMAP LOADED!");

            myWallpaperManager.setBitmap(image);
        } catch (Exception e) {
            Log.d("SERVICE", "EXCEPTION: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "DESTROYED!");

    }

    Runnable operateLink = new Runnable() {
        @Override
        public void run() {
            setWallpaper(currentLink);
        }
    };

}
