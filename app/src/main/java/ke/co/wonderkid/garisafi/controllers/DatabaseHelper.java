package ke.co.wonderkid.garisafi.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ben on 5/30/2016.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    //DATABASE VARIABLES
    public static final String DATABASE_NAME = "GS_DB";
    public static final String DEVICE_INFO_TABLE = "user_table";
    public static final String MAKE_TABLE= "make_table";
    public static final String MODEL_TABLE= "model_table";
    public static final String YEAR_TABLE= "year_table";
    public static final String FEATURES_TABLE= "feature_table";
    public static final String SOLD_TABLE= "sold_table";
    public static final String LIKES_TABLE= "likes_table";

    //DEVICE REGISTRATION VARIABLES
    public static final String COL_1DEVICE = "user_id";
    public static final String COL_2DEVICE = "mobile_no1";
    public static final String COL_3DEVICE = "user_fname";
    public static final String COL_4DEVICE = "user_email";

    //MAKE VARIABLES
    public static final String COL_1MAKE = "idtbl_carmake";
    public static final String COL_2MAKE = "make_name";


    //MODEL VARIABLES
    public static final String COL_1MODEL = "idtbl_carmodel";
    public static final String COL_2MODEL = "tbl_carmake_idtbl_carmake";
    public static final String COL_3MODEL = "model_name";

    //MAKE VARIABLES
    public static final String COL_1YEAR = "idtbl_car_year";
    public static final String COL_2YEAR = "year_name";


    //FEATURES VARIABLE
    public static final String COL_1FEATURE= "idtbl_feature";
    public static final String COL_2FEATURE = "feature_group";
    public static final String COL_3FEATURE = "feature_name";


    //SOLD CARS
    public static final String COL_1SOLD = "sold_cars";
    public static final String COL_2SOLD = "sold_user_id";

    //MY LIKES
    public static final String COL_1LIKES= "car_likes";
    public static final String COL_2LIKES = "liked_user_id";



    //working local variables
    Context ctx;
    String TOKEN_MAKE_ID;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.ctx = context;


    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + DEVICE_INFO_TABLE + " (user_id INTEGER NOT NULL DEFAULT 0,user_fname TEXT NOT NULL DEFAULT '',mobile_no1 TEXT NOT NULL DEFAULT '',user_email TEXT NOT NULL DEFAULT '')");
        db.execSQL("create table " + MAKE_TABLE + " (idtbl_carmake INTEGER NOT NULL DEFAULT 0,make_name TEXT NOT NULL DEFAULT '')");
        db.execSQL("create table " + MODEL_TABLE + " (idtbl_carmodel INTEGER NOT NULL DEFAULT 0,tbl_carmake_idtbl_carmake INTEGER NOT NULL DEFAULT 0,model_name TEXT NOT NULL DEFAULT '' )");
        db.execSQL("create table " + YEAR_TABLE + " (idtbl_car_year INTEGER NOT NULL DEFAULT 0,year_name TEXT NOT NULL DEFAULT '')");
        db.execSQL("create table " + FEATURES_TABLE + " (idtbl_feature INTEGER NOT NULL DEFAULT 0,feature_group INTEGER NOT NULL DEFAULT 0,feature_name TEXT NOT NULL DEFAULT '')");
        db.execSQL("create table " + SOLD_TABLE + " (sold_cars INTEGER PRIMARY KEY NOT NULL DEFAULT 0, sold_user_id TEXT NOT NULL DEFAULT '0')");
        db.execSQL("create table " + LIKES_TABLE + " (car_likes INTEGER PRIMARY KEY NOT NULL DEFAULT 0, liked_user_id TEXT NOT NULL DEFAULT '0')");


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_INFO_TABLE);
        onCreate(db);
    }

    public boolean insertUser(int user_id,String mobile_no1,String user_fname,String user_email) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+DEVICE_INFO_TABLE);
        db.execSQL("VACUUM");

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1DEVICE, user_id);
        contentValues.put(COL_2DEVICE, mobile_no1);
        contentValues.put(COL_3DEVICE, user_fname);
        contentValues.put(COL_4DEVICE, user_email);

        long result = db.insert(DEVICE_INFO_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            Log.d("woooo",""+result);
            return true;
    }


    public boolean insert_make(int idtbl_carmake, String make_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1MAKE, idtbl_carmake);
        contentValues.put(COL_2MAKE, make_name);

        long result = db.insert(MAKE_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insert_model(int idtbl_carmodel, int tbl_carmake_idtbl_carmake, String model_name) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1MODEL, idtbl_carmodel);
        contentValues.put(COL_2MODEL, tbl_carmake_idtbl_carmake);
        contentValues.put(COL_3MODEL, model_name);

        long result = db.insert(MODEL_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }




    public boolean insert_year(int idtbl_caryear, String year_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1YEAR, idtbl_caryear);
        contentValues.put(COL_2YEAR, year_name);

        long result = db.insert(YEAR_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insert_features(int idtbl_feature, int feature_group, String feature_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1FEATURE, idtbl_feature);
        contentValues.put(COL_2FEATURE, feature_group);
        contentValues.put(COL_3FEATURE, feature_name);

        long result = db.insert(FEATURES_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }

    public void log_out() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+DEVICE_INFO_TABLE);
        db.execSQL("VACUUM");
    }

    public Cursor select_user() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT user_id,mobile_no1,user_fname,user_email FROM  user_table LIMIT 1",null);
        return c;
    }



    public Cursor get_make_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT idtbl_carmake, make_name FROM make_table ORDER BY make_name ASC" , null);

        return c;
    }



    public Cursor get_filter_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT idtbl_carmodel,model_name,idtbl_carmake, make_name FROM " +
                "model_table INNER JOIN make_table ON tbl_carmake_idtbl_carmake=idtbl_carmake ORDER BY make_name ASC" , null);

        return c;
    }

    public Cursor get_make_id(String make_name_sent){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT idtbl_carmake FROM make_table  WHERE make_name=?  " ,new String[] {make_name_sent});

        return c;
    }

    public Cursor get_model_token_id(String make_sent,String model_name_sent){

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor_make_id = db.rawQuery("SELECT idtbl_carmake FROM make_table  WHERE make_name=?  LIMIT 1" ,new String[] {make_sent});
        if(cursor_make_id.getCount()==0) {
            TOKEN_MAKE_ID="";

        }else {

            while (cursor_make_id.moveToNext()) {
                TOKEN_MAKE_ID = cursor_make_id.getString(0);

            }
        }

        Cursor c = db.rawQuery("SELECT idtbl_carmodel FROM model_table  WHERE model_name=? AND tbl_carmake_idtbl_carmake=?  LIMIT 1" ,new String[] {model_name_sent,TOKEN_MAKE_ID});

        return c;
    }

    public Cursor get_model_lables(String make_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  idtbl_carmodel,model_name FROM model_table WHERE tbl_carmake_idtbl_carmake=? " , new String[]{make_id});
        // returning lables
        return c;
    }

    public Cursor get_model_filters(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT model_name FROM model_table " , null);
        // returning lables
        return c;
    }

    public Cursor get_model_id(String model_sent){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  idtbl_carmodel,model_name FROM model_table WHERE model_name=? LIMIT 1" , new String[]{model_sent});
        // returning lables
        return c;
    }



    public Cursor get_year_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  idtbl_car_year,year_name FROM year_table" , null);

        // returning lables
        return c;
    }


    public Cursor get_features_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  idtbl_feature,feature_group,feature_name FROM feature_table" , null);

        // returning lables
        return c;
    }

    public Cursor get_safety_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  feature_name FROM feature_table WHERE feature_group=?" ,new String[]{"2"});

        // returning lables
        return c;
    }

    public Cursor get_windows_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  feature_name FROM feature_table WHERE feature_group=?" ,new String[]{"3"});

        // returning lables
        return c;
    }

    public Cursor get_others_lables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  feature_name FROM feature_table WHERE feature_group=?" ,new String[]{"4"});

        // returning lables
        return c;
    }


    //METHODS TO MARK THE CAR AS SOLD

    public boolean mark_as_sold(int ad_id,String user_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1SOLD, ad_id);
        contentValues.put(COL_2SOLD, user_id);

        long result = db.insertWithOnConflict(SOLD_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }



    public Cursor is_car_sold(int ad_id,String user_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  sold_cars FROM sold_table WHERE sold_cars =? AND sold_user_id=? " , new String[]{ad_id+"",user_id.trim()});
        // returning lables
        return c;
    }

    public boolean delete_from_sold(int ad_id,String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(SOLD_TABLE,
                COL_1SOLD + " = ? " +" AND "+COL_2SOLD+ " = ? ",
                new String[] {ad_id+"",user_id});
        if (result == -1)
            return false;
        else
            return true;
    }





    //THIS ARE METHOD TO DEAL WITH LIKED CARS

    public boolean mark_as_liked(int ad_id,String user_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1LIKES, ad_id);
        contentValues.put(COL_2LIKES, user_id);

        long result = db.insertWithOnConflict(LIKES_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor is_car_liked(int ad_id,String user_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  car_likes FROM likes_table WHERE car_likes =? AND liked_user_id=? " , new String[]{ad_id+"",user_id.trim()});
        // returning lables
        return c;
    }

    public Cursor select_liked_cars(String user_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  car_likes FROM likes_table WHERE liked_user_id=? " , new String[]{user_id.trim()});
        // returning lables
        return c;
    }
    public boolean delete_from_liked(int ad_id,String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(LIKES_TABLE,
                COL_1LIKES + " = ? " +" AND "+COL_2LIKES+ " = ? ",
                new String[] {ad_id+"",user_id});
        if (result == -1)
            return false;
        else
            return true;
    }

}
