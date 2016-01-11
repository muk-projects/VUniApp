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

package at.mukprojects.vuniapp.daemons.certificatenotifier;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

/**
 * Daemon zum Abfragen der Zeugnissen in bestimmten Intervallen um
 * Zeugnisbenachrichtigungen zu ermöglichen.
 * 
 * @author kerrim
 */
public class CertificateNotifier extends Service {
    private static final String TAG                        = CertificateNotifier.class
                                                                   .getSimpleName();

    private static final String PREF_FILE                  = "PrefFileCertificateNotifier";
    private static final String PREF_FILE_INTERVALL_HOUR   = "certificatenotifier_intervall_hour";
    private static final String PREF_FILE_INTERVALL_MINUTE = "certificatenotifier_intervall_minute";
    private static final String PREF_FILE_ONLYWLAN         = "certificatenotifier_onlywlan";

    private SharedPreferences   prefFile                   = null;

    private Timer               timer                      = null;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onCreate()
     */
    @Override
    public final void onCreate() {
        Log.d(TAG, "CertificateNotifier gestartet.");
        timer = new Timer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    public int onStartCommand(final Intent intent, final int flags,
            final int startId) {
        Log.d(TAG, "CertificateNotifier.onStartCommand() ausgeführt.");

        prefFile = getSharedPreferences(PREF_FILE, 0);

        int minute = prefFile.getInt(PREF_FILE_INTERVALL_MINUTE, 0);
        int hour = prefFile.getInt(PREF_FILE_INTERVALL_HOUR, 25);

        if (hour == 24) {
            hour = 0;
        }
        if (hour == 25) {
            hour = 24;
        }

        int time = hour * 60 * 60 * 1000 + minute * 60 * 1000;
        Log.d(TAG, "hour=" + hour + ";minute=" + minute + ";intervalltime="
                + time);
        boolean onlywlan = prefFile.getBoolean(PREF_FILE_ONLYWLAN, true);
        timer.schedule(new CheckCertificatesTask(onlywlan, this), 0, time);

        return START_STICKY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public final IBinder onBind(final Intent intent) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onDestroy()
     */
    @Override
    public final void onDestroy() {
        timer.cancel();
        Log.d(TAG, "CertificateNotifier beendet.");
    }

}
