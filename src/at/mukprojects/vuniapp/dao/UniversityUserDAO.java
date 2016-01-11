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

import java.util.List;

import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.UniversityUser;

/**
 * Interface f&uuml;r den Zugriff auf den UserStorage. Der Service, welcher
 * dieses Interface implementiert muss sich auch um eine passende
 * Verschl&uuml;sselung der Benutzer k&uuml;mmern. Dazu kann zum Beispiel der
 * SecurityHelper zur Hilfe genommen werden.
 * 
 * @author Mathias
 */
public interface UniversityUserDAO {
    /**
     * Durchsucht den Storage nach einem User mit der angegebenen ID und gibt
     * diesen gegebenenfalls zur&uuml;ck.
     * 
     * @param id
     *            Die ID des Users.
     * @return Liefert den User mit der angegebenen ID oder null falls kein User
     *         mit dieser ID gefunden wurde.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    UniversityUser getUserByID(long id) throws ReadException;

    /**
     * Liefert alle User zur&uuml;ck.
     * 
     * @return Alle User im Storage.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    List<UniversityUser> getAllUsers() throws ReadException;

    /**
     * Liefert alle User zur&uuml;ck, die &auml;quivalente Attribute besitzen.
     * 
     * @param user
     *            User, welcher als Vergleichswert dient.
     * @return Alle User, welche &auml;quivalent mit dem &uuml;bergebenen User
     *         sind.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    List<UniversityUser> getUsers(UniversityUser user) throws ReadException;

    /**
     * Erstellt den &uuml;bergebenen User im Storage.
     * 
     * @param user
     *            Der zu erstellende User.
     * @return Liefert den User, mit gesetzter ID, zur&uuml;ck.
     * @throws CreateException
     *             Die Exception wird geworfen, wenn es beim Erstellen eines
     *             Objekts zu einem Fehler kommt und das Objekt nicht erstellt
     *             werden kann.
     */
    UniversityUser createUser(UniversityUser user) throws CreateException;

    /**
     * Aktualisiert die User.
     * 
     * @param setUser
     *            Der User ent&auml;hlt die Werte auf, welche alle gefunden User
     *            gesetzt werden. Dabei wird die ID des setUsers nicht beachtet,
     *            da diese nicht ver&auml;ndert werden kann.
     * @param whereUser
     *            Der User dient als Vergleichswert, um festzustellen, welche
     *            User ge&auml;ndert werden sollen.
     * @return Liefert einen Integer zur&uuml;ck, welcher angibt wie viele User
     *         ge&auml;ndert wurden.
     * @throws UpdateException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des Updates
     *             zu einem Fehler kommt.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    int updateUser(final UniversityUser setUser, final UniversityUser whereUser)
            throws UpdateException, ReadException;

    /**
     * L&ouml;scht alle User, welche den Kriterien entsprechen aus dem Storage.
     * 
     * @param user
     *            User, welcher als Vergleichswert dient.
     * @return Liefert einen Integer zur&uuml;ck, welcher angibt wie viele User
     *         gel&ouml;scht wurden.
     * @throws DeleteException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             L&ouml;schvorgangs zu einem Fehler kommt.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    int deleteUser(UniversityUser user) throws DeleteException, ReadException;
}
