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

package at.mukprojects.vuniapp.widget.task;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.dao.TaskDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.services.TaskService;
import at.mukprojects.vuniapp.services.TaskServiceStd;

/**
 * Der TaskListProvider.
 * 
 * @author Mathias
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TaskListProvider implements RemoteViewsFactory {
    private static final String TAG      = TaskListProvider.class
                                                 .getSimpleName();

    private Context             context  = null;
    private int                 appWidgetId;
    private ArrayList<Task>     taskList = new ArrayList<Task>();

    /**
     * Erstellt einen neuen TaskListProvider.
     * 
     * @param context
     *            Der Context.
     * @param intent
     *            Das Intent.
     */
    public TaskListProvider(final Context context, final Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        Log.d(TAG, "TaskListProvider erstellt.");
    }

    /**
     * L&auml;dt die Tasks der Liste.
     */
    private void loadTasks() {
        ArrayList<Task> resultTask = new ArrayList<Task>();
        TaskService service = null;
        try {
            service = new TaskServiceStd(new TaskDAOAndroidDB(context));
        } catch (ConnectionException connectionException) {
            connectionException.printStackTrace(); // TODO M: ...
        }

        if (service != null) {
            try {
                resultTask.addAll(service.getAllTasks());
            } catch (ReadException readException) {
                readException.printStackTrace(); // TODO M: ...
            }
        }

        Log.d(TAG, resultTask.size() + " Tasks wurden geladen.");
        taskList = resultTask;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#onCreate()
     */
    @Override
    public final void onCreate() {
        Log.d(TAG, "OnCreate wird durch Widget id:" + appWidgetId
                + " aufgerufen.");
        loadTasks();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#onDestroy()
     */
    @Override
    public final void onDestroy() {
        Log.d(TAG, "OnDestroy wird durch Widget id:" + appWidgetId
                + " aufgerufen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#getCount()
     */
    @Override
    public final int getCount() {
        return taskList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#getViewAt(int)
     */
    @Override
    public final RemoteViews getViewAt(final int position) {
        Task task = taskList.get(position);
        Log.d(TAG,
                "GetViewAt an der Position: " + position + " Task: "
                        + task.getTitle());
        RemoteViews rv = new RemoteViews(this.context.getPackageName(),
                R.layout.adapter_task_simpletasklist);

        rv.setTextViewText(R.id.adapter_task_simpleTasklist_listItem,
                buildListItem(task));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Task.TASK, task);
        rv.setOnClickFillInIntent(R.id.adapter_task_simpleTasklist_listItem,
                fillInIntent);

        return rv;
    }

    /**
     * Erzeugt den String des Items.
     * 
     * @param task
     *            Der Task mit den Informationen.
     * @return Der anzuzeigende String.
     */
    private String buildListItem(final Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getTitle());
        if (!task.getDescription().equals("")) {
            builder.append("\n").append(task.getDescription());
        }
        if (task.getDeadlineDate() != null && task.getDeadlineDate() != 0) {
            builder.append("\n").append(task.getDeadlineDate());
        }
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.RemoteViewsService.RemoteViewsFactory#getLoadingView()
     */
    @Override
    public final RemoteViews getLoadingView() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.RemoteViewsService.RemoteViewsFactory#getViewTypeCount()
     */
    @Override
    public final int getViewTypeCount() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#getItemId(int)
     */
    @Override
    public final long getItemId(final int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.RemoteViewsService.RemoteViewsFactory#hasStableIds()
     */
    @Override
    public final boolean hasStableIds() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.RemoteViewsService.RemoteViewsFactory#onDataSetChanged()
     */
    @Override
    public final void onDataSetChanged() {
        Log.d(TAG, "OnDataSetChanged");
        loadTasks();
    }
}