package com.example.ybook;

public class BookListModel {
    private int icon;
    private String title;

    public BookListModel(String title) {
        this.title = title;
    }

    public BookListModel(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
