package com.fistandantilus.surprise.mvp.main;

public interface MainPresenter {

    void loadFriends();

    void detectFriends();

    void loadWallpapers();

    void sendWallpaper(String userUID, String wallpaperUID);

    void logout();

    void attachWallpaperService();

    void deattachWallpaperService();
}
