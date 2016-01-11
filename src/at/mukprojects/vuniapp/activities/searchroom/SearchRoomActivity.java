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

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.Request;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.Response;
import at.mukprojects.vuniapp.baseclasses.activities.ActivitySearchListWithDetailFragment;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.RoomInterface;

/**
 * Die Activity implementiert eine Raumsuche f&uuml;r unterschiedliche
 * Universit&auml;ten.
 * 
 * @author Mathias
 */
public class SearchRoomActivity extends
        ActivitySearchListWithDetailFragment<RoomInterface, Room> {
    private static final String TAG       = SearchRoomActivity.class
                                                  .getSimpleName();
    private static final String PREF_FILE = "PrefFileSearchRoom";

    /**
     * Erstellt eine neue SearchRoomActivity.
     */
    public SearchRoomActivity() {
        super(PREF_FILE, Room.ROOM_EXTRA, new RoomDetailsFragment(),
                Universities.getInstance().getUniversitiesWithRooms());
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        super.onCreate(savedInstanceState, SearchRoomActivity.class,
                getResources().getString(R.string.activity_searchroom));

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #manageBundleExtra(java.lang.Object)
     */
    @Override
    protected final String[] manageBundleExtra(final Object extra) {
        Log.i(TAG, "Methode: manageBundleExtra wird gestartet.");
        String[] output = new String[2];
        super.extraObject = (Room) extra;
        output[0] = extraObject.getUniversity().getName();
        output[1] = extraObject.getRoomName();
        Log.i(TAG, "Methode: manageBundleExtra wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #searchButtonClick()
     */
    @Override
    protected final void searchButtonClick() {
        Log.i(TAG, "Methode: searchButtonClick wird gestartet.");
        Log.i(TAG, "Methode: searchButtonClick wird verlassen.");
        (new SearchRoomTask(activitySearchName.getText().toString(), null,
                getResources().getString(R.string.activity_searchroom),
                getResources()
                        .getString(R.string.activity_searchroom_searching),
                this, universities.get((String) activityUniversity
                        .getSelectedItem()), activityResultsText,
                activityResults)).execute();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #searchButtonClick(java.lang.Object)
     */
    @Override
    protected final void searchButtonClick(final Room extra) {
        Log.i(TAG, "Methode: searchButtonClick wird gestartet.");
        Log.i(TAG, "Methode: searchButtonClick wird verlassen.");
        (new SearchRoomTask(null, extra, getResources().getString(
                R.string.activity_searchroom), getResources().getString(
                R.string.activity_searchroom_searching),
                this, universities.get((String) activityUniversity
                        .getSelectedItem()), activityResultsText,
                activityResults)).execute();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.ActivityListWithDetailFragment
     * #getListViewAdapter(java.util.ArrayList)
     */
    @Override
    protected final ListAdapter getListViewAdapter(final ArrayList<Room> result) {
        Log.i(TAG, "Methode: getListViewAdapter wird gestartet.");
        Log.i(TAG, "Methode: getListViewAdapter wird verlassen.");
        return new RoomResultListAdapter(getCurrentActivity(), result);
    }
}
