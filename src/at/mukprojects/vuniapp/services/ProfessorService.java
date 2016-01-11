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

import at.mukprojects.vuniapp.models.Professor;

/**
 * Service f&uuml;r den Umgang mit Professoren.
 * 
 * @author kerrim
 */
public interface ProfessorService {

    /**
     * Sucht nach Professoren die dem Input &auml;hneln und gibt eine Liste der
     * gefundenen Professoren zur&uuml;ck.
     * 
     * @param input
     *            Suchbegriff nach dem gesucht werden soll.
     * @return Liste der gefundenen Professoren.
     * @throws IOException
     *             Wird geworfen falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     */
    ArrayList<Professor> searchProfessor(String input) throws IOException;

    /**
     * L&auml;dt die Details eines Professors nach. Hierbei wird der
     * &uuml;bergebene Professor nicht kopiert, sondern die Details
     * hineingeladen.
     * 
     * @param professor
     *            Professor dessen Details geladen werden sollen.
     * @return Vorher &uuml;bergebener Professor mit den Details.
     * @throws IOException
     *             Wird geworfen falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     */
    Professor getDetails(Professor professor) throws IOException;

}
