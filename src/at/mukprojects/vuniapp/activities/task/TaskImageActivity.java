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

package at.mukprojects.vuniapp.activities.task;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import at.mukprojects.vuniapp.R;

/**
 * Activity f&uuml;r das TaskImageFragment.
 * 
 * @author Mathias
 */
public class TaskImageActivity extends FragmentActivity {
    private static final String TAG = TaskImageActivity.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "Die Methode onCreate wird gestartet.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskimage);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        TaskImageFragment fragment = new TaskImageFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        fragmentTransaction.replace(R.id.activity_intern_fragment, fragment);
        fragmentTransaction.commit();
        Log.d(TAG, "Die Methode onCreate wird verlassen.");
    }
}
