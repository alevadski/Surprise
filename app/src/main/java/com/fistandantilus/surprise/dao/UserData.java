package com.fistandantilus.surprise.dao;

import java.util.List;

public class UserData {

    private String name;
    private String email;
    private String password;
    private boolean isOnline;
    private int screenWidth;
    private int screenHeight;
    private String wallpaper;
    private String phoneNumber;
    private DateOfBirth dateOfBirth;
    private List<String> friends;

    public UserData() {

    }

    public UserData(String name, String email, String password, boolean isOnline, int screenWidth, int screenHeight, String wallpaper, String phoneNumber, DateOfBirth dateOfBirth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOnline = isOnline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallpaper = wallpaper;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public UserData(String email, String password, boolean isOnline, int screenWidth, int screenHeight, String wallpaper, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.isOnline = isOnline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallpaper = wallpaper;
        this.phoneNumber = phoneNumber;
    }

    public UserData(String name, String email, String password, boolean isOnline, int screenWidth, int screenHeight, String wallpaper, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOnline = isOnline;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallpaper = wallpaper;
        this.phoneNumber = phoneNumber;
    }

    public UserData(String name, String email, String password, int screenWidth, int screenHeight) {
        this(name, email, password, false, screenWidth, screenHeight, null, null);
    }

    public UserData(String email, String password, int screenWidth, int screenHeight, String wallpaper, boolean isOnline) {
        this(email, password, isOnline, screenWidth, screenHeight, wallpaper, null);
    }

    public UserData(String email, String password, int screenWidth, int screenHeight) {
        this(email, password, false, screenWidth, screenHeight, null, null);
    }

    public UserData(String email, String password, String phone) {
        this(email, password, false, 0, 0, null, phone);
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        if (isOnline != userData.isOnline) return false;
        if (screenWidth != userData.screenWidth) return false;
        if (screenHeight != userData.screenHeight) return false;
        if (name != null ? !name.equals(userData.name) : userData.name != null) return false;
        if (email != null ? !email.equals(userData.email) : userData.email != null) return false;
        if (password != null ? !password.equals(userData.password) : userData.password != null)
            return false;
        if (wallpaper != null ? !wallpaper.equals(userData.wallpaper) : userData.wallpaper != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(userData.phoneNumber) : userData.phoneNumber == null;

    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isOnline=" + isOnline +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", wallpaper='" + wallpaper + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
