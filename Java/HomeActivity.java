package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

// HomeActivity: class for the home activity of the application
public class HomeActivity extends AppCompatActivity {

    // used to display username, first name, last name, and email
    String username;
    String firstname;
    String lastname;
    String email;

    // TextViews for those above
    TextView usernameText;
    TextView firstnameText;
    TextView lastnameText;
    TextView emailText;

    int userid = 0; // used for userid

    SQLiteDatabase db; // used to connect to database

    // SharedPreference to see if logged in
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get TextViews
        usernameText = findViewById(R.id.username);
        firstnameText = findViewById(R.id.firstname);
        lastnameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.email);

        // get sending intent and the userid extra
        Intent in = getIntent();
        userid = in.getIntExtra("userid",userid);

        // connect to database
        File path = getApplication().getFilesDir();
        String dbpath = path + "/" +"ProFinderDB";
        db = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.CREATE_IF_NECESSARY);

        // get username based on userid
        String query = "SELECT * FROM User";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // if userid found
            if(c.getInt(0) == userid){
                username = c.getString(1);
                break;
            }
            c.moveToNext();
        }
        // get first name, last name, and email
        query = "SELECT * FROM UserInfo";
        c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // if userid found
            if(c.getInt(0) == userid) {
                firstname = c.getString(1);
                lastname = c.getString(2);
                email = c.getString(3);
                break;
            }
            c.moveToNext();
        }

        // set the textviews
        usernameText.setText(username);
        firstnameText.setText("First Name: "+firstname);
        lastnameText.setText("Last Name: " +lastname);
        emailText.setText("E-mail: " +email);
    }

    // home: onclick for home button, stay on activity
    public void home(View view) {
        // do nothing, stay on page
    }

    // industry: onClick to go to the industry activity
    public void industry(View view) {
        // go to industry activity
        Intent in = new Intent(this,IndustryActivity.class);
        in.putExtra("userid",userid);
        startActivity(in);
    }

    // logout: onClick for logout button
    public void logout(View view) {
        // remove the shared preference record since logged out
        sp = getSharedPreferences(userPref, Context.MODE_PRIVATE);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        spe = sp.edit();
        spe.remove(androidID);
        spe.commit();
        // display logged out
        Toast.makeText(getApplicationContext(),"You have been logged out",Toast.LENGTH_LONG).show();
        // send back to login activity
        getIntent().removeExtra("userid");
        Intent i = new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }

    // professionalList: method to go to professionallist activity
    public void professionallist(View view)
    {
        // go to activity based on which popular industry was selected
        if(view.getId() == R.id.tutor){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Tutors");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.car){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Car Repairs");
            in.putExtra("userid",userid);
            startActivity(in);
        }else{
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Home Repairs");
            in.putExtra("userid",userid);
            startActivity(in);
        }
    }
}