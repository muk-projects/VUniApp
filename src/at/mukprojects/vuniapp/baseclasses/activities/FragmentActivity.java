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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;

/**
 * Activity zum Hosten von Fragments. Dieser Activity muss das Fragment als
 * Extra (fragment) mitgegeben werden. Will man dem Fragment ein Bundle mitgeben
 * muss auch dieses als Extra ("bundle") mitgegeben werden.
 * 
 * @author kerrim
 */
public class FragmentActivity extends VUniAppActivity {
    private static final String TAG          = FragmentActivity.class
                                                     .getSimpleName();
    private static final String FRAGMENT_TAG = "FragmentActivityTag";

    @SuppressWarnings("unchecked")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_fragment);
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);

        Intent intent = getIntent();

        Class<? extends Activity> prevActivity = null;
        if (intent
                .getSerializableExtra(VUniAppActivityWithFragment.KEY_PREVACTIVITY) != null) {
            prevActivity = (Class<? extends Activity>) intent
                    .getSerializableExtra(VUniAppActivityWithFragment.KEY_PREVACTIVITY);
        }
        Log.d(TAG, "prevActivity: " + prevActivity);

        super.onCreate(savedInstanceState, prevActivity);

        VUniAppFragment fragment = (VUniAppFragment) intent
                .getSerializableExtra(VUniAppActivityWithFragment.KEY_FRAGMENT);

        Log.d(TAG, "fragment: " + fragment);

        if (intent.getExtras() != null
                && intent.getExtras().getInt(
                        VUniAppActivityWithFragment.KEY_ACTIONBAR_DRAWABLE) != 0) {
            setActionbarBackground(intent.getExtras().getInt(
                    VUniAppActivityWithFragment.KEY_ACTIONBAR_DRAWABLE));
        }

        Bundle bundle = null;
        if (intent.getParcelableExtra(VUniAppActivityWithFragment.KEY_BUNDEL) != null) {
            bundle = (Bundle) intent
                    .getParcelableExtra(VUniAppActivityWithFragment.KEY_BUNDEL);
            Log.d(TAG, "bundle: " + bundle);
            fragment.setArguments(bundle);
        }

        if (prevActivity != null
                && findViewById(R.id.activity_fragment_biglandmodeavailable) != null) {
            Intent intentTwoViewsMode = new Intent(this, prevActivity);
            intentTwoViewsMode.putExtra(
                    VUniAppActivityWithFragment.KEY_FRAGMENT, fragment);
            intentTwoViewsMode.putExtra(VUniAppActivityWithFragment.KEY_BUNDEL,
                    bundle);
            intentTwoViewsMode
                    .putExtra(
                            VUniAppActivityWithFragment.KEY_ACTIVNAVIGATIONITEM,
                            intent.getStringExtra(VUniAppActivityWithFragment.KEY_ACTIVNAVIGATIONITEM));
            startActivity(intentTwoViewsMode);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.replace(R.id.activity_fragment_fragment, fragment,
                FRAGMENT_TAG);
        fragmentTransaction.commit();

        super.setActiveNavigationItem(intent
                .getStringExtra(VUniAppActivityWithFragment.KEY_ACTIVNAVIGATIONITEM));

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    @Override
    public final void onBackPressed() {
        final VUniAppFragment uniAppFragment = (VUniAppFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);

        if (uniAppFragment != null && uniAppFragment.needBackPressAktion()) {
            uniAppFragment.onBackPressAktion();
        } else {
            super.onBackPressed();
        }
    }
}
