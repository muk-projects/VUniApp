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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ListView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.TaskDAOAndroidDB;
import at.mukprojects.vuniapp.enumeration.TaskSortArg;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.services.TaskService;
import at.mukprojects.vuniapp.services.TaskServiceStd;

/**
 * Die Klasse FindTasksTask sucht alle Tasks aus der Datenbank und bef&uult;
 * anschlie&szlig;end die ListView mit den bestehenden Daten.
 * 
 * @author Mathias
 */
class FindTasksTask extends BaseTask<Void, List<Task>> {
    private static final String TAG = FindTasksTask.class.getSimpleName();
    private Activity            activity;
    private ListView            taskList;
    private TaskSortArg         arg;

    /**
     * Erstellt einen neuen TaskFinder.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param taskList
     *            ListView indem die Tasks ausgegeben werden sollen.
     * @param arg
     *            Sortier Argument.
     */
    public FindTasksTask(final Activity activity, final ListView taskList,
            final TaskSortArg arg) {
        super(activity.getString(R.string.activity_task), activity
                .getString(R.string.activity_task_loading), false, activity);
        this.activity = activity;
        this.taskList = taskList;
        this.arg = arg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected List<Task> doInBackground(final Void... params) {
        List<Task> resultTask = new ArrayList<Task>();
        TaskService service = null;

        try {
            service = new TaskServiceStd(new TaskDAOAndroidDB(activity));
        } catch (ConnectionException connectionException) {
            super.connectionException = connectionException;
        }

        if (service != null) {
            try {
                resultTask.addAll(service.getAllTasksSorted(arg));
            } catch (ReadException readException) {
                super.readException = readException;
            }
        }

        return resultTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final List<Task> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            TaskListAdapter adapter = new TaskListAdapter(activity, result);
            taskList.setAdapter(adapter);
        }
    }
}
