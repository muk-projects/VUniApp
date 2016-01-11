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

import java.util.Collections;
import java.util.List;

import android.util.Log;
import at.mukprojects.vuniapp.dao.TaskDAO;
import at.mukprojects.vuniapp.dao.UniversityUserDAO;
import at.mukprojects.vuniapp.enumeration.TaskSortArg;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.models.UniversityUser;

/**
 * Die Klasse TaskServiceStd ist die standard Implementierung des TaskService
 * Interfaces.
 * 
 * @author Mathias
 */
public class TaskServiceStd implements TaskService {
    private static final String TAG = TaskServiceStd.class.getSimpleName();

    /** DAOs. */
    private TaskDAO             taskDAO;

    /**
     * Erstellt eine neue TaskServiceStd.
     * 
     * @param taskDAO
     *            Die TaskDAO, welche verwendet werden soll.
     * @throws ConnectionException
     *             Diese Exception wird geworfen, wenn es w&auml;hrend dem
     *             Verbindungsaufbau mit der DAO zu einem Fehler kommt.
     */
    public TaskServiceStd(final TaskDAO taskDAO) throws ConnectionException {
        this.taskDAO = taskDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.TaskService#getTaskById(long)
     */
    @Override
    public final Task getTaskById(final long id) throws ReadException {
        Log.i(TAG, "Methode: getTaskById wird gestartet.");
        Task retTask = taskDAO.getTaskById(id);
        Log.i(TAG, "Methode: getTaskById wird verlassen.");
        return retTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#getTasks(at.mukprojects.vuniapp
     * .models.Task)
     */
    @Override
    public final List<Task> getTasks(final Task searchTask)
            throws ReadException {
        Log.i(TAG, "Methode: getTasks wird gestartet.");
        List<Task> retTaskList = taskDAO.getTasks(searchTask);
        Log.i(TAG, "Methode: getTasks wird verlassen.");
        return retTaskList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.TaskService#getAllTasks()
     */
    @Override
    public final List<Task> getAllTasks() throws ReadException {
        Log.i(TAG, "Methode: getAllTasks wird gestartet.");
        List<Task> retTaskList = taskDAO.getAllTasks();
        Log.i(TAG, "Methode: getAllTasks wird verlassen.");
        return retTaskList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#getAllTasksSorted(at.mukprojects
     * .vuniapp.enumeration.TaskSortArg)
     */
    @Override
    public final List<Task> getAllTasksSorted(final TaskSortArg sortArg)
            throws ReadException {
        Log.i(TAG, "Methode: getAllTasks wird gestartet.");
        List<Task> retTaskList = taskDAO.getAllTasks();

        for (Task task : retTaskList) {
            task.setSortArg(sortArg);
        }
        Collections.sort(retTaskList);

        Log.i(TAG, "Methode: getAllTasks wird verlassen.");
        return retTaskList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#createTask(at.mukprojects
     * .vuniapp.models.Task)
     */
    @Override
    public final Task createTask(final Task createTask) throws CreateException {
        Log.i(TAG, "Methode: createTask wird gestartet.");
        Task retTask = taskDAO.createTask(createTask);
        Log.i(TAG, "Methode: createTask wird verlassen.");
        return retTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#createOrUpdateTask(at.mukprojects
     * .vuniapp.models.Task)
     */
    @Override
    public final Task createOrUpdateTask(final Task task)
            throws CreateException, UpdateException, ReadException {
        Log.i(TAG, "Methode: createOrUpdateTask wird gestartet.");
        if (!validateInputParameter(task)) {
            throw new CreateException("Der übergebene Task muss gültig sein. "
                    + "(Title und Description dürfen nicht null sein)");
        }

        Task retTask = null;
        boolean create = true;
        if (task.getId() != null) {
            Task dbTask = taskDAO.getTaskById(task.getId());
            if (dbTask != null) {
                create = false;
                Log.d(TAG, "Task mit selber ID im Datenspeicher"
                        + " gefunden - Update.");
                int changedRows = taskDAO.updateTasks(task, dbTask);
                if (changedRows != 1) {
                    throw new UpdateException("Beim Updaten des Tasks kam"
                            + " es zu einem Fehler es wurden mehr Eintränge"
                            + " geändert als erwartet.");
                } else {
                    retTask = task;
                }
            }
        }
        if (create) {
            retTask = taskDAO.createTask(task);
        }

        Log.i(TAG, "Methode: createOrUpdateTask wird verlassen.");
        return retTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#updateTasks(at.mukprojects
     * .vuniapp.models.Task, at.mukprojects.vuniapp.models.Task)
     */
    @Override
    public final int updateTasks(final Task setTask, final Task whereTask)
            throws UpdateException, ReadException {
        Log.i(TAG, "Methode: updateTasks wird gestartet.");
        int retRows = taskDAO.updateTasks(setTask, whereTask);
        Log.i(TAG, "Methode: updateTasks wird verlassen.");
        return retRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.TaskService#deleteTasks(at.mukprojects
     * .vuniapp.models.Task)
     */
    @Override
    public final int deleteTasks(final Task deleteTask) throws DeleteException,
            ReadException {
        Log.i(TAG, "Methode: deleteTasks wird gestartet.");
        int retRows = taskDAO.deleteTasks(deleteTask);
        Log.i(TAG, "Methode: deleteTasks wird verlassen.");
        return retRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.TaskService#deleteTask(long)
     */
    @Override
    public final int deleteTask(final long id) throws DeleteException,
            ReadException {
        Log.i(TAG, "Methode: deleteTasks wird gestartet.");

        Task deleteTask = new Task(null, null, null, null, null);
        deleteTask.setId(id);
        int retRows = taskDAO.deleteTasks(deleteTask);
        if (retRows > 1) {
            throw new DeleteException(
                    "Es wurden mehr Dateneinträge gelöscht als vorhergesehen");
        }

        Log.i(TAG, "Methode: deleteTasks wird verlassen.");
        return retRows;
    }

    /**
     * Validiert einen Task, ob er in die Datenbank geschrieben werden kann.
     * 
     * @param task
     *            Der zu pr&uuml;fende Task.
     * @return Liefert True falls der Task valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateInputParameter(final Task task) {
        if (task == null) {
            return false;
        }
        if (task.getTitle() == null || task.getDescription() == null) {
            return false;
        }
        return true;
    }
}
