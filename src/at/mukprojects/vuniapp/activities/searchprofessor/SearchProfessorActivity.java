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

package at.mukprojects.vuniapp.activities.searchprofessor;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.ActivitySearchListWithDetailFragment;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface;

/**
 * Die Activity implementiert eine Professorensuche f&uuml;r unterschiedliche
 * Universit&auml;ten.
 * 
 * @author Mathias
 * @author kerrim
 */
public class SearchProfessorActivity extends
        ActivitySearchListWithDetailFragment<ProfessorInterface, Professor> {
    private static final String TAG       = SearchProfessorActivity.class
                                                  .getSimpleName();
    private static final String PREF_FILE = "PrefFileSearchProfessor";

    /**
     * Erstellt eine neue SearchProfessorActivity.
     */
    public SearchProfessorActivity() {
        super(PREF_FILE, Professor.PROFESSOR, new ProfessorDetailsFragment(),
                Universities.getInstance().getUniversitiesWithProfs());
    }
    
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        super.onCreate(savedInstanceState, SearchProfessorActivity.class,
                getResources().getString(R.string.activity_searchprofessor));

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #manageBundleExtra(java.lang.Object)
     */
    @Override
    protected final String[] manageBundleExtra(final Object extra) {
        Log.i(TAG, "Methode: manageBundleExtra wird gestartet.");
        String[] output = new String[2];
        super.extraObject = (Professor) extra;
        output[0] = extraObject.getUniversity().getName();
        output[1] = extraObject.getName();
        Log.i(TAG, "Methode: manageBundleExtra wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #searchButtonClick()
     */
    @Override
    protected final void searchButtonClick() {
        Log.i(TAG, "Methode: searchButtonClick wird gestartet.");
        Log.i(TAG, "Methode: searchButtonClick wird verlassen.");
        (new SearchProfessorTask(
                getResources().getString(R.string.activity_searchprofessor),
                getResources().getString(
                        R.string.activity_searchprofessor_searching),
                this,
                activitySearchName.getText().toString(),
                universities.get((String) activityUniversity.getSelectedItem()),
                activityResultsText, activityResults)).execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #searchButtonClick(java.lang.Object)
     */
    @Override
    protected final void searchButtonClick(final Professor extra) {
        Log.i(TAG, "Methode: searchButtonClick wird gestartet.");
        searchButtonClick();
        Log.i(TAG, "Methode: searchButtonClick wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #getListViewAdapter(java.util.ArrayList)
     */
    @Override
    protected final ListAdapter getListViewAdapter(
            final ArrayList<Professor> result) {
        Log.i(TAG, "Methode: getListViewAdapter wird gestartet.");
        Log.i(TAG, "Methode: getListViewAdapter wird verlassen.");
        return new ProfessorResultListAdapter(getCurrentActivity(), result);
    }
}
