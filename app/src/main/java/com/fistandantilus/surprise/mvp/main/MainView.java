package com.fistandantilus.surprise.mvp.main;

public interface MainView {

    public void showFriendsList();

    public void showWallpapersList();

    public void logout();

    public void showSettings();

    public void attachPresenter();

    public void detachPresenter();
}
