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

package at.mukprojects.vuniapp.activities.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;

/**
 * List Item f&uuml;r eine Android Service (Hier als Daemon bezeichnet) Liste.
 * 
 * @author kerrim
 */
public class ServiceControl {
    private String          name;
    private VUniAppFragment settingsFragment;
    @SuppressWarnings("rawtypes")
    private Class           service;
    private VUniAppActivity activity;

    /**
     * Erzeugt ein neues ListItem.
     * 
     * @param name
     *            Name des Daemons.
     * @param settingsFragment
     *            Einstellungsfragment f&uuml;r den Daemon.
     * @param service
     *            Klasse des Daemons.
     * @param servicesActivity
     *            Activity mit der Liste der Daemons.
     */
    @SuppressWarnings("rawtypes")
    public ServiceControl(final String name,
            final VUniAppFragment settingsFragment, final Class service,
            final VUniAppActivity activity) {
        this.name = name;
        this.settingsFragment = settingsFragment;
        this.service = service;
        this.activity = activity;
    }

    /**
     * Liefert den Namen mit dem der Service angezeigt werden soll zur&uuml;ck.
     * 
     * @return Anzeigename des Services.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert das SettingsFragment des Daemons zur&uuml;ck.
     * 
     * @return SettingsFragment des Daemons.
     */
    public final VUniAppFragment getSettingsFragment() {
        return settingsFragment;
    }

    /**
     * Liefert zur&uuml;ck ob der Daemon gerade l&auml;ft oder nicht.
     * 
     * @return Falls der Daemon gerade l&auml;ft dann true, sonst false.
     */
    public final boolean isRunning() {
        ActivityManager manager = (ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo serviceInfo : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (service.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Startet den Daemon.
     */
    public final void start() {
        Intent intent = new Intent(activity.getCurrentActivity(), service);
        activity.startService(intent);
    }

    /**
     * Beendet den Daemon.
     */
    public final void end() {
        Intent intent = new Intent(activity.getCurrentActivity(), service);
        activity.stopService(intent);
    }

    /**
     * Startet den Daemon neu.
     */
    public final void restart() {
        this.end();
        this.start();
    }
}