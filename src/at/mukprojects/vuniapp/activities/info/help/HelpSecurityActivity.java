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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.HomeActivity;
import at.mukprojects.vuniapp.activities.settings.security.SecurityActivity;
import at.mukprojects.vuniapp.activities.settings.security.SecurityFragment;

/**
 * F&uuml;fter Hilfe Screen.
 * 
 * @author Mathias
 */
public class HelpSecurityActivity extends HelpBaseActivity {
    private static final String TAG = SecurityActivity.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.VUniAppActivity#onCreate(android.os
     * .Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_security);
        setSkipButton();
        setNextButton(HomeActivity.class, true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.activity_helpsecurity_fragment,
                new SecurityFragment());
        fragmentTransaction.commit();
    }
}