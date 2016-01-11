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
import at.mukprojects.vuniapp.models.Subject;

/**
 * Interface zum Holen von F&auml;chern und Informationen wie ECTS,
 * Wochenstunden, Pr&uuml;fungsterminen usw.
 * 
 * @author kerrim
 */
public interface SubjectService {

    /**
     * Liefert eine Liste aller gespeicherten F&auml;cher zur&uuml;ck.
     * 
     * @return Liste aller gespeicherten F&auml;cher.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    ArrayList<Subject> readSubjects() throws IOException, InvalidLoginException;

    /**
     * L&auml;dt die Details eines Faches nach.
     * 
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
    Subject readDetails(Subject subject) throws IOException,
            InvalidLoginException;

    /**
     * Speichert F&auml;cher ab.
     * 
     * @param subjects
     *            F&auml;cher die gespeichert werden sollen.
     * @throws IOException
     *             Wird geworfen falls beim Speichern der F&auml;cher ein Felher
     *             passiert ist.
     */
    void writeSubjects(ArrayList<Subject> subjects) throws IOException;

    /**
     * L&ouml;scht gespeicherte F&auml;cher.
     * 
     * @param subjects
     *            Liste der zu l&ouml;schenden F&auml;cher.
     * @throws IOException
     *             Wird geworfen falls beim L&ouml;schen ein Fehler aufgetreten
     *             ist.
     */
    void removeSubjects(ArrayList<Subject> subjects) throws IOException;
}
