package com.fistandantilus.surprise.mvp.main;

public interface MainView {

    void showFriendsList();

    void showWallpapersList();

    void logout();

    void showSettings();

    void attachPresenter();

    void detachPresenter();
}
