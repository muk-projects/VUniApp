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

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Der TaskWidgetService.
 * 
 * @author Mathias
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TaskWidgetService extends RemoteViewsService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.RemoteViewsService#onGetViewFactory(android.content.Intent
     * )
     */
    @Override
    public final RemoteViewsFactory onGetViewFactory(final Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new TaskListProvider(this.getApplicationContext(), intent));
    }
}
