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

package at.mukprojects.vuniapp.dao;

import java.io.IOException;

import at.mukprojects.vuniapp.models.HtmlData;

/**
 * Interface f&uuml;r den Datenzugriff auf wichtige Termine.
 * 
 * @author kerrim
 */
public interface HtmlDataDAO {

    /**
     * Liest Html Daten aus und gibt diese zur&uuml;ck.
     * 
     * @param url
     *            URL unter welche die Daten zu finden sind.
     * @return Daten als String in HTML formatiert.
     * @throws IOException
     *             Falls beim Lesen ein Fehler aufgetreten ist.
     */
    HtmlData readHtmlData(final String url) throws IOException;

    /**
     * Speichert Html Daten unter einem bestimmten URL ab.
     * 
     * @param url
     *            URL unter welche die Daten zu finden sind.
     * @param htmlData
     *            Daten als String in HTML formatiert.
     * @throws IOException
     *             Falls beim Schreiben ein Fehler aufgetreten ist.
     */
    void writeHtmlData(final String url, final HtmlData htmlData)
            throws IOException;

}