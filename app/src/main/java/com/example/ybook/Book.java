package com.example.ybook;

public class Book {
    private String title;
    private String type;
    private String author;
    private int year;
    private int pages;
    private String comments;
    private boolean isRead;
    private int position;

    public Book() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Book(String title, String type, String author,
                int year, int pages, String comments) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.comments = comments;
    }

    public Book(String title, String type, String author,
                int year, int pages, String comments, boolean isRead, int position) {
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
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
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
