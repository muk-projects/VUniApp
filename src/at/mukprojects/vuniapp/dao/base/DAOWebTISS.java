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

package at.mukprojects.vuniapp.dao.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Basisklasse f&uuml;r alle DAOs welche Zugriff auf TISS ben&ouml;tigen.
 * Enth&auml;lt auch eine Funktion zum Pr&uuml;fen des Benutzernamens und
 * Passworts.
 * 
 * @author kerrim
 */
public class DAOWebTISS extends DAOWeb {
    private static final String TAG = DAOWebTISS.class.getSimpleName();

    private static Date         tissLinkSet;
    private UniversityUser      user;
    private University          university;

    /**
     * Stellt die DAO f&uuml;r TISS richtig ein.
     * 
     * @param user
     *            G&uuml;ltiger TISS Benutzer.
     * @param universityKey
     *            Universit&auml;tskey zum Eindeutigen identifizieren der
     *            Universit&auml;t
     */
    public DAOWebTISS(final UniversityUser user, final University university) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
        this.university = university;
    }

    /**
     * Pr&uuml;ft ob eine Anmeldung f&uuml;r TISS n&ouml;tig ist und meldet sich
     * gegebenfalls an.
     * 
     * @throws IOException
     *             Wird geworfen, falls beim Anmelden ein Fehler aufgetreten
     *             ist.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     */
    protected final void login() throws IOException, InvalidLoginException {
        Log.i(TAG, "Methode: login wird gestartet.");
        if (!getSharedInternetConnection().cookieExists("_tiss_session")
                || tissLinkSet == null
                || tissLinkSet.getTime() + 8 * 60 * 1000 < (new Date())
                        .getTime()) {
            try {
                HttpPost httppost = new HttpPost(
                        "https://iu.zid.tuwien.ac.at/AuthServ.portal");

                ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
                        3);
                nameValuePairs.add(new BasicNameValuePair("name", user
                        .getUsername()));
                nameValuePairs.add(new BasicNameValuePair("pw", user
                        .getPassword()));
                nameValuePairs.add(new BasicNameValuePair("app", "76"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                getSharedInternetConnection().execute(httppost).getEntity()
                        .consumeContent();
                tissLinkSet = new Date();
            } catch (IOException e) {
                throw new IOException();
            }
        }
        if (!getSharedInternetConnection().cookieExists("_tiss_session")) {
            throw new InvalidLoginException();
        }
        Log.i(TAG, "Methode: login wird verlassen.");
    }

    /**
     * Pr&uuml;ft ob die Login Informationen zul&auml;ssig sind.
     * 
     * @return Falls die Benutzerdaten vom Server akzeptiert wurden true, sonst
     *         false.
     * @throws IOException
     *             Wird geworfen falls beim Aufrufen der Seite ein Fehler
     *             auftrat.
     */
    public final boolean validLogin() throws IOException {
        Log.i(TAG, "Methode: validLogin wird gestartet.");
        getSharedInternetConnection().clearCookies();
        try {
            login();
        } catch (InvalidLoginException e) {
            return false;
        }
        Log.i(TAG, "Methode: validLogin wird verlassen.");
        return true;
    }

    /**
     * @return the university
     */
    public final University getUniversity() {
        return university;
    }

}
