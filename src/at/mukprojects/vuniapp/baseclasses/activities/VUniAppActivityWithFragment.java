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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListAdapter;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activityfragmentcommunication.FragmentListener;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.AddListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.LoadFragmentRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.RemoveListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.Request;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.BooleanResponse;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.Response;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.models.UniversityUser;

// CHECKSTYLE OFF

/**
 * Diese Klasse implementiert die Basisfunktionalit&auml;t um eine Activity mit
 * einem Fragment zu verwalten. Wichtig f&uuml;r die Verwendung der
 * VUniAppActivityWithFragment Klasse ist es, dass das Fragment, welches
 * verwendet werden soll die ID activity_intern_fragment besitzt. Nur so ist
 * sichergestellt, dass sich die Applikation beim Drehen des Ger&auml;ts richtig
 * verh&auml;hlt.
 * 
 * @author kerrim
 * @author Mathias
 */
public abstract class VUniAppActivityWithFragment extends VUniAppActivity
        implements FragmentListener {
    private static final String TAG                     = VUniAppActivityWithFragment.class
                                                                .getSimpleName();

    public static final String  KEY_FRAGMENT            = "fragment";
    public static final String  KEY_BUNDEL              = "bundle";
    public static final String  KEY_PREVACTIVITY        = "prevActivity";
    public static final String  KEY_ACTIVNAVIGATIONITEM = "activeNavigationItem";
    public static final String  KEY_ACTIONBAR_DRAWABLE  = "actionbardrawable";

    @Override
    protected final void onResumeFragments() {
        super.onResumeFragments();

        Intent intent = getIntent();

        if (findViewById(R.id.activity_intern_fragment) != null
                && intent.getSerializableExtra(KEY_FRAGMENT) != null) {
            loadFragment(
                    (VUniAppFragment) intent.getSerializableExtra(KEY_FRAGMENT),
                    intent.getStringExtra(KEY_ACTIVNAVIGATIONITEM),
                    (Bundle) intent.getParcelableExtra(KEY_BUNDEL));
        }
    }

    /**
     * L&auml;dt ein Fragment je nach Monitorgr&ouml;&szlig;e und Ausrichtung,
     * in den Fragment Container dieser Activity oder in eine neue Activity.
     * 
     * @param fragment
     *            Fragment welches geladen werden soll.
     * @param activeNavigationItem
     *            Name des aktuell aktiven Elements in der Navigationsbar. Bei
     *            null wird das Aufrufen der Methode der Superklasse
     *            &uuml;bersprungen.
     * @param bundle
     *            Argumente welche dem Fragment mitgegeben werden sollen.
     */
    public final void loadFragment(final VUniAppFragment fragment,
            final String activeNavigationItem, final Bundle bundle) {
        Log.i(TAG, "Methode: loadFragment wird gestartet.");
        loadFragment(fragment, activeNavigationItem, bundle, null);
        Log.i(TAG, "Methode: loadFragment wird verlassen.");
    }

    /**
     * L&auml;dt ein Fragment je nach Monitorgr&ouml;&szlig;e und Ausrichtung,
     * in den Fragment Container dieser Activity oder in eine neue Activity.
     * 
     * @param fragment
     *            Fragment welches geladen werden soll.
     * @param activeNavigationItem
     *            Name des aktuell aktiven Elements in der Navigationsbar. Bei
     *            null wird das Aufrufen der Methode der Superklasse
     *            &uuml;bersprungen.
     * @param bundle
     *            Argumente welche dem Fragment mitgegeben werden sollen.
     * @param actionbarDrawable
     *            Der Hintergrund der Aktionbar.
     */
    public final void loadFragment(final VUniAppFragment fragment,
            final String activeNavigationItem, final Bundle bundle,
            Integer actionbarDrawable) {
        Log.i(TAG, "Methode: loadFragment wird gestartet.");
        Log.d(TAG, "fragment: " + fragment);
        Log.d(TAG, "bundle: " + bundle);
        if (findViewById(R.id.activity_intern_fragment) != null) {
            Log.d(TAG, "Fragment wird im selben Fenster geladen.");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            fragment.setArguments(bundle);
            String fragmentTag = fragment.getClass().toString();
            fragmentTransaction.replace(R.id.activity_intern_fragment,
                    fragment, fragmentTag).addToBackStack(fragmentTag);
            fragmentTransaction.commit();

            if (activeNavigationItem != null) {
                super.setActiveNavigationItem(activeNavigationItem);
            }
        } else {
            Log.d(TAG, "Fragment wird in der FragmentActivity geladen.");
            Intent intent = new Intent(super.getCurrentActivity(),
                    FragmentActivity.class);
            intent.putExtra(KEY_FRAGMENT, fragment);
            intent.putExtra(KEY_BUNDEL, bundle);
            intent.putExtra(KEY_PREVACTIVITY, super.getActivityClass());
            intent.putExtra(KEY_ACTIVNAVIGATIONITEM, activeNavigationItem);
            if (actionbarDrawable != null) {
                intent.putExtra(KEY_ACTIONBAR_DRAWABLE, actionbarDrawable);
            }
            startActivity(intent);
        }
        Log.i(TAG, "Methode: loadFragment wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        final VUniAppFragment uniAppFragment = (VUniAppFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_intern_fragment);

        if (uniAppFragment != null && uniAppFragment.needBackPressAktion()) {
            uniAppFragment.onBackPressAktion();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * L&auml;dt ein Fragment je nach Monitorgr&ouml;&szlig;e und Ausrichtung,
     * in den Fragment Container dieser Activity oder in eine neue Activity.
     * 
     * @param fragment
     *            Fragment welches geladen werden soll.
     * @param bundle
     *            Argumente welche dem Fragment mitgegeben werden sollen.
     */
    public final void loadFragment(final VUniAppFragment fragment,
            final Bundle bundle) {
        loadFragment(fragment, null, bundle, null);
    }

    /**
     * L&auml;dt ein Fragment je nach Monitorgr&ouml;&szlig;e und Ausrichtung,
     * in den Fragment Container dieser Activity oder in eine neue Activity.
     * 
     * @param fragment
     *            Fragment welches geladen werden soll.
     */
    public final void loadFragment(final VUniAppFragment fragment) {
        loadFragment(fragment, null, null, null);
    }

    /**
     * Zeigt an, ob es eine Activity mit Fragment ist.
     * 
     * @return Boolean, welche Art der Activity angibt.
     */
    protected final boolean hasFragment() {
        return findViewById(R.id.activity_intern_fragment) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.FragmentListener#invoke(at.mukprojects
     * .vuniapp.models.base.Request)
     */
    @Override
    public Response invoke(final Request request) {
        Log.d(TAG, "Request erhalten: " + request.toString());

        Response response = null;
        if (request instanceof LoadFragmentRequest) {
            LoadFragmentRequest fragmentRequest = (LoadFragmentRequest) request;
            loadFragment(fragmentRequest.getFragment(),
                    fragmentRequest.getBundle());
            response = new BooleanResponse(true, "Fragment wurde geladen.");
        }
        return response;
    }
}
