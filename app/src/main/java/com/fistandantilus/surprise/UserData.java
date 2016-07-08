package com.fistandantilus.surprise;

public class UserData {
    private String nickname;
    private String password;
    private boolean isOnline;
    private int screenWidth;
    private int screenHeight;
    private String wallpaper;

    public UserData(String nickname, String password, int screenWidth, int screenHeight, String wallpaper, boolean isOnline) {
        this.nickname = nickname;
        this.password = password;
        this.screenWidth = screenWidth;
        this.isOnline = isOnline;
        this.screenHeight = screenHeight;
        this.wallpaper = wallpaper;
    }

    public UserData(String nickname, String password, int screenWidth, int screenHeight) {
        this.nickname = nickname;
        this.password = password;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallpaper = "none";
        this.isOnline = false;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }
}
