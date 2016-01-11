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

import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;

/**
 * Interface zum Holen von Zeugnissen.
 * 
 * @author kerrim
 */
public interface CertificateService {

    /**
     * Liefert eine Liste aller gespeicherten Zeugnisse zur&uuml;ck.
     * 
     * @return Liste aller gespeicherten Zeugnisse.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    ArrayList<Certificate> readCertificates() throws IOException,
            InvalidLoginException;
}
