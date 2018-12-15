package com.example.ybook.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ybook.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Class responsible for providing a way to save or retrieve user information from shared preferences
 */
public class SharedPreferencesCommon {
    public static void saveUserInSharedPreferences(User user,
                                                   Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("USER", MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user); // myObject - instance of MyObject
        prefsEditor.putString("User", json);
        prefsEditor.commit();
    }

    public static User readUserInSharedPreferences(Context context, String key) {
        SharedPreferences mPrefs = context.getSharedPreferences("USER", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString(key, "");
        User user = gson.fromJson(json, User.class);

        return user;
    }
}
