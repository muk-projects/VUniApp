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

package at.mukprojects.vuniapp.baseclasses.fragments;

import java.io.Serializable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import at.mukprojects.vuniapp.activityfragmentcommunication.FragmentListener;
import at.mukprojects.vuniapp.activityfragmentcommunication.OnBackPressListener;
import at.mukprojects.vuniapp.exceptions.NoTwoPaneException;

// CHECKSTYLE OFF

/**
 * Basisklasse f&uuml;r in der VUniApp genutzten Fragments.
 * 
 * @author Mathias
 */
public abstract class VUniAppFragment extends Fragment implements Serializable,
        OnBackPressListener {
    private static final long serialVersionUID = 1L;
    private FragmentListener  listener;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public final void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentListener) {
            listener = (FragmentListener) activity;
        }
    }

    /**
     * Gibt an ob FragmentListener implementiert ist.
     * 
     * @return Boolean, welcher angibt um welche Art es sich handelt.
     */
    protected boolean isTwoPaneWithListener() {
        return listener != null;
    }

    /**
     * Liefert den FragmentListener zur&uuml;ck.
     * 
     * @return Den FragmentListener
     * @throws NoTwoPaneException
     *             Die Exception wird geworfen, wenn es sich nicht um eine
     *             TwoPaneActivity handelt.
     */
    protected final FragmentListener getFragmentListener()
            throws NoTwoPaneException {
        if (listener == null) {
            throw new NoTwoPaneException();
        }
        return listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.activityfragmentcommunication.OnBackPressListener#
     * needBackPressAktion()
     */
    @Override
    public boolean needBackPressAktion() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.activityfragmentcommunication.OnBackPressListener#
     * onBackPressAktion()
     */
    @Override
    public void onBackPressAktion() {
    }
}
