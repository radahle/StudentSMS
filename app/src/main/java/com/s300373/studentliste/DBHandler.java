package com.s300373.studentliste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RudiAndre on 12.10.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    SimpleDateFormat tidspunkt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    static int DATABASE_VERSION = 3;
    static String DATABASE_NAME = "Studenter";
    static String TABLE_STUDENTER = "Studenter";
    static String KEY_ID = "_ID";
    static String KEY_NAME = "Fornavn";
    static String KEY_LAST_NAME = "Etternavn";
    static String KEY_PH_NO = "Telefon";
    static String TABLE_MELDINGER = "Meldinger";
    static String KEY_TIME = "Tidspunkt";
    static String KEY_MESSAGE = "Meldinger";
    static String KEY_SENDT = "Status";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String OPPRETT_STUDENTER = "CREATE TABLE " + TABLE_STUDENTER + "(" + KEY_ID +
                " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_LAST_NAME + " TEXT," +
                KEY_PH_NO + " INTEGER" + ")";
        db.execSQL(OPPRETT_STUDENTER);
        String OPPRETT_MELDINGER = "CREATE TABLE " + TABLE_MELDINGER + "(" + KEY_ID +
                " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT," + KEY_MESSAGE + " TEXT," + KEY_SENDT + " TEXT" + ")";
        db.execSQL(OPPRETT_MELDINGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTER);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MELDINGER);
        onCreate(db);
    }

    public void leggTilStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getFornavn());
        values.put(KEY_LAST_NAME, student.getEtternavn());
        values.put(KEY_PH_NO, student.getTelefon());
        db.insert(TABLE_STUDENTER, null, values);
        db.close();
    }

    public void leggTilMelding(Melding melding) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, String.valueOf(melding.getTidspunkt()));
        values.put(KEY_MESSAGE, melding.getMelding());
        values.put(KEY_SENDT, "Ikke sendt");
        db.insert(TABLE_MELDINGER, null, values);
        db.close();
    }

    public ArrayList<Melding> hentAlleMeldinger() {
        ArrayList<Melding> meldingListe = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MELDINGER + " ORDER BY " + KEY_TIME + " COLLATE NOCASE;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Melding melding = new Melding();
                melding.set_ID(cursor.getLong(0));
                melding.setTidspunkt(cursor.getString(1));
                melding.setMelding(cursor.getString(2));
                melding.setStatus(cursor.getString(3));
                meldingListe.add(melding);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return meldingListe;
    }

    public ArrayList<Melding> hentUsendteMeldinger() {
        ArrayList<Melding> meldingListe = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MELDINGER + " WHERE " + KEY_SENDT + " LIKE 'Ikke sendt' AND DATETIME('NOW') < " + KEY_TIME + " ORDER BY " + KEY_TIME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Melding melding = new Melding();
                melding.set_ID(cursor.getLong(0));
                melding.setTidspunkt(cursor.getString(1));
                melding.setMelding(cursor.getString(2));
                melding.setStatus(cursor.getString(3));
                meldingListe.add(melding);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return meldingListe;
    }

    public List<Student> hentAlleStudenter() {
        List<Student> studentListe = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTER + " ORDER BY " + KEY_NAME + " COLLATE NOCASE;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.set_ID(cursor.getLong(0));
                student.setFornavn(cursor.getString(1));
                student.setEtternavn(cursor.getString(2));
                student.setTelefon(cursor.getString(3));
                studentListe.add(student);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return studentListe;
    }

    public ArrayList<Integer> hentAlleNummer() {
        ArrayList<Integer> nummerListe = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_PH_NO + " FROM " + TABLE_STUDENTER + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                nummerListe.add(cursor.getInt(0));
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return nummerListe;
    }

    public List<String> hentAlleNavn() {
        List<String> nummerListe = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_STUDENTER + " ORDER BY " + KEY_NAME + " COLLATE NOCASE;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                nummerListe.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return nummerListe;
    }

    public void slettStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTER, KEY_ID + " =? ",
                new String[]{ id });
        db.close();
    }

    public int oppdaterStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getFornavn());
        values.put(KEY_LAST_NAME, student.getEtternavn());
        values.put(KEY_PH_NO, student.getTelefon());
        int endret = db.update(TABLE_STUDENTER, values, KEY_ID + " =? ",
                new String[] {
                        String.valueOf(student.get_ID())
                });
        db.close();
        return endret;
    }

    public int oppdaterMelding(Melding melding) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, String.valueOf(melding.getTidspunkt()));
        values.put(KEY_MESSAGE, melding.getMelding());
        values.put(KEY_SENDT, melding.getStatus());
        int endret = db.update(TABLE_MELDINGER, values, KEY_ID + " =? ",
                new String[] {
                        String.valueOf(melding.get_ID())
                });
        db.close();
        return endret;
    }


}
