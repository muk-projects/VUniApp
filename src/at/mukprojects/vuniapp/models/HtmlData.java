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

package at.mukprojects.vuniapp.models;

/**
 * Das Model HtmlData enth&auml;t einen String, welcher Html Code beinhaltet.
 * Dieser kann dann zum Beispiel in ein WebView geladen werden.
 * 
 * @author Mathias
 */
public class HtmlData {
    private String html;

    /**
     * Erstellt ein neues HtmlData Objekt.
     * 
     * @param html
     *            Der komplette Html Code als String. Wenn das Objekt mittels
     *            dieses Konstruktors erstellt wird, so muss sichergestellt
     *            sein, dass der &uml;bergebene String ein
     *            funktionst&uml;chtiger Html Code ist.
     */
    public HtmlData(final String html) {
        super();
        this.html = html;
    }

    /**
     * Erstellt ein neues HtmlData Objekt.
     * 
     * @param title
     *            Der Titel des Html Objekts.
     * @param bodyContent
     *            Der Content, welcher auf der Html Seite angezeigt werden soll.
     */
    public HtmlData(final String title, final String bodyContent) {
        html = "<html>";
        html += "<head>";
        html += "<title>" + title + "</title>";
        html += "</head>";
        html += "<body>";
        html += bodyContent;
        html += "</body>";
        html += "</html>";
    }

    /**
     * Liefert die HtmlData als String zur&uml;ck.
     * 
     * @return Die HtmlData als String.
     */
    public final String getHtml() {
        return html;
    }

}
