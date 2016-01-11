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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWeb;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.Universities;

/**
 * Implementierung der DAO f&uuml;r den MuK Server.
 * 
 * @author Mathias
 */
public class RoomDAOMuKServer extends DAOWeb implements RoomDAO {
    private static final String TAG         = RoomDAOMuKServer.class
                                                    .getSimpleName();
    private static final String VERSION     = "V2";

    private static final String ARRAY_ROOM  = "rooms";
    private static final String ROOM_NAME   = "roomName";
    private static final String ROOM_UNI    = "university";
    private static final String ROOM_URL    = "urlToDetails";
    private static final String ROOM_ADDRES = "address";
    private static final String ROOM_INFO   = "detailInfo";

    private String              json;

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.RoomDAO#searchRooms(java.lang.String)
     */
    @Override
    public final ArrayList<Room> searchRooms(final University university,
            final String input) throws JSONException, IOException {
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        json = getData(university, input);
        if (json != null) {
            Log.i(TAG, "Methode: onCreate wird verlassen.");
            return extractJson(json);
        }
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return new ArrayList<Room>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.RoomDAO#searchRooms(at.mukprojects.vuniapp
     * .models.Room)
     */
    @Override
    public final ArrayList<Room> searchRooms(final University university,
            final Room input) throws JSONException, IOException {
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        Room serverRoom = null;
        ArrayList<Room> retRooms = new ArrayList<Room>();
        json = getData(university, input.getRoomName());
        if (json != null) {
            ArrayList<Room> rooms = extractJson(json);
            if (rooms.size() != 1) {
                Log.e(TAG, "Ungültige Anzahl an Ergebnissen!");
                retRooms = rooms;
            } else {
                Log.d(TAG,
                        "Raum wird mit den Informationen des Servers ergänzt.");
                serverRoom = rooms.get(0);
                String urlToDetails;
                String address;
                String info;
                if (input.getUrlToDetails() == null) {
                    urlToDetails = serverRoom.getUrlToDetails();
                } else {
                    urlToDetails = input.getUrlToDetails();
                }
                if (input.getAddress() == null) {
                    address = serverRoom.getAddress();
                } else {
                    address = input.getAddress();
                }
                if (input.getDetailInfo() == null) {
                    info = serverRoom.getDetailInfo();
                } else {
                    info = input.getDetailInfo();
                }
                retRooms.add(new Room(input.getRoomName(), input
                        .getUniversity(), urlToDetails, address, info));
            }
        }
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return retRooms;
    }

    /**
     * Erstellt aus einem Json Text ein Array mit Rooms.
     * 
     * @param jsonString
     *            Json als String.
     * @return ArrayList mit Rooms.
     * @throws JSONException
     *             Wird geworfen, wenn die Umwandlung des String nicht
     *             funktioniert.
     */
    private ArrayList<Room> extractJson(final String jsonString)
            throws JSONException {
        Log.d(TAG, "ExtractJson");
        ArrayList<Room> retList = new ArrayList<Room>();
        JSONObject jsonObject = null;

        Log.d(TAG, "Decode Json Object.");
        jsonObject = new JSONObject(jsonString);
        JSONArray rooms = jsonObject.getJSONArray(ARRAY_ROOM);
        for (int i = 0; i < rooms.length(); i++) {
            JSONObject room = rooms.getJSONObject(i);

            String roomName = room.getString(ROOM_NAME);
            String university = room.getString(ROOM_UNI);
            String urlToDetails = room.getString(ROOM_URL);
            String address = room.getString(ROOM_ADDRES);
            String info = room.getString(ROOM_INFO);

            Room jsonRoom = new Room(roomName, Universities.getInstance().getUniversityFromKey(university), urlToDetails,
                    address, info);
            retList.add(jsonRoom);
            Log.d(TAG, "Room: " + jsonRoom);
        }

        return retList;
    }

    /**
     * &Ouml;ffnet eine Verbindung und holt die Daten.
     * 
     * @param university
     *            Zu suchenden Universit&auml;t.
     * @param input
     *            PHP Skript Parameter.
     * @return BuffderdReader mit den Daten.
     * @throws IOException
     *             Wird geworfen, falls es beim Laden der Daten zu einem Fehler
     *             kommt.
     */
    private String getData(final University university, final String input)
            throws IOException {
        Log.d(TAG, "HTTP Request an MuK Server wird erstellt.");
        String url = "http://mukprojects.at/vuniapp/services/roomfinder/"
                + VERSION + ".php?university=" + university.getKeyName() + "&room="
                + formatInput(input);
        Log.d(TAG, "URL: " + url);
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        String line, jsonString = "";
        do {
            line = br.readLine();
            if (line != null) {
                Log.v(TAG, "Html Line: " + line);
                jsonString += line;
            }
        } while (line != null);
        br.close();

        Log.d(TAG, "Daten aus dem Internet geholt.");
        return jsonString;
    }

    /**
     * Formatiert den input zu HtmlCode.
     * 
     * @param input
     *            Benutzereingabe.
     * @return HtmlFormat der Eingabe.
     */
    private String formatInput(final String input) {
        Log.d(TAG, "UserInput: " + input);
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ') {
                output += "%20";
            } else if (c == 'ä') {
                output += "%C3%A4";
            } else if (c == 'Ä') {
                output += "%C3%84";
            } else if (c == 'ö') {
                output += "%C3%B6";
            } else if (c == 'Ö') {
                output += "%C3%B6";
            } else if (c == 'ü') {
                output += "%C3%BC";
            } else if (c == 'Ü') {
                output += "%C3%BC";
            } else {
                output += c;
            }
        }
        Log.d(TAG, "HTMLOutput: " + output);
        return output;
    }
}
