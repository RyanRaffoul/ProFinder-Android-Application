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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

// ProfessionalListActivity: class to display professionals of a specific industry
public class ProfessionalListActivity extends AppCompatActivity {

    // used for userid and profession
    int userid = 0;
    String profession = "";

    // textview for profession title
    TextView tw;

    // used to connect to database
    SQLiteDatabase db;

    // used for name, star rating, and count
    String name;
    int rating;
    int ratingCount;

    // Views to display information
    TextView personName;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    TextView personRatingCount;

    // Views used display information for a 2nd professional
    TextView personName1;
    ImageView star11;
    ImageView star21;
    ImageView star31;
    ImageView star41;
    ImageView star51;
    TextView personRatingCount1;

    // used to shared preference
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_list);

        tw = findViewById(R.id.profession); // get profession id

        // get userid from intent
        Intent in = getIntent();
        userid = in.getIntExtra("userid", userid);

        // set profession
        profession = in.getStringExtra("Profession");
        tw.setText(profession);

        // connect to database
        File path = getApplication().getFilesDir();
        String dbpath = path + "/" + "ProFinderDB";
        db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        // get all View ids
        personName = findViewById(R.id.personName);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        personRatingCount = findViewById(R.id.personRatingCount);

        // get all View ids
        personName1 = findViewById(R.id.personName1);
        star11 = findViewById(R.id.star11);
        star21 = findViewById(R.id.star21);
        star31 = findViewById(R.id.star31);
        star41 = findViewById(R.id.star41);
        star51 = findViewById(R.id.star51);
        personRatingCount1 = findViewById(R.id.personRatingCount1);

        setPeople(); // method to set information
    }

    // back: onClick to go back
    public void back(View view) {
        finish();
    }

    // industry: onClick to go to industry activity
    public void industry(View view) {
        // go to industry activity
        Intent in = new Intent(this, IndustryActivity.class);
        in.putExtra("userid", userid);
        getIntent().removeExtra("Profession");
        finish();
        startActivity(in);
    }

    // home: onClick to go to home activity
    public void home(View view) {
        // go to home activity
        Intent in = new Intent(this, HomeActivity.class);
        in.putExtra("userid", userid);
        getIntent().removeExtra("Profession");
        finish();
        startActivity(in);
    }

    // logout: onClick when user wants to logout
    public void logout(View view) {
        // delete the shared preference record when user wants to logout
        sp = getSharedPreferences(userPref, Context.MODE_PRIVATE);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        spe = sp.edit();
        spe.remove(androidID);
        spe.commit();
        Toast.makeText(getApplicationContext(), "You have been logged out", Toast.LENGTH_LONG).show(); // display logged out
        // go to main activity
        getIntent().removeExtra("userid");
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    // setPeople: method to display a professionals information
    public void setPeople() {
        // since at this point, there is only 2 professionals for each industry the below code will do
        // if a new professional is added this will all need to be in loops
        int i = 1;
        // used to get professionals
        String query = "SELECT * FROM Professional";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // find selected profession
            if (c.getString(2).equalsIgnoreCase(profession)) {
                // get name, rating, and rating count
                name = c.getString(3) + " " + c.getString(4);
                rating = c.getInt(9);
                ratingCount = c.getInt(10);
                // if 1st professional
                if (i == 1) {
                    personName.setText(name); // set name
                    // set star rating
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
                    personRatingCount.setText(ratingCount + " Reviews"); // set reviews
                    ++i; // go to 2nd professional
                }else{
                    // set the next professional name
                    personName1.setText(name);
                    // set the star rating
                    if (rating == 1) {
                        star11.setImageResource(R.drawable.ic_star);
                        star21.setImageResource(R.drawable.ic_star1);
                        star31.setImageResource(R.drawable.ic_star1);
                        star41.setImageResource(R.drawable.ic_star1);
                        star51.setImageResource(R.drawable.ic_star1);
                    } else if (rating == 2) {
                        star11.setImageResource(R.drawable.ic_star);
                        star21.setImageResource(R.drawable.ic_star);
                        star31.setImageResource(R.drawable.ic_star1);
                        star41.setImageResource(R.drawable.ic_star1);
                        star51.setImageResource(R.drawable.ic_star1);
                    } else if (rating == 3) {
                        star11.setImageResource(R.drawable.ic_star);
                        star21.setImageResource(R.drawable.ic_star);
                        star31.setImageResource(R.drawable.ic_star);
                        star41.setImageResource(R.drawable.ic_star1);
                        star51.setImageResource(R.drawable.ic_star1);
                    } else if (rating == 4) {
                        star11.setImageResource(R.drawable.ic_star);
                        star21.setImageResource(R.drawable.ic_star);
                        star31.setImageResource(R.drawable.ic_star);
                        star41.setImageResource(R.drawable.ic_star);
                        star51.setImageResource(R.drawable.ic_star1);
                    } else {
                        star11.setImageResource(R.drawable.ic_star);
                        star21.setImageResource(R.drawable.ic_star);
                        star31.setImageResource(R.drawable.ic_star);
                        star41.setImageResource(R.drawable.ic_star);
                        star51.setImageResource(R.drawable.ic_star);
                    }
                    personRatingCount1.setText(ratingCount + " Reviews"); // set reviews
                    break;
                }
            }
            c.moveToNext();
        }
    }

    // one: if 1st professional clicked
    public void one(View view)
    {
        // go to profession activity
        String aa = personName.getText().toString();
        Intent in = new Intent(this,ProfessionalActivity.class);
        in.putExtra("userid",userid);
        in.putExtra("name",aa);
        in.putExtra("Profession",profession);
        startActivity(in);
    }

    // two: if 2nd professional clicked
    public void two(View view)
    {
        // go to profession activity
        String bb = personName1.getText().toString();
        Intent in = new Intent(this,ProfessionalActivity.class);
        in.putExtra("userid",userid);
        in.putExtra("name",bb);
        in.putExtra("Profession",profession);
        startActivity(in);
    }

}