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

import at.mukprojects.vuniapp.dao.EventDAO;
import at.mukprojects.vuniapp.exceptions.MissingDataException;
import at.mukprojects.vuniapp.models.Event;

/**
 * Implementierung des Interfaces zum Zugriff auf Events.
 * 
 * @author kerrim
 */
public class EventServiceStd implements EventService {

    private EventDAO eventDAO;

    /**
     * Erstellt einen neuen StandardService f&uuml;r den Zugriff auf Events.
     * 
     * @param eventDAO
     *            Die DAO die zum Datenzugriff genutzt werden soll.
     */
    public EventServiceStd(final EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.EventService#writeEvent(at.mukprojects
     * .vuniapp.models.Event)
     */
    @Override
    public final void writeEvent(final Event event) throws IOException,
            MissingDataException {
        eventDAO.writeEvent(event);
    }

}
