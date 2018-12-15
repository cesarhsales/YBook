package com.example.ybook;

import android.media.Image;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Class responsible for representing the user
 * @author Cesar Sales, David Souza, Evan Harrison
 * @version 1.0
 * @since December, 15, 2018
 */
@IgnoreExtraProperties
public class User {
    private String username;
    private String email;
    private Image profileImage;
    private List<Book> books;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public User() {

    }

    /**
     * Non default constructor
     * @param username
     * @param email
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Non default constructor
     * @param username
     * @param email
     * @param books
     */
    public User(String username, String email, List<Book> books) {
        this.username = username;
        this.email = email;
        this.books = books;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}
