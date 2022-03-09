package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

// UserLoader: class to store user information and perform ASync Task
public class UserLoader extends AsyncTaskLoader<String> {

    // user information
    String username;
    String password;
    String firstname;
    String lastname;
    String email;
    SQLiteDatabase db;

    // Userloader: constructor to store user information
    UserLoader(Context context, String username, String password, String firstname, String lastname, String email, SQLiteDatabase db)
    {
        super(context);
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.db = db;
    }

    // onStartLoading: method to start the ASync task
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    // loadInBackground: method to call NetworkUtils
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getUserInfo(username,password,firstname,lastname,email,db);
    }
}
