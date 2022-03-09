package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

// MainActivity: class to handle login page or sign up
public class MainActivity extends AppCompatActivity {

    // used for login
    EditText usernameEdit;
    EditText passwordEdit;
    String dbUsername;
    String dbPassword;

    boolean check = false; // used to check if valid inputs

    SQLiteDatabase db; // used to connect to database

    int userid = 0; // used to get userid from database

    // SharedPreference used to check if the user is already logged in
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // username and password TextViews
        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);

        // get the Shared Preference
        sp = getSharedPreferences(userPref,Context.MODE_PRIVATE);
        // get unique Android ID for the key in the Shared Preference
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // if the android ID is found then the user is already logged in
        if(sp.contains(androidID)){
            alreadyLoggedIn();
        }
    }

    // userLogIn: method to get the username and password and login the user if valid
    public void userLogIn(View view)
    {
        // hid keyboard after entered
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // string for textviews
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // if username or password is empty then return they cannot be empty
        if(username.equals("")){
            Toast.makeText(getApplicationContext(),"Username is empty!",Toast.LENGTH_LONG).show();
        }else if(password.equals("")){
            Toast.makeText(getApplicationContext(),"Password is empty!",Toast.LENGTH_LONG).show();
        }else{
            // connect to database
            File path = getApplication().getFilesDir();
            String dbpath = path + "/" +"ProFinderDB";
            db = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.CREATE_IF_NECESSARY);

            // query database checking for username and password
            String query = "SELECT * FROM User";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                dbUsername = c.getString(1);
                dbPassword = c.getString(2);
                // if found set check and get the userid
                if ((username.equalsIgnoreCase(dbUsername)) && (password.equals(dbPassword))) {
                    userid = c.getInt(0);
                    check = true;
                    break;
                }
                c.moveToNext();
            }
            // if found
            if(check){
                // add the android ID and userid to the shared preference
                spe = sp.edit();
                spe.putInt(androidID,userid);
                spe.commit();
                // go to the home screen
                Intent in = new Intent(this,HomeActivity.class);
                in.putExtra("userid",userid);
                startActivity(in);
                finish();
            }else{
                // if not found
                Toast.makeText(getApplicationContext(),"Username Password combination not found!",Toast.LENGTH_LONG).show();
            }
        }
    }

    // userSignUp: method to go to SignUpActivity if the user wants to sign up
    public void userSignUp(View view)
    {
        Intent in = new Intent(this,SignUpActivity.class);
        startActivity(in);
    }

    // alreadyLoggedIn: method to straight to home activity if the user is already logged in
    public void alreadyLoggedIn()
    {
        // get userid from Shared Preference
        int userid = 0;
        userid = sp.getInt(androidID,0);

        // go to Home Activity
        Intent in = new Intent(this,HomeActivity.class);
        in.putExtra("userid",userid);
        startActivity(in);
        finish();
    }
}