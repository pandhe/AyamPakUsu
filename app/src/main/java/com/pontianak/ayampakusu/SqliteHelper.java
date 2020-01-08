package com.pontianak.ayampakusu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mykeranjang_db";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Notifikasi.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Notifikasi.TABLE_NAME);
        onCreate(db);


    }

    public long insertNote(String title_notif, String content_notif, String logo_notif, String action_notif, String id_action_notif) {
        // get writable database as we want to write data

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Notifikasi.COLUMN_TITLE_NOTIF, title_notif);
        values.put(Notifikasi.COLUMN_CONTENT_NOTIF, content_notif);
        values.put(Notifikasi.COLUMN_LOGO_NOTIF, logo_notif);
        values.put(Notifikasi.COLUMN_ACTION_NOTIF, action_notif);
        values.put(Notifikasi.COLUMN_ID_ACTION, id_action_notif);



        // insert row
        long id = db.insert(Notifikasi.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Notifikasi getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Notifikasi.TABLE_NAME,
                new String[]{Notifikasi.COLUMN_ID, Notifikasi.COLUMN_TITLE_NOTIF, Notifikasi.COLUMN_CONTENT_NOTIF, Notifikasi.COLUMN_LOGO_NOTIF, Notifikasi.COLUMN_ACTION_NOTIF, Notifikasi.COLUMN_ID_ACTION},
                Notifikasi.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Notifikasi notifikasi = new Notifikasi(
                cursor.getInt(cursor.getColumnIndex(Notifikasi.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_TITLE_NOTIF)),
                cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_CONTENT_NOTIF)),
                cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_LOGO_NOTIF)),
                cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_ACTION_NOTIF)),
                cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_ID_ACTION))
                );

        // close the db connection
        cursor.close();

        return notifikasi;
    }

    public void WipeFloor(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Notifikasi.TABLE_NAME, null, null);
    }


    public List<Notifikasi> getAllNotes() {
        List<Notifikasi> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Notifikasi.TABLE_NAME + " ORDER BY " +
                Notifikasi.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notifikasi notifikasi = new Notifikasi();
                notifikasi.setId(cursor.getInt(cursor.getColumnIndex(Notifikasi.COLUMN_ID)));
                notifikasi.setTitle_notif(cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_TITLE_NOTIF)));
                notifikasi.setContent_notif(cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_CONTENT_NOTIF)));
                notifikasi.setLogo_notif(cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_LOGO_NOTIF)));
                notifikasi.setAction_notif(cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_ACTION_NOTIF)));
                notifikasi.setId_action_notif(cursor.getString(cursor.getColumnIndex(Notifikasi.COLUMN_ID_ACTION)));



                notes.add(notifikasi);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Notifikasi.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public boolean delete(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean bol= db.delete(Notifikasi.TABLE_NAME, Notifikasi.COLUMN_ID + "=" + name, null) > 0;
        db.close();
        return bol;
    }
}
