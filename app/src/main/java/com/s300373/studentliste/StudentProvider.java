package com.s300373.studentliste;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by RudiAndre on 28.10.2017.
 */

public class StudentProvider extends ContentProvider {

    DBHandler dbHandler;
    SQLiteDatabase db;
    private static final String PROVIDER = "com.s300373.studentliste.StudentProvider";
    private static final String TABLE_STUDENTER = "Studenter";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/" + TABLE_STUDENTER);
    static final int STUDENT_ID = 1;
    static final int STUDENTER = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "Studenter", STUDENTER);
        uriMatcher.addURI(PROVIDER, "Studenter/#", STUDENT_ID);
    }


    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        db = dbHandler.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cur = null;
        if (uriMatcher.match(uri) == STUDENT_ID) {
            cur = db.query(TABLE_STUDENTER, strings, dbHandler.KEY_ID + "=" + uri.getPathSegments().get(1), strings1, null, null, s1);
            return cur;
        } else {
            cur = db.query(TABLE_STUDENTER, null, null, null, null, null, null);
            return cur;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENTER:
                return "vnd.android.cursor.dir/vnd.example.student";
            case STUDENT_ID:
                return "vnd.android.cursor.item/vnd.example.student";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.insert(TABLE_STUDENTER, null, contentValues);
        Cursor c = db.query(TABLE_STUDENTER, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (uriMatcher.match(uri) == STUDENT_ID) {
            db.delete(TABLE_STUDENTER, dbHandler.KEY_ID + " = " + uri.getPathSegments().get(1), strings);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == STUDENTER) {
            db.delete(TABLE_STUDENTER, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        System.out.println("_______________________" + uri);
        if (uriMatcher.match(uri) == STUDENT_ID) {
            System.out.println("_______________________ matcher STUDENT_ID" );
            db.update(TABLE_STUDENTER, contentValues, dbHandler.KEY_ID + " = " + uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == STUDENTER) {
            db.update(TABLE_STUDENTER, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}
