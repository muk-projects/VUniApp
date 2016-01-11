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

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import at.mukprojects.vuniapp.exceptions.NoInternetConnectionException;

/**
 * Basisklasse aller AsyncTask mit Verbindung zum Internet, um die
 * Fehlerbehandlung zu vereinheitlichen und einen ConnectionCheck
 * auszuf&uuml;hren. Standardm&auml;ssig ist ein InternetConnectionException
 * immer kritisch, dies kann aber mit einem speziellen Konstruktor ge&auml;ndert
 * werden. Erbt die Fehlerbehandlung des Basetasks.
 * 
 * @author Mathias
 * 
 * @param <I>
 *            Input
 * @param <A>
 *            Output
 */
public abstract class InternetBaseTask<I, A> extends BaseTask<I, A> {

    private boolean critical;

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
    public InternetBaseTask(final Activity activity) {
        super(activity);
        critical = true;
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
    public InternetBaseTask(final String titel, final String message,
            final boolean progressBar, final Activity activity) {
        super(titel, message, progressBar, activity);
        critical = true;
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
     * @param critical
     *            Mit Hilfe dieses Parameters kann bestimmt werden, ob bei einer
     *            noInternetException der Fehler kritisch oder unkritisch
     *            behandelt wird.
     */
    public InternetBaseTask(final String titel, final String message,
            final boolean progressBar, final Activity activity,
            final boolean critical) {
        super(titel, message, progressBar, activity);
        this.critical = critical;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected final A doInBackground(final I... params) {
        if (isOnline()) {
            return executeBackgroundMethod(params);
        } else {
            super.internetConnectionException = new NoInternetConnectionException(
                    critical);
            return null;
        }
    }

    /**
     * Die Methode wird von der erbenden Klasse &uuml;berschrieben und
     * enth&auml;lt die eigetnliche Logik. Sie wird von doInBackground
     * aufgerufen, wenn eine Connection vorhanden ist.
     * 
     * @param params
     *            Parameter von doInBackground.
     * @return R&uuml;ckgabewert f&uuml;r doInBackground.
     */
    protected abstract A executeBackgroundMethod(final I... params);

    /**
     * Prüft, ob eine Internetverbindung aufgebaut werden kann.
     * 
     * @return True, falls Internet vorhanden ist, andernfalls False.
     */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Setzt den Parameter, welcher angibt, ob es sich um eine kritsche
     * InternetConnectionException oder um eine unkritische handelt.
     * 
     * @param critical
     *            Parameter, welcher angibt, ob es sich um eine kritsche
     *            InternetConnectionException handelt.
     */
    public final void setCritical(boolean critical) {
        this.critical = critical;
    }
}
