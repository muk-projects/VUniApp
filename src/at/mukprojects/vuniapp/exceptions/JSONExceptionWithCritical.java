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

package at.mukprojects.vuniapp.exceptions;

import org.json.JSONException;

import at.mukprojects.vuniapp.exceptions.base.CriticalException;

/**
 * Die JSONException mit implementierten CriticalException Interface.
 * 
 * @author Mathias
 */
public class JSONExceptionWithCritical extends JSONException implements
        CriticalException {
    private static final long serialVersionUID = 1L;
    private boolean           critical;

    /**
     * Erstellt eine neue JSONExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     */
    public JSONExceptionWithCritical(final String detailMessage) {
        super(detailMessage);
        this.critical = true;
    }

    /**
     * Erstellt eine neue JSONExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public JSONExceptionWithCritical(final String detailMessage,
            final boolean critical) {
        super(detailMessage);
        this.critical = critical;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.exceptions.base.CriticalException#isCreiticalException
     * ()
     */
    @Override
    public final boolean isCreiticalException() {
        return critical;
    }
}