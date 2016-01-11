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

package at.mukprojects.vuniapp.universities.interfaces;

import java.io.IOException;
import java.io.Serializable;

import at.mukprojects.vuniapp.models.HtmlData;

/**
 * Das DatesInterface wird von allen Universit&auml;ten implementiert welche
 * eine Auflistung aller wichtigen Termine zur Verf&uuml;gung stellt.
 * 
 * @author Mathias
 */
public interface DatesInterface extends Serializable {
    /**
     * Liefert die Termine der Universit&auml;t.
     * 
     * @return Die Termine.
     * @throws IOException
     *             Die Exception wird geworfen, wenn es beim Auslesen der
     *             Termine zu einem Fehler kommt.
     */
    HtmlData getUniversityDates() throws IOException;
}
