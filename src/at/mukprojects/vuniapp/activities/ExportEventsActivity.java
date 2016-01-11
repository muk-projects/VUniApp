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

package at.mukprojects.vuniapp.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.EventDAOGoogleCalendar;
import at.mukprojects.vuniapp.exceptions.MissingDataException;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.services.EventService;
import at.mukprojects.vuniapp.services.EventServiceStd;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;

/**
 * Activity welche Termine in einen Kalender exportieren kann.
 * 
 * @author kerrim
 */
public class ExportEventsActivity extends VUniAppActivity {
    public static final String                        EXTRA_EVENTS                 = "events";
    public static final String                        EXTRA_EVENT                  = "event";

    private static final String                       TAG                          = ExportEventsActivity.class
                                                                                           .getSimpleName();
    private static final String                       PREF_FILE                    = "PrefFileExportEvents";
    private static final String                       PREF_ACCOUNT_NAME            = "PrefAccountName";

    private SharedPreferences                         prefFile                     = null;

    private final HttpTransport                       transport                    = AndroidHttp
                                                                                           .newCompatibleTransport();
    private final JsonFactory                         jsonFactory                  = GsonFactory
                                                                                           .getDefaultInstance();
    private GoogleAccountCredential                   credential;

    static final int                                  REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int                                  REQUEST_AUTHORIZATION        = 1;
    static final int                                  REQUEST_ACCOUNT_PICKER       = 2;

    private ArrayList<Event>                          events;

    private com.google.api.services.calendar.Calendar client;

    @SuppressWarnings("unchecked")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        setContentView(R.layout.activity_exportdates);
        super.setCurrentActivity(this);
        super.onCreate(savedInstanceState, ExportEventsActivity.class);
        prefFile = getSharedPreferences(PREF_FILE, 0);

        Intent intent = getIntent();

        events = (ArrayList<Event>) intent.getSerializableExtra(EXTRA_EVENTS);

        if (events == null) {
            events = new ArrayList<Event>();
            Event event = (Event) intent.getSerializableExtra(EXTRA_EVENT);
            if (event == null) {
                Log.e(TAG,
                        "Die ExportEventsActivity hat keine brauchbaren Daten mitbekommen.");
            } else {
                events.add(event);
            }
        }

        Log.d(TAG, "Zu bearbeitende Events werden nun aufgelistet.");
        for (Event event : events) {
            Log.d(TAG, event.toString());
        }

        // Google Accounts
        credential = GoogleAccountCredential.usingOAuth2(getCurrentActivity(),
                Collections.singleton(CalendarScopes.CALENDAR));
        credential.setSelectedAccountName(prefFile.getString(PREF_ACCOUNT_NAME,
                null));

        // Calendar client
        String applicationName = getResources().getString(R.string.app_name)
                + "/" + Build.VERSION.RELEASE;
        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName(
                applicationName).build();

        Log.d(TAG, "Kalender Client erstellt: " + client);
        Log.d(TAG, "ApplicationName: " + applicationName);

        if (checkGooglePlayServicesAvailable()) {
            haveGooglePlayServices();
        }

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /**
     * Gibt einen Fehler Dialog aus, falls Google Play Services nicht vorhanden
     * ist.
     * 
     * @param connectionStatusCode
     *            Aufgetretener Fehlercode.
     */
    final void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, ExportEventsActivity.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    @Override
    protected final void onActivityResult(final int requestCode,
            final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    loadCalendar();
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null
                        && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences.Editor editor = prefFile.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        loadCalendar();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Checkt ob die PlayServices APK installiert ist.
     * 
     * @return Falls ja true, sonst false.
     */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    /**
     * Gegebenfalls Account aussuchen, sonst Kalender laden.
     */
    private void haveGooglePlayServices() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            loadCalendar();
        }
    }

    /**
     * Startet die Account Auswahl.
     */
    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    /**
     * L&auml;dt die Kalender des Accounts.
     */
    private void loadCalendar() {
        (new DateExportTask()).execute();
    }

    /**
     * Exportiert die Termine in einen Google Kalender.
     * 
     * @author Mathias
     */
    class DateExportTask extends BaseTask<Event, Void> {
        private float                            progress;
        private float                            currentProgress;
        private UserRecoverableAuthIOException userRecoverableAuthIOException;

        /**
         * Erstellt einen neuen Task.
         */
        public DateExportTask() {
            super(getResources().getString(R.string.activity_exportevents),
                    getResources().getString(
                            R.string.activity_exportevents_dates_loading),
                    true, getCurrentActivity());
        }

        @Override
        protected Void doInBackground(final Event... params) {
            float eventSize = events.size();
            progress = 100 / eventSize;
            currentProgress = 0;
            Log.d(TAG, "ProgessSteps: " + progress + " Events: " + eventSize);
            try {
                    EventService eventService = new EventServiceStd(
                            new EventDAOGoogleCalendar("VUniApp", client));
                    
                for (Event event : events) {
                    try {
                        eventService.writeEvent(event);
                    } catch (MissingDataException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int value = Math.round(currentProgress + progress);
                    publishProgress(value);
                    currentProgress += progress;
                }
            } catch (UserRecoverableAuthIOException e) {
                userRecoverableAuthIOException = e;
            } catch (IOException e) {
                super.ioException = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            Log.i(TAG, "ASyncTask: DateExport wird verlassen.");
            if (super.noSuperError && userRecoverableAuthIOException == null) {
                finish();
            } else if (userRecoverableAuthIOException != null) {
                startActivityForResult(userRecoverableAuthIOException.getIntent(), REQUEST_AUTHORIZATION);
            }
        }
    }

}
