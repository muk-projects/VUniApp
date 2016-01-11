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
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Implementierung der SubjectDAO zur Nutzung des Caches.
 * 
 * @author kerrim
 */
public class SubjectDAOCache implements SubjectDAO {
    private static final long   CACHINGTIME = 60 * 60 * 1000;
    private static final String TAG         = SubjectDAOCache.class
                                                    .getSimpleName();
    private static final String CACHETAG    = "Subjects";

    private String              universityKey;

    /**
     * Erzeugt eine neue SubjectDAO f&uuml;r das Cachen der F&auml;cher einer
     * bestimmten Universit&auml;t.
     * 
     * @param universityKey
     *            Key der Universit&auml;t deren F&auml;cher gecacht werden
     *            sollen.
     */
    public SubjectDAOCache(final String universityKey) {
        this.universityKey = universityKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#readSubjects()
     */
    @SuppressWarnings("unchecked")
    @Override
    public final ArrayList<Subject> readSubjects() throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(CACHETAG);
        HashMap<String, ArrayList<Subject>> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, ArrayList<Subject>>) cachedDataObject;
        } else {
            Log.i(TAG, "Methode: read wird verlassen.");
            return null;
        }
        ArrayList<Subject> output = cachedData.get(universityKey);
        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#writeSubjects(java.util.ArrayList)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void writeSubjects(final ArrayList<Subject> subjects) {
        Log.i(TAG, "Methode: write wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(CACHETAG);
        HashMap<String, ArrayList<Subject>> cachedData;
        if (cachedDataObject instanceof HashMap) {
            cachedData = (HashMap<String, ArrayList<Subject>>) cachedDataObject;
        } else {
            cachedData = new HashMap<String, ArrayList<Subject>>();
            cache.cacheData(CACHETAG, cachedData, CACHINGTIME);
        }
        cachedData.put(universityKey, subjects);
        Log.i(TAG, "Methode: write wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#readDescription(at.mukprojects.
     * vuniapp.models.Subject)
     */
    @Override
    public final Subject readDetails(final Subject subject) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt alle F&auml;cher vom Cache.
     */
    public static void removeSubjects() {
        Log.i(TAG, "Methode: removeCertificates wird gestartet.");
        Cache cache = Cache.getCache();
        cache.removeData(CACHETAG);
        Log.i(TAG, "Methode: removeCertificates wird verlassen.");

    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(ArrayList<Subject> subjects) throws IOException {
        throw new UnsupportedOperationException();
    }

}
