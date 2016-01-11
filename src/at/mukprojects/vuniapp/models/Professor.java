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

import at.mukprojects.vuniapp.models.base.University;

/**
 * Professoren Objekt.
 * 
 * @author kerrim
 */
public class Professor implements Serializable {
    private static final long  serialVersionUID = 1L;
    public static final String PROFESSOR        = "professor";

    private String             name;
    private University         university;
    private String             urlToDetails;
    private String             details;

    /**
     * Erzeugt einen neuen Professor.
     * 
     * @param name
     *            Name des Professors.
     * @param universityKey
     *            Universit&auml;t des Professors.
     * @param urlToDetails
     *            URL mit weiteren Informationen zum Professor.
     */
    public Professor(final String name, final University university,
            final String urlToDetails) {
        super();
        this.name = name;
        this.urlToDetails = urlToDetails;
        this.university = university;
    }

    /**
     * Liefert die Details eines Professors zur&uuml;ck.
     * 
     * @return Details des Professors.
     */
    public final String getDetails() {
        return details;
    }

    /**
     * Setzt die Details eines Professors.
     * 
     * @param details
     *            Zu setzende Details.
     */
    public final void setDetails(final String details) {
        this.details = details;
    }

    /**
     * Liefert den Namen des Professors zur&uuml;ck.
     * 
     * @return Name des Professors.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert die URL zu den Details des Professors zur&uuml;ck.
     * 
     * @return URL zu den Deteils des Professors.
     */
    public final String getUrlToDetails() {
        return urlToDetails;
    }

    /**
     * Liefert die Universit&auml;t des Professors zur&uuml;ck.
     * 
     * @return Universit&auml;t des Professors.
     */
    public final University getUniversity() {
        return university;
    }

    @Override
    public final String toString() {
        return "Professor [name=" + name + ", urlToDetails=" + urlToDetails
                + "]";
    }

}
