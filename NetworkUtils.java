package com.example.mytest.profinder;

// libraries used
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// NetworkUtils: class used to add the information into the database
// Reason for this ASync Task is this is the only spot in the app where a users information will be inputted
public class NetworkUtils
{

    // getUserInfo: method to add the users signup information into the database
    public static String getUserInfo(String username,String password,String firstname,String lastname,String email,SQLiteDatabase db)
    {
        // used for query and userid
        String query = "";
        int userid = 0;

        // begin
        db.beginTransaction();
        try{
            // insert username and password into Table User
            query = "INSERT INTO User(Username,Password) VALUES" +"('" +username+"',"+"'"+password+"')";
            // execute query
            db.execSQL(query);
            db.setTransactionSuccessful();

            // get the userid to return to the calling class
            query = "SELECT * FROM User";
            Cursor c = db.rawQuery(query,null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(username.equalsIgnoreCase(c.getString(1))){
                    userid = c.getInt(0);
                    break;
                }
                c.moveToNext();
            }

            // insert userinfo into database
            query = "INSERT INTO UserInfo(ID,Firstname,Lastname,Email) VALUES(" +userid +",'"+firstname+"','" +lastname+"','"+email+"')";
            db.execSQL(query);
        }catch(SQLException e){
            // if error
            return "no";
        }catch(Exception e){
            // if error
            return "no";
        }finally {
            // end the transaction
            db.endTransaction();
        }
        // return userid as string
        String r = "" +userid;
        return r;
    }

}
