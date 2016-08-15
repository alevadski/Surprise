package com.fistandantilus.surprise.mvp.main;

import com.fistandantilus.surprise.mvp.model.API;

public class MainPresenterImpl implements MainPresenter {

    private final MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void loadFriends() {

    }

    @Override
    public void detectFriends() {

    }

    @Override
    public void loadWallpapers() {

    }

    @Override
    public void sendWallpaper(String userUID, String wallpaperUID) {

    }

    @Override
    public void logout() {
        API.logout();
    }

    @Override
    public void attachWallpaperService() {

    }

    @Override
    public void deattachWallpaperService() {

    }
}
