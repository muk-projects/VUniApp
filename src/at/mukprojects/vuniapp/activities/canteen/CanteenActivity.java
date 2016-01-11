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

import android.os.Bundle;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.dates.DatesActivity;
import at.mukprojects.vuniapp.baseclasses.activities.ActivityWithWebView;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.CanteenInterface;

/**
 * Die Activity kann zu unterschiedlichen Mensen die Menus anzeigen. Weiters ist
 * es m&ouml;glich sich f&uuml;r einen Tag alle Menus der unterschiedlichen
 * Wiener Mensen anzeigen zu lassen.
 * 
 * @author kerrim
 */
public class CanteenActivity extends ActivityWithWebView<CanteenInterface> {
    private static final String TAG       = CanteenActivity.class
                                                  .getSimpleName();
    private static final String PREF_FILE = "PrefFileMensa";

    /**
     * Erstellt eine neue DatesActivity.
     */
    public CanteenActivity() {
        super(PREF_FILE, Universities.getInstance()
                .getUniversitiesWithCanteens(), R.layout.activity_mensa,
                R.id.activity_mensa_Mensa, R.id.activity_mensa_WebView);
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        super.onCreate(savedInstanceState, DatesActivity.class, getResources()
                .getString(R.string.activity_mensa));

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.baseclasses.activities.ActivityWithWebView#
     * spinnerItemSelected()
     */
    @Override
    protected final void spinnerItemSelected() {
        Log.i(TAG, "Methode: spinnerItemSelected wird gestartet.");

        (new CanteenTask(getResources().getString(R.string.activity_mensa),
                getResources().getString(R.string.activity_mensa_loading),
                this, universities.get((String) activityUniversity
                        .getSelectedItem()),
                (String) activityUniversity.getSelectedItem(), activityContent))
                .execute();

        Log.i(TAG, "Methode: spinnerItemSelected wird verlassen.");
    }
}