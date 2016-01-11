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

package at.mukprojects.vuniapp.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Statische Methoden f&uuml;r den Umgang mit Events.
 * 
 * @author kerrim
 */
public abstract class EventHandlingHelper {
    /**
     * Methode zum Erstellen einer Liste von w&ouml;chentlichen Events.
     * 
     * @param startDate
     *            Datum und Zeit des ersten Auftretens des Termins.
     * @param duration
     *            Dauer des Termins.
     * @param endDate
     *            Termin nach letztem Vorkommen.
     * @param name
     *            Name des Events.
     * @param location
     *            Ort des Events.
     * @param description
     *            Beschreibung des Events.
     * @param university
     *            Universit&auml;tskey, welcher die Universität eindeutig beschreibt.
     * @return Liste aller Events zwischen startDate und EndDate im Ein-Wochen
     *         Abstand.
     */
    public static ArrayList<Event> generateWeeklyEvents(final Date startDate,
            final long duration, final Date endDate, final String name,
            final String location, final String description,
            final University university) {
        ArrayList<Event> output = new ArrayList<Event>();

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);

        while (gc.getTime().getTime() <= endDate.getTime()) {
            output.add(new Event(name, description, gc.getTime(), duration,
                    location, university));
            gc.add(GregorianCalendar.DAY_OF_MONTH, 7);
        }

        return output;
    }

    /**
     * Methode zum Erstellen einer Liste von w&ouml;chentlichen ganztages
     * Events.
     * 
     * @param startDate
     *            Datum und Zeit des ersten Auftretens des Termins.
     * @param endDate
     *            Termin nach letztem Vorkommen.
     * @param name
     *            Name des Events.
     * @param location
     *            Ort des Events.
     * @param description
     *            Beschreibung des Events.
     * @param university
     *            Universit&auml;tstag, welcher die Universität eindeutig beschreibt.
     * @param placeURL
     *            Url der Details zum Ort.
     * @return Liste aller Events zwischen startDate und EndDate im Ein-Wochen
     *         Abstand.
     */
    public static ArrayList<Event> generateWholeDayWeeklyEvents(
            final Date startDate, final Date endDate, final String name,
            final String location, final String description,
            final University university, final String placeURL) {
        ArrayList<Event> output = new ArrayList<Event>();

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);

        while (gc.getTime().getTime() <= endDate.getTime()) {
            output.add(new Event(name, description, gc.getTime(), location,
                    true, university, placeURL));
            gc.add(GregorianCalendar.DAY_OF_MONTH, 7);
        }

        return output;
    }
}
