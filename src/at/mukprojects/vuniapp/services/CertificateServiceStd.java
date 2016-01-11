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
 * Standardimplementierung des CertificateServices.
 *
 * @author kerrim
 */
public class CertificateServiceStd implements CertificateService {
    private static final String TAG = SubjectServiceStd.class.getSimpleName();

    private CertificateDAO          certificateDAO;

    /**
     * Erzeugt einen neue Standard SubjectService.
     * 
     * @param certificateDAO
     *            DAO die zum Holen der F&auml;cher genutzt werden soll.
     */
    public CertificateServiceStd(final CertificateDAO certificateDAO) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.certificateDAO = certificateDAO;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.CertificateService#readCertificates()
     */
    @Override
    public final ArrayList<Certificate> readCertificates() throws IOException, InvalidLoginException {
        Log.i(TAG, "Methode: readCertificates wird gestartet.");
        ArrayList<Certificate> output = certificateDAO.readCertificates();
        Log.i(TAG, "Methode: readCertificates wird verlassen.");
        return output;
    }

}
