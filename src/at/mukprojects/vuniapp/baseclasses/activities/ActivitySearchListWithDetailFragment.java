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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;

// CHECKSTYLE OFF

/**
 * Die Klasse ActivityListWithDetailFragment wird dazu verwendet, um h&auml;ufig
 * verwendete Art von FragmentActivitys zu abstrahieren.
 * 
 * @author Mathias
 * @param <T>
 *            Der Parameter steht f&uuml;r das verwendete Interface.
 * @param <A>
 *            Der Parameter steht f&uuml;r das verwendete Objekt, welches in der
 *            ListView verwendet wird.
 */
public abstract class ActivitySearchListWithDetailFragment<T, A> extends
        VUniAppActivityWithFragment {
    private static final String   TAG                = ActivitySearchListWithDetailFragment.class
                                                             .getSimpleName();
    /** Fragment. */
    protected VUniAppFragment     fragment;

    /** Preference File. */
    protected static final String PREF_LAST_USED_UNI = "lastUniversityUsed";
    private String                prefFileString;
    protected SharedPreferences   prefFile;

    /** Content Elemente. */
    protected Spinner             activityUniversity;
    protected Button              activitySearchButton;
    protected EditText            activitySearchName;
    protected TextView            activityResultsText;
    protected ListView            activityResults;

    /** Objekte. */
    protected String              extraBundle;
    protected A                   extraObject;
    protected ListAdapter         adapter;

    /** Liste der verwendeten Universit&auuml;ten. */
    protected HashMap<String, T>  universities;

    /**
     * Super Konstruktor eines ActivitySearchListWithDetailFragment.
     * 
     * @param prefFileString
     *            Name des Sharedpreference Files der Activity.
     * @param extraString
     *            String welcher den Objekten des Bundles beim Laden eines
     *            Fragment mitgegeben wird.
     * @param fragment
     *            Das Fragment welches verwendet werden soll.
     * @param universities
     *            Liste aller zu verwendeten Universit&auuml;ten.
     */
    public ActivitySearchListWithDetailFragment(String prefFileString,
            String extraString, VUniAppFragment fragment,
            HashMap<String, T> universities) {
        super();
        super.setCurrentActivity(this);
        this.prefFileString = prefFileString;

        this.extraBundle = extraString;
        this.fragment = fragment;
        this.universities = universities;
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

        setContentView(R.layout.activity_abstractsearch);
        super.setActiveNavigationItem(activeNavigationItem);
        super.onCreate(savedInstanceState, activityClass);
        super.language = getString(R.string.title);

        super.setActionbarBackground(R.drawable.actionbar_background_red);
        
        activityUniversity = (Spinner) findViewById(R.id.activity_search_university);
        activitySearchButton = (Button) findViewById(R.id.activity_search_searchbutton);
        activitySearchName = (EditText) findViewById(R.id.activity_search_name);
        activityResultsText = (TextView) findViewById(R.id.activity_search_resultsText);
        activityResults = (ListView) findViewById(R.id.activity_search_results);

        setUpLayout();

        if (extraObject != null) {
            activitySearchButton.performClick();
        }

        Log.i(TAG, "Methode: setUpLonCreateayout wird verlassen.");
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

        String lastUniversityUsed;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(extraBundle) != null) {
            String[] params = manageBundleExtra(bundle.get(extraBundle));
            lastUniversityUsed = params[0];
            activitySearchName.setText(params[1]);
        } else {
            lastUniversityUsed = prefFile.getString(PREF_LAST_USED_UNI, null);
        }

        if (lastUniversityUsed != null) {
            for (int i = 0; i < universityNames.size(); i++) {
                if (universityNames.get(i).equals(lastUniversityUsed)) {
                    activityUniversity.setSelection(i);
                    break;
                }
            }
        }

        setUpButtonListener();
        activityResultsText.setVisibility(TextView.INVISIBLE);
        activityResults.setVisibility(ListView.INVISIBLE);

        Log.i(TAG, "Methode: setUpLayout wird verlassen.");
    }

    /**
     * Diese Methode setzt alle Listener der Content Button.
     */
    private void setUpButtonListener() {
        activitySearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Suche wurde geklickt.");
                if (extraObject != null) {
                    searchButtonClick(extraObject);
                } else {
                    searchButtonClick();
                }
            }
        });
    }

    /**
     * Diese Methode setzt den ListView und behandelt das Result.
     * 
     * @param result
     *            Ergebnis des AsyncTasks.
     */
    public void showResult(final ArrayList<A> result) {
        Log.i(TAG, "Methode: showResult wird gestartet.");
        adapter = getListViewAdapter(result);
        activityResults.setAdapter(adapter);

        activityResults.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> arg0, final View arg1,
                    final int position, final long arg3) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(extraBundle,
                        (Serializable) adapter.getItem(position));
                loadFragment(fragment, null, bundle, R.drawable.actionbar_background_red);
            }

        });

        /** Wenn der Raum eindeutig ist, wird direkt die Details aufgerufen. */
        if (result.size() == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(extraBundle, (Serializable) result.get(0));
            loadFragment(fragment, bundle);
        }
        Log.i(TAG, "Methode: showResult wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();
        Log.i(TAG, "Methode: onStart wird verlassen.");
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
     * Die Methode getListViewAdapter liefert den Adapter f&uuml;r die
     * ResultListe zur&uuml;ck.
     * 
     * @param result
     *            Die ResultListe.
     * @return Adapter der Liste.
     */
    protected abstract ListAdapter getListViewAdapter(ArrayList<A> result);

    /**
     * Diese Methode h&auml;ndelt einen Buttonklick ohne ein existierendes
     * ExtraObjekt.
     */
    protected abstract void searchButtonClick();

    /**
     * Diese Methode h&auml;ndelt einen Buttonklick mit eiem existierendem
     * ExtraObjekt.
     * 
     * @param extra
     *            Das ExtraObjekt.
     */
    protected abstract void searchButtonClick(final A extra);

    /**
     * Die Methode manageBundelExtra wird von setUpLayout aufegrufen, falls
     * diese feststellt, dass sich ein Extra im Bundle befindet. Die Aufgabe der
     * Methode ist es einerseits die Variable extraObject zu setzten und
     * andererseites einen String zur&uuml;ckzuliefern, welcher dazu verwendet
     * werden kann den Spinner korrekt zu setzten. Andererseits soll als zweiter
     * Parameter ein String zur&uuml;ckgeliefert werden, welcher als
     * Suchparameter verwendet wird.
     * 
     * @param extra
     *            Das Objekt, welches aus dem Bundle extrahiert wurde.
     * @return Ein String[], welches auf dem Index 0 den Wert f&uuml;r den
     *         Spinner und am Index 1 den Wert f&uuml;r das Textfeld gespeichert
     *         hat.
     */
    protected abstract String[] manageBundleExtra(Object extra);
}
