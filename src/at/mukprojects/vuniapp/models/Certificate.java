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
 * Diese Klasse stellt ein Zeugnis dar.
 * 
 * @author kerrim
 */
public class Certificate implements Serializable, Comparable<Certificate> {
    private static final long serialVersionUID = 1L;

    private University        university;
    private String            nr;
    private String            type;
    private String            title;
    private Float             hours;
    private Float             ects;
    private Date              examDate;
    private String            stNr;
    private String            grade;
    private String            professor;
    private boolean           active;

    /**
     * Erzeugt ein neues Zeugnis.
     * 
     * @param university
     *            Universit&auml; auf welche das Zeugnis ausgestellt wurde.
     * @param nr
     *            LVA Nummer der Lehrveranstaltung.
     * @param type
     *            Typ der Lehrveranstaltung.
     * @param title
     *            Titel der Lehrveranstaltung.
     * @param hours
     *            Anzahl der Wochenstunden der Lehrveranstaltung.
     * @param ects
     *            Anzahl der ECTS der Lehrveranstaltung.
     * @param examDate
     *            Pr&uuml;ngstermin der Lehrveranstaltung. Bei &Uuml;bungen
     *            hängt dieser Wert von der Eintragung der Universit&auml;t ab.
     * @param stNr
     *            Studienkennzahl des zum Anmelden genutzten Studiums.
     * @param grade
     *            Note die in dieser Lehrveranstaltung erreicht wurde.
     * @param professor
     *            Professor, welcher die Pr&uuml;fung oder Lehrveranstaltung
     *            geleitet hat.
     * @param active
     *            Falls dieses Zeugnis nicht mehr gilt auf false setzen, sonst
     *            true.
     */
    public Certificate(final University university, final String nr,
            final String type, final String title, final float hours,
            final float ects, final Date examDate, final String stNr,
            final String grade, final String professor, final boolean active) {
        super();
        this.university = university;
        this.nr = nr;
        this.type = type;
        this.title = title;
        this.hours = hours;
        this.ects = ects;
        this.examDate = examDate;
        this.stNr = stNr;
        this.grade = grade;
        this.professor = professor;
        this.active = active;
    }

    @Override
    public final String toString() {
        return "Certificate [nr=" + nr + ", type=" + type + ", title=" + title
                + ", hours=" + hours + ", ects=" + ects + ", examDate="
                + examDate + ", stNr=" + stNr + ", grade=" + grade
                + ", professor=" + professor + "]";
    }

    /**
     * Liefert die Universit&auml;t, an die das Fach absolviert worden ist,
     * zur&uuml;ck.
     * 
     * @return Universit&auml;t an die das Fach absolviert wurde.
     */
    public final University getUniversity() {
        return university;
    }

    /**
     * Liefert die Lehrveranstaltungs Nummer zur&uuml;ck.
     * 
     * @return Lehrveranstaltungsnummer des Faches.
     */
    public final String getNr() {
        return nr;
    }

    /**
     * Liefert den Typen des Faches zur&uuml;ck (z.B. UE, VO, ...).
     * 
     * @return Typ des Faches.
     */
    public final String getType() {
        return type;
    }

    /**
     * Liefert den Namen der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Name der Lehrveranstaltung.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Liefert die Anzahl der Wochenstunden dieser Lehrveranstaltung
     * zur&uuml;ck.
     * 
     * @return Anzahl der Wochenstunden.
     */
    public final float getHours() {
        return hours;
    }

    /**
     * Liefert die Anzahl der ECTS Punkte, die das Fach Wert ist, zur&uuml;ck.
     * 
     * @return Anzahl der ECTS Punkte.
     */
    public final float getEcts() {
        return ects;
    }

    /**
     * Liefert das Pr&uuml;fungsdatum der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Datum der absolvierten Pr&uuml;fung.
     */
    public final Date getExamDate() {
        return examDate;
    }

    /**
     * Liefert die Studienkennzahl zur&uuml;ck.
     * 
     * @return Studienkennzahl, mit welcher diese Lehrveranstaltung absolviert
     *         wurde.
     */
    public final String getStNr() {
        return stNr;
    }

    /**
     * Liefert die Note der Lehrveranstaltung zur&uuml;ck.
     * 
     * @return Note der Lehrveranstaltung.
     */
    public final String getGrade() {
        return grade;
    }

    /**
     * Liefert den Namen des Professors zur&uuml;ck.
     * 
     * @return Name des Professors.
     */
    public final String getProfessor() {
        return professor;
    }

    /**
     * Liefert zur&uuml;ck ob das Zeugnis noch g&uuml;ltig ist.
     * 
     * @return Ist das Zeugnis noch g&uuml;ltig.
     */
    public final boolean getActive() {
        return active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final Certificate another) {
        return examDate.compareTo(another.examDate);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((ects == null) ? 0 : ects.hashCode());
        result = prime * result
                + ((examDate == null) ? 0 : examDate.hashCode());
        result = prime * result + ((grade == null) ? 0 : grade.hashCode());
        result = prime * result + ((hours == null) ? 0 : hours.hashCode());
        result = prime * result + ((nr == null) ? 0 : nr.hashCode());
        result = prime * result
                + ((professor == null) ? 0 : professor.hashCode());
        result = prime * result + ((stNr == null) ? 0 : stNr.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result
                + ((university == null) ? 0 : university.hashCode());
        return result;
    }

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
        Certificate other = (Certificate) obj;
        if (active != other.active) {
            return false;
        }
        if (ects == null) {
            if (other.ects != null) {
                return false;
            }
        } else if (!ects.equals(other.ects)) {
            return false;
        }
        if (examDate == null) {
            if (other.examDate != null) {
                return false;
            }
        } else if (!examDate.equals(other.examDate)) {
            return false;
        }
        if (grade != other.grade) {
            return false;
        }
        if (hours == null) {
            if (other.hours != null) {
                return false;
            }
        } else if (!hours.equals(other.hours)) {
            return false;
        }
        if (nr == null) {
            if (other.nr != null) {
                return false;
            }
        } else if (!nr.equals(other.nr)) {
            return false;
        }
        if (professor == null) {
            if (other.professor != null) {
                return false;
            }
        } else if (!professor.equals(other.professor)) {
            return false;
        }
        if (stNr == null) {
            if (other.stNr != null) {
                return false;
            }
        } else if (!stNr.equals(other.stNr)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (university == null) {
            if (other.university != null) {
                return false;
            }
        } else if (!university.equals(other.university)) {
            return false;
        }
        return true;
    }

    /**
     * Setzt ein Zeugnis auf g&uuml;ltig oder ung&uuml;ltig.
     * 
     * @param active
     *            G&uuml;ltigkeit des Zeugnisses.
     */
    public final void setActive(final boolean active) {
        this.active = active;
    }
}
