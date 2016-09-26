package com.fistandantilus.surprise.mvp.wallpapers;

import com.fistandantilus.surprise.dao.Wallpaper;

import rx.Observable;

public interface WallpapersView {

    void showWallpapers(Observable<Wallpaper> wallpapers);

    void showEmptyView();

    void showLoading();
}
