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

import at.mukprojects.vuniapp.exceptions.base.BaseException;

/**
 * Die Exception wird geworfen, wenn es keine Benutzer zu den unterst&uuml;tzten
 * Universit&auml;ten gibt.
 * 
 * @author kerrim
 */
public class NoUniversityUsersException extends BaseException {
    private static final long   serialVersionUID = 1L;

    /**
     * Nachricht, welche Ausgegeben wird und beim Erstellen des Objekts noch
     * erweitert werden kann.
     */
    private static final String MESSAGE          = "Es wurden keine Benutzer"
                                                         + " zu den unterstützten"
                                                         + " Universitäten gefunden";

    /**
     * Erstellt eine neue NoUniversityUsersException.
     */
    public NoUniversityUsersException() {
        super(MESSAGE);
    }

    /**
     * Erstellt eine neue NoUniversityUsersException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     */
    public NoUniversityUsersException(final String detailMessage) {
        super(MESSAGE, detailMessage);
    }

    /**
     * Erstellt eine neue NoUniversityUsersException.
     * 
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public NoUniversityUsersException(final boolean critical) {
        super(MESSAGE, critical);
    }

    /**
     * Erstellt eine neue NoUniversityUsersException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     */
    public NoUniversityUsersException(final String detailMessage,
            final Throwable throwable) {
        super(MESSAGE, detailMessage, throwable);
    }

    /**
     * Erstellt eine neue NoUniversityUsersException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public NoUniversityUsersException(final String detailMessage,
            final boolean critical) {
        super(MESSAGE, detailMessage, critical);
    }

    /**
     * Erstellt eine neue NoUniversityUsersException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public NoUniversityUsersException(final String detailMessage,
            final Throwable throwable, final boolean critical) {
        super(MESSAGE, detailMessage, throwable, critical);
    }
}
