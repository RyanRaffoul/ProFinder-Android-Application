package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

// ProfessionalActivity: class used to display a Professionals information and allow user to email them
public class ProfessionalActivity extends AppCompatActivity {

    // get userid from previous activity
    int userid = 0;
    // used for professional information
    String professionalName;
    String profession;

    // Views to display information
    TextView proName;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    TextView review;
    TextView emailName;
    TextView ageName;
    TextView cityName;
    TextView provinceName;
    Button emailButton;

    SQLiteDatabase db; // used to connect to database

    // used for sharedpreference
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);

        // get userid, professional name, and profession from previous activities
        Intent in = getIntent();
        userid = in.getIntExtra("userid",userid);
        professionalName = in.getStringExtra("name");
        profession = in.getStringExtra("Profession");

        // get all the views
        proName = findViewById(R.id.professionalname);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        review = findViewById(R.id.reviewcount);
        emailName = findViewById(R.id.email);
        ageName = findViewById(R.id.age);
        cityName = findViewById(R.id.city);
        provinceName = findViewById(R.id.province);
        emailButton = findViewById(R.id.emailButton);

        // connect to database
        File path = getApplication().getFilesDir();
        String dbpath = path + "/" +"ProFinderDB";
        db = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.CREATE_IF_NECESSARY);

        setValues(); // set all professional information
    }

    // back: go to previous activity if pressed
    public void back(View view)
    {
        finish();
    }

    // industry: onClick to go to industry activity
    public void industry(View view)
    {
        // go to industry activity, delete extras
        Intent in = new Intent(this, IndustryActivity.class);
        in.putExtra("userid", userid);
        getIntent().removeExtra("Profession");
        getIntent().removeExtra("name");
        finish();
        startActivity(in);
    }

    // home: onClick to go to home activity
    public void home(View view)
    {
        // go to home activity, delete extras
        Intent in = new Intent(this, HomeActivity.class);
        in.putExtra("userid", userid);
        getIntent().removeExtra("Profession");
        getIntent().removeExtra("name");
        finish();
        startActivity(in);
    }

    // logout: onClick to logout
    public void logout(View view)
    {
        // delete shared preference since logged out
        sp = getSharedPreferences(userPref, Context.MODE_PRIVATE);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        spe = sp.edit();
        spe.remove(androidID);
        spe.commit();
        Toast.makeText(getApplicationContext(), "You have been logged out", Toast.LENGTH_LONG).show(); // display logged out
        // go back to the main activity
        getIntent().removeExtra("userid");
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    // setValues: method to set all the Professional information
    public void setValues()
    {
        proName.setText(professionalName); // set name

        // get first and last name
        String[] names = professionalName.split(" ");
        String fname = names[0];
        String lname = names[1];

        // professional information
        int rating = 0;
        int count = 0;
        String email = "";
        int age = 0;
        String city = "";
        String province = "";

        // get all user information
        String query = "SELECT * FROM Professional";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // if found
            if( (c.getString(3).equalsIgnoreCase(fname)) && (c.getString(4).equalsIgnoreCase(lname)) && (c.getString(2).equals(profession))){
                rating = c.getInt(9);
                count = c.getInt(10);
                email = c.getString(5);
                age = c.getInt(6);
                city = c.getString(7);
                province = c.getString(8);
            }
            c.moveToNext();
        }
        // set the star rating based on the Professionals rating
        if (rating == 1) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star1);
            star3.setImageResource(R.drawable.ic_star1);
            star4.setImageResource(R.drawable.ic_star1);
            star5.setImageResource(R.drawable.ic_star1);
        } else if (rating == 2) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star1);
            star4.setImageResource(R.drawable.ic_star1);
            star5.setImageResource(R.drawable.ic_star1);
        } else if (rating == 3) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star1);
            star5.setImageResource(R.drawable.ic_star1);
        } else if (rating == 4) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star1);
        } else {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star);
        }
        // set the other information
        review.setText(count + " Reviews");
        emailName.setText("E-mail: " +email);
        ageName.setText("Age: "+age);
        cityName.setText("City: " +city);
        provinceName.setText("Province: " +province);
    }

    // emailPro: method to email the Professional you are interested in hiring them
    public void emailPro(View view)
    {
        // display toast
        Toast.makeText(getApplicationContext(),"Professional has been e-mailed that you want to hire them! They will contact you if interested!",Toast.LENGTH_LONG).show();
        // email intent
        Intent in = new Intent(Intent.ACTION_SEND);
        // send email
        // Just included my email as a sample since all the emails I used for the Professional database are not actual emails just samples
        in.setType("plain/type");
        in.putExtra(Intent.EXTRA_EMAIL,new String[]{"ryanraffoul1@hotmail.com"});
        in.putExtra(Intent.EXTRA_SUBJECT,"ProFinder Job Request");
        in.putExtra(Intent.EXTRA_TEXT,"A user on ProFinder is interested in hiring you for a job! Please respond if interested");
        if(in.resolveActivity(getPackageManager())!= null){
            startActivity(Intent.createChooser(in,""));
        }
    }

}