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

package at.mukprojects.vuniapp.exceptions.base;

/**
 * Basisklasse der Exceptions.
 * 
 * @author Mathias
 */
public abstract class BaseException extends Exception implements
        CriticalException {
    private static final long   serialVersionUID = 1L;
    private static final String STD_MESSAGE      = "Es ist ein Fehler aufgetreten.";

    private String              message;
    private boolean             critical;

    /**
     * Erstellt eine neue BaseException.
     */
    public BaseException() {
        super();
        this.message = STD_MESSAGE;
        critical = true;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     */
    public BaseException(final String message, final String detailMessage) {
        super(detailMessage);
        this.message = message;
        critical = true;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     */
    public BaseException(final String message) {
        super();
        this.message = message;
        this.critical = true;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public BaseException(final String message, final boolean critical) {
        super();
        this.message = message;
        this.critical = critical;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     */
    public BaseException(final String message, final String detailMessage,
            final Throwable throwable) {
        super(detailMessage, throwable);
        critical = true;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public BaseException(final String message, final String detailMessage,
            final boolean critical) {
        super(detailMessage);
        this.message = message;
        this.critical = critical;
    }

    /**
     * Erstellt eine neue BaseException.
     * 
     * @param message
     *            Allgemeine Fehlermeldung der Exception.
     * @param detailMessage
     *            Detaillierte Beschreibung der Exception.
     * @param throwable
     *            Exception, welche zum Werfen einer ConnectionException
     *            gef&uuml;hrt hat.
     * @param critical
     *            Gibt an, ob es sich um einen fatalen Fehler handelt.
     */
    public BaseException(final String message, final String detailMessage,
            final Throwable throwable, final boolean critical) {
        super(detailMessage, throwable);
        this.critical = critical;
    }

    /**
     * Liefert eine detalierte Fehlermeldung.
     * 
     * @return Eine detalierte Fehlermeldung.
     */
    @Override
    public final String getMessage() {
        return message + "\n\n" + super.getMessage();
    }

    /**
     * Liefert eine standard Fehlermeldung.
     * 
     * @return Eine standard Fehlermeldung.
     */
    @Override
    public final String toString() {
        return message;
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
