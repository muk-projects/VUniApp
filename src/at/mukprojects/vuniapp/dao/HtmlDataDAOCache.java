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
import java.util.HashMap;

import android.util.Log;
import at.mukprojects.vuniapp.models.HtmlData;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Implementierung der HtmlDataDAO f&uuml;r den Cache.
 * 
 * @author Mathias
 */
public class HtmlDataDAOCache implements HtmlDataDAO {
    private static final String TAG         = HtmlDataDAOCache.class
                                                    .getSimpleName();

    private static final long   CACHINGTIME = 60 * 60 * 1000;
    private final String        cacheTag    = "Dates";

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.HtmlDataDAO#readImportantDates(java.
     * lang.String)
     */
    @Override
    public final HtmlData readHtmlData(final String url) throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(cacheTag);

        HashMap<String, HtmlData> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, HtmlData>) cachedDataObject;
        } else {
            Log.i(TAG, "Methode: read wird verlassen.");
            return null;
        }
        HtmlData output = cachedData.get(url);
        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.HtmlDataDAO#writeImportantDates(java
     * .lang.String, java.lang.String)
     */
    @Override
    public final void writeHtmlData(final String url, final HtmlData htmlData)
            throws IOException {
        Log.i(TAG, "Methode: write wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(cacheTag);
        HashMap<String, HtmlData> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, HtmlData>) cachedDataObject;
        } else {
            cachedData = new HashMap<String, HtmlData>();
            cache.cacheData(cacheTag, cachedData, CACHINGTIME);
        }
        cachedData.put(url, htmlData);
        Log.i(TAG, "Methode: write wird verlassen.");
    }

}
