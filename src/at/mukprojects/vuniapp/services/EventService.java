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

import at.mukprojects.vuniapp.exceptions.MissingDataException;
import at.mukprojects.vuniapp.models.Event;

/**
 * Interface f&uuml;r den Zugriff auf Termine.
 * 
 * @author kerrim
 */
public interface EventService {
    /**
     * Schreibt einen Event.
     * 
     * @param event
     *            Event der gespeichert werden soll.
     * @throws IOException
     *             Wird geworfen falls beim Speichern ein Fehler aufgetreten
     *             ist.
     * @throws MissingDataException
     *             Wird geworfen falls ein relevantes Feld in der Eventklasse
     *             auf null gesetzt war.
     */
    void writeEvent(Event event) throws IOException, MissingDataException;

}
