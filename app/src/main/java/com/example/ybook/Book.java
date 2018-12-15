package com.example.ybook;

/**
 * The class responsible for abstracting a book
 * @author Cesar Sales, David Souza, Evan Harrison
 * @version 1.0
 * @since December, 15, 2018
 */
public class Book {
    private String title;
    private String type;
    private String author;
    private String year;
    private String pages;
    private String comments;
    private boolean isRead;
    private int position;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public Book() {

    }

    /**
     * Non default constructor
     * @param title
     * @param type
     * @param author
     * @param year
     * @param pages
     * @param comments
     */
    public Book(String title, String type, String author,
                String year, String pages, String comments) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.comments = comments;
    }

    /**
     * Non default constructor
     * @param title
     * @param type
     * @param author
     * @param year
     * @param pages
     * @param comments
     * @param isRead
     * @param position
     */
    public Book(String title, String type, String author,
                String year, String pages, String comments, boolean isRead, int position) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.comments = comments;
        this.isRead = isRead;
        this.position = position;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getPages() {
        return pages;
    }
    public void setPages(String pages) {
        this.pages = pages;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
