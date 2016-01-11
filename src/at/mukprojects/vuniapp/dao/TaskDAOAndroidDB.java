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
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.storages.VUniAppDBConnection;

/**
 * Die Klasse TaskDAOAndroidDB implementiert die Methoden des TaskDAO
 * Interfaces. Die Tasks werden in einer Android Datenbank lokal auf dem
 * Smartphone gespeichert.
 * 
 * @author Mathias
 */
public class TaskDAOAndroidDB implements TaskDAO {
    private static final String TAG                 = TaskDAOAndroidDB.class
                                                            .getSimpleName();
    /** Datenbank der DAO. */
    private VUniAppDBConnection database;

    /** Name der User Tabelle. */
    private static final String TABLENAME           = "Task";

    /** Spalten der Tabelle User. */
    private static final String COLUMN_ID           = "id";
    private static final String COLUMN_TITLE        = "title";
    private static final String COLUMN_DESCRIPTION  = "description";
    private static final String COLUMN_COLOR        = "color";
    private static final String COLUMN_DEADLINEDATE = "deadlineDate";
    private static final String COLUMN_IMAGEPATH    = "imagePath";

    /**
     * Erstellt eine neue TaskDAOAndroidDB.
     * 
     * @param context
     *            Context der Applikation.
     * @throws ConnectionException
     *             Diese Exception wird geworfen, wenn es w&auml;hrend dem
     *             Verbindungsaufbau mit dem Storage zu einem Fehler kommt.
     */
    public TaskDAOAndroidDB(final Context context) throws ConnectionException {
        database = VUniAppDBConnection.getInstance(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.TaskDAO#getTaskById(long)
     */
    @Override
    public final Task getTaskById(final long id) throws ReadException {
        Log.i(TAG, "Methode: getTaskById wird gestartet.");
        Task searchTask = new Task(null, null, null, null, null);
        searchTask.setId(id);
        List<Task> retList = getTasks(searchTask);
        Task retTask = null;
        if (retList.size() > 0) {
            retTask = retList.get(0);
        }
        Log.i(TAG, "Methode: getTaskById wird verlassen.");
        return retTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.TaskDAO#getTasks(at.mukprojects.vuniapp.models
     * .Task)
     */
    @Override
    public final List<Task> getTasks(final Task searchTask)
            throws ReadException {
        Log.i(TAG, "Methode: getTasks wird gestartet.");
        Log.d(TAG, "Es wird eine SQL- SelectQuery für den"
                + " übergebenen Task erzeugt.");

        ArrayList<Task> retList = new ArrayList<Task>();
        SQLiteDatabase readableDatabase = database.getReadableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (getTasks) gestartet.");
        readableDatabase.beginTransaction();

        Cursor cursor = null;
        try {
            Log.d(TAG, "RawQuery: " + QueryBuilder.buildSelectQuery(searchTask));
            String debugArgs = "";
            for (String arg : QueryBuilder.getSelectionArgs(searchTask)) {
                debugArgs += arg + " ";
            }
            Log.d(TAG, "RawQueryArgs: ( " + debugArgs + ")");

            cursor = readableDatabase.rawQuery(
                    QueryBuilder.buildSelectQuery(searchTask),
                    QueryBuilder.getSelectionArgs(searchTask));

            if (cursor.moveToFirst()) {
                Task listItem = new Task(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getLong(4), cursor.getString(5));
                listItem.setId(cursor.getLong(0));
                retList.add(listItem);

                while (cursor.moveToNext()) {
                    listItem = new Task(cursor.getString(1),
                            cursor.getString(2), cursor.getString(3),
                            cursor.getLong(4), cursor.getString(5));
                    listItem.setId(cursor.getLong(0));
                    retList.add(listItem);
                }
            }

            Log.d(TAG, "Die Transaktion (getTasks)"
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
            Log.d(TAG, "Die Transaktion (getTasks) wird beendet.");
            readableDatabase.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG, "Methode: getTasks wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.TaskDAO#getAllTasks()
     */
    @Override
    public final List<Task> getAllTasks() throws ReadException {
        Log.i(TAG, "Methode: getAllTasks wird gestartet.");
        List<Task> retList = getTasks(null);
        Log.i(TAG, "Methode: getAllTasks wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.TaskDAO#createTask(at.mukprojects.vuniapp.
     * models.Task)
     */
    @Override
    public final Task createTask(final Task createTask) throws CreateException {
        Log.i(TAG, "Methode: createTask wird gestartet.");
        Log.d(TAG, "Es wird eine SQL- InsertQuery für"
                + " den übergebenen User erzeugt.");
        if (!validateInputParameter(createTask)) {
            Log.e(TAG, "Der Methode (createTask) darf keinen ungültiger Task"
                    + " übergeben werden.");
            throw new CreateException("Der übergebene Task muss gültig sein. "
                    + "(Title und Description dürfen nicht null sein)");
        }

        Task retTask = new Task(createTask.getTitle(),
                createTask.getDescription(), createTask.getColor(),
                createTask.getDeadlineDate(), createTask.getImagePath());
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (createTask) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            statement = writableDatabase.compileStatement("INSERT INTO "
                    + TABLENAME + " (" + COLUMN_TITLE + ", "
                    + COLUMN_DESCRIPTION + ", " + COLUMN_COLOR + ", "
                    + COLUMN_DEADLINEDATE + ", " + COLUMN_IMAGEPATH
                    + ") VALUES(?, ?, ?, ?, ?);");
            statement.bindString(1, createTask.getTitle());
            statement.bindString(2, createTask.getDescription());
            if (createTask.getColor() != null) {
                statement.bindString(3, createTask.getColor());
            } else {
                statement.bindNull(3);
            }
            if (createTask.getDeadlineDate() != null) {
                statement.bindLong(4, createTask.getDeadlineDate());
            } else {
                statement.bindNull(4);
            }
            if (createTask.getImagePath() != null) {
                statement.bindString(5, createTask.getImagePath());
            } else {
                statement.bindNull(5);
            }

            long lastUserID = statement.executeInsert();
            if (lastUserID != -1) {
                retTask.setId(lastUserID);
                Log.d(TAG, "Die Transaktion (createTask)"
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
            Log.d(TAG, "Die Transaktion (createTask) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }

        Log.i(TAG, "Methode: createTask wird verlassen.");
        return retTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.TaskDAO#updateTasks(at.mukprojects.vuniapp
     * .models.Task, at.mukprojects.vuniapp.models.Task)
     */
    @Override
    public final int updateTasks(final Task setTask, final Task whereTask)
            throws UpdateException, ReadException {
        Log.i(TAG, "Methode: updateTasks wird gestartet.");

        Log.d(TAG,
                "Es wird eine SQL- UpdateQuery für die übergebenen Task erzeugt.");
        if (!validateUpdateParameter(setTask)) {
            Log.e(TAG, "Der übergebene Task war null oder"
                    + " alle Parameter waren null, es konnte"
                    + " keine Query erstellt werden.");
            throw new UpdateException("Der übergebene setUser ist ungülitg.");
        }

        int editRows = 0;
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (updateTasks) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            statement = writableDatabase.compileStatement(QueryBuilder
                    .buildUpdateQuery(setTask, whereTask));
            Log.d(TAG,
                    "UpdateQuery: "
                            + QueryBuilder.buildUpdateQuery(setTask, whereTask));

            int updateCounter = 1;
            /** SET Parameter */
            if (setTask.getTitle() != null) {
                statement.bindString(updateCounter, setTask.getTitle());
                updateCounter++;
            }
            if (setTask.getDescription() != null) {
                statement.bindString(updateCounter, setTask.getDescription());
                updateCounter++;
            }
            if (setTask.getColor() != null) {
                statement.bindString(updateCounter, setTask.getColor());
                updateCounter++;
            }
            if (setTask.getDeadlineDate() != null) {
                statement.bindLong(updateCounter, setTask.getDeadlineDate());
                updateCounter++;
            }
            if (setTask.getImagePath() != null) {
                statement.bindString(updateCounter, setTask.getImagePath());
                updateCounter++;
            }
            /** WHERE Parameter */
            if (whereTask != null) {
                if (whereTask.getId() != null) {
                    statement.bindLong(updateCounter, whereTask.getId());
                    updateCounter++;
                }
                if (whereTask.getTitle() != null) {
                    statement.bindString(updateCounter, whereTask.getTitle());
                    updateCounter++;
                }
                if (whereTask.getDescription() != null) {
                    statement.bindString(updateCounter,
                            whereTask.getDescription());
                    updateCounter++;
                }
                if (whereTask.getColor() != null) {
                    statement.bindString(updateCounter, whereTask.getColor());
                    updateCounter++;
                }
                if (whereTask.getDeadlineDate() != null) {
                    statement.bindLong(updateCounter,
                            whereTask.getDeadlineDate());
                    updateCounter++;
                }
                if (whereTask.getImagePath() != null) {
                    statement.bindString(updateCounter,
                            whereTask.getImagePath());
                    updateCounter++;
                }
            }

            /** Ermittlung der Anzahl der zu &auml;ndernden Tasks */
            editRows = getTasks(whereTask).size();
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
            Log.d(TAG, "Die Transaktion (updateTasks) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }

        Log.i(TAG, "Methode: updateTasks wird verlassen.");
        return editRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.TaskDAO#deleteTasks(at.mukprojects.vuniapp
     * .models.Task)
     */
    @Override
    public final int deleteTasks(final Task deleteTask) throws DeleteException,
            ReadException {
        Log.i(TAG, "Methode: deleteTasks wird gestartet.");

        Log.d(TAG, "Es wird eine SQL- DeleteQuery für"
                + " die übergebenen Task erzeugt.");
        int editRows = 0;
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        Log.d(TAG, "Es wird eine neue Transaktion (deleteTasks) gestartet.");
        writableDatabase.beginTransaction();

        SQLiteStatement statement = null;
        try {
            Log.d(TAG,
                    "DeleteQuery: " + QueryBuilder.buildDeleteQuery(deleteTask));
            statement = writableDatabase.compileStatement(QueryBuilder
                    .buildDeleteQuery(deleteTask));

            int updateCounter = 1;
            /** WHERE Parameter */
            if (deleteTask != null) {
                if (deleteTask.getId() != null) {
                    statement.bindLong(updateCounter, deleteTask.getId());
                    updateCounter++;
                }
                if (deleteTask.getTitle() != null) {
                    statement.bindString(updateCounter, deleteTask.getTitle());
                    updateCounter++;
                }
                if (deleteTask.getDescription() != null) {
                    statement.bindString(updateCounter,
                            deleteTask.getDescription());
                    updateCounter++;
                }
                if (deleteTask.getColor() != null) {
                    statement.bindString(updateCounter, deleteTask.getColor());
                    updateCounter++;
                }
                if (deleteTask.getDeadlineDate() != null) {
                    statement.bindLong(updateCounter,
                            deleteTask.getDeadlineDate());
                    updateCounter++;
                }
                if (deleteTask.getImagePath() != null) {
                    statement.bindString(updateCounter,
                            deleteTask.getImagePath());
                    updateCounter++;
                }
            }

            /** Ermittlung der Anzahl der zu l&ouml;schenden User */
            editRows = getTasks(deleteTask).size();
            statement.execute();
            Log.d(TAG, "Die Transaktion (deleteTasks)"
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
            Log.d(TAG, "Die Transaktion (deleteTask) wird beendet.");
            writableDatabase.endTransaction();
            if (statement != null) {
                statement.close();
            }
        }

        Log.i(TAG, "Methode: deleteUser wird verlassen.");
        return editRows;
    }

    /**
     * Validiert einen Task, ob er in die Datenbank geschrieben werden kann.
     * 
     * @param task
     *            Der zu pr&uuml;fende Task.
     * @return Liefert True falls der Task valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateInputParameter(final Task task) {
        if (task == null) {
            return false;
        }
        if (task.getTitle() == null || task.getDescription() == null) {
            return false;
        }
        return true;
    }

    /**
     * Validiert einen Task, ob er als Update Parameter verwendet werden kann.
     * 
     * @param task
     *            Der zu pr&uuml;fende Task.
     * @return Liefert True falls der Task valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateUpdateParameter(final Task task) {
        if (task == null) {
            return false;
        }
        if (task.getTitle() == null && task.getDescription() == null
                && task.getColor() == null && task.getDeadlineDate() == null
                && task.getImagePath() == null) {
            return false;
        }
        return validateInputParameter(task);
    }

    /**
     * Die Klasse QueryBuilder beinhaltet Methoden mit dessen Hilfe aus einem
     * Task eine SQL Query erzeugt werden kann.
     * 
     * @author Mathias
     */
    static class QueryBuilder {
        /**
         * Erzeugt eine SQL Delete Query aus dem &uuml;bergebenen Task.
         * 
         * @param task
         *            Task f&uuml;r das Delete Statement.
         * @return Eine Prepared Statement als String.
         */
        static String buildDeleteQuery(final Task task) {
            String query = "DELETE FROM " + TABLENAME;
            if (task == null) {
                return query;
            } else {
                query += build(task);
            }
            return query + ";";
        }

        /**
         * Erzeugt eine SQL Update Query aus dem &uuml;bergebenen Tasks.
         * 
         * @param setTask
         *            Task f&uuml;r das Set Statement.
         * @param whereTask
         *            User f&uuml;r das Where Statement.
         * @return Eine Prepared Statement als String.
         */
        static String buildUpdateQuery(final Task setTask, final Task whereTask) {
            String query = "UPDATE " + TABLENAME;
            query += buildSet(setTask);
            if (whereTask == null) {
                return query;
            } else {
                query += build(whereTask);
            }
            return query + ";";
        }

        /**
         * Erstellt die Set- Query.
         * 
         * @param setTask
         *            Task f&uuml;r das Set Statement.
         * @return Den Set- Teil der Query.
         */
        public static String buildSet(final Task setTask) {
            ArrayList<String> args = new ArrayList<String>();
            String query = "";

            if (setTask.getTitle() != null) {
                args.add(COLUMN_TITLE);
            }
            if (setTask.getDescription() != null) {
                args.add(COLUMN_DESCRIPTION);
            }
            if (setTask.getColor() != null) {
                args.add(COLUMN_COLOR);
            }
            if (setTask.getDeadlineDate() != null) {
                args.add(COLUMN_DEADLINEDATE);
            }
            if (setTask.getImagePath() != null) {
                args.add(COLUMN_IMAGEPATH);
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
         * Erzeugt eine SQL Selection Query aus dem &uuml;bergebenen Task.
         * 
         * @param task
         *            Der Task, aus dem die Query erzeugt wird.
         * @return Eine Prepared Statement als String.
         */
        static String buildSelectQuery(final Task task) {
            String query = "SELECT * FROM " + TABLENAME;
            if (task == null) {
                return query;
            } else {
                query += build(task);
            }
            return query + ";";
        }

        /**
         * Erstellt aus einem Task ein Array mit den Suchkriterien.
         * 
         * @param task
         *            Der Task anhand dessen die Kriterien bestimmt werden.
         * @return String Array der Suchkriterien.
         */
        static String[] getSelectionArgs(final Task task) {
            if (task == null) {
                return new String[0];
            }
            ArrayList<String> args = new ArrayList<String>();
            if (task.getId() != null) {
                args.add(task.getId().toString());
            }
            if (task.getTitle() != null) {
                args.add(task.getTitle());
            }
            if (task.getDescription() != null) {
                args.add(task.getDescription());
            }
            if (task.getColor() != null) {
                args.add(task.getColor());
            }
            if (task.getDeadlineDate() != null) {
                args.add(task.getDeadlineDate().toString());
            }
            if (task.getImagePath() != null) {
                args.add(task.getImagePath());
            }

            String[] ret = new String[args.size()];
            args.toArray(ret);
            return ret;
        }

        /**
         * Die Methode build erstellt die Where Bedingungen der Query.
         * 
         * @param task
         *            Der Task, aus dem die Query erzeugt wird.
         * @return Einen String aus den Where Bedingungen.
         */
        private static String build(final Task task) {
            ArrayList<String> args = new ArrayList<String>();
            String query = "";

            if (task.getId() != null) {
                args.add(COLUMN_ID);
            }
            if (task.getTitle() != null) {
                args.add(COLUMN_TITLE);
            }
            if (task.getDescription() != null) {
                args.add(COLUMN_DESCRIPTION);
            }
            if (task.getColor() != null) {
                args.add(COLUMN_COLOR);
            }
            if (task.getDeadlineDate() != null) {
                args.add(COLUMN_DEADLINEDATE);
            }
            if (task.getImagePath() != null) {
                args.add(COLUMN_IMAGEPATH);
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
