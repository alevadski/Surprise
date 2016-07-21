package com.fistandantilus.surprise.dao;

public class Wallpaper {
    private String link;
    private int width;
    private int height;

    public Wallpaper() {
    }

    public Wallpaper(String link, int width, int height) {
        this.link = link;
        this.width = width;
        this.height = height;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallpaper wallpaper = (Wallpaper) o;

        if (width != wallpaper.width) return false;
        if (height != wallpaper.height) return false;
        return link.equals(wallpaper.link);

    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}