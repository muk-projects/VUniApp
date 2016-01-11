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

package at.mukprojects.vuniapp.activities.dates;

import java.io.IOException;

import android.webkit.WebView;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.exceptions.IOExceptionWithCritical;
import at.mukprojects.vuniapp.universities.interfaces.DatesInterface;

/**
 * Professoren suchen und Listview updaten.
 * 
 * @author Mathias
 */
class DatesTask extends InternetBaseTask<Void, String> {
    private static final String TAG = DatesTask.class.getSimpleName();

    private DatesActivity       datesActivity;
    private DatesInterface      university;
    private WebView             activityResultsWebView;

    /**
     * Erstellt einen neuen Task.
     * 
     * @param title
     *            Titel des Dialogs.
     * @param message
     *            Message des Dialogs.
     * @param activity
     *            Activity, welche den Task ausf&uuml;hrt.
     * @param university
     *            Universit&auml;t zum ausf&uuml;hren der Terminsuche.
     * @param activityResultsWebView
     *            Der activityResultsWebView.
     */
    public DatesTask(final String title, final String message,
            final DatesActivity activity, final DatesInterface university,
            final WebView activityResultsWebView) {
        super(title, message, false, activity);
        this.datesActivity = activity;
        this.university = university;
        this.activityResultsWebView = activityResultsWebView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask#
     * executeBackgroundMethod(I[])
     */
    @Override
    protected String executeBackgroundMethod(final Void... params) {
        String result = "";

        try {
            result = university.getUniversityDates().getHtml();
        } catch (IOException ioException) {
            super.ioException = new IOExceptionWithCritical(
                    ioException.getMessage(), ioException, true);

        }
        
        return result;
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            activityResultsWebView.loadDataWithBaseURL("http://dates", result,
                    "text/html", "utf-8", "");
        }
    }
}