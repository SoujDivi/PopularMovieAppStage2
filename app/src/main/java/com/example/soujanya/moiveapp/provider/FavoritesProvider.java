package com.example.soujanya.moiveapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by souji on 25/12/15.
 */
public class FavoritesProvider extends ContentProvider {


    static final String PROVIDER_NAME = "com.example.soujanya.movieapp.provider.FavoritesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final public Uri CONTENT_URI = Uri.parse(URL);

    public static final String name = "movieId";
    public static final String url = "img";
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> FAVORITES_PROJECTION_MAP;
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);

    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;

    static final String DATABASE_NAME = "Collection";
    static final String FAVORITES_TABLE_NAME = "favorites";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FAVORITES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " movieId TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d("favorites", " " + values);



        /**
         * Add a new student record
         */
        long rowID = db.insert(FAVORITES_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }


        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        return 0;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

        }
        return null;

    }

    public  Cursor c;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FAVORITES_TABLE_NAME);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

         db = dbHelper.getReadableDatabase();

        String tableName=uri.getLastPathSegment();


        c = qb.query(db, projection, selection, selectionArgs, null, null, null);

        return c;

    }
}





