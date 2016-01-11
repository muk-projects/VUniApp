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

package at.mukprojects.vuniapp.baseclasses.tasks;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.exceptions.ListEmptyException;
import at.mukprojects.vuniapp.exceptions.NoInternetConnectionException;
import at.mukprojects.vuniapp.exceptions.NoUniversitiesException;
import at.mukprojects.vuniapp.exceptions.NoUniversityUsersException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.exceptions.base.CriticalException;

//CHECKSTYLE OFF

/**
 * Basisklasse aller AsyncTask, um die Fehlerbehandlung zu vereinheitlichen.
 * Wenn ein Fehler in w&auml;hrend der ausf&uuml;hrung auftritt so kann dieser
 * mittels super."entsprechndem Fehler" dem BaseTask &uuml;bergeben werden. Dem
 * Fehler kann mittels dem Interface CriticalException ein Wert mitgegeben
 * werden, welcher aussagt, ob es sich um einen schweren Fehler oder, um einen
 * leichten Fehler handelt. Bei leichten Fehlern wird der User gefragt, ob er
 * trotz Fehler fortsetzten möchte. Bei schweren Fehleren wird der Task in
 * jedemfall abgebrochen. Wenn die Exception das Interface nicht implementiert
 * hat wird immer von einem schweren Fehler ausgegangen. Wenn ein unkritische
 * Fehler &uuml;bergeben wird sollte auch die Methode onTaskErrorResume
 * &uuml;berschrieben werden.
 * 
 * @author Mathias
 * 
 * @param <I>
 *            Input
 * @param <A>
 *            Output
 */
public abstract class BaseTask<I, A> extends AsyncTask<I, Integer, A> {
    private static final String             TAG = BaseTask.class
                                                        .getSimpleName();

    private boolean                         dialog;
    private ProgressDialog                  progressDialog;
    private String                          titel;
    private String                          message;
    private boolean                         progressBar;

    protected Activity                      activity;
    protected boolean                       noSuperError;

    private ArrayList<ExceptionBundle>      exceptionBundles;

    protected ConnectionException           connectionException;
    protected CreateException               createException;
    protected ReadException                 readException;
    protected UpdateException               updateException;
    protected DeleteException               deleteException;
    protected IOException                   ioException;
    protected JSONException                 jsonException;
    protected InvalidLoginException         invalidLoginException;
    protected NoInternetConnectionException internetConnectionException;
    protected ListEmptyException            listEmptyException;
    protected NoUniversitiesException       noUniversitiesException;
    protected NoUniversityUsersException    noUniversityUsersException;
    protected NotInitializedException       notInitializedException;

    /**
     * Erstellt einen neuen Task.
     * <p>
     * Bei der Verwendung dieses Konstruktors, sollte die onPreExecute Methode
     * &uuml;berschrieben werden, da der User sonst kein Feedback f&uuml;r die
     * Dauer des Tasks erh&auml;lt.
     * </p>
     * 
     * @param activity
     *            Context der aufrufenden Activity.
     */
    public BaseTask(final Activity activity) {
        dialog = false;
        noSuperError = true;
        this.activity = activity;
        exceptionBundles = new ArrayList<ExceptionBundle>();
    }

    /**
     * Erstellt einen neuen Task.
     * 
     * <p>
     * Bei der Verwendung dieses Konstruktors wird ein Dialog durch die BaseTask
     * Klasse erzeugt und daher sollte onPostExecute nicht &uuml;berschrieben
     * werden bzw. super.postExecute gegebenfalls aufgerufen werden.
     * </p>
     * 
     * @param titel
     *            Titel des Progress Dialogs.
     * @param message
     *            Text des Progress Dialogs.
     * @param progressBar
     *            Style des Progress Dialogs. Wenn Sie die progressBar auf true
     *            setzten ist zu bedenken, dass es notwenig ist w&auml;hrend der
     *            doInBackground Methode den aktuellen Fortschritt zu
     *            aktualisieren. Der ProgressDialog wird immer mit setMax(100)
     *            inizialisiert, daher sollte der Fortschritt einen
     *            prozentuellen Charakter haben.
     * @param activity
     *            Context der aufrufenden Activity.
     */
    public BaseTask(final String titel, final String message,
            final boolean progressBar, final Activity activity) {
        dialog = true;
        noSuperError = true;
        this.titel = titel;
        this.message = message;
        this.progressBar = progressBar;
        this.activity = activity;
        exceptionBundles = new ArrayList<ExceptionBundle>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        if (dialog) {
            if (progressBar) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle(titel);
                progressDialog.setMessage(message);
                progressDialog.setMax(100);
                progressDialog
                        .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
            } else {
                progressDialog = ProgressDialog.show(activity, titel, message);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onProgressUpdate(Progress[])
     */
    @Override
    protected void onProgressUpdate(final Integer... values) {
        if (progressBar && progressDialog != null) {
            progressDialog.setProgress(values[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(A result) {
        Log.d(TAG, "Die Methode onPostExecute führt"
                + " eine Fehlerbehandelung druch.");

        if (connectionException != null) {
            Log.e(TAG, connectionException.getMessage(), connectionException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_connection_titel), activity
                    .getString(R.string.exception_connection_message),
                    connectionException));
        } else if (createException != null) {
            Log.e(TAG, createException.getMessage(), createException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_create_titel), activity
                    .getString(R.string.exception_create_message),
                    createException));
        } else if (readException != null) {
            Log.e(TAG, readException.getMessage(), readException);
            exceptionBundles
                    .add(new ExceptionBundle(activity
                            .getString(R.string.exception_read_titel), activity
                            .getString(R.string.exception_read_message),
                            readException));
        } else if (updateException != null) {
            Log.e(TAG, updateException.getMessage(), updateException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_update_titel), activity
                    .getString(R.string.exception_update_message),
                    updateException));
        } else if (deleteException != null) {
            Log.e(TAG, deleteException.getMessage(), deleteException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_delete_titel), activity
                    .getString(R.string.exception_delete_message),
                    deleteException));
        } else if (ioException != null) {
            Log.e(TAG, ioException.getMessage(), ioException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_io_titel), activity
                    .getString(R.string.exception_io_message), ioException));
        } else if (jsonException != null) {
            Log.e(TAG, jsonException.getMessage(), jsonException);
            exceptionBundles
                    .add(new ExceptionBundle(activity
                            .getString(R.string.exception_json_titel), activity
                            .getString(R.string.exception_json_message),
                            jsonException));
        } else if (invalidLoginException != null) {
            Log.e(TAG, invalidLoginException.getMessage(),
                    invalidLoginException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_login_titel), activity
                    .getString(R.string.exception_login_message),
                    invalidLoginException));
        } else if (internetConnectionException != null) {
            Log.e(TAG, internetConnectionException.getMessage(),
                    internetConnectionException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_internet_titel), activity
                    .getString(R.string.exception_internet_message),
                    internetConnectionException));
        } else if (listEmptyException != null) {
            Log.e(TAG, listEmptyException.getMessage(),
                    internetConnectionException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_listempty_title), activity
                    .getString(R.string.exception_listempty_message),
                    listEmptyException));
        } else if (noUniversitiesException != null) {
            Log.e(TAG, noUniversitiesException.getMessage(),
                    noUniversitiesException);
            exceptionBundles
                    .add(new ExceptionBundle(
                            activity.getString(R.string.exception_nouniversities_title),
                            activity.getString(R.string.exception_nouniversities_message),
                            noUniversitiesException));
        } else if (noUniversityUsersException != null) {
            Log.e(TAG, noUniversityUsersException.getMessage(),
                    noUniversityUsersException);
            exceptionBundles
                    .add(new ExceptionBundle(
                            activity.getString(R.string.exception_nouniversityusers_title),
                            activity.getString(R.string.exception_nouniversityusers_message),
                            noUniversityUsersException));
        } else if (notInitializedException != null) {
            Log.e(TAG, notInitializedException.getMessage(),
                    notInitializedException);
            exceptionBundles.add(new ExceptionBundle(activity
                    .getString(R.string.exception_notInit_titel), activity
                    .getString(R.string.exception_notInit_message),
                    notInitializedException));
        }

        ExceptionBundle showBundle = null;
        boolean critical = false;
        if (exceptionBundles.size() > 0) {
            noSuperError = false;

            for (ExceptionBundle bundle : exceptionBundles) {
                if (bundle.isCritical()) {
                    showBundle = bundle;
                    critical = true;
                    break;
                } else {
                    showBundle = bundle;
                }
            }
        }

        if (!noSuperError) {
            Log.d(TAG, "Ein Fehler ist aufgetreten, es "
                    + "wird eine Fehler Dialog erstellt.");
            if (critical) {
                createErrorMessage(showBundle.getTitle(),
                        showBundle.getMessage()).show();
            } else {
                createErrorMessageWithOption(
                        showBundle.getTitle(),
                        showBundle.getMessage()
                                + "\n\n"
                                + activity
                                        .getString(R.string.exception_extend_message))
                        .show();
            }
        }

        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Es wurde versucht einen nicht zu"
                        + " Activity gehörendes Dialogfenster zu schließen.");
            }
        }
    }

    /**
     * Diese Methode wird ausgef&uuml;rt wenn der Task eine Exception wirft der
     * User diesen aber dennoch fortsetzten m&ouml;chte.
     */
    protected void onTaskErrorResume() {
        Log.d(TAG, "Der Task wird trotz Fehler fortgeführt.");
    }

    /**
     * Dieser Alertdialog kann dazu verwendet werden Fehlermeldungen anzuzeigen
     * und anschlie&szlig;end die Activity zu beenden.
     * 
     * @param titel
     *            Titel des Dialogs.
     * @param message
     *            Fehlermeldung, welche angezeigt werden soll.
     * @return Liefert den AlterDialog zur&uuml;ck.
     */
    private AlertDialog createErrorMessage(final String title,
            final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setNeutralButton(activity.getString(R.string.dialog_oKButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                        activity.finish();
                    }
                });

        return builder.create();
    }

    /**
     * Dieser Alertdialog kann dazu verwendet werden Fehlermeldungen anzuzeigen
     * und anschlie&szlig;end die Activity zu beenden oder dennoch fortzufahren.
     * 
     * @param titel
     *            Titel des Dialogs.
     * @param message
     *            Fehlermeldung, welche angezeigt werden soll.
     * @return Liefert den AlterDialog zur&uuml;ck.
     */
    private AlertDialog createErrorMessageWithOption(final String title,
            final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton(
                activity.getString(R.string.dialog_yesButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                        onTaskErrorResume();
                    }
                });
        builder.setNegativeButton(activity.getString(R.string.dialog_noButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                        activity.finish();
                    }
                });

        return builder.create();
    }

    class ExceptionBundle {
        private String    title;
        private String    message;
        private Exception exception;

        /**
         * Erstellt ein neues Bundle.
         * 
         * @param title
         *            Titel der Excpetion.
         * @param message
         *            Text der Exception.
         * @param exception
         *            Die Exception.
         */
        public ExceptionBundle(String title, String message, Exception exception) {
            super();
            this.title = title;
            this.message = message;
            this.exception = exception;
        }

        /**
         * Liefert den Titel.
         * 
         * @return Den Titel.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Liefert den Text.
         * 
         * @return Der Text.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Liefert die Exception.
         * 
         * @return Die Exception.
         */
        public Exception getException() {
            return exception;
        }

        /**
         * Liefert einen Wahrheitswert, ob es sich, um eine CrticalException
         * handelt. Wenn die Exception das Interface nicht implementiert hat
         * wird immer true zur&uuml;ckgegeben.
         * 
         * @return
         */
        public boolean isCritical() {
            if (exception instanceof CriticalException) {
                CriticalException criticalException = (CriticalException) exception;
                return criticalException.isCreiticalException();
            }
            return true;
        }
    }
}
