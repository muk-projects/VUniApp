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

package at.mukprojects.vuniapp.services;

import java.io.IOException;

import at.mukprojects.vuniapp.models.Canteen;

/**
 * Service f&uuml;r den Umgang mit Mensen.
 * 
 * @author Mathias
 */
public interface CanteenService {
    /**
     * Liest eine Mensa aus.
     * 
     * @param mensaName
     *            Name der auszulesenden Mensa.
     * @param mensaId
     *            ID der auszulesenden Mensa.
     * @return Liefert die ausgelesene Mensa zur&uuml;ck.
     * @throws IOException
     *             Wird geworfen, wenn es w&auml;hrend dem Auslesen zu einem
     *             Fehler kommt.
     */
    Canteen read(String mensaName, int mensaId) throws IOException;
}
