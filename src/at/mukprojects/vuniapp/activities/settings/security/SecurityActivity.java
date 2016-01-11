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

package at.mukprojects.vuniapp.activities.settings.security;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;

/**
 * Die SecurityActivity gibt dem Benutzer die M&ouml;glichkeit das Passwort zu
 * &auml;ndern.
 * 
 * @author Mathias
 */
public class SecurityActivity extends VUniAppActivity {
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
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_security);
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_security));
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.activity_security_fragment,
                new SecurityFragment());
        fragmentTransaction.commit();

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }
}