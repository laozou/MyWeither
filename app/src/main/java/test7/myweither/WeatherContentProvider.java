package test7.myweither;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by 123 on 2016/3/5.
 */
public class WeatherContentProvider extends ContentProvider {

    private WeatherSQL mWeatherSQL;
    private static UriMatcher mUriMatcher;
    public static final int URI_MATCH_WEATHER = 1;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(WeatherURI.AUTHORITY, WeatherSQL.TABLE_NAME, URI_MATCH_WEATHER);
    }

    @Override
    public boolean onCreate() {
        mWeatherSQL = new WeatherSQL(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tablename=getTableName(uri);
        if(TextUtils.isEmpty(tablename)){
            return null;
        }

        return mWeatherSQL.getReadableDatabase().query(tablename, projection, selection, selectionArgs, null, null, sortOrder);
    }



    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if(TextUtils.isEmpty(tableName)){
            return null;
        }
        long id = mWeatherSQL.getWritableDatabase().insert(tableName, null,values);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(TextUtils.isEmpty(tableName)){
            return -1;
        }

        return mWeatherSQL.getWritableDatabase().delete(tableName, selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(TextUtils.isEmpty(tableName)){
            return -1;
        }

        return mWeatherSQL.getWritableDatabase().update(tableName, values,selection,selectionArgs);
    }

    private String getTableName(Uri uri) {
        String name =null;
        switch (mUriMatcher.match(uri)){
            case URI_MATCH_WEATHER:
                name=WeatherSQL.TABLE_NAME;
                break;
            default:
                break;
        }

        return name;
    }
}
