package com.fistandantilus.surprise.mvp.main;

public interface MainPresenter {

    public void loadFriends();

    public void detectFriends();

    public void loadWallpapers();

    public void sendWallpaper(String userUID, String wallpaperUID);

    public void attachWallpaperService();

    public void deattachWallpaperService();
}
