package com.fistandantilus.surprise.mvp.main;

public interface MainView {

    void showFriendsList();

    void showWallpapersList(String friendUID);

    void showLogout();

    void showSettings();

    void attachPresenter();

    void detachPresenter();
}
