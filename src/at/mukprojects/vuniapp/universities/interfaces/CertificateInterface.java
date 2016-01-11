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

package at.mukprojects.vuniapp.universities.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.universities.interfaces.base.LoginInterface;

/**
 * Das Interface CertificateInterface wird in alle Universit&auml;ten
 * implementiert, welche eine Funkioenen f&uuml;r Zeugnisse implementiert haben.
 * 
 * @author Mathias
 */
public interface CertificateInterface extends LoginInterface {
    /**
     * Liefert eine Liste aller gespeicherten Zeugnisse zur&uuml;ck.
     * 
     * @param users
     *            Liste der User die zum Holen der Zeugnisse gebraucht werden.
     *            (UserKey, User)
     * @return Liste aller gespeicherten Zeugnisse.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    ArrayList<Certificate> getCertificates(HashMap<String, UniversityUser> users)
            throws IOException, InvalidLoginException;

    /**
     * Diese Methode liefert Keys zur&uuml;ck, welche die notwendigen User
     * definieren. Gibt es nur einen User zu einer Universit&auml;t wird eine
     * leere ArrayList zur&uuml;ckgeliefert. Sprich ist bei einer
     * Universit&auml;t ein Benutzer f&uuml;r alle Anwendungen ausreichend, so
     * ist dieser ohnehin durch die Universit&auml;t eindeutig definiert. Gibt
     * es jedoch mehr als einen Benutzer zum Beispiel, weil Daten aus
     * unterschiedlichen System ausgelesen werden m&uuml;ssen, so k&ouml;nnen
     * alle f&uuml;r das Interface ben&ouml;tigten Benutzer als extra Key
     * &uuml;bergeben werden.
     * 
     * @return ArrayList mit n&ouml;tigen UserKeys.
     */
    ArrayList<String> getCertificateKeys();

    /**
     * Liefert den Wert zu einer Note. Jede Universit&auml;t kann somit die
     * Zeugnisse nach dem jeweiligen Notenschema anzeigen.
     * 
     * @param grade
     *            String, welcher die Note wiederspiegelt.
     * @return Liefert den Wert der Note zur&uuml;ck oder null, falls kein Wert
     *         gefunden wurde. Wenn eine Note keinen Wert besitzt wird ebenso
     *         null zur&uuml;ckgeliefert.
     */
    Integer getValueToGrade(String grade);

    /**
     * Mit Hilfe dieser Methode kann zu einer &uuml;bergebenen Note der
     * anzuzeigende String zur&uuml;ckgeliefert werden.
     * 
     * @param grade
     *            String, welcher die Note wiederspiegelt.
     * @return Liefert den anzuzeigenden Namen der Note zur&uuml;ck oder null ,
     *         falls kein Wert gefunden wurde.
     */
    String getDisplayedNameToGrade(String grade);
}
