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

package at.mukprojects.vuniapp.baseclasses.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import at.mukprojects.vuniapp.R;

//CHECKSTYLE OFF

/**
 * Die Klasse ActivityWithWebView wird dazu verwendet, um die h&auml;ufig
 * verwendete Art von Activities zu abstrahieren. Dabei kann das Layout, welches
 * &uml;bergeben wird beliebig sein. Einzige vorraussetzunh es muss einen
 * Spinner und ein WebView enthalten. Das WebView reagiert dann auf
 * &Auml;nderungen des Spinners.
 * 
 * @author Mathias
 * @param <T>
 *            Der Parameter steht f&uuml;r das verwendete Interface.
 * @author Mathias
 */
public abstract class ActivityWithWebView<T> extends VUniAppActivity {
    private static final String   TAG                = ActivitySearchListWithDetailFragment.class
                                                             .getSimpleName();

    /** Preference File. */
    protected static final String PREF_LAST_USED_UNI = "lastUniversityUsed";
    private String                prefFileString;
    protected SharedPreferences   prefFile;

    /** Content Elemente. */
    protected Spinner             activityUniversity;
    protected WebView             activityContent;

    /** Liste der verwendeten Universit&auuml;ten. */
    protected HashMap<String, T>  universities;

    /** IDs. */
    private int                   layoutID;
    private int                   spinnerID;
    private int                   webViewID;

    /**
     * Super Konstruktor eines ActivityWithWebView.
     * 
     * @param prefFileString
     *            Name des Sharedpreference Files der Activity.
     * @param universities
     *            Liste aller zu verwendeten Universit&auuml;ten.
     * @param layoutID
     *            Die ID des zu verwendenden Layouts.
     * @param spinnerID
     *            Die ID des zu verwendenden Spinners.
     * @param webViewID
     *            Die ID des zu verwendenden WebViews.
     */
    public ActivityWithWebView(final String prefFileString,
            final HashMap<String, T> universities, final int layoutID,
            final int spinnerID, final int webViewID) {
        super();
        super.setCurrentActivity(this);
        this.prefFileString = prefFileString;
        this.universities = universities;
        this.layoutID = layoutID;
        this.spinnerID = spinnerID;
        this.webViewID = webViewID;
    }

    /**
     * Wird beim Erstellen der Activity aufgerufen.
     * 
     * @param savedInstanceState
     *            Bundle welches an die ActivityKlasse weitergereicht wird.
     * @param activityClass
     *            Klasse der Activity, welche f&uuml;r die Daten zust&auml;ndig
     *            ist. Diese bestimmt unter anderem welche Navigation angezeigt
     *            wird.
     * @param activeNavigationItem
     *            Der Aktuelle Name des Navigationsitems indem sich die App
     *            befindet.
     */
    protected final void onCreate(final Bundle savedInstanceState,
            final Class<? extends Activity> activityClass,
            final String activeNavigationItem) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        setContentView(layoutID);
        super.setActiveNavigationItem(activeNavigationItem);
        super.onCreate(savedInstanceState, activityClass);
        super.language = getString(R.string.title);
        
        super.setActionbarBackground(R.drawable.actionbar_background_red);
        
        activityUniversity = (Spinner) findViewById(spinnerID);
        activityContent = (WebView) findViewById(webViewID);
        
        setUpLayout();

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /**
     * Diese Methode setzt alle n&ouml;tigen Gui Einstellungen. Bevor diese
     * Methode aufgerufen werden kann m&uuml;ssen alle Variablen der Basisklasse
     * gesetzt worden sein.
     */
    private void setUpLayout() {
        Log.i(TAG, "Methode: setUpLayout wird gestartet.");
        this.prefFile = getSharedPreferences(prefFileString, 0);

        ArrayList<String> universityNames = new ArrayList<String>();
        for (String name : universities.keySet()) {
            universityNames.add(name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, universityNames);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityUniversity.setAdapter(dataAdapter);

        String lastUniversityUsed = prefFile
                .getString(PREF_LAST_USED_UNI, null);
        if (lastUniversityUsed != null) {
            for (int i = 0; i < universityNames.size(); i++) {
                if (universityNames.get(i).equals(lastUniversityUsed)) {
                    activityUniversity.setSelection(i);
                    break;
                }
            }
        }

        activityUniversity
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> arg0,
                            final View arg1, final int arg2, final long arg3) {
                        spinnerItemSelected();
                    }

                    @Override
                    public void onNothingSelected(final AdapterView<?> arg0) {
                    }
                });

        Log.i(TAG, "Methode: setUpLayout wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStop()
     */
    @Override
    protected final void onStop() {
        Log.i(TAG, "Methode: onStop wird gestartet.");
        super.onStop();
        SharedPreferences.Editor prefs = prefFile.edit();
        prefs.putString(PREF_LAST_USED_UNI,
                (String) activityUniversity.getSelectedItem());
        prefs.commit();
        Log.i(TAG, "Methode: onStop wird verlassen.");
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein anderes Item des Spinners
     * selektiert wurde.
     */
    protected abstract void spinnerItemSelected();
}
