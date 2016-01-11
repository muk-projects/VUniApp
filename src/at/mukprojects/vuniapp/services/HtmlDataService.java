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

import at.mukprojects.vuniapp.models.HtmlData;

/**
 * Interface f&uuml;r den Service auf wichtige Termine.
 * 
 * @author kerrim
 */
public interface HtmlDataService {

    /**
     * Liest Html Daten aus einer bestimmten URL aus.
     * 
     * @param url
     *            Url der Daten die ausgelesen werden sollen.
     * @return Html Daten welcher unter der Url stehen.
     * @throws IOException
     *             Falls beim Lesen ein Fehler aufgetreten ist.
     */
    HtmlData readHtmlData(final String url) throws IOException;

}