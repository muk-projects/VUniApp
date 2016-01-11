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

import android.util.Log;
import at.mukprojects.vuniapp.dao.HtmlDataDAO;
import at.mukprojects.vuniapp.models.HtmlData;

/**
 * Ein HtmlDataService mit Caching.
 * 
 * @author kerrim
 */
public class HtmlDataServiceCaching implements HtmlDataService {
    private static final String TAG = HtmlDataServiceCaching.class
                                            .getSimpleName();

    private HtmlDataDAO   htmlDataDAO;
    private HtmlDataDAO   cache;

    /**
     * Erzeugt einen ImportantDates Service welcher das Cachen beherrscht.
     * 
     * @param htmlDataDAO
     *            HtmlDataDAO die f&uuml;r den Service genutzt werden
     *            soll.
     * @param cache
     *            HtmlDataDAO die zum Cachen der Daten genutzt werden
     *            soll.
     */
    public HtmlDataServiceCaching(
            final HtmlDataDAO htmlDataDAO,
            final HtmlDataDAO cache) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.htmlDataDAO = htmlDataDAO;
        this.cache = cache;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.HtmlDataService#readImportantDates
     * (java.lang.String)
     */
    @Override
    public final HtmlData readHtmlData(final String url) throws IOException {
        Log.i(TAG, "Methode: readImportantDates wird gestartet.");
        HtmlData output = null;

        if (cache.readHtmlData(url) != null) {
            output = cache.readHtmlData(url);
        } else {
            output = htmlDataDAO.readHtmlData(url);
            cache.writeHtmlData(url, output);
        }

        Log.i(TAG, "Methode: readImportantDates wird verlassen.");
        return output;
    }

}
