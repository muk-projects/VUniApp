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

import android.content.Context;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.universities.interfaces.base.LoginInterface;

/**
 * Das Interface SubjectInterface wird in alle Universit&auml;ten implementiert,
 * welche eine Funkioenen f&uuml;r F&auml;cher implementiert haben.
 * 
 * @author Mathias
 */
public interface SubjectInterface extends LoginInterface {
    /**
     * Liefert eine Liste aller gespeicherten F&auml;cher zur&uuml;ck.
     * 
     * @param users
     *            Liste der User die zum Holen der F&auml;cher ben&ouml;tigt
     *            werden. (UserKey, User)
     * @return Liste aller gespeicherten F&auml;cher.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    ArrayList<Subject> getSubjects(HashMap<String, UniversityUser> users, final Context context)
            throws IOException, InvalidLoginException, ConnectionException;

    /**
     * L&auml;dt die Details eines Faches nach.
     * 
     * @param users
     *            Liste der User die zum Holen der F&auml;cher ben&ouml;tigt
     *            werden. (UserKey, User)
     * @param subject
     *            Fach dessen Details nachgeladen werden sollen.
     * @return Fach mit den geladenen Details.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    Subject getSubjectDetails(HashMap<String, UniversityUser> users, Subject subject, final Context context) throws IOException,
            InvalidLoginException, ConnectionException;

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
    ArrayList<String> getSubjectKeys();
}
