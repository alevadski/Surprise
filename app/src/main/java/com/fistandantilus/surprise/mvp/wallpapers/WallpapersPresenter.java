package com.fistandantilus.surprise.mvp.wallpapers;

import android.content.Context;

public interface WallpapersPresenter {

    void loadCategories(Context context);

    void loadWallpapers(Context context, String category);

    void makePhoto(Context context);

    void pickPhoto(Context context);
}
