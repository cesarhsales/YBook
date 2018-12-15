package com.example.ybook;

/**
 * Class responsible for defining the books list model
 * @author Cesar Sales
 * @version 1.0
 * @since December, 15, 2018
 */
public class BookListModel {
    private int icon;
    private String title;

    /**
     * Non default constructor
     * @param title
     */
    public BookListModel(String title) {
        this.title = title;
    }

    /**
     * Non default constructor
     * @param icon
     * @param title
     */
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
