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
import at.mukprojects.vuniapp.activities.settings.user.UserFragment;

/**
 * Der Vierte Hilfe Screen.
 * 
 * @author Mathias
 */
public class HelpUserActivity extends HelpBaseActivity {
    private static final String TAG = HelpUserActivity.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_universityuser);
        setSkipButton();
        setNextButton(HelpSecurityActivity.class, false);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.activity_helpuniversityuser_fragment,
                new UserFragment());
        fragmentTransaction.commit();
    }
}
