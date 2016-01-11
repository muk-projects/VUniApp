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
import java.util.List;

import android.util.Log;
import at.mukprojects.vuniapp.exceptions.MissingDataException;
import at.mukprojects.vuniapp.models.Event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

/**
 * Diese Klasse implementiert eine EventDAO für den Google Kalender &uuml;ber
 * die Google API.
 * 
 * @author kerrim
 */
public class EventDAOGoogleCalendar implements EventDAO {
    private static final String                       TAG = EventDAOGoogleCalendar.class
                                                                  .getSimpleName();

    private String                                    id;
    private com.google.api.services.calendar.Calendar client;

    /**
     * Erzeugt eine neue EventDAO f&uuml;r den Google Kalender.
     * 
     * @param calendarSummary
     *            Name des zu nutzenden Kalenders.
     * @param client
     *            Bereits angemeldeter Client der auf den Kalender zugreifen
     *            kann.
     * @throws IOException
     *             Wird geworfen falls bei einem Lese- oder Schreibzugriff ein
     *             Fehler aufgetreten ist.
     */
    public EventDAOGoogleCalendar(final String calendarSummary,
            final com.google.api.services.calendar.Calendar client)
            throws IOException {

        this.client = client;

        CalendarList feed = client.calendarList().list()
                .setFields("items(id,summary)").execute();
        List<CalendarListEntry> calendarList = feed.getItems();
        boolean vuniappExists = false;
        for (CalendarListEntry calendarListEntry : calendarList) {
            if (calendarListEntry.getSummary().equals(calendarSummary)) {
                vuniappExists = true;
                this.id = calendarListEntry.getId();
            }
        }

        if (!vuniappExists) {
            Calendar vuniapp = new Calendar();
            vuniapp.setSummary(calendarSummary);
            Calendar calendar = client.calendars().insert(vuniapp)
                    .setFields("id,summary").execute();
            this.id = calendar.getId();
            Log.d(TAG, calendar.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.EventDAO#writeEvent(at.mukprojects.vuniapp
     * .models.Event)
     */
    @Override
    public final void writeEvent(final Event event) throws IOException,
            MissingDataException {
        Log.i(TAG, "Methode: writeEvent wird gestartet.");
        com.google.api.services.calendar.model.Event tEvent = new com.google.api.services.calendar.model.Event();
        com.google.api.services.calendar.model.EventDateTime start = new com.google.api.services.calendar.model.EventDateTime();
        start.setDateTime(new DateTime(event.getDate()));
        com.google.api.services.calendar.model.EventDateTime end = new com.google.api.services.calendar.model.EventDateTime();
        end.setDateTime(new DateTime(event.getDate().getTime()
                + event.getDuration()));
        tEvent.setSummary(event.getName());
        String university = event.getUniversity().getName();
        tEvent.setLocation(university + " " + event.getPlace());
        tEvent.setStart(start);
        tEvent.setEnd(end);
        tEvent.setDescription(event.getDescription());
        com.google.api.services.calendar.model.Event inserted = client.events()
                .insert(id, tEvent).execute();
        Log.d(TAG, "Folgender Termin den Google Kalender hinzugefügt: "
                + inserted);
        Log.i(TAG, "Methode: writeEvent wird verlassen.");

    }

}
