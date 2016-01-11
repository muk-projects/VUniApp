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

package at.mukprojects.vuniapp.activities.canteen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import android.webkit.WebView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.dao.CanteenDAOCache;
import at.mukprojects.vuniapp.dao.CanteenDAOWebRSS;
import at.mukprojects.vuniapp.models.Canteen;
import at.mukprojects.vuniapp.models.CanteenMenu;
import at.mukprojects.vuniapp.services.CanteenService;
import at.mukprojects.vuniapp.services.CanteenServiceCaching;
import at.mukprojects.vuniapp.universities.interfaces.CanteenInterface;

/**
 * Synchronisation des WebViews mit den anderen Elementen.
 * 
 * @author kerrim
 * @author Mathias
 */
class CanteenTask extends InternetBaseTask<Void, ArrayList<Canteen>> {
    private static final String TAG = CanteenTask.class.getSimpleName();

    private CanteenActivity     canteenActivity;
    private CanteenInterface    university;
    private String              choosenUniversity;
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
     * @param choosenUniversity
     *            Die gew&auml;hlte Universit&auml;t.
     * @param activityResultsWebView
     *            Der activityResultsWebView.
     */
    public CanteenTask(final String title, final String message,
            final CanteenActivity activity, final CanteenInterface university,
            final String choosenUniversity, final WebView activityResultsWebView) {
        super(title, message, false, activity);
        this.canteenActivity = activity;
        this.university = university;
        this.activityResultsWebView = activityResultsWebView;
        this.choosenUniversity = choosenUniversity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask#
     * executeBackgroundMethod(I[])
     */
    @Override
    protected ArrayList<Canteen> executeBackgroundMethod(final Void... params) {
        ArrayList<Canteen> output = new ArrayList<Canteen>();
        try {
            output.add(university.getCanteen(choosenUniversity));
        } catch (IOException e) {
            super.ioException = e;
        }
        return output;
    }

    @Override
    protected void onPostExecute(final ArrayList<Canteen> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            String html = "<html><body>";
            for (Canteen actMensa : result) {

                Log.d(TAG, actMensa.getMensaMenus().keySet().toString());
                ArrayList<String> mensaMenusNameList = new ArrayList<String>(
                        actMensa.getMensaMenus().keySet());
                Collections.sort(mensaMenusNameList);
                Log.d(TAG, mensaMenusNameList.toString());

                for (String mms : mensaMenusNameList) {
                    CanteenMenu mm = actMensa.getMensaMenus().get(mms);

                    html += "<h3>" + mm.getName() + "</h3><hr />";

                    ArrayList<Long> mealsDateList = new ArrayList<Long>(mm
                            .getMeals().keySet());
                    Collections.sort(mealsDateList);

                    for (Long d : mealsDateList) {
                        html += mm.getMeals().get(d) + "<br /><hr />";
                    }
                }
            }
            html += "</body></html>";

            activityResultsWebView.getSettings().setBuiltInZoomControls(true);
            activityResultsWebView.loadDataWithBaseURL("http://mensa", html,
                    "text/html", "utf-8", "");

        }
        Log.i(TAG, "ASyncTask: RefreshViewTask wird verlassen.");
    }
}
