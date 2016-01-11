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
import at.mukprojects.vuniapp.models.Canteen;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Implementierung der MensaDAO f&uuml;r den Cache.
 * 
 * @author kerrim
 */
public class CanteenDAOCache implements CanteenDAO {
    private static final long   CACHINGTIME = 60 * 60 * 1000;
    private static final String TAG         = CanteenDAOCache.class
                                                    .getSimpleName();
    private final String        cacheTag    = "Canteen";

    @SuppressWarnings("unchecked")
    @Override
    public final Canteen read(final String mensaName, final int mensaId)
            throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(cacheTag);
        HashMap<String, Canteen> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, Canteen>) cachedDataObject;
        } else {
            Log.i(TAG, "Methode: read wird verlassen.");
            return null;
        }
        Canteen output = cachedData.get(mensaName);
        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void write(final String mensaName, final Canteen mensa)
            throws IOException {
        Log.i(TAG, "Methode: write wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(cacheTag);
        HashMap<String, Canteen> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, Canteen>) cachedDataObject;
        } else {
            cachedData = new HashMap<String, Canteen>();
            cache.cacheData(cacheTag, cachedData, CACHINGTIME);
        }
        cachedData.put(mensaName, mensa);
        Log.i(TAG, "Methode: write wird verlassen.");
    }

}
