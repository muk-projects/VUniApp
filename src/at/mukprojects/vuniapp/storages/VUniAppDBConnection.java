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

package at.mukprojects.vuniapp.storages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.ConnectionException;

/**
 * Die Klasse VUniAppDBConnection stellt eine Verbindung zu einer SQLite
 * Datenbank her. Die Klasse wurde nach dem Singleton Pattern entworfen, um
 * sicherzustellen, dass es nur eine Verbindung zur Datenbank gibt.
 * 
 * @author Mathias
 */
public final class VUniAppDBConnection extends SQLiteOpenHelper {
    private static final String        TAG         = VUniAppDBConnection.class
                                                           .getSimpleName();

    /** Instanz der Klasse. */
    private static VUniAppDBConnection instance    = null;

    /** Application Context. */
    private Context                    context;

    /** DB Attribute. */
    private static final String        DBNAME      = "VUniAppDB.db";
    private static final int           DBVERSION   = 4;
    private static final String        CREATE_FILE = "db/create.sql";
    private static final String        INSERT_FILE = "db/insert.sql";
    private static final String        DROP_FILE   = "db/drop.sql";

    /**
     * Liefert die einzige Instanz der Klasse zur&uuml;ck.
     * 
     * @param context
     *            Context der Applikation.
     * @return Instanz der Klasse VUniAppDBConnection.
     * @throws ConnectionException
     *             Diese Exception wird geworfen, wenn es w&auml;hrend dem
     *             Verbindungsaufbau mit dem Storage zu einem Fehler kommt.
     */
    public static synchronized VUniAppDBConnection getInstance(
            final Context context) throws ConnectionException {
        Log.i(TAG, "Methode: getInstance wird gestartet.");
        if (instance == null) {
            if (context != null) {
                instance = new VUniAppDBConnection(
                        context.getApplicationContext());
            } else {
                Log.e(TAG,
                        "Es wurde null als Context übergeben, es konnte keine Verbindung"
                                + " mit der Datenbank hergestellt werden.");
                throw new ConnectionException(
                        "Der übergebene Context darf nicht null sein.");
            }
            Log.d(TAG, "Eine neue VUniAppDBConnection wurde erstellt.");
        }
        Log.i(TAG, "Methode: getInstance wird verlassen.");
        return instance;
    }

    /**
     * Erstellt ein neues VUniAppDBConnection Objekt.
     * 
     * @param context
     *            Context der Applikation.
     */
    private VUniAppDBConnection(final Context context) {
        super(context, DBNAME, null, DBVERSION);
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        Log.d(TAG, "Eine neue Datenbank wird erstellt.");

        for (String query : getSQLQueries(CREATE_FILE)) {
            Log.d(TAG, query);
            try {
                db.execSQL(query);
            } catch (SQLiteException sqliteException) {
                Log.e(TAG, "Die SQL- Query (" + query
                        + ") konnte nicht ausgeführt werden.", sqliteException);
            }

        }
        for (String query : getSQLQueries(INSERT_FILE)) {
            Log.d(TAG, query);
            try {
                db.execSQL(query);
            } catch (SQLiteException sqliteException) {
                Log.e(TAG, "Die SQL- Query (" + query
                        + ") konnte nicht ausgeführt werden.", sqliteException);
            }
        }

        Log.d(TAG, "Es wurde eine neue Datenbank mit dem Namen " + DBNAME
                + " erstellt.");
        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        Log.i(TAG, "Methode: onUpgrade wird gestartet.");
        Log.d(TAG, "Version " + oldVersion + " wird zu " + newVersion
                + " upgedatet.");

        /** Wird je nach Version&auml;nderung angepasst. */

        // TODO M: User sollten nicht verloren gehen.
        try {
            for (String query : getSQLQueries(DROP_FILE)) {
                Log.d(TAG, query);
                db.execSQL(query);
            }
            for (String query : getSQLQueries(CREATE_FILE)) {
                Log.d(TAG, query);
                db.execSQL(query);
            }
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, "Eine SQL Query konnte nicht ausgeführt werden.",
                    sqliteException);
        }

        Log.i(TAG, "Methode: onUpgrade wird verlassen.");
    }

    /**
     * Die Methode liest SQL Befehle aus einem angegebenen File ein und liefert
     * diese als ArrayList<String> zur&uuml;ck.
     * 
     * @param filePath
     *            Pfad des Files.
     * @return Die SQL Queries in einer ArrayList.
     */
    private ArrayList<String> getSQLQueries(final String filePath) {
        String fileString = "";
        try {
            fileString = loadSQLFile(filePath);
        } catch (IOException e) {
            Log.e(TAG, "Beim Versuch das File(" + filePath
                    + ") einzulesen kam es zu einem Fehler.", e);
            return new ArrayList<String>();
        }

        ArrayList<String> queries = new ArrayList<String>();
        String sqlQuery = "";
        boolean newQuery = true;
        for (char c : fileString.toCharArray()) {
            if (!newQuery && c == ' ') {
                sqlQuery += c;
                newQuery = false;
            } else if (!newQuery && c == '\n') {
                sqlQuery += ' ';
                newQuery = false;
            } else if (c != ' ' && c != '\n') {
                sqlQuery += c;
                newQuery = false;
                if (c == ';') {
                    queries.add(sqlQuery);
                    newQuery = true;
                    sqlQuery = "";
                }
            }
        }
        Log.d(TAG, "Das File wurde in eine ArrayList extrahiert.");

        return queries;
    }

    /**
     * L&auml;dt das File aus dem angegebenen Pfad und speichert es in einen
     * String.
     * 
     * @param filePath
     *            Pfad des Files.
     * @return File als String.
     * @throws IOException
     *             Es wird eine IOException geworfen, falls es beim Lesen des
     *             Files zu einem Fehler kommt.
     */
    private String loadSQLFile(final String filePath) throws IOException {
        Log.d(TAG, "Das File (" + filePath + ") wird geladen.");
        InputStream inputStream = context.getAssets().open(filePath);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferdReader = new BufferedReader(inputReader);
        Log.d(TAG, "Das File (" + filePath + ") wurde erfolgreich geladen.");

        StringBuilder sqlScript = new StringBuilder();
        String line;
        while ((line = bufferdReader.readLine()) != null) {
            sqlScript.append(line);
            sqlScript.append('\n');
        }
        Log.d(TAG, "Der Inhalt des Files wurde erfolgreich"
                + " in einen String umgewandelt");
        return sqlScript.toString();
    }
}
