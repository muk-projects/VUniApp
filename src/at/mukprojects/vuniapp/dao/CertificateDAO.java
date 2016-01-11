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

import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;

/**
 * Interface f&uuml;r den Datenzugriff auf Mensen.
 * 
 * @author kerrim
 */
public interface CertificateDAO {

    /**
     * 
     * Liest alle Zeugnisse aus und gibt diese in einer ArrayList zur&uuml;ck.
     * 
     * @return Zeugnisse in einer ArrayList.
     * @throws IOException
     *             Wird geworfen falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException 
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    ArrayList<Certificate> readCertificates() throws IOException, InvalidLoginException;

    /**
     * Schreibt eine Liste von Zeugnissen in den Speicher.
     * 
     * @param certificates
     *            Liste der zu speichernden Zeugnissen.
     * @throws IOException
     *             Wird geworfen falls ein Speichern nicht m&ouml;glich war.
     */
    void writeCertificates(ArrayList<Certificate> certificates)
            throws IOException;

}
