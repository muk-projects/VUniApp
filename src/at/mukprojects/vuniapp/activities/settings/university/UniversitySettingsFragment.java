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

package at.mukprojects.vuniapp.activities.settings.university;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.UniversitySettings;

/**
 * Das UniversitySettingsFragment gibt die M&ouml;glichkeit die
 * Universit&auml;ten einzustellen.
 * 
 * @author Mathias
 */
public class UniversitySettingsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;
    private static final String TAG              = UniversitySettingsFragment.class
                                                         .getSimpleName();

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
        return inflater.inflate(R.layout.fragment_universitysettings,
                container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    public final void onStart() {
        super.onStart();
        ArrayList<University> universityList = null;
        try {
            universityList = UniversitySettings.getInstance()
                    .getCopyOfAvailableUniversities();
        } catch (NotInitializedException e) {
            universityList = UniversitySettings.initialize(
                    getActivity().getSharedPreferences(
                            VUniAppActivity.PREF_FILE_CONFIG, 0))
                    .getCopyOfAvailableUniversities();
        }
        ((ListView) getView().findViewById(
                R.id.activity_universitysettings_list))
                .setAdapter(new UniversityListAdapter(getActivity(),
                        universityList, getActivity().getSharedPreferences(
                                VUniAppActivity.PREF_FILE_CONFIG, 0)));
    }
}
