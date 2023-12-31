package com.example.sticky_notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "NOTES";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_DESCRIPTION = "Description";
    Context context;

    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }




    public long addNotes(String title, String description) {
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, title);
            values.put(KEY_DESCRIPTION, description);
            result = db.insert(TABLE_NOTES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return result;
    }





    public ArrayList<NotesModel> fetchNotes() {
        ArrayList<NotesModel> arrNotes = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
            if (cursor.moveToFirst()) {
                do {
                    NotesModel model = new NotesModel();
                    model.id = cursor.getInt(0);
                    model.title = cursor.getString(1);
                    model.description = cursor.getString(2);
                    arrNotes.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrNotes;
    }

//    public void updateNote(NotesModel notesModel){
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(KEY_TITLE,notesModel.title);
//        cv.put(KEY_DESCRIPTION,notesModel.description);
//        database.update(TABLE_NOTES,cv,KEY_ID+"="+notesModel.id,null);
//
//    }


    public void updateNote(NotesModel notesModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, notesModel.title);
        cv.put(KEY_DESCRIPTION, notesModel.description);

        int rowsAffected = database.update(TABLE_NOTES, cv, KEY_ID + "=?", new String[]{String.valueOf(notesModel.id)});
        if (rowsAffected > 0) {
            Toast.makeText(context, "Note updated in the database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update note in the database", Toast.LENGTH_SHORT).show();
        }
    }



    public void deleteContact(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NOTES,KEY_ID+"=?",new String[]{String.valueOf(id)});
    }




}

