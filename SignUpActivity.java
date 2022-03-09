package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

// SignUpActivity: class to create an account using ASync Task
public class SignUpActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    // used to get username and password
    String username = "";
    String password = "";

    // EditTexts for all user information
    EditText usernameEdit;
    EditText passwordEdit;
    EditText password1Edit;
    EditText firstnameEdit;
    EditText lastnameEdit;
    EditText emailEdit;

    // checks used for database
    boolean check = false;
    boolean check1 = true;

    // used to connect and check database
    SQLiteDatabase db;
    String query;

    // SharedPreference to add if the user is signed up correctly
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // get the edittexts
        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);
        password1Edit = findViewById(R.id.password1);
        firstnameEdit = findViewById(R.id.first);
        lastnameEdit = findViewById(R.id.last);
        emailEdit = findViewById(R.id.email);

        // support loader manager
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        // used to connect to database
        File path = getApplication().getFilesDir();
        String dbpath = path + "/" +"ProFinderDB";
        db = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.CREATE_IF_NECESSARY);

        // get shared preference and the android ID
        sp = getSharedPreferences(userPref,Context.MODE_PRIVATE);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // userSignUp: method to sign up the user if valid inputs
    public void userSignUp(View view)
    {

        // hid the keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // strings used to get EditTexts strings
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String password1 = password1Edit.getText().toString();
        String firstname = firstnameEdit.getText().toString();
        String lastname = lastnameEdit.getText().toString();
        String email = emailEdit.getText().toString();

        // the following are all the checks to see if a signup form is valid
        if(username.equals("") || username.length() <= 2){
            Toast.makeText(getApplicationContext(),"Invalid Username! Must be greater than 2 characters",Toast.LENGTH_LONG).show();
        }else if(password.length() <= 5){
            Toast.makeText(getApplicationContext(),"Invalid Password! Must be greater than 5 characters",Toast.LENGTH_LONG).show();
        }else if(!password1.equals(password)){
            Toast.makeText(getApplicationContext(),"Passwords don't match!",Toast.LENGTH_LONG).show();
        }else if(firstname.length() <= 1){
            Toast.makeText(getApplicationContext(),"Invalid First Name! Must be greater than 1 character",Toast.LENGTH_LONG).show();
        }else if(lastname.length() <= 1){
            Toast.makeText(getApplicationContext(),"Invalid Last Name! Must be greater than 1 character",Toast.LENGTH_LONG).show();
        }else if((email.length() <= 5)){
            Toast.makeText(getApplicationContext(),"Invalid E-mail",Toast.LENGTH_LONG).show();
        }else{
            check = true; // set to true if all valid
        }

        int check2 = 0; // check for database
        // if valid then execute the query
        if(check){
            query = "SELECT * FROM User";
            try {
                // check if username already exists
                Cursor c = db.rawQuery(query, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    if (username.equalsIgnoreCase(c.getString(1))) {
                        ++check2;
                        check1 = false;
                        break;
                    }
                    c.moveToNext();
                }
                // if username already exists
                if (!check1 && check2 > 0) {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                } else {
                    // check if a email already exists
                    // this is needed to be unique because Professional and User may communicate by e-mail
                    query = "SELECT * FROM UserInfo";
                    Cursor c1 = db.rawQuery(query, null);
                    c1.moveToFirst();
                    while (!c1.isAfterLast()) {
                        if (email.equalsIgnoreCase(c1.getString(3))) {
                            check1 = false;
                            break;
                        }
                        c1.moveToNext();
                    }
                }
            }catch(Exception e){
            }
            // if username doesn't exist and signup doesn't exist then start the process for the ASync Task
            if(check1){
                // add all values to the bundle
                Bundle queryBundle = new Bundle();
                queryBundle.putString("username",username);
                queryBundle.putString("password",password);
                queryBundle.putString("firstname",firstname);
                queryBundle.putString("lastname",lastname);
                queryBundle.putString("email",email);
                getSupportLoaderManager().restartLoader(0,queryBundle,this); // start ASync
            }else{
                Toast.makeText(getApplicationContext(), "E-mail already exists", Toast.LENGTH_LONG).show();
            }
        }

    }

    // back: if user presses back, then go back to MainActivity
    public void back(View view){
        finish();
    }

    // onCreateLoader: method to begin the ASync Task by calling the UserLoader class
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        // values
        String firstname = "";
        String lastname = "";
        String email = "";

        // if not null
        if (args != null) {
            // get query,type, and store type for later use
            username = args.getString("username");
            password = args.getString("password");
            firstname = args.getString("firstname");
            lastname = args.getString("lastname");
            email = args.getString("email");
        }

        // call new Weather Background Task
        return new UserLoader(this, username,password,firstname,lastname,email,db);
    }

    // onLoadFinished: method when done to get the userid and then go to the next activity
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        // if an error was found in the ASync Task
        if(data.equals("no")){
            Toast.makeText(getApplicationContext(),"Error Inserting into database",Toast.LENGTH_LONG).show();
        }else{
            // values
            int userid = 0;
            String dbUsername = "";
            String dbPassword = "";

            // query to used for database
            String query = "SELECT * FROM User";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                // get the userid based on the username and password
                dbUsername = c.getString(1);
                dbPassword = c.getString(2);
                if ((username.equalsIgnoreCase(dbUsername)) && (password.equals(dbPassword))) {
                    userid = c.getInt(0);
                    check = true;
                    break;
                }
                c.moveToNext();
            }
            // shared preference used to add the androidID and userid
            spe = sp.edit();
            spe.putInt(androidID,userid);
            spe.commit();
            // go to HomeActivity now
            Intent in = new Intent(this,HomeActivity.class);
            in.putExtra("userid",userid);
            startActivity(in);
            finish();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // required
    }
}