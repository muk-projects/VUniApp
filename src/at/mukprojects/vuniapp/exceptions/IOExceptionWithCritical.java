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

import java.io.IOException;

import at.mukprojects.vuniapp.exceptions.base.CriticalException;

/**
 * Die IOException mit implementierten CriticalException Interface.
 * 
 * @author Mathias
 */
public class IOExceptionWithCritical extends IOException implements
        CriticalException {
    private static final long serialVersionUID = 1L;
    private boolean critical;

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     */
    public IOExceptionWithCritical() {
        super();
        this.critical = true;
    }

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     */
    public IOExceptionWithCritical(final String detailMessage) {
        super(detailMessage);
        this.critical = true;
    }

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     * 
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public IOExceptionWithCritical(final boolean critical) {
        super();
        this.critical = critical;
    }

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     */
    public IOExceptionWithCritical(final String detailMessage,
            final Throwable throwable) {
        super(detailMessage, throwable);
        this.critical = true;
    }

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public IOExceptionWithCritical(final String detailMessage,
            final boolean critical) {
        super(detailMessage);
        this.critical = critical;
    }

    /**
     * Erstellt eine neue IOExceptionWithCritical.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public IOExceptionWithCritical(final String detailMessage,
            final Throwable throwable, final boolean critical) {
        super(detailMessage, throwable);
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
