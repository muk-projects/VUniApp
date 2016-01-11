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

package at.mukprojects.vuniapp.dao.base;

import java.util.ArrayList;

import android.util.Log;
import at.mukprojects.vuniapp.storages.SharedInternetConnection;

/**
 * Basisklasse f&uuml;r alle DAOs welche Webseiten zum Holen Ihrer Informationen
 * nutzen.
 * 
 * @author kerrim
 */
public abstract class DAOWeb {
    private static final String      TAG = DAOWeb.class.getSimpleName();
    private SharedInternetConnection sharedInternetConnection;

    /**
     * Stellt die DAO f&uuml;r den Zugriff auf Webseiten richtig ein.
     */
    protected DAOWeb() {
        this.sharedInternetConnection = SharedInternetConnection
                .getSharedInternetConnection();
    }

    /**
     * Gibt die gemeinsame Internetverbindung zur&uuml;ck.
     * 
     * @return Gemeinsame Internetverbindung.
     */
    protected final SharedInternetConnection getSharedInternetConnection() {
        return sharedInternetConnection;
    }

    /**
     * Extrahiert von einem String in HTML die Informationen in einem Tag. Zum
     * Beispiel wird aus &lt;p&gt;Information&lt;/p&gt; Information extrahiert.
     * 
     * @param input
     *            String mit Text und HTML Tags.
     * @return Ein String Array mit allen extrahierten informationen. Dabei wird
     *         immer von einem &gt; zum n&auml;chsten &lt; als ein String
     *         zusammengefasst.
     */
    protected final String[] extractContentFromHTML(final String input) {
        ArrayList<String> output = new ArrayList<String>();

        String temp = null;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '>') {
                temp = "";
            } else if (input.charAt(i) == '<' && temp != null
                    && temp.length() > 0) {
                output.add(temp);
                temp = null;
            } else if (temp != null) {
                temp += input.charAt(i);
            }
        }

        String[] tOutput = new String[output.size()];
        for (int i = 0; i < output.size(); i++) {
            tOutput[i] = output.get(i);
        }
        if (tOutput.length == 0) {
            String[] tFallbackOutput = new String[1];
            tFallbackOutput[0] = input;
            return tFallbackOutput;
        }
        return tOutput;
    }

    /**
     * Extrahiert aus einer HTML Tablelle alle Daten aus den Feldern und
     * speichert diese in verschachtelte ArrayLists. Die ArrayList
     * &auml;u&szlig;ere ArrayList repr&auml;sentiert dabei eine Zeile und die
     * inneren eine Spalte.
     * 
     * @param input
     *            HTML Code dessen Daten aus der ersten gefundenen Tabelle
     *            extrahiert wird.
     * @return Verschachtelte ArrayList der gefundenen Daten.
     */
    protected final ArrayList<ArrayList<String>> extractStringArrayListFromHTMLTable(
            final String input) {
        Log.i(TAG, "Methode: extractHTMLTableToStringArrayList wird gestartet.");

        int tableStart = input.indexOf("<table");

        if (tableStart == -1) {
            // TODO K: Keine Tabelle gefunden.
            Log.d(TAG, "Es wurde keine Tabelle gefunden.");
            Log.i(TAG,
                    "Methode: extractHTMLTableToStringArrayList wird verlassen.");
            return null;
        }

        if (input.indexOf("</table>") == -1) {
            // TODO K: Keine Tabelle gefunden.
            Log.d(TAG, "Es wurde keine komplette Tabelle gefunden.");
            Log.i(TAG,
                    "Methode: extractHTMLTableToStringArrayList wird verlassen.");
            return null;
        }

        int tableEnd = input.indexOf("</table>") + 8;

        String temp = input.substring(tableStart, tableEnd);

        String[] tempTableRows = temp.split("</?tr[^>]*>");
        if (tempTableRows.length < 2) {
            // TODO K: Fehler in der Bearbeitung der Tabelle.
            Log.d(TAG, "Fehler in der Bearbeitung der Tabelle.");
            Log.i(TAG,
                    "Methode: extractHTMLTableToStringArrayList wird verlassen.");
            return null;
        }

        ArrayList<String> tableRows = new ArrayList<String>();
        for (int i = 1; i < tempTableRows.length - 1; i++) {
            if (tempTableRows[i].length() != 0) {
                tableRows.add(tempTableRows[i]);
            }
        }

        if (tableRows.size() == 0) {
            // TODO K: Keine Tabellenzeilen gefunden.
            Log.d(TAG, "Es wurde keine Tabellenzeilen gefunden.");
            Log.i(TAG,
                    "Methode: extractHTMLTableToStringArrayList wird verlassen.");
            return null;
        }

        ArrayList<ArrayList<String>> tableFields = new ArrayList<ArrayList<String>>();

        for (String tableRow : tableRows) {
            String[] rowCols = tableRow.split("</?t[dh][^>]*>");
            if (rowCols.length > 1) {
                tableFields.add(new ArrayList<String>());
                for (int i = 0; i < rowCols.length; i++) {
                    if (i % 2 == 1) {
                        tableFields.get(tableFields.size() - 1).add(rowCols[i]);
                    }
                }
            }
        }

        Log.i(TAG, "Methode: extractHTMLTableToStringArrayList wird verlassen.");
        return tableFields;
    }

    /**
     * Liefert ein Array mit allen Daten die zwischen dem Anfang eines
     * Startstrings und dem Ende eines Endstrings gefunden worden sind.
     * 
     * @param input
     *            Inhalt von dem extrahiert werden sollen.
     * @param startTag
     *            Anfang des Inhaltsbereiches der extrahiert werden soll.
     * @param endTag
     *            Ende des Inhaltsbereiches der extrahiert werden soll.
     * @return Array mit den gefundenen Tabellen.
     */
    protected final String[] extractDataBetweenStartAndEndString(
            final String input, final String startTag, final String endTag) {

        if (startTag == null || endTag == null || input == null
                || startTag.length() == 0 || endTag.length() == 0) {
            throw new IllegalArgumentException();
        }

        String tInput = input;
        ArrayList<String> output = new ArrayList<String>();

        while (tInput.length() > 0) {
            int startPos = tInput.indexOf(startTag);
            if (startPos < 0) {
                break;
            } else {
                tInput = tInput.substring(startPos);
                int endPos = tInput.indexOf(endTag) + endTag.length();
                output.add(tInput.substring(0, endPos));
                tInput = tInput.substring(endPos);
            }
        }

        String[] tOutput = new String[output.size()];
        for (int i = 0; i < output.size(); i++) {
            tOutput[i] = output.get(i);
        }
        if (tOutput.length == 0) {
            String[] tFallbackOutput = new String[1];
            tFallbackOutput[0] = input;
            return tFallbackOutput;
        }
        return tOutput;
    }

}
