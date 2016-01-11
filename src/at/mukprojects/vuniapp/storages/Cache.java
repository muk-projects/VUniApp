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

package at.mukprojects.vuniapp.storages;

import java.util.Date;
import java.util.HashMap;

import android.util.Log;

/**
 * Cache Speicher als Singleton. Dieser soll zum cachen von Daten, auf die sonst
 * nicht so schnell zugegriffen werden kann, verwendet werden.
 * 
 * @author kerrim
 * @author Mathias
 */
public final class Cache {
    private static final String     TAG = Cache.class.getSimpleName();
    private static Cache            cache;

    private HashMap<String, Object> cachingContainer;
    private HashMap<String, Long>   cachingDuration;
    private HashMap<String, Long>   cachingStartTime;

    /**
     * Erzeugt einen neuen Cache. Der Konstruktor wurde private gesetzt damit
     * niemand au&szlig;er dieser Klasse einen Cache erzeugen kann.
     */
    private Cache() {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.cachingContainer = new HashMap<String, Object>();
        this.cachingDuration = new HashMap<String, Long>();
        this.cachingStartTime = new HashMap<String, Long>();
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /**
     * Methode zum Holen des Caches.
     * 
     * @return Liefert den Cache zur&uuml;ck.
     */
    public static Cache getCache() {
        Log.i(TAG, "Methode: getCache wird gestartet.");
        if (Cache.cache == null) {
            Cache.cache = new Cache();
        }
        Log.i(TAG, "Methode: getCache wird verlassen.");
        return Cache.cache;
    }

    /**
     * Liefert im Cache gespeicherte Objekt zur&uuml;ck. Falls das Objekt nicht
     * vorhanden, oder die zu speichernde Zeit vergangen ist, wird null
     * zur&uuml;ckgegeben.
     * 
     * @param name
     *            Name des zu holenden Objekts.
     * @return Liefert zuvor gespeichertes Objekt zur&uuml;ck.
     */
    public Object getCachedData(final String name) {
        Log.i(TAG, "Methode: getCachedData wird gestartet.");
        if (this.cachingContainer.get(name) == null) {
            Log.i(TAG, "Methode: getCachedData wird verlassen.");
            return null;
        }
        if (this.cachingDuration.get(name) != null
                && this.cachingStartTime.get(name)
                        + this.cachingDuration.get(name) < (new Date())
                            .getTime()) {
            this.cachingContainer.remove(name);
            this.cachingDuration.remove(name);
            this.cachingStartTime.remove(name);
            Log.i(TAG, "Methode: getCachedData wird verlassen.");
            return null;
        }
        Log.i(TAG, "Methode: getCachedData wird verlassen.");
        return this.cachingContainer.get(name);
    }

    /**
     * Speichert Daten f&uuml;r eine gewisse Zeit im Cache.
     * 
     * @param name
     *            Name (Schl&uuml;ssel) der zu speichernden Daten.
     * @param object
     *            Zu speicherndes Objekt.
     * @param cachingDuration
     *            Zeit, in ms, die das Objekt gespeichert bleiben soll. Falls
     *            das Objekt nicht ablaufen soll null &uuml;bergeben.
     */
    public void cacheData(final String name, final Object object,
            final Long cachingDuration) {
        Log.i(TAG, "Methode: cacheData wird gestartet.");
        this.cachingContainer.put(name, object);
        this.cachingDuration.put(name, cachingDuration);
        this.cachingStartTime.put(name, (new Date()).getTime());
        Log.i(TAG, "Methode: cacheData wird verlassen.");
    }

    /**
     * Pr&uuml;ft, ob Daten vorhanden sind.
     * 
     * @param name
     *            Name (Schl&uuml;ssel) der Daten.
     * @return Liefert True, falls vorhanden oder andernfalls False.
     */
    public boolean containsData(final String name) {
        if (cachingContainer.containsKey(name)) {
            return !(this.cachingDuration.get(name) != null && this.cachingStartTime
                    .get(name) + this.cachingDuration.get(name) < (new Date())
                        .getTime());
        } else {
            return false;
        }
    }

    /**
     * Entfernt einen Eintrag aus dem Cache.
     * 
     * @param name
     *            Name des Eintrags, welcher entfernt werden soll.
     */
    public void removeData(final String name) {
        this.cachingContainer.remove(name);
        this.cachingDuration.remove(name);
        this.cachingStartTime.remove(name);
    }

}
