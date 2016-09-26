package com.fistandantilus.surprise.mvp.wallpapers;

import android.content.Context;

import com.fistandantilus.surprise.dao.Wallpaper;
import com.fistandantilus.surprise.mvp.model.API;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WallpapersPresenterImpl implements WallpapersPresenter {

    private WallpapersView view;

    public WallpapersPresenterImpl(WallpapersView view) {
        this.view = view;
    }


    @Override
    public void loadWallpapers(Context context, String category) {
        Observable<Wallpaper> wallpaperObserver =
                API
                        .getWallpapersByCategory(context, category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        view.showWallpapers(wallpaperObserver);
    }

    @Override
    public void makePhoto(Context context) {

    }

    @Override
    public void pickPhoto(Context context) {

    }
}
