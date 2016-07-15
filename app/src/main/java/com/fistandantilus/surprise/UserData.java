package com.fistandantilus.surprise;

public class UserData {
    private String nickname;
    private String password;
    private boolean isOnline;
    private int screenWidth;
    private int screenHeight;
    private String wallpaper;

    private String phoneNumber;

    public UserData() {

    }

    public UserData(String nickname, String password, boolean isOnline, int screenWidth, int screenHeight, String wallpaper, String phoneNumber) {
        this.nickname = nickname;
        this.password = password;
        this.isOnline = isOnline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallpaper = wallpaper;
        this.phoneNumber = phoneNumber;
    }

    public UserData(String nickname, String password, int screenWidth, int screenHeight, String wallpaper, boolean isOnline) {
        this(nickname, password, isOnline, screenWidth, screenHeight, wallpaper, null);
    }

    public UserData(String nickname, String password, int screenWidth, int screenHeight) {
        this(nickname, password, false, screenWidth, screenHeight, null, null);
    }

    public UserData(String nickname, String password, String phone) {
        this(nickname, password, false, 0, 0, null, phone);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
