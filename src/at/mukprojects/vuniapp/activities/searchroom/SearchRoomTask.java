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

package at.mukprojects.vuniapp.activities.searchroom;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.exceptions.IOExceptionWithCritical;
import at.mukprojects.vuniapp.exceptions.JSONExceptionWithCritical;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.universities.interfaces.RoomInterface;

/**
 * Dieser Task sucht nach den passenden R&aumml;men und updatet den ListView.
 * 
 * @author Mathias
 */
class SearchRoomTask extends InternetBaseTask<Void, ArrayList<Room>> {
    private static final String TAG = SearchRoomTask.class.getSimpleName();

    private String              input;
    private Room                room;
    private RoomInterface       university;
    private TextView            activityResultsText;
    private ListView            activityResults;
    private SearchRoomActivity  roomActivity;

    /**
     * Erstellt einen neuen Task. Es kann entweder ein input oder ein room
     * übergeben werden.
     * 
     * @param input
     *            Suchvariable als String.
     * @param room
     *            Raum, nach dem gesucht werden soll.
     * @param titel
     *            Titel des Dialogs.
     * @param message
     *            Message des Dialogs.
     * @param activity
     *            Activity, welche den Task ausf&uuml;hrt.
     * @param university
     *            Universit&auml;t zum ausf&uuml;hren der Raumsuche.
     * @param activityResultsText
     *            Der ResultText.
     * @param activityResults
     *            Der ResultListView.
     */
    public SearchRoomTask(final String input, final Room room,
            final String titel, final String message,
            final SearchRoomActivity activity, final RoomInterface university,
            final TextView activityResultsText, final ListView activityResults) {
        super(titel, message, false, activity);
        this.input = input;

        this.room = room;
        if (room != null) {
            super.setCritical(false);
        }

        this.university = university;
        this.activityResultsText = activityResultsText;
        this.activityResults = activityResults;
        this.roomActivity = activity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.baseclasses.InternetBaseTask#executeBackgroundMethod
     * (I[])
     */
    @Override
    protected ArrayList<Room> executeBackgroundMethod(final Void... params) {
        try {
            if (input != null) {
                return university.searchRooms(input);
            } else {
                return university.searchRooms(room);
            }
        } catch (JSONException e) {
            if (room != null) {
                super.jsonException = new JSONExceptionWithCritical(
                        e.getMessage(), false);
            } else {
                super.jsonException = new JSONExceptionWithCritical(
                        e.getMessage(), true);
            }
        } catch (IOException e) {
            if (room != null) {
                super.ioException = new IOExceptionWithCritical(e.getMessage(),
                        e, false);
            } else {
                super.ioException = new IOExceptionWithCritical(e.getMessage(),
                        e, true);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<Room> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            if (result == null || result.isEmpty()) {
                Log.d(TAG, "Kein passendes Element gefunden.");
                activityResultsText.setVisibility(TextView.VISIBLE);
                activityResults.setVisibility(ListView.INVISIBLE);
                noResultOrError();
            } else {
                Log.d(TAG, "Elemente gefunden.");
                activityResultsText.setVisibility(TextView.INVISIBLE);
                activityResults.setVisibility(ListView.VISIBLE);
                roomActivity.showResult(result);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.baseclasses.BaseTask#onTaskErrorResume(java
     * .lang.Object)
     */
    @Override
    public void onTaskErrorResume() {
        super.onTaskErrorResume();
        activityResultsText.setVisibility(TextView.VISIBLE);
        activityResults.setVisibility(ListView.INVISIBLE);
        noResultOrError();
    }

    /**
     * Methode falls kein Ergebnis gefunden wird oder ein Fehler auftritt,
     * jedoch bereits ein Raum verhanden ist. Details werden mit diesem Raum
     * aufgerufen.
     */
    private void noResultOrError() {
        if (room != null) {
            RoomDetailsFragment fragment = new RoomDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Room.ROOM_EXTRA, room);
            roomActivity.loadFragment(fragment, bundle);
        }
    }
}
