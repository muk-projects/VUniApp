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
import java.util.ArrayList;

import at.mukprojects.vuniapp.models.base.University;

/**
 * Klasse zum Speichern von F&auml;chern.
 * 
 * @author Kerrim
 */
public class Subject implements Serializable {
    private static final long    serialVersionUID = 5L;
    public static final String   SUBJECT          = "subject";

    private University           university;
    private String               name;
    private boolean              taken;

    private String               link;


    private String               lvanr;
    private String               type;
    private String               semester;

    private Float                ects;
    private Float                hours;

    private boolean              containsDetails  = false;
    private ArrayList<Professor> professors;
    private ArrayList<Event>     exams;
    private ArrayList<Event>     appointments;
    private String               goals;
    private String               content;

    private String               id;

    public Subject(final String id) {
        this.setId(id);
    }

    /**
     * Initialisiert ein Fach mit allen n&ouml;tigen Daten.
     * 
     * @param university
     *            Universit&auml;t auf die das Fach belegt ist.
     * @param name
     *            Name des Faches.
     * @param taken
     *            true falls man in diesem Fach angemeldet ist.
     * @param link
     *            Link zu genaueren Informationen über das Fach.
     * @param lvanr
     *            Die Lehrveranstaltungsnummer des Faches.
     * @param semester
     *            Das Semester dieses Faches.
     */
    public Subject(final University university, final String name,
            final boolean taken, final String link, final String lvanr,
            final String semester) {
        this(university, name, taken, link, lvanr, null, semester, null, null);
    }

    /**
     * Initialisiert ein Fach mit allen n&ouml;tigen Daten.
     * 
     * @param university
     *            Universit&auml;t auf die das Fach belegt ist.
     * @param name
     *            Name des Faches.
     * @param taken
     *            true falls man in diesem Fach angemeldet ist.
     * @param link
     *            Link zu genaueren Informationen über das Fach.
     * @param lvanr
     *            Die Lehrveranstaltungsnummer des Faches.
     * @param type
     *            Typ des Faches, also VU (Vorlesung mit &Uuml;bung), VO
     *            (Vorlesung) oder UE (&Uuml;bung).
     * @param semester
     *            Das Semester dieses Faches.
     * @param ects
     *            ECTS Punkte des Faches.
     * @param hours
     *            Semesterwochenstunden des Faches.
     */
    public Subject(final University university, final String name,
            final boolean taken, final String link, final String lvanr,
            final String type, final String semester, final Float ects,
            final Float hours) {
        this.university = university;
        this.name = name;
        this.taken = taken;
        this.link = link;
        this.lvanr = lvanr;
        this.type = type;
        this.semester = semester;
        this.ects = ects;
        this.hours = hours;
        
        this.id = university.getKeyName() + name + link + lvanr + type + semester;
    }

    /**
     * Liefert den Namen des Faches zur&uuml;ck.
     * 
     * @return Name des Faches.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert die LVANr des Faches zur&uuml;ck.
     * 
     * @return LVANr des Faches.
     */
    public final String getLvanr() {
        return lvanr;
    }

    /**
     * Liefert den Typ der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Typ des Faches.
     */
    public final String getType() {
        return type;
    }

    /**
     * Gibt eine kurze Informationszeile &uuml;ber das Fach aus.
     * 
     * @return Ein String mit einigen Informationen &uuml;ber das Fach.
     */
    public final String getInfo() {
        // return lvanr + " | " + (taken ? "Angemeldet" : "Nicht Angemeldet") +
        // " | " + semester + " | " + ects + " ECTS | " + hours + " SWS";
        String lvanr = (this.lvanr == null) ? "" : (this.lvanr + " | ");
        String semester = (this.semester == null) ? ""
                : (" | " + this.semester);
        String ects = (this.ects == null) ? "" : (" | " + this.ects + " ECTS");
        String hours = (this.hours == null) ? ""
                : (" | " + this.hours + " SWS");
        return lvanr + (taken ? "A" : "N") + semester + ects + hours;
    }

    /**
     * Liefert die URL der Detailseite des Faches zur&uuml;ck.
     * 
     * @return URL mit Detailinformationen zum Fach.
     */
    public final String getURL() {
        return link;
    }

    /**
     * Liefert die Universit&auml;t auf die das Fach belegt ist zur&uuml;ck.
     * 
     * @return Die Universit&auml;t in der das Fach belegt wird.
     */
    public final University getUniversity() {
        return university;
    }

    /**
     * Liefert eine Liste aller Vortragenden zur&uuml;ck.
     * 
     * @return Liste aller Vortragenden.
     */
    public final ArrayList<Professor> getProfessors() {
        return professors;
    }

    /**
     * Liefert die URL mit weiteren Informationen &uuml;ber das Fach
     * zur&uuml;ck.
     * 
     * @return URL zu weiteren Informationen.
     */
    public final String getLink() {
        return link;
    }

    /**
     * Setzt die Liste der Vortragenden.
     * 
     * @param professors
     *            Liste aller Vortragenden.
     */
    public final void setProfessors(final ArrayList<Professor> professors) {
        this.professors = professors;
    }

    /**
     * Schaut ob die Details bereits in dieses Fach geladen worden sind.
     * 
     * @return Falls Details in dieses Fach geladen worden sind true, sonst
     *         false.
     */
    public final boolean isContainsDetails() {
        return containsDetails;
    }

    /**
     * Muss gesetzt werden wenn Details in dieses Fach geladen worden sind.
     * 
     * @param containsDetails
     *            Falls Details geladen worden sind auf true setzen.
     */
    public final void setContainsDetails(final boolean containsDetails) {
        this.containsDetails = containsDetails;
    }

    /**
     * Liefert die Ziele der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Ziele der Lehrveranstaltung.
     */
    public final String getGoals() {
        return goals;
    }

    /**
     * Setzt die Ziele der Lehrveranstaltung.
     * 
     * @param goals
     *            Ziele der Lehrveranstaltung.
     */
    public final void setGoals(final String goals) {
        this.goals = goals;
    }

    /**
     * Liefert den Inhalt der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Inhalt der Lehrveranstaltung.
     */
    public final String getContent() {
        return content;
    }

    /**
     * Setzt den Inhalt der Lehrveranstaltung.
     * 
     * @param content
     *            Inhalt der Lehrveranstaltung.
     */
    public final void setContent(final String content) {
        this.content = content;
    }

    /**
     * Gibt die Pr&uuml;fungen der Lehrveranstalung zur&uuml;ck.
     * 
     * @return Pr&uuml;fungen der Lehrveranstalung.
     */
    public final ArrayList<Event> getExams() {
        return exams;
    }

    /**
     * Setzt die Pr&uuml;fungen der Lehrveranstalung.
     * 
     * @param exams
     *            Pr&uuml;fungen der Lehrveranstalung.
     */
    public final void setExams(final ArrayList<Event> exams) {
        this.exams = exams;
    }

    /**
     * Liefert die Kurs (Vorlesung/&Uuml;bung) Termine der Lehrveranstaltung.
     * 
     * @return Kurs Termine der Lehrveranstaltung.
     */
    public final ArrayList<Event> getAppointments() {
        return appointments;
    }

    /**
     * Setzt die Kurs (Vorlesung/&Uuml;bung) Termine der Lehrveranstaltung.
     * 
     * @param appointments
     *            Kurs Termine der Lehrveranstaltung.
     */
    public final void setAppointments(final ArrayList<Event> appointments) {
        this.appointments = appointments;
    }

    /**
     * Liefert eine eindeutige Kennung dieses Faches zur&uuml;ck.
     * 
     * @return Eindeutige ID des Faches.
     */
    public final String getId() {
        return id;
    }

    /**
     * Setzt die ID des Faches.
     * 
     * @param id
     *            Eindeutige ID des Faches.
     */
    public final void setId(final String id) {
        this.id = id;
    }
    
    public String getSemester() {
        return semester;
    }

    public Float getEcts() {
        return ects;
    }

    public Float getHours() {
        return hours;
    }
    
    public boolean isTaken() {
        return taken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public void setLvanr(String lvanr) {
        this.lvanr = lvanr;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setEcts(Float ects) {
        this.ects = ects;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }
}
