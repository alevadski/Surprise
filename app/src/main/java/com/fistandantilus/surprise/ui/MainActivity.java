package com.fistandantilus.surprise.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.tools.Util;
import com.fistandantilus.surprise.ui.fragments.FriendsFragment;
import com.fistandantilus.surprise.ui.fragments.WallpapersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String userUID;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initUI();

        createWallpapers();

    }

    private void createWallpapers() {

        String categories[] = {
                "3D",
                "Animal",
                "Christmas",
                "Cute",
                "Financial",
                "Flowers",
                "Food & Drinks",
                "Love",
                "Music",
                "Sports",

        };

        String images[] = {
                ""
        };

        Wallpaper tempPaper = null;

        DatabaseReference reference = database.getReference().child("wallpapers").child("categories").child("");

        Wallpaper wallpaper = null;

        for (String image : images) {
            wallpaper = new Wallpaper(image, 1080, 1920);
            reference.child(Util.md5(wallpaper.getLink() + ":" + wallpaper.getWidth() + ":" + wallpaper.getWidth())).setValue(wallpaper);
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
                setTitle(userData.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WallpapersFragment(), "WALLPAPERS");
        adapter.addFragment(new FriendsFragment(), "FRIENDS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        database.getReference(Const.USERS_PATH).child(firebaseAuth.getCurrentUser().getUid()).child("online").setValue(false);
        firebaseAuth.signOut();
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_left);
    }
}

class Wallpaper {
    private String link;
    private int width;
    private int height;

    public Wallpaper() {
    }

    public Wallpaper(String link, int width, int height) {
        this.link = link;
        this.width = width;
        this.height = height;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallpaper wallpaper = (Wallpaper) o;

        if (width != wallpaper.width) return false;
        if (height != wallpaper.height) return false;
        return link.equals(wallpaper.link);

    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}