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

import android.util.Log;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Implementierung der ProfessorDAOWebWUWienCache zur Nutzung des Caches.
 * 
 * @author Mathias
 */
public class ProfessorDAOWebWUWienCache implements ProfessorDAO {
    private static final long   CACHINGTIME = 24 * 60 * 60 * 1000;
    private static final String TAG         = ProfessorDAOWebWUWienCache.class
                                                    .getSimpleName();
    private final String        cacheTag    = "ProfessorWUWien";

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.ProfessorDAO#searchProfessor(java.lang.String)
     */
    @Override
    public final ArrayList<Professor> searchProfessor(final String input)
            throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Cache cache = Cache.getCache();
        Object cachedDataObject = cache.getCachedData(cacheTag);
        ArrayList<Professor> cachedData;
        if (cachedDataObject instanceof ArrayList) {
            cachedData = (ArrayList<Professor>) cachedDataObject;
        } else {
            Log.i(TAG, "Methode: read wird verlassen.");
            return null;
        }
        Log.i(TAG, "Methode: read wird verlassen.");
        return cachedData;
    }

    /**
     * Speichert eine Liste an Professoren ab.
     * 
     * @param professors
     *            Liste der Professoren die gespeichert werden sollen.
     */
    public final void writeProfessor(final ArrayList<Professor> professors) {
        Log.i(TAG, "Methode: write wird gestartet.");
        if (professors == null) {
            Log.e(TAG, "Der Methode writeProfessor wurde "
                    + "null als Argument übergeben.");
        } else {
            Cache cache = Cache.getCache();
            Object cachedDataObject = cache.getCachedData(cacheTag);
            ArrayList<Professor> cachedData;
            if (cachedDataObject instanceof ArrayList) {
                cachedData = (ArrayList<Professor>) cachedDataObject;
            } else {
                cachedData = new ArrayList<Professor>();
                cache.cacheData(cacheTag, cachedData, CACHINGTIME);
            }
            cachedData.clear();
            cachedData.addAll(professors);
        }
        Log.i(TAG, "Methode: write wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.ProfessorDAO#getDetails(at.mukprojects.vuniapp
     * .models.Professor)
     */
    @Override
    public final Professor getDetails(final Professor professor)
            throws IOException {
        throw new UnsupportedOperationException();
    }

}
