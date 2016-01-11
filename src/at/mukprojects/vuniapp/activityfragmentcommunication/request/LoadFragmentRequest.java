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

package at.mukprojects.vuniapp.activityfragmentcommunication.request;

import android.os.Bundle;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;

/**
 * Der LoadFragmentRequest dient dazu, ein anderes Fragment statt dem
 * derzeitigen Fragment zu laden.
 * 
 * @author Mathias
 */
public class LoadFragmentRequest implements Request {
    private static final long serialVersionUID = 1L;

    private VUniAppFragment   fragment;
    private Bundle            bundle;

    /**
     * Erstellt einen neunen LoadFragmentRequest.
     * 
     * @param fragment
     *            Zu ladendes Fragment.
     * @param bundle
     *            Bundle des Fragments.
     */
    public LoadFragmentRequest(final VUniAppFragment fragment,
            final Bundle bundle) {
        super();
        this.fragment = fragment;
        this.bundle = bundle;
    }

    /**
     * Liefert das Fragment.
     * 
     * @return Das Fragment.
     */
    public final VUniAppFragment getFragment() {
        return fragment;
    }

    /**
     * Liefert das Bundle.
     * 
     * @return Das Bundle.
     */
    public final Bundle getBundle() {
        return bundle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "LoadFragmentRequest";
    }
}
