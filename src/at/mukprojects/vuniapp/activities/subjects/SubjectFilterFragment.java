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

package at.mukprojects.vuniapp.activities.subjects;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.models.Subject;

/**
 * Fragment zum Anzeigen eines Faches. Dieses bekommt ein Subject Objekt und
 * zeigt dieses an.
 * 
 * @author kerrim
 */
public class SubjectFilterFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = SubjectFilterFragment.class
                                                         .getSimpleName();

    private Subject             subject;

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects_filtered,
                container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();
        
        // TODO K: Liste mit den Fächern der FilterDAO befüllen
        
        // TODO K: Bei Klick auf einem Fach dieses aus der FilterDAO löschen

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

}
