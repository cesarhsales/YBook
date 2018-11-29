package com.example.ybook;

public class Book {
    private String comments;
    private String title;
    private String type;
    private String author;
    private String year;
    private String pages;
    private boolean isRead;
    // private int position;

    // Constructor

    public Book(String title, String type, String author,
                String year, String pages, String comments){//boolean isRead, int position) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.comments = comments;
        //this.isRead = isRead;
        //this.position = position;
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
    //public int getPosition() { return position; }
    //public void setPosition(int position) { this.position = position; }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

}
