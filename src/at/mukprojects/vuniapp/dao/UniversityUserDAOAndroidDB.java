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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.storages.VUniAppDBConnection;

/**
 * DAO Klasse, welche eine Kommunikation mit der VUniAppDB erm&ouml;glicht.
 * 
 * @author Mathias
 */
public final class UniversityUserDAOAndroidDB implements UniversityUserDAO {
    private static final String TAG                  = UniversityUserDAOAndroidDB.class
                                                             .getSimpleName();

    /** Datenbank der DAO. */
    private VUniAppDBConnection database;

    /** Name der User Tabelle. */
    private static final String TABLENAME            = "User";

    /** Spalten der Tabelle User. */
    private static final String COLUMN_ID            = "id";
    private static final String COLUMN_USERNAME      = "username";
    private static final String COLUMN_PASSWORD      = "password";
    private static final String COLUMN_UNIVERSITY    = "university";
    private static final String COLUMN_UNIVERSITYKEY = "userkey";

    /**
     * Erstellt eine neue UserDAOAndoridDB.
     * 
     * @param context
     *            Context der Applikation.
     * @throws ConnectionException
     *             Diese Exception wird geworfen, wenn es w&auml;hrend dem
     *             Verbindungsaufbau mit dem Storage zu einem Fehler kommt.
     */
    public UniversityUserDAOAndroidDB(final Context context)
            throws ConnectionException {
        database = VUniAppDBConnection.getInstance(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#getUserByID(int)
     */
    @Override
    public UniversityUser getUserByID(final long id) throws ReadException {
        Log.i(TAG, "Methode: getUserByID wird gestartet.");
        UniversityUser searchUser = new UniversityUser(null, null, null, null);
        searchUser.setId(id);
        List<UniversityUser> retList = getUsers(searchUser);
        UniversityUser retUser = null;
        if (retList.size() > 0) {
            retUser = retList.get(0);
        }
        Log.i(TAG, "Methode: getUserByID wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#getUsers(at.mukprojects.vuniapp
     * .models.User)
     */
    @Override
    public List<UniversityUser> getUsers(final UniversityUser user)
            throws ReadException {
        Log.i(TAG, "Methode: getUsers wird gestartet.");
        Log.d(TAG,
                "Es wird eine SQL- SelectQuery für den übergebenen User erzeugt.");
        ArrayList<UniversityUser> retList = new ArrayList<UniversityUser>();
        SQLiteDatabase readableDatabase = database.getReadableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (getUsers) gestartet.");
        readableDatabase.beginTransaction();

        Cursor cursor = null;
        try {
            Log.d(TAG, "RawQuery: " + QueryBuilder.buildSelectQuery(user));
            String debugArgs = "";
            for (String arg : QueryBuilder.getSelectionArgs(user)) {
                debugArgs += arg + " ";
            }
            Log.d(TAG, "RawQueryArgs: ( " + debugArgs + ")");

            cursor = readableDatabase.rawQuery(
                    QueryBuilder.buildSelectQuery(user),
                    QueryBuilder.getSelectionArgs(user));

            if (cursor.moveToFirst()) {
                UniversityUser listItem = new UniversityUser(
                        cursor.getString(1), cursor.getString(2), Universities
                                .getInstance().getUniversityFromKey(
                                        cursor.getString(3)),
                        cursor.getString(4));
                listItem.setId(cursor.getLong(0));
                retList.add(listItem);

                while (cursor.moveToNext()) {
                    listItem = new UniversityUser(cursor.getString(1),
                            cursor.getString(2), Universities.getInstance()
                                    .getUniversityFromKey(cursor.getString(3)),
                            cursor.getString(4));
                    listItem.setId(cursor.getLong(0));
                    retList.add(listItem);
                }
            }

            Log.d(TAG, "Die Transaktion (getUsers)"
                    + " wird als erfolgreich Makiert.");
            readableDatabase.setTransactionSuccessful();

        } catch (SQLiteException sqliteException) {
            Log.e(TAG, "Während der Select Abfrage kam es zu einem Fehler.",
                    sqliteException);
            throw new ReadException(sqliteException.getMessage(),
                    sqliteException);
        } finally {
            /**
             * Eine Transaktion, welche nicht auf erfolgreich gesetzt wurde wird
             * automatisch einem Rollback unterzogen.
             */
            Log.d(TAG, "Die Transaktion (getUsers) wird beendet.");
            readableDatabase.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.i(TAG, "Methode: getUsers wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#getAllUsers()
     */
    @Override
    public List<UniversityUser> getAllUsers() throws ReadException {
        Log.i(TAG, "Methode: getUsers wird gestartet.");
        List<UniversityUser> retList = getUsers(null);
        Log.i(TAG, "Methode: getUsers wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#createUser(at.mukprojects.vuniapp
     * .models.User)
     */
    @Override
    public UniversityUser createUser(final UniversityUser user)
            throws CreateException {
        Log.i(TAG, "Methode: createUser wird gestartet.");
        Log.d(TAG,
                "Es wird eine SQL- InsertQuery für den übergebenen User erzeugt.");
        if (!validateInputParameter(user)) {
            Log.e(TAG, "Der Methode (createUser) darf keinen ungültiger User"
                    + " übergeben werden.");
            throw new CreateException(
                    "Der übergebene User muss gültig sein. (Kein Parameter darf null sein)");
        }

        UniversityUser retUser = new UniversityUser(user.getUsername(),
                user.getPassword(), user.getUniversity(), user.getUserKey());
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (createUser) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            statement = writableDatabase
                    .compileStatement("INSERT INTO "
                            + TABLENAME
                            + " (username, password, university, userkey) VALUES(?, ?, ?, ?);");
            statement.bindString(1, user.getUsername());
            statement.bindString(2, user.getPassword());
            statement.bindString(3, user.getUniversity().getKeyName());
            statement.bindString(4, user.getUserKey());
            long lastUserID = statement.executeInsert();
            if (lastUserID != -1) {
                retUser.setId(lastUserID);
                Log.d(TAG, "Die Transaktion (createUser)"
                        + " wird als erfolgreich Makiert.");
                writableDatabase.setTransactionSuccessful();
            }
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, sqliteException.getMessage(), sqliteException);
            throw new CreateException(sqliteException.getMessage(),
                    sqliteException);
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
        Log.i(TAG, "Methode: createUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#updateUser(at.mukprojects.vuniapp
     * .models.User)
     */
    @Override
    public int updateUser(final UniversityUser setUser,
            final UniversityUser whereUser) throws ReadException,
            UpdateException {
        Log.i(TAG, "Methode: updateUser wird gestartet.");

        Log.d(TAG,
                "Es wird eine SQL- UpdateQuery für die übergebenen User erzeugt.");
        if (!validateUpdateParameter(setUser)) {
            Log.e(TAG, "Der übergebene setUser war null oder"
                    + " alle Parameter waren null, es konnte"
                    + " keine Query erstellt werden.");
            throw new UpdateException("Der übergebene setUser ist ungülitg.");
        }

        int editRows = 0;
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (updateUser) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            statement = writableDatabase.compileStatement(QueryBuilder
                    .buildUpdateQuery(setUser, whereUser));
            Log.d(TAG,
                    "UpdateQuery: "
                            + QueryBuilder.buildUpdateQuery(setUser, whereUser));

            int updateCounter = 1;
            /** SET Parameter */
            if (setUser.getUsername() != null) {
                statement.bindString(updateCounter, setUser.getUsername());
                updateCounter++;
            }
            if (setUser.getPassword() != null) {
                statement.bindString(updateCounter, setUser.getPassword());
                updateCounter++;
            }
            if (setUser.getUniversity() != null) {
                statement.bindString(updateCounter, setUser.getUniversity()
                        .getKeyName());
                updateCounter++;
            }
            if (setUser.getUserKey() != null) {
                statement.bindString(updateCounter, setUser.getUserKey());
                updateCounter++;
            }
            /** WHERE Parameter */
            if (whereUser != null) {
                if (whereUser.getId() != null) {
                    statement.bindLong(updateCounter, whereUser.getId());
                    updateCounter++;
                }
                if (whereUser.getUsername() != null) {
                    statement
                            .bindString(updateCounter, whereUser.getUsername());
                    updateCounter++;
                }
                if (whereUser.getPassword() != null) {
                    statement
                            .bindString(updateCounter, whereUser.getPassword());
                    updateCounter++;
                }
                if (setUser.getUniversity() != null) {
                    statement.bindString(updateCounter, whereUser
                            .getUniversity().getKeyName());
                    updateCounter++;
                }
                if (setUser.getUserKey() != null) {
                    statement.bindString(updateCounter, whereUser.getUserKey());
                }
            }

            /** Ermittlung der Anzahl der zu &auml;ndernden User */
            editRows = getUsers(whereUser).size();
            statement.execute();
            Log.d(TAG, "Die Transaktion (updateUser)"
                    + " wird als erfolgreich Makiert.");
            writableDatabase.setTransactionSuccessful();
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, sqliteException.getMessage(), sqliteException);
            throw new UpdateException(sqliteException.getMessage(),
                    sqliteException);
        } finally {
            /**
             * Eine Transaktion, welche nicht auf erfolgreich gesetzt wurde wird
             * automatisch einem Rollback unterzogen.
             */
            Log.d(TAG, "Die Transaktion (updateUser) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }
        Log.i(TAG, "Methode: updateUser wird verlassen.");
        return editRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.UserDAO#deleteUser(at.mukprojects.vuniapp
     * .models.User)
     */
    @Override
    public int deleteUser(final UniversityUser user) throws ReadException,
            DeleteException {
        Log.i(TAG, "Methode: deleteUser wird gestartet.");

        Log.d(TAG,
                "Es wird eine SQL- DeleteQuery für die übergebenen User erzeugt.");
        int editRows = 0;
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (deleteUser) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            Log.d(TAG, "DeleteQuery: " + QueryBuilder.buildDeleteQuery(user));
            statement = writableDatabase.compileStatement(QueryBuilder
                    .buildDeleteQuery(user));

            int updateCounter = 1;
            /** WHERE Parameter */
            if (user != null) {
                if (user.getId() != null) {
                    statement.bindLong(updateCounter, user.getId());
                    updateCounter++;
                }
                if (user.getUsername() != null) {
                    statement.bindString(updateCounter, user.getUsername());
                    updateCounter++;
                }
                if (user.getPassword() != null) {
                    statement.bindString(updateCounter, user.getPassword());
                    updateCounter++;
                }
                if (user.getUniversity() != null) {
                    statement.bindString(updateCounter, user.getUniversity()
                            .getKeyName());
                    updateCounter++;
                }
                if (user.getUserKey() != null) {
                    statement.bindString(updateCounter, user.getUserKey());
                }
            }

            /** Ermittlung der Anzahl der zu l&ouml;schenden User */
            editRows = getUsers(user).size();
            statement.execute();
            Log.d(TAG, "Die Transaktion (deleteUser)"
                    + " wird als erfolgreich Makiert.");
            writableDatabase.setTransactionSuccessful();
        } catch (SQLiteException sqliteException) {
            Log.e(TAG, sqliteException.getMessage(), sqliteException);
            throw new DeleteException(sqliteException.getMessage(),
                    sqliteException);
        } finally {
            /**
             * Eine Transaktion, welche nicht auf erfolgreich gesetzt wurde wird
             * automatisch einem Rollback unterzogen.
             */
            Log.d(TAG, "Die Transaktion (deleteUser) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }
        Log.i(TAG, "Methode: deleteUser wird verlassen.");
        return editRows;
    }

    /**
     * Validiert einen User, ob er in die Datenbank geschrieben werden kann.
     * 
     * @param user
     *            Der zu pr&uuml;fende User.
     * @return Liefert True falls der User valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateInputParameter(final UniversityUser user) {
        if (user == null) {
            return false;
        }
        if (user.getUsername() == null || user.getPassword() == null
                || user.getUniversity() == null || user.getUserKey() == null) {
            return false;
        }
        return true;
    }

    /**
     * Validiert einen User, ob er in die Datenbank geschrieben werden kann.
     * 
     * @param setUser
     *            Der zu pr&uuml;fende User.
     * @return Liefert True falls der User valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateUpdateParameter(final UniversityUser setUser) {
        if (setUser == null) {
            return false;
        }
        if (setUser.getUsername() == null && setUser.getPassword() == null
                && setUser.getUniversity() == null
                && setUser.getUserKey() == null) {
            return false;
        }
        return true;
    }

    /**
     * Die Klasse QueryBuilder beinhaltet Methoden mit dessen Hilfe aus einem
     * User eine SQL Query erzeugt werden kann.
     * 
     * @author Mathias
     */
    static class QueryBuilder {
        /**
         * Erzeugt eine SQL Delete Query aus dem &uuml;bergebenen User.
         * 
         * @param user
         *            User f&uuml;r das Delete Statement.
         * @return Eine Prepared Statement als String.
         */
        static String buildDeleteQuery(final UniversityUser user) {
            String query = "DELETE FROM " + TABLENAME;
            if (user == null) {
                return query;
            } else {
                query += build(user);
            }
            return query + ";";
        }

        /**
         * Erzeugt eine SQL Update Query aus dem &uuml;bergebenen Usern.
         * 
         * @param setUser
         *            User f&uuml;r das Set Statement.
         * @param whereUser
         *            User f&uuml;r das Where Statement.
         * @return Eine Prepared Statement als String.
         */
        static String buildUpdateQuery(final UniversityUser setUser,
                final UniversityUser whereUser) {
            String query = "UPDATE " + TABLENAME;
            query += buildSet(setUser);
            if (whereUser == null) {
                return query;
            } else {
                query += build(whereUser);
            }
            return query + ";";
        }

        /**
         * Erstellt die Set- Query.
         * 
         * @param setUser
         *            User f&uuml;r das Set Statement.
         * @return Den Set- Teil der Query.
         */
        public static String buildSet(final UniversityUser setUser) {
            ArrayList<String> args = new ArrayList<String>();
            String query = "";

            if (setUser.getUsername() != null) {
                args.add(COLUMN_USERNAME);
            }
            if (setUser.getPassword() != null) {
                args.add(COLUMN_PASSWORD);
            }
            if (setUser.getUniversity() != null) {
                args.add(COLUMN_UNIVERSITY);
            }
            if (setUser.hasUserKey()) {
                args.add(COLUMN_UNIVERSITYKEY);
            }

            if (args.size() == 0) {
                return query;
            } else {
                for (String column : args) {
                    if (query.length() != 0) {
                        query += ", ";
                    }
                    query += column + " = ?";
                }
            }

            return " SET " + query;
        }

        /**
         * Erzeugt eine SQL Selection Query aus dem &uuml;bergebenen User.
         * 
         * @param user
         *            Der User, aus dem die Query erzeugt wird.
         * @return Eine Prepared Statement als String.
         */
        static String buildSelectQuery(final UniversityUser user) {
            String query = "SELECT * FROM " + TABLENAME;
            if (user == null) {
                return query;
            } else {
                query += build(user);
            }
            return query + ";";
        }

        /**
         * Erstellt aus einem User ein Array mit den Suchkriterien.
         * 
         * @param user
         *            Der User anhand dessen die Kriterien bestimmt werden.
         * @return String Array der Suchkriterien.
         */
        static String[] getSelectionArgs(final UniversityUser user) {
            if (user == null) {
                return new String[0];
            }
            ArrayList<String> args = new ArrayList<String>();
            if (user.getId() != null) {
                args.add(user.getId().toString());
            }
            if (user.getUsername() != null) {
                args.add(user.getUsername());
            }
            if (user.getPassword() != null) {
                args.add(user.getPassword());
            }
            if (user.getUniversity() != null) {
                args.add(user.getUniversity().getKeyName());
            }
            if (user.hasUserKey()) {
                args.add(user.getUserKey());
            }

            String[] ret = new String[args.size()];
            args.toArray(ret);
            return ret;
        }

        /**
         * Die Methode build erstellt die Where Bedingungen der Query.
         * 
         * @param user
         *            Der User, aus dem die Query erzeugt wird.
         * @return Einen String aus den Where Bedingungen.
         */
        private static String build(final UniversityUser user) {
            ArrayList<String> args = new ArrayList<String>();
            String query = "";

            if (user.getId() != null) {
                args.add(COLUMN_ID);
            }
            if (user.getUsername() != null) {
                args.add(COLUMN_USERNAME);
            }
            if (user.getPassword() != null) {
                args.add(COLUMN_PASSWORD);
            }
            if (user.getUniversity() != null) {
                args.add(COLUMN_UNIVERSITY);
            }
            if (user.hasUserKey()) {
                args.add(COLUMN_UNIVERSITYKEY);
            }

            if (args.size() == 0) {
                return query;
            } else {
                for (String column : args) {
                    if (query.length() != 0) {
                        query += " AND ";
                    }
                    query += column + " = ?";
                }
            }

            return " WHERE " + query;
        }
    }
}
