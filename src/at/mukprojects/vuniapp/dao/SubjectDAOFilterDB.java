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
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.storages.VUniAppDBConnection;

/**
 * TODO K: Beschreibung
 * 
 * @author kerrim
 */
public class SubjectDAOFilterDB implements SubjectDAO {
    private static final String TAG       = SubjectDAOFilterDB.class
                                                  .getSimpleName();

    private VUniAppDBConnection database;

    private static final String TABLENAME = "SubjectFilter";

    public SubjectDAOFilterDB(final Context context) throws ConnectionException {
        database = VUniAppDBConnection.getInstance(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#readSubjects()
     */
    @Override
    public final ArrayList<Subject> readSubjects() throws IOException,
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
                    output.add(new Subject(subjectid));
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
    public final void writeSubjects(final ArrayList<Subject> subjects)
            throws IOException {
        SQLiteDatabase writableDatabase = database.getWritableDatabase();
        writableDatabase.beginTransaction();
        SQLiteStatement statement = null;
        try {
            for (Subject subject : subjects) {
                statement = writableDatabase.compileStatement("INSERT INTO "
                        + TABLENAME + " (subjectid) VALUES(?);");
                statement.bindString(1, subject.getId());
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
     * at.mukprojects.vuniapp.dao.SubjectDAO#readDetails(at.mukprojects.vuniapp
     * .models.Subject)
     */
    @Override
    public Subject readDetails(final Subject subject) throws IOException,
            InvalidLoginException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(final ArrayList<Subject> subjects) throws IOException {
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

}
