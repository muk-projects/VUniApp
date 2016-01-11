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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Basisklasse f&uuml;r alle DAOs welche Zugriff auf Moodle ben&ouml;tigen.
 * Enth&auml;lt auch eine Funktion zum Pr&uuml;fen des Benutzernamens und
 * Passworts.
 * 
 * @author kerrim
 */
public class DAOWebMOODLE extends DAOWeb {
    private static final String TAG = DAOWebMOODLE.class.getSimpleName();

    // private static String univisLink;
    private static Date         moodleLinkSet;
    private UniversityUser      user;
    private University          university;

    /**
     * Stellt die DAO f&uuml;r UNIVIS richtig ein.
     * 
     * @param user
     *            G&uuml;ltiger LPIS Benutzer.
     * @param universityKey
     *            Universit&auml;tskey zum Eindeutigen identifizieren der
     *            Universit&auml;t
     */
    public DAOWebMOODLE(final UniversityUser user, final University university) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
        this.university = university;
    }

    /**
     * Pr&uuml;ft ob eine Anmeldung f&uuml;r UNIVIS n&ouml;tig ist und meldet
     * sich gegebenfalls an.
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
        if (moodleLinkSet != null
                && moodleLinkSet.getTime() + 8 * 60 * 1000 < (new Date())
                        .getTime()) {
            moodleLinkSet = null;
        }

        if (moodleLinkSet == null) {
            try {

                HttpGet httpget = new HttpGet("https://moodle.univie.ac.at/my");
                getSharedInternetConnection().execute(httpget).getEntity()
                        .consumeContent();

                /** Anmeldung bei Moodle **/
                HttpPost httppost = new HttpPost(
                        "https://weblogin.univie.ac.at/idp/Authn/UserPassword");

                ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("j_username", user
                        .getUsername()));
                nameValuePairs.add(new BasicNameValuePair("j_password", user
                        .getPassword()));
                nameValuePairs.add(new BasicNameValuePair("lang", ""));
                nameValuePairs.add(new BasicNameValuePair("doLogin", "OK"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = getSharedInternetConnection().execute(
                        httppost);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "iso-8859-1"));

                HttpPost httppost2 = new HttpPost(
                        "https://moodle.univie.ac.at/Shibboleth.sso/SAML2/POST");
                ArrayList<BasicNameValuePair> nameValuePairs2 = new ArrayList<BasicNameValuePair>();
                String line = null;

                while ((line = br.readLine()) != null) {
                    if (line.contains("RelayState")) {
                        String value = line
                                .substring(0, line.lastIndexOf("\""));
                        value = value.substring(value.lastIndexOf("\"") + 1);
                        nameValuePairs2.add(new BasicNameValuePair(
                                "RelayState", value));
                        Log.d(TAG, "RelayState=" + value);
                    } else if (line.contains("SAMLResponse")) {
                        String value = line
                                .substring(0, line.lastIndexOf("\""));
                        value = value.substring(value.lastIndexOf("\"") + 1);
                        nameValuePairs2.add(new BasicNameValuePair(
                                "SAMLResponse", value));
                        Log.d(TAG, "SAMLResponse=" + value);
                    }
                }

                httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                getSharedInternetConnection().execute(httppost2).getEntity()
                        .consumeContent();

                moodleLinkSet = new Date();

            } catch (IOException e) {
                throw new IOException();
            }
        }
        if (moodleLinkSet == null) {
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
        Log.i(TAG, "Methode: validLlogin wird gestartet.");
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
    public University getUniversity() {
        return university;
    }
}
