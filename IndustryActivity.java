package com.example.mytest.profinder;

// libraries used
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// IndustryActivity: class to display all the industries
public class IndustryActivity extends AppCompatActivity {

    // used to get userid
    int userid;

    // used for shared preference
    public static final String userPref = "UserPrefs";
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);

        // get sending intent and userid
        Intent in = getIntent();
        userid = in.getIntExtra("userid",userid);
    }

    // professionallist: onClick to go to the correct industry
    public void professionallist(View view)
    {
        // based on id, set extra and go to ProfessionalList Activity
        if(view.getId() == R.id.business){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Business");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.car){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Car Repairs");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.chef){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Chefs");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.dentist){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Dentists");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.doctor){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Doctors");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.homerepair){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Home Repairs");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.personal){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Personal Trainers");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.sport){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Sports Lessons");
            in.putExtra("userid",userid);
            startActivity(in);
        }else if(view.getId() == R.id.tutor){
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Tutors");
            in.putExtra("userid",userid);
            startActivity(in);
        }else{
            Intent in = new Intent(this,ProfessionalListActivity.class);
            in.putExtra("Profession","Other");
            in.putExtra("userid",userid);
            startActivity(in);
        }
    }

    // back: go back
    public void back(View view)
    {
        finish();
    }

    // home: onClick to go to home activity
    public void home(View view)
    {
        // go to home activity
        Intent in = new Intent(this,HomeActivity.class);
        in.putExtra("userid",userid);
        finish();
        startActivity(in);
    }

    // logout: onClick for when user wants to logout
    public void logout(View view)
    {
        // delete shared preference record since user wants to logout
        sp = getSharedPreferences(userPref, Context.MODE_PRIVATE);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        spe = sp.edit();
        spe.remove(androidID);
        spe.commit();
        // display logged out and go back to home activity
        Toast.makeText(getApplicationContext(),"You have been logged out",Toast.LENGTH_LONG).show();
        getIntent().removeExtra("userid");
        Intent i = new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }
}