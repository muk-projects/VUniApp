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
import at.mukprojects.vuniapp.dao.CanteenDAO;
import at.mukprojects.vuniapp.models.Canteen;

/**
 * Implementierung des MensaService mit Caching der Mensa Daten.
 * 
 * @author kerrim
 */
public class CanteenServiceCaching implements CanteenService {
    private static final String TAG = CanteenServiceCaching.class
                                            .getSimpleName();

    private CanteenDAO          canteenDAO;
    private CanteenDAO          cache;

    /**
     * Erzeugt einen MensaService mit Caching.
     * 
     * @param canteenDAO
     *            MensaDAO die f&uuml;r den Service genutzt werden soll.
     * @param cache
     *            MensaDAO die zum Cachen der Daten genutzt werden soll.
     */
    public CanteenServiceCaching(final CanteenDAO canteenDAO,
            final CanteenDAO cache) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.canteenDAO = canteenDAO;
        this.cache = cache;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.service.MensaService#read(java.lang.String)
     */
    @Override
    public final Canteen read(final String mensaName, final int mensaId)
            throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Canteen output = null;

        if (cache.read(mensaName, mensaId) != null) {
            output = cache.read(mensaName, mensaId);
        } else {
            output = canteenDAO.read(mensaName, mensaId);
            cache.write(mensaName, output);
        }

        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

}
