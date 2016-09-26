package com.fistandantilus.surprise;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WallpaperChangerService extends Service {

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

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.USERS_PATH).child(userUID);

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
