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

package at.mukprojects.vuniapp.activityfragmentcommunication.response;

/**
 * Mittels des BooleanResponse kann angegeben werden, ob ein Request erfolgreich
 * war.
 * 
 * @author Mathias
 */
public class BooleanResponse implements Response {
    private static final long serialVersionUID = 1L;
    private boolean           response;
    private String            message;

    /**
     * Erstellt einen neuen BooleanResponse.
     * 
     * @param response
     *            Antwort des Invoke Aufrufs. Kann entweder true oder falls
     *            sein.
     */
    public BooleanResponse(final boolean response) {
        this.response = response;
        this.message = "Keine genauere Beschreibung vorhanden.";
    }

    /**
     * Erstellt einen neuen BooleanResponse.
     * 
     * @param response
     *            Antwort des Invoke Aufrufs. Kann entweder true oder falls
     *            sein.
     * @param message
     *            Mit Hilfe der Nachricht kann die Antwort noch n&auml;her
     *            beschrieben werden.
     */
    public BooleanResponse(final boolean response, final String message) {
        this.response = response;
        this.message = message;
    }

    /**
     * Liefert den Wert der Antwort.
     * 
     * @return Die Antwort. Diese ist entweder true oder false.
     */
    public final boolean getResponse() {
        return response;
    }

    /**
     * Liefert eine nähere beschreibeung der Antwort.
     * 
     * @return Beschreibung der Antwort.
     */
    public final String getMessage() {
        return message;
    }

}
