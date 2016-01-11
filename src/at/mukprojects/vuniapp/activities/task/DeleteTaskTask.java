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

package at.mukprojects.vuniapp.activities.task;

import android.app.Activity;
import android.widget.Toast;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.TaskDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.services.TaskService;
import at.mukprojects.vuniapp.services.TaskServiceStd;

/**
 * Die Klasse SaveTaskTask speichert den &uuml;bergeben Task in den
 * Datenspeicher. Wenn Bereits ein Task mit der gleichen ID existiert wird
 * dieser stattdessen geupdatet.
 * 
 * @author Mathias
 */
class DeleteTaskTask extends BaseTask<Void, Void> {
    private static final String TAG = DeleteTaskTask.class.getSimpleName();
    private Activity            activity;
    private TaskFragment        fragment;
    private long                taskID;

    /**
     * Erstellt einen neuen TaskFinder.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param fragment
     *            Das TaskFragment.
     * @param taskID
     *            Die ID des Tasks, welcher gel&ouml;scht werden soll.
     */
    public DeleteTaskTask(final Activity activity, final TaskFragment fragment,
            final Long taskID) {
        super(activity.getString(R.string.activity_task), activity
                .getString(R.string.activity_task_loading), false, activity);
        this.activity = activity;
        this.taskID = taskID;
        this.fragment = fragment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Void doInBackground(final Void... params) {
        TaskService service = null;

        try {
            service = new TaskServiceStd(new TaskDAOAndroidDB(activity));
        } catch (ConnectionException connectionException) {
            super.connectionException = connectionException;
        }

        if (service != null) {
            try {
                service.deleteTask(taskID);
            } catch (ReadException readException) {
                super.readException = readException;
            } catch (DeleteException deleteException) {
                super.deleteException = deleteException;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final Void result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            fragment.sendRealodListRequest();
            Toast.makeText(activity,
                    activity.getString(R.string.activity_task_deleteToast),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
