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

import at.mukprojects.vuniapp.enumeration.TaskColor;
import at.mukprojects.vuniapp.enumeration.TaskSortArg;

/**
 * Die Klasse Task enth&auml;lt alle Attribute einer Aufgabe. Jeder Aufgabe hat
 * eine ID. Diese wird von der DAO gesetzt, wenn die Aufgabe in die Datenbank
 * gechrieben wird beziehungweise aus der Datenbank geladen wird.
 * 
 * @author Mathias
 */
public class Task implements Serializable, Comparable<Task> {
    private static final long  serialVersionUID = 1L;

    public static final String TASK             = "task";

    private Long               id;
    private String             title;
    private String             description;
    private TaskColor          color;
    private Long               deadlineDate;
    private String             imagePath;

    private TaskSortArg        sortArg;

    /**
     * Erstellt einen neuen Task.
     * 
     * @param title
     *            Der Titel des Tasks.
     * @param description
     *            Die Beschreibung des Tasks.
     * @param color
     *            Die Farbe des Tasks. (Optional)
     * @param deadlineDate
     *            Das Datum f&uuml;r die Deadline als Millisekunden. Wenn 0
     *            &uuml;bergegebn wird so wird der Parameter auf null gesetzt.
     *            (Optional)
     * @param imagePath
     *            Der Pfad zu dem Bild des Tasks. (Optional)
     */
    public Task(final String title, final String description,
            final String color, final Long deadlineDate, final String imagePath) {
        this.title = title;
        this.description = description;
        if (color != null) {
            this.color = TaskColor.valueOfTaskColor(color);
        }
        if (deadlineDate == null || deadlineDate == 0) {
            this.deadlineDate = null;
        } else {
            this.deadlineDate = deadlineDate;
        }
        this.imagePath = imagePath;
    }

    /**
     * Liefert die ID des Tasks.
     * 
     * @return Die ID des Task.
     */
    public final Long getId() {
        return id;
    }

    /**
     * Liefert den Titel des Tasks.
     * 
     * @return Den Titel des Tasks.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Liefert die Beschreibung des Tasks.
     * 
     * @return Die Beschreibung des Tasks.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Liefert die Farbe des Tasks.
     * 
     * @return Die Farbe des Tasks.
     */
    public final String getColor() {
        if (color != null) {
            return color.displayValue();
        }
        return null;
    }

    /**
     * Liefert die Farbe des Tasks.
     * 
     * @return Die Farbe des Tasks.
     */
    public final TaskColor getTaskColor() {
        return color;
    }

    /**
     * Liefert das Datum der Deadline.
     * 
     * @return Das Datum der Deadline.
     */
    public final Long getDeadlineDate() {
        return deadlineDate;
    }

    /**
     * Liefert den Pfad des Bildes.
     * 
     * @return Den Pfad des Bildes.
     */
    public final String getImagePath() {
        return imagePath;
    }

    /**
     * Setzt die ID des Tasks.
     * 
     * @param id
     *            Die ID des Tasks.
     */
    public final void setId(final Long id) {
        this.id = id;
    }

    /**
     * Setzt die Farbe des Tasks.
     * 
     * @param color
     *            Die Farbe des Tasks.
     */
    public final void setColor(final String color) {
        this.color = TaskColor.valueOfTaskColor(color);
    }

    /**
     * Setzt die Deadline des Tasks.
     * 
     * @param deadlineDate
     *            Die Deadline des Tasks.
     */
    public final void setDeadlineDate(final Long deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    /**
     * Setzt den Pfad des Bildes.
     * 
     * @param imagePath
     *            Den Pfad des Bildes.
     */
    public final void setImagePath(final String imagePath) {
        this.imagePath = imagePath;
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
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result
                + ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((imagePath == null) ? 0 : imagePath.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        Task other = (Task) obj;
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        if (deadlineDate == null) {
            if (other.deadlineDate != null) {
                return false;
            }
        } else if (!deadlineDate.equals(other.deadlineDate)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (imagePath == null) {
            if (other.imagePath != null) {
                return false;
            }
        } else if (!imagePath.equals(other.imagePath)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Task [id=" + id + ", title=" + title + ", description="
                + description + ", color=" + color + ", deadlineDate="
                + deadlineDate + ", imagePath=" + imagePath + "]";
    }

    /**
     * Mittels dieser Methode wird das sortArg gesetzt. Dadurch wird bestimmt
     * wie zwei Tasks mit einander verglichen werden.
     * 
     * @param sortArg
     *            Argument welche die compareTo Methode bestimmt.
     */
    public final void setSortArg(final TaskSortArg sortArg) {
        this.sortArg = sortArg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final Task another) {
        int retValue = 0;

        if (sortArg == TaskSortArg.TITLE) {
            retValue = another.title.compareTo(another.getTitle());
        } else if (sortArg == TaskSortArg.DATE) {
            retValue = another.deadlineDate
                    .compareTo(another.getDeadlineDate());
        } else {
            retValue = another.color.compareTo(another.getTaskColor());
        }

        return retValue;
    }
}
