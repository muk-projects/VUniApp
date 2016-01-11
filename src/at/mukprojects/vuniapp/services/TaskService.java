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

import java.util.List;

import at.mukprojects.vuniapp.enumeration.TaskSortArg;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.Task;

/**
 * Dieses Interface wird von allen TaskServicess implementiert und beschreibt
 * alle Methoden welche zum Laden und Speichern eines Tasks notwendig sind.
 * 
 * @author Mathias
 */
public interface TaskService {
    /**
     * Liefert einen Task mit der &uuml;bergeben ID zur&uuml;ck.
     * 
     * @param id
     *            Die ID des Tasks, welcher gesucht wird.
     * @return Den Task mit der &uuml;bergebenen ID oder null, falls kein Task
     *         mit der ID gefunden werden konnte.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    Task getTaskById(long id) throws ReadException;

    /**
     * Liefert die gesuchten Tasks zur&uuml;ck.
     * 
     * @param searchTask
     *            Die Attribute des Tasks werden dazu verwendet die
     *            gew&uuml;nschten Tasks zu finden.
     * @return Eine Liste aller Tasks, welche mit dem &uuml;bergeben Task
     *         &uuml;bereinstimmen.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    List<Task> getTasks(Task searchTask) throws ReadException;

    /**
     * Liefert alle Tasks zur&uuml;ck.
     * 
     * @return Eine Liste aller Tasks.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    List<Task> getAllTasks() throws ReadException;

    /**
     * Liefert alle Tasks zur&uuml;ck. Dabei kann mittels eines Parameters
     * angegeben werden wie die Tasks sortiert werden sollen.
     * 
     * @param sortArg
     *            Mit Hilfe des Arguments kann angegeben werden, wie die Liste
     *            sortiert werden soll.
     * @return Eine sortierte Liste aller Tasks.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    List<Task> getAllTasksSorted(TaskSortArg sortArg) throws ReadException;

    /**
     * Erstellt einen neuen Task im Datenspeicher.
     * 
     * @param createTask
     *            Der Task, welcher erstellt werden soll. Hat dieser Task
     *            bereits eine ID so wird diese ignoriert.
     * @return Den Task mit gesetzter ID.
     * @throws CreateException
     *             Die Exception wird geworfen, wenn es beim Erstellen eines
     *             Objekts zu einem Fehler kommt und das Objekt nicht erstellt
     *             werden kann.
     */
    Task createTask(Task createTask) throws CreateException;

    /**
     * Die Methode such mit Hilfe des &uuml;bergeben Tasks, ob im Datenspeicher
     * bereits ein Task mit der selben ID vorhanden ist. Wenn dies der Fall ist
     * so wird dieser Task aktualisiert, anderenfalls wird ein neuer Task
     * erstellt.
     * 
     * @param task
     *            Der &uuml;bergebene Task.
     * @return Liefert den Task zur&uuml;ck, welcher in die Datenbank
     *         geschrieben wurde.
     * @throws CreateException
     *             Die Exception wird geworfen, wenn es beim Erstellen eines
     *             Objekts zu einem Fehler kommt und das Objekt nicht erstellt
     *             werden kann.
     * @throws UpdateException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des Updates
     *             zu einem Fehler kommt.
     * @throws ReadException
     *             Diese Exception wird geworfen, wenn es beim Auslesen der zu
     *             &auml;ndernden Tasks zu einem Fehler kommt.
     */
    Task createOrUpdateTask(Task task) throws CreateException, UpdateException,
            ReadException;

    /**
     * Aktualisiert die gew&uuml;nschten Tasks im Datenspeicher.
     * 
     * @param setTask
     *            Der Task ent&auml;hlt die Werte auf, welche alle gefunden Task
     *            gesetzt werden. Dabei wird die ID des setTask nicht beachtet,
     *            da diese nicht ver&auml;ndert werden kann.
     * @param whereTask
     *            Der Task dient als Vergleichswert, um festzustellen, welche
     *            Tasks ge&auml;ndert werden sollen.
     * @return Liefert einen Integer zur&uuml;ck, welcher angibt wie viele Tasks
     *         ge&auml;ndert wurden.
     * @throws UpdateException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des Updates
     *             zu einem Fehler kommt.
     * @throws ReadException
     *             Diese Exception wird geworfen, wenn es beim Auslesen der zu
     *             &auml;ndernden Tasks zu einem Fehler kommt.
     */
    int updateTasks(final Task setTask, final Task whereTask)
            throws UpdateException, ReadException;

    /**
     * L&ouml;scht alle Tasks, welche den Kriterien entsprechen aus dem
     * Datenspeicher.
     * 
     * @param deleteTask
     *            Task, welcher als Vergleichswert dient, um die zu
     *            l&ouml;schenden Tasks zu finden.
     * @return Liefert einen Integer zur&uuml;ck, welcher angibt wie viele Tasks
     *         gel&ouml;scht wurden.
     * @throws DeleteException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             L&ouml;schvorgangs zu einem Fehler kommt.
     * @throws ReadException
     *             Diese Exception wird geworfen, wenn es beim Auslesen der zu
     *             l&ouml;schenden Tasks zu einem Fehler kommt.
     */
    int deleteTasks(Task deleteTask) throws DeleteException, ReadException;

    /**
     * L&ouml;scht den Task mit der angegeben ID.
     * 
     * @param id
     *            Die ID des zu l&ouml;schenden Tasks.
     * @return Liefer 0 falls kein Task mit der ID gefunden wurde oder 1 falls
     *         der Task entfernt wurde.
     * @throws DeleteException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             L&ouml;schvorgangs zu einem Fehler kommt.
     * @throws ReadException
     *             Diese Exception wird geworfen, wenn es beim Auslesen der zu
     *             l&ouml;schenden Tasks zu einem Fehler kommt.
     */
    int deleteTask(long id) throws DeleteException, ReadException;
}
