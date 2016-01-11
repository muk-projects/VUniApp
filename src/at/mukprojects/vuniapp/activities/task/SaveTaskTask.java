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
class SaveTaskTask extends BaseTask<Void, Void> {
    private static final String TAG = SaveTaskTask.class.getSimpleName();
    private Activity            activity;
    private TaskFragment        fragment;
    private Task                saveTask;

    /**
     * Erstellt einen neuen TaskFinder.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param fragment
     *            Das TaskFragment.
     * @param saveTask
     *            Der Task welcher gespeichert werden soll.
     */
    public SaveTaskTask(final Activity activity, final TaskFragment fragment,
            final Task saveTask) {
        super(activity.getString(R.string.activity_task), activity
                .getString(R.string.activity_task_loading), false, activity);
        this.activity = activity;
        this.saveTask = saveTask;
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
                service.createOrUpdateTask(saveTask);
            } catch (ReadException readException) {
                super.readException = readException;
            } catch (CreateException createException) {
                super.createException = createException;
            } catch (UpdateException updateException) {
                super.updateException = updateException;
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
                    activity.getString(R.string.activity_task_saveToast),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
