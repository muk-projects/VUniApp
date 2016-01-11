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

import android.os.Bundle;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.ActivityWithWebView;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.DatesInterface;

/**
 * Die DatesActivity zeigt Termine der einzelenen Universit&auml;ten an.
 * 
 * @author Mathias
 */
public class DatesActivity extends ActivityWithWebView<DatesInterface> {
    private static final String TAG       = DatesActivity.class.getSimpleName();
    private static final String PREF_FILE = "PrefFileDates";

    /**
     * Erstellt eine neue DatesActivity.
     */
    public DatesActivity() {
        super(PREF_FILE, Universities.getInstance().getUniversitiesWithDates(),
                R.layout.activity_dates, R.id.activity_dates_spinner,
                R.id.activity_dates_webview);
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        super.onCreate(savedInstanceState, DatesActivity.class, getResources()
                .getString(R.string.activity_dates));

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

        (new DatesTask(getResources().getString(R.string.activity_dates),
                getResources().getString(R.string.activity_dates_loading),
                this, universities.get((String) activityUniversity
                        .getSelectedItem()), activityContent)).execute();

        Log.i(TAG, "Methode: spinnerItemSelected wird verlassen.");
    }
}