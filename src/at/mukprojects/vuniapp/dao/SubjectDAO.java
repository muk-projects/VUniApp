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
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.models.Subject;

/**
 * Interface zum Holen von F&auml;chern und Informationen wie ECTS,
 * Wochenstunden, Pr&uuml;fungsterminen usw.
 * 
 * @author kerrim
 */
public interface SubjectDAO {

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
     * @throws ReadException 
     */
    ArrayList<Subject> readSubjects() throws IOException, InvalidLoginException;

    /**
     * Speichert eine Liste an F&auml;chern ab.
     * 
     * @param subjects
     *            Liste der F&auml;cher die gespeichert werden sollen.
     * @throws IOException 
     *             Wird geworfen, falls beim Schreiben der Daten ein Fehler
     *             aufgetreten ist.
     */
    void writeSubjects(ArrayList<Subject> subjects) throws IOException;
    
    /**
     * L&ouml;scht eine Liste von F&auml;chern.
     * 
     * @param subjects
     *            Liste der F&auml;cher die gel&ouml;scht werden sollen.
     * @throws IOException 
     *             Wird geworfen, falls beim L&ouml;schen der Daten ein Fehler
     *             aufgetreten ist.
     */
    void removeSubjects(ArrayList<Subject> subjects) throws IOException;

    /**
     * L&auml;dt die Fachbeschreibung, Termine und weitere gefundene
     * Informationen in das Subject Objekt.
     * 
     * @param subject
     *            Fach welches mit Informationen gef&uuml;llt werden soll.
     * @return Fach mit gefundenen Informationen.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws InvalidLoginException 
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    Subject readDetails(Subject subject) throws IOException, InvalidLoginException;
}
