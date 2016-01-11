/**
 * Copyright (C) 2013 by
 * Mathias Markl and Kerrim Abd El Hamed
 *
 * This program is free software!
 * You are allowed to redistribute it and/or modify it
 * under the terms of the GNU General Public License, version 2.
 * For details of the GNU General Public License see
 * http://www.gnu.org/licenses/gpl-2.0.html
 */

package at.mukprojects.vuniapp.dao;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.storages.VUniAppDBConnection;

/**
 * TODO K: Beschreibung
 * 
 * @author kerrim
 */
public class SubjectDAOAndroidDB implements SubjectDAO {
    private static final String TAG       = SubjectDAOAndroidDB.class
                                                  .getSimpleName();

    private VUniAppDBConnection database;

    private static final String TABLENAME = "Subjects";

    public SubjectDAOAndroidDB(final Context context)
            throws ConnectionException {
        database = VUniAppDBConnection.getInstance(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#readSubjects()
     */
    @Override
    public ArrayList<Subject> readSubjects() throws IOException,
            InvalidLoginException {
        ArrayList<Subject> output = new ArrayList<Subject>();
        SQLiteDatabase readableDatabase = database.getReadableDatabase();

        readableDatabase.beginTransaction();
        Cursor cursor = null;

        try {

            cursor = readableDatabase.rawQuery("SELECT * FROM " + TABLENAME
                    + ";", null);

            if (cursor.moveToFirst()) {
                do {
                    String subjectid = cursor.getString(1);
                    String ukey = cursor.getString(2);
                    String name = cursor.getString(3);
                    String taken = cursor.getString(4);
                    String link = cursor.getString(5);
                    String lvanr = cursor.getString(6);
                    String type = cursor.getString(7);
                    String semester = cursor.getString(8);
                    Float ects = cursor.getFloat(9);
                    Float hours = cursor.getFloat(10);
                    Subject s = new Subject(Universities.getInstance()
                            .getUniversityFromKey(ukey), name,
                            (taken.equals("t") ? true : false), link, lvanr,
                            type, semester, ects, hours);
                    s.setId(subjectid);
                    output.add(s);
                } while (cursor.moveToNext());
            }

            readableDatabase.setTransactionSuccessful();

        } catch (SQLiteException sqliteException) {
            Log.e(TAG, "Während der Select Abfrage kam es zu einem Fehler.",
                    sqliteException);
            // FIXME K: throw new
            // ReadException(sqliteException.getMessage(),sqliteException);
            throw new IOException();
        } finally {
            readableDatabase.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#writeSubjects(java.util.ArrayList)
     */
    @Override
    public void writeSubjects(final ArrayList<Subject> subjects)
            throws IOException {
        SQLiteDatabase writableDatabase = database.getWritableDatabase();
        writableDatabase.beginTransaction();
        SQLiteStatement statement = null;
        try {
            for (Subject subject : subjects) {
                statement = writableDatabase
                        .compileStatement("INSERT INTO "
                                + TABLENAME
                                + " (subjectid,university,name,taken,link,lvanr,type,semester,ects,hours) VALUES(?,?,?,?,?,?,?,?,?,?);");
                statement.bindString(1, subject.getId());
                statement.bindString(2, subject.getUniversity().getKeyName());
                statement.bindString(3, subject.getName());
                statement.bindString(4, (subject.isTaken() ? "t" : "f"));
                statement.bindString(5, subject.getLink());
                statement.bindString(6, subject.getLvanr());
                statement.bindString(7, subject.getType());
                statement.bindString(8, subject.getSemester());
                statement.bindDouble(9, subject.getEcts());
                statement.bindDouble(10, subject.getHours());
                
                long lastUserID = statement.executeInsert();
            }
            writableDatabase.setTransactionSuccessful();
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, sqliteException.getMessage(), sqliteException);
            // FIXME K: throw new
            // ReadException(sqliteException.getMessage(),sqliteException);
            throw new IOException();
        } finally {
            /**
             * Eine Transaktion, welche nicht auf erfolgreich gesetzt wurde wird
             * automatisch einem Rollback unterzogen.
             */
            Log.d(TAG, "Die Transaktion (createUser) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(final ArrayList<Subject> subjects)
            throws IOException {
        SQLiteDatabase writableDatabase = database.getWritableDatabase();
        writableDatabase.beginTransaction();
        SQLiteStatement statement = null;
        try {
            for (Subject subject : subjects) {
                statement = writableDatabase.compileStatement("DELETE FROM "
                        + TABLENAME + " WHERE subjectid = ?;");
                statement.bindString(1, subject.getId());
                long lastUserID = statement.executeInsert();
            }
            writableDatabase.setTransactionSuccessful();
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, sqliteException.getMessage(), sqliteException);
            // FIXME: K: throw new
            // ReadException(sqliteException.getMessage(),sqliteException);
            throw new IOException();
        } finally {
            /**
             * Eine Transaktion, welche nicht auf erfolgreich gesetzt wurde wird
             * automatisch einem Rollback unterzogen.
             */
            Log.d(TAG, "Die Transaktion (createUser) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#readDetails(at.mukprojects.vuniapp
     * .models.Subject)
     */
    @Override
    public Subject readDetails(final Subject subject) throws IOException,
            InvalidLoginException {
        return subject;
    }

}
