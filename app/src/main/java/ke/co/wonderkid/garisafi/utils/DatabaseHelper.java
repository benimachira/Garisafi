package ke.co.wonderkid.garisafi.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ben on 1/9/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //DATABASE VARIABLES
    public static final String DATABASE_NAME = "db_garisafi";
    public static final String DEVICE_INFO_TABLE = "user_table";

    //DEVICE REGISTRATION VARIABLES
    public static final String COL_1DEVICE = "user_id";
    public static final String COL_2DEVICE = "username";
    public static final String COL_3DEVICE = "access_code";
    public static final String COL_4DEVICE = "name1";
    public static final String COL_5DEVICE = "name2";
    public static final String COL_6DEVICE = "photo";
    public static final String COL_7DEVICE = "subscription_status";

    //Car




    Context ctx;
    int print_count;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.ctx = context;


    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DEVICE_INFO_TABLE + " (user_id INTEGER,username TEXT,access_code INTEGER DEFAULT 0," +
                "name1 TEXT DEFAULT '',name2 TEXT DEFAULT '',photo TEXT DEFAULT '',subscription_status DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_INFO_TABLE);
        onCreate(db);

    }
}
