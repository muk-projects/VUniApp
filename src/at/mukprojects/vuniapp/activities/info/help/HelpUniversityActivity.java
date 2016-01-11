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

package at.mukprojects.vuniapp.activities.info.help;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.settings.university.UniversityListAdapter;
import at.mukprojects.vuniapp.activities.settings.university.UniversitySettingsFragment;
import at.mukprojects.vuniapp.activities.settings.user.UserFragment;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.UniversitySettings;

/**
 * Dritter Hilfe Screen.
 * 
 * @author Mathias
 */
public class HelpUniversityActivity extends HelpBaseActivity {
    private static final String TAG = HelpUniversityActivity.class
                                            .getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_university);
        setSkipButton();
        setNextButton(HelpUserActivity.class, false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(
                R.id.activity_helpuniversitysettings_fragment,
                new UniversitySettingsFragment());
        fragmentTransaction.commit();
    }
}
