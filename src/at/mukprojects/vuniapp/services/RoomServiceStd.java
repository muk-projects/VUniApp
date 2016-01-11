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

package at.mukprojects.vuniapp.services;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.util.Log;
import at.mukprojects.vuniapp.dao.RoomDAO;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Standardimplementierung des RoomService Interfaces.
 * 
 * @author Mathias
 */
public class RoomServiceStd implements RoomService {
    private static final String TAG = RoomServiceStd.class.getSimpleName();
    private RoomDAO             roomDAO;

    /**
     * Erzeugt eine Standard RoomService Implementierung.
     * 
     * @param roomDAO
     *            Zu nutzende DAO.
     */
    public RoomServiceStd(final RoomDAO roomDAO) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.roomDAO = roomDAO;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.RoomService#searchRooms(java.lang.String)
     */
    @Override
    public final ArrayList<Room> searchRooms(final University university,
            final String input) throws JSONException, IOException {
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        ArrayList<Room> output = roomDAO.searchRooms(university, input);
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.RoomService#searchRooms(at.mukprojects
     * .vuniapp.models.Room)
     */
    @Override
    public final ArrayList<Room> searchRooms(final University university,
            final Room input) throws JSONException, IOException {
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        ArrayList<Room> output = roomDAO.searchRooms(university, input);
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return output;
    }
}