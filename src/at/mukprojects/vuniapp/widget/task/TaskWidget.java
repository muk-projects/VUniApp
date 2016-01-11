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

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.StartActivity;
import at.mukprojects.vuniapp.activities.task.TaskActivity;
import at.mukprojects.vuniapp.models.Task;

/**
 * Mithilfe des Widgets kann der User seine Tasks auch am Homescreen managen.
 * 
 * @author Mathias
 */
@SuppressLint("NewApi")
public class TaskWidget extends AppWidgetProvider {
    private static final String TAG = TaskWidget.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.appwidget.AppWidgetProvider#onUpdate(android.content.Context,
     * android.appwidget.AppWidgetManager, int[])
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onUpdate(final Context context,
            final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            int sdkVersion = android.os.Build.VERSION.SDK_INT;

            if (sdkVersion < 14) {
                Log.d(TAG, "SDK_Version = " + sdkVersion
                        + " OldWidget wird geladen.");

                RemoteViews views = new RemoteViews(context.getPackageName(),
                        R.layout.widget_task_old);
                setUpButton(context, views);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            } else {
                Log.d(TAG, "SDK_Version = " + sdkVersion
                        + " NewWidget wird geladen.");

                Intent intent = new Intent(context, TaskWidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetIds[i]);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

                RemoteViews views = new RemoteViews(context.getPackageName(),
                        R.layout.widget_task_new);
                setUpButton(context, views);
                views.setRemoteAdapter(appWidgetIds[i], R.id.widget_task_list,
                        intent);
                views.setEmptyView(R.id.widget_task_list, android.R.id.empty);

                Intent startWithItem = new Intent(context, StartActivity.class);
                startWithItem.putExtra(StartActivity.START_ACTIVITY,
                        TaskActivity.class);
                PendingIntent startWithItemPendingIntent = PendingIntent
                        .getActivity(context, 0, startWithItem,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_task_list,
                        startWithItemPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    /**
     * Konfiguriert die Button des Widgets.
     * 
     * @param context
     *            Context des Widgets.
     * @param views
     *            View des Widgets.
     */
    private void setUpButton(final Context context, final RemoteViews views) {
        Intent intentStartActivity = new Intent(context, StartActivity.class);
        intentStartActivity.putExtra(StartActivity.START_ACTIVITY,
                TaskActivity.class);
        views.setOnClickPendingIntent(R.id.widget_task_startTask,
                PendingIntent.getActivity(context, 0, intentStartActivity, 0));

        Intent intentNewTask = new Intent(context, StartActivity.class);
        intentNewTask
                .putExtra(StartActivity.START_ACTIVITY, TaskActivity.class);
        intentNewTask.putExtra(Task.TASK, new Task("", "", null, null, null));
        views.setOnClickPendingIntent(R.id.widget_task_newTask,
                PendingIntent.getActivity(context, 0, intentNewTask, 0));
    }
}
