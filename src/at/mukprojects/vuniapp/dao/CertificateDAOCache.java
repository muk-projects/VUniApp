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
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Implementierung der CertificateDAO zur Nutzung des Caches.
 * 
 * @author kerrim
 */
public class CertificateDAOCache implements CertificateDAO {
    private static final long   CACHINGTIME = 60 * 60 * 1000;
    private static final String TAG         = CertificateDAOCache.class
                                                    .getSimpleName();
    private static final String CACHETAG    = "Certificates";

    private String              universityKey;

    /**
     * Erzeugt eine neue SubjectDAO f&uuml;r das Cachen der F&auml;cher einer
     * bestimmten Universit&auml;t.
     * 
     * @param universityKey
     *            Key der Universit&auml;t deren F&auml;cher gecacht werden
     *            sollen.
     */
    public CertificateDAOCache(final String universityKey) {
        this.universityKey = universityKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.CertificateDAO#readCertificates()
     */
    @SuppressWarnings("unchecked")
    @Override
    public final ArrayList<Certificate> readCertificates() throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(CACHETAG);
        HashMap<String, ArrayList<Certificate>> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, ArrayList<Certificate>>) cachedDataObject;
        } else {
            Log.i(TAG, "Methode: read wird verlassen.");
            return null;
        }
        ArrayList<Certificate> output = cachedData.get(universityKey);
        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.CertificateDAO#writeCertificates(java.util
     * .ArrayList)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void writeCertificates(
            final ArrayList<Certificate> certificates) throws IOException {
        Log.i(TAG, "Methode: writeCertificates wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(CACHETAG);
        HashMap<String, ArrayList<Certificate>> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, ArrayList<Certificate>>) cachedDataObject;
        } else {
            cachedData = new HashMap<String, ArrayList<Certificate>>();
            cache.cacheData(CACHETAG, cachedData, CACHINGTIME);
        }
        cachedData.put(universityKey, certificates);
        Log.i(TAG, "Methode: writeCertificates wird verlassen.");

    }

    /**
     * Entfernt alle Zeugnisse vom Cache.
     */
    public static void removeCertificates() {
        Log.i(TAG, "Methode: removeCertificates wird gestartet.");
        Cache cache = Cache.getCache();
        cache.removeData(CACHETAG);
        Log.i(TAG, "Methode: removeCertificates wird verlassen.");

    }

}
