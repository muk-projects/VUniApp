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
import java.util.ArrayList;

import android.util.Log;
import at.mukprojects.vuniapp.dao.CertificateDAO;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;

/**
 * Caching unterst&uuml;tzende Implementierung des CertificateService.
 * 
 * @author kerrim
 */
public class CertificateServiceCaching implements CertificateService {
    private static final String TAG = CertificateServiceCaching.class
                                            .getSimpleName();

    private CertificateDAO      certificateDAO;
    private CertificateDAO      cache;

    /**
     * Erzeugt einen neuen CertificateService welches Caching unterst&uuml;tzt.
     * 
     * @param certificateDAO
     *            CertificateDAO aus der die Daten gelesen werden sollen.
     * @param cache
     *            CertificateDAO in welche gecacht wird.
     */
    public CertificateServiceCaching(final CertificateDAO certificateDAO,
            final CertificateDAO cache) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.certificateDAO = certificateDAO;
        this.cache = cache;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.CertificateService#readCertificates()
     */
    @Override
    public final ArrayList<Certificate> readCertificates() throws IOException, InvalidLoginException {
        Log.i(TAG, "Methode: read wird gestartet.");
        ArrayList<Certificate> output = null;

        if (cache.readCertificates() != null) {
            output = cache.readCertificates();
        } else {
            output = certificateDAO.readCertificates();
            cache.writeCertificates(output);
        }

        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

}
