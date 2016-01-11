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

package at.mukprojects.vuniapp.dao;

import java.io.IOException;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.MissingDataException;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.storages.Universities;

/**
 * Eine Implementierung der EventDAO welche den Google Kalender zum Speichern
 * der Termine nutzt.
 * 
 * @author kerrim
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EventDAOLocalCalendar implements EventDAO {
    private static final String  TAG                 = EventDAOLocalCalendar.class
                                                             .getSimpleName();
    private Context              context;
    private static final String  CALENDAR_TYPE       = CalendarContract.ACCOUNT_TYPE_LOCAL;
    private static final String  EVENT_TIMEZONE      = "Europe/Vienna";

    private String               calendarName        = "VUniApp";
    private Uri                  calendarUri;

    public static final String[] CALENDAR_PROJECTION = new String[] { Calendars._ID };

    // The indices for the projection array above.
    private static final int     PROJECTION_ID_INDEX = 0;

    /**
     * Erstellt eine neue EventDAO welche einen Loakeln Kalendar zum Speichern der
     * Termine nutzt.
     * 
     * @param context
     *            Context der Activity.
     * @param calendarName
     *            Name des zu nutzenden Kalenders.
     */
    public EventDAOLocalCalendar(final Context context,
            final String calendarName) {
        this.context = context;
        this.calendarName = calendarName;
        this.calendarUri = Calendars.CONTENT_URI
                .buildUpon()
                .appendQueryParameter(
                        android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,
                        "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, calendarName)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, CALENDAR_TYPE)
                .build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.EventDAO#writeEvent(at.mukprojects.vuniapp
     * .models.Event)
     */
    @Override
    public final void writeEvent(final Event event) throws IOException,
            MissingDataException {
        Log.i(TAG, "Methode: writeEvent wird gestartet.");

        if (event.getDate() == null || event.getName() == null
                || event.getDuration() < 0) {
            throw new MissingDataException();
        }
        Log.d(TAG, "Prüfung des übergebenen Events beendet.");

        Log.d(TAG, "Folgendes Event wird in den Kalender geschrieben: " + event);
        ContentResolver contentResolver = context.getContentResolver();
        long calID = getCalendarID(contentResolver);
        Log.d(TAG, "ContentValues für das Event werden erstellt.");

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, event.getDate().getTime());
        values.put(Events.DTEND,
                event.getDate().getTime() + event.getDuration());
        if (event.isWholeDay()) {
            values.put(Events.ALL_DAY, 1);
        }
        values.put(Events.TITLE, event.getName());
        values.put(Events.DESCRIPTION, event.getDescription());
        String university = event.getUniversity().getName();
        values.put(Events.EVENT_LOCATION, university + " " + event.getPlace());
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, EVENT_TIMEZONE);
        values.put(Events.GUESTS_CAN_MODIFY, true);
        Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.d(TAG, "ID des neu geschriebenen Events: " + eventID);

        Log.i(TAG, "Methode: writeEvent wird verlassen.");
    }

    /**
     * Holt sich die ID des VUniApp Kalenders. Falls keiner vorhanden ist wird
     * einer erstellt.
     * 
     * @param contentResolver
     *            Zu nutzender ContentResolver.
     * @return ID des VUniApp Kalenders.
     * @throws IOException
     *             Wird geworfen falls beim Zugreifen ein Fehler geschehen ist.
     */
    private long getCalendarID(final ContentResolver contentResolver)
            throws IOException {
        Log.i(TAG, "Methode: getCalendarID wird gestartet.");
        Cursor managedCursor = contentResolver.query(calendarUri,
                CALENDAR_PROJECTION, null, null, null);

        Log.d(TAG, "ManagedCursor wurde erstellt: " + managedCursor);

        Long calID = null;

        if (managedCursor.moveToFirst()) {
            calID = managedCursor.getLong(PROJECTION_ID_INDEX);
            Log.d(TAG, "ID des VUniApp Kalenders: " + calID);
            managedCursor.close();
        } else {
            createCalendar(context.getContentResolver());
            managedCursor = contentResolver.query(calendarUri,
                    CALENDAR_PROJECTION, null, null, null);
            if (managedCursor.moveToFirst()) {
                calID = managedCursor.getLong(PROJECTION_ID_INDEX);
                Log.d(TAG, "ID des VUniApp Kalenders: " + calID);
                managedCursor.close();
            } else {
                throw new IOException();
            }
        }

        if (calID == null) {
            throw new IOException();
        }
        Log.i(TAG, "Methode: getCalendarID wird verlassen.");

        return calID;
    }

    /**
     * Erzeugt einen neuen VUniApp Kalender.
     * 
     * @param contentResolver
     *            Zu benutzender ContentResolver.
     */
    private void createCalendar(final ContentResolver contentResolver) {
        Log.i(TAG, "Methode: createCalendar wird gestartet.");
        final ContentValues cv = new ContentValues();
        cv.put(Calendars.ACCOUNT_NAME, calendarName);
        cv.put(Calendars.ACCOUNT_TYPE, CALENDAR_TYPE);
        cv.put(Calendars.NAME, calendarName);
        cv.put(Calendars.OWNER_ACCOUNT, calendarName);
        cv.put(Calendars.CALENDAR_DISPLAY_NAME, calendarName);
        cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        cv.put(Calendars.VISIBLE, 1);
        cv.put(Calendars.SYNC_EVENTS, 1);
        contentResolver.insert(calendarUri, cv);
        Log.i(TAG, "Methode: createCalendar wird verlassen.");
    }

    /**
     * Entfernt den f&uuml;r VUniApp lokal genutzten Kalender.
     */
    public final void removeCalendar() {
        Log.i(TAG, "Methode: removeCalendar wird gestartet.");
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(calendarUri, null, null);
        Log.i(TAG, "Methode: removeCalendar wird verlassen.");
    }
}
