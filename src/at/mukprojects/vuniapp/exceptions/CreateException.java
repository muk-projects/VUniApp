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
 * Die Exception wird geworfen, wenn es beim Erstellen eines Objekts zum
 * Beispiel in einer Datenbank zu einem Fehler kommt und das Objekt nicht
 * erstellt werden kann.
 * 
 * @author Mathias
 */
public class CreateException extends BaseException {
    private static final long   serialVersionUID = 1L;

    /**
     * Nachricht, welche Ausgegeben wird und beim Erstellen des Objekts noch
     * erweitert werden kann.
     */
    private static final String MESSAGE          = "Beim Versuch das Objekt"
                                                         + " zu erstellen kam"
                                                         + " es zu einem Fehler.";

    /**
     * Erstellt eine neue CreateException.
     */
    public CreateException() {
        super(MESSAGE);
    }

    /**
     * Erstellt eine neue CreateException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     */
    public CreateException(final String detailMessage) {
        super(MESSAGE, detailMessage);
    }

    /**
     * Erstellt eine neue CreateException.
     * 
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public CreateException(final boolean critical) {
        super(MESSAGE, critical);
    }

    /**
     * Erstellt eine neue CreateException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     */
    public CreateException(final String detailMessage,
            final Throwable throwable) {
        super(MESSAGE, detailMessage, throwable);
    }

    /**
     * Erstellt eine neue CreateException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public CreateException(final String detailMessage,
            final boolean critical) {
        super(MESSAGE, detailMessage, critical);
    }

    /**
     * Erstellt eine neue CreateException.
     * 
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public CreateException(final String detailMessage,
            final Throwable throwable, final boolean critical) {
        super(MESSAGE, detailMessage, throwable, critical);
    }
}
