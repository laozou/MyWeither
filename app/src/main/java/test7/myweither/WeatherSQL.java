package test7.myweither;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 123 on 2016/3/4.
 */
public class WeatherSQL extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db";
    public static final String TABLE_NAME = "weatherofcity";
    public static final String TITLE = "title";
    public static final String CONTENT="content";

    private static final int VERSION=2;

    public WeatherSQL(Context context){
        super(context, DATABASE_NAME,null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+"("+TITLE+" varchar(20) not null, "+CONTENT+" varchar(40) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
