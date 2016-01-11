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

package at.mukprojects.vuniapp.models;

import java.io.Serializable;
import java.util.Date;

import at.mukprojects.vuniapp.models.base.University;

/**
 * Diese Klasse stellt einen Termin dar.
 * 
 * @author kerrim
 * @author Mathias
 */
public class Event implements Serializable, Comparable<Event> {
    private static final long serialVersionUID = 2L;

    private String            name;
    private String            description;
    private Date              date;
    private long              duration;
    private String            place;
    private Room              room;
    private String            placeURL;
    private boolean           wholeDay         = false;
    private University        university;

    /**
     * Erstellt ein neues Event Objekt.
     * 
     * @param name
     *            Name des Events.
     * @param description
     *            Eine Beschreibung des Events.
     * @param date
     *            Datum an dem das Event statt findet.
     * @param duration
     *            Dauer des Events in ms.
     * @param place
     *            Ort des Events.
     * @param university
     *            Universit&auml;t des Events.
     */
    public Event(final String name, final String description, final Date date,
            final long duration, final String place, final University university) {
        super();
        this.name = name;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.place = place;
        this.university = university;
        room = new Room(place, university);
    }

    /**
     * Erstellt ein neues Event Objekt.
     * 
     * @param name
     *            Name des Events.
     * @param description
     *            Eine Beschreibung des Events.
     * @param date
     *            Datum an dem das Event statt findet.
     * @param duration
     *            Dauer des Events in ms.
     * @param place
     *            Ort des Events.
     * @param university
     *            Universit&auml;t des Events.
     * @param placeURL
     *            Url der Details zum Ort.
     */
    public Event(final String name, final String description, final Date date,
            final long duration, final String place,
            final University university, final String placeURL) {
        super();
        this.name = name;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.place = place;
        this.university = university;
        this.placeURL = placeURL;
        room = new Room(place, university, placeURL);
    }

    /**
     * Erstellt ein neues Event Objekt.
     * 
     * @param name
     *            Name des Events.
     * @param description
     *            Eine Beschreibung des Events.
     * @param date
     *            Datum an dem das Event statt findet.
     * @param place
     *            Ort des Events.
     * @param wholeDay
     *            Falls dieser Event den ganzen Tag dauern soll auf true setzen,
     *            standardm&auml;&szlig;ig ist wholeDay auf false.
     * @param university
     *            Universit&auml;t des Events.
     * @param placeURL
     *            Url der Details zum Ort.
     */
    public Event(final String name, final String description, final Date date,
            final String place, final boolean wholeDay,
            final University university, final String placeURL) {
        this(name, description, date, 0, place, university, placeURL);
        this.wholeDay = wholeDay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Event [name=" + name + ", description=" + description
                + ", date=" + date + ", duration=" + duration + ", place="
                + place + ", placeURL=" + placeURL + ", wholeDay=" + wholeDay
                + "]";
    }

    /**
     * Liefert den Namen des Events zur&uuml;ck.
     * 
     * @return Name des Events.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert die Beschreibung des Events zur&uuml;ck.
     * 
     * @return Beschreibung des Events.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Liefert den Zeitpunkt des Events zur&uuml;ck.
     * 
     * @return Zeitpunkt des Events.
     */
    public final Date getDate() {
        return date;
    }

    /**
     * Liefert die Dauer des Events zur&uuml;ck.
     * 
     * @return Dauer des Events.
     */
    public final long getDuration() {
        return duration;
    }

    /**
     * Liefert den Ort des Events zur&uuml;ck.
     * 
     * @return Ort des Events.
     */
    public final String getPlace() {
        return place;
    }

    /**
     * Liefert zur&uuml;ck ob es sich bei dem Event um ein ganztages Event
     * handelt.
     * 
     * @return Falls es sich um ein ganzt&auml;giges Event handelt true, sonst
     *         false.
     */
    public final boolean isWholeDay() {
        return wholeDay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final Event another) {
        return date.compareTo(another.date);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((place == null) ? 0 : place.hashCode());
        result = prime * result
                + ((placeURL == null) ? 0 : placeURL.hashCode());
        result = prime * result + (wholeDay ? 1231 : 1237);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (duration != other.duration) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (place == null) {
            if (other.place != null) {
                return false;
            }
        } else if (!place.equals(other.place)) {
            return false;
        }
        if (placeURL == null) {
            if (other.placeURL != null) {
                return false;
            }
        } else if (!placeURL.equals(other.placeURL)) {
            return false;
        }
        if (wholeDay != other.wholeDay) {
            return false;
        }
        return true;
    }

    /**
     * Liefert die URL des Raums.
     * 
     * @return URL des Raums.
     */
    public final String getPlaceURL() {
        return placeURL;
    }

    /**
     * Liefert die Universit&auml; des Events.
     * 
     * @return Universit&auml; des Events.
     */
    public final University getUniversity() {
        return university;
    }

    /**
     * Liefert den Raum des Events.
     * 
     * @return Den Raum.
     */
    public final Room getRoom() {
        return room;
    }

}
