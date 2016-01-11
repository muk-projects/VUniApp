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
import at.mukprojects.vuniapp.R;

/**
 * Zweiter Screen der Hilfe.
 *
 * @author Mathias
 */
public class HelpNavigationActivity  extends HelpBaseActivity {
    private static final String TAG = HelpNavigationActivity.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_navigation);
        setSkipButton();
        setNextButton(HelpUniversityActivity.class, false);
    }
}
