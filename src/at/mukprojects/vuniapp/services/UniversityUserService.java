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

import java.util.HashMap;
import java.util.List;

import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Interface f&uuml;r den Zugriff auf den UserDAO.
 * 
 * @author Mathias
 */
public interface UniversityUserService {
    /**
     * Liefert alle User zur&uuml;ck.
     * 
     * @return Alle User im Storage.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> getUsers() throws ReadException,
            NotInitializedException;

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
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> getUsers(UniversityUser user) throws ReadException,
            NotInitializedException;

    /**
     * Liefert alle User einer Universit&auml;t zur&uuml;ck.
     * 
     * @param university
     *            Die Universit&auml;t zu welcher die Benutzer ausgelsen werden
     *            sollen.
     * @return Alle User der Univerist&auml;t (Der Key der HashMap ist der
     *         jeweilige UserKey und die Value der User).
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    HashMap<String, UniversityUser> getUsersFromUniversity(University university)
            throws ReadException, NotInitializedException;

    /**
     * Diese Methode liest die Bentzer zu einer Universit&auml;t aus.
     * 
     * @param university
     *            Die Universit&auml;t zu welcher die Benutzer ausgelsen werden
     *            sollen.
     * @param userKeys
     *            Wenn eine Universit&auml;t mehr als einen Benutzer
     *            ben&ouml;tigt kann mit einer List an UserKeys spezifiziert
     *            werden welche Benutzer gesucht werden sollen. Wenn es f&uuml;r
     *            die &uuml;bergebene Universit&auml;t nur einen einzigen
     *            Benutzer gibt so kann eine leere Liste oder auch null
     *            &uuml;bergeben werden.
     * @return Liefert alle den Parametern entsprechende Benutzer zur&uuml;ck
     *         (Der Key der HashMap ist der jeweilige UserKey und die Value der
     *         User).
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    HashMap<String, UniversityUser> getUsersFromUniversity(
            University university, List<String> userKeys) throws ReadException,
            NotInitializedException;

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
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser getUserByID(long id) throws ReadException,
            NotInitializedException;

    /**
     * Erstellt den &uuml;bergebenen User mit Hilfe der DAO.
     * 
     * @param user
     *            Der zu erstellende User. Wenn als UserKey null &uuml;bergeben
     *            wird so geht die Datenbank davon aus, dass diese
     *            Universit&auml;t nur einen Benutzer hat und speichert diesen
     *            Benutzer mit dem UserKey "onlyuser".
     * @return Liefert den User, mit gesetzter ID, zur&uuml;ck.
     * @throws CreateException
     *             Die Exception wird geworfen, wenn es beim Erstellen eines
     *             Objekts zu einem Fehler kommt und das Objekt nicht erstellt
     *             werden kann.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser createUser(UniversityUser user) throws CreateException,
            NotInitializedException;

    /**
     * Abh&auml;ngig davon, ob der User bereits existiert wird er entweder
     * erstellt oder aktualisiert.
     * 
     * @param user
     *            er zu erstellende User. Wenn als UserKey null &uuml;bergeben
     *            wird so geht die Datenbank davon aus, dass diese
     *            Universit&auml;t nur einen Benutzer hat und speichert diesen
     *            Benutzer mit dem UserKey "onlyuser"
     * @return Liefert den User, mit gesetzter ID, zur&uuml;ck.
     * @throws CreateException
     *             Die Exception wird geworfen, wenn es beim Erstellen eines
     *             Objekts zu einem Fehler kommt und das Objekt nicht erstellt
     *             werden kann.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     * @throws UpdateException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des Updates
     *             zu einem Fehler kommt.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser createOrUpdateUser(UniversityUser user)
            throws CreateException, ReadException, UpdateException,
            NotInitializedException;

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
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    int updateUser(final UniversityUser setUser, final UniversityUser whereUser)
            throws UpdateException, ReadException, NotInitializedException;

    /**
     * L&ouml;scht alle User, welche den Kriterien entsprechen.
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
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    int deleteUser(UniversityUser user) throws DeleteException, ReadException,
            NotInitializedException;

    /**
     * Verschl&uuml;sselt alle User mit einem neuen Pin.
     * 
     * @param newPin
     *            Neuer Pin. Falls Null &uuml;bergeben wird, werden die User nur
     *            entschl&uuml;sselt.
     * @param oldPin
     *            Alter Pin. Falls Null &uuml;bergeben wird, werden die User nur
     *            verschl&uuml;sselt.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     * @throws UpdateException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des Updates
     *             zu einem Fehler kommt.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    void encryptionUsers(String newPin, String oldPin) throws ReadException,
            UpdateException, NotInitializedException;

    /**
     * Verschl&uuml;sselt einen User und liefert diesen zur&uuml;ck.
     * 
     * @param user
     *            Der zu verschl&uuml;sselnde User.
     * @return Der verschl&uuml;sselte User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser encryptUser(UniversityUser user)
            throws NotInitializedException;

    /**
     * Entschl&uuml;sselt einen User und liefert diesen zur&uuml;ck.
     * 
     * @param user
     *            Der zu entschl&uuml;sselnde User.
     * @return Der entschl&uuml;sselte User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser decryptUser(UniversityUser user)
            throws NotInitializedException;

    /**
     * Verschl&uuml;sselt eine UserList und liefert diese zur&uuml;ck.
     * 
     * @param userList
     *            Die zu verschl&uuml;sselnden User.
     * @return Die verschl&uuml;sselten User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> encryptUserList(List<UniversityUser> userList)
            throws NotInitializedException;

    /**
     * Entschl&uuml;sselt eine UserList und liefert diese zur&uuml;ck.
     * 
     * @param userList
     *            Der zu entschl&uuml;sselnden User.
     * @return Die entschl&uuml;sselten User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> decryptUserList(List<UniversityUser> userList)
            throws NotInitializedException;

    /**
     * Verschl&uuml;sselt einen User und liefert diesen zur&uuml;ck.
     * 
     * @param user
     *            Der zu verschl&uuml;sselnde User.
     * @param pin
     *            Der Pin der Verschl&uuml;sselung.
     * @return Der verschl&uuml;sselte User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser encryptUser(UniversityUser user, String pin)
            throws NotInitializedException;

    /**
     * Entschl&uuml;sselt einen User und liefert diesen zur&uuml;ck.
     * 
     * @param user
     *            Der zu entschl&uuml;sselnde User.
     * @param pin
     *            Der Pin der Verschl&uuml;sselung.
     * @return Der entschl&uuml;sselte User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    UniversityUser decryptUser(UniversityUser user, String pin)
            throws NotInitializedException;

    /**
     * Verschl&uuml;sselt eine UserList und liefert diese zur&uuml;ck.
     * 
     * @param userList
     *            Die zu verschl&uuml;sselnden User.
     * @param pin
     *            Der Pin der Verschl&uuml;sselung.
     * @return Die verschl&uuml;sselten User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> encryptUserList(List<UniversityUser> userList,
            String pin) throws NotInitializedException;

    /**
     * Entschl&uuml;sselt eine UserList und liefert diese zur&uuml;ck.
     * 
     * @param userList
     *            Der zu entschl&uuml;sselnden User.
     * @param pin
     *            Der Pin der Verschl&uuml;sselung.
     * @return Die entschl&uuml;sselten User.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    List<UniversityUser> decryptUserList(List<UniversityUser> userList,
            String pin) throws NotInitializedException;
}