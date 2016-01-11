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
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.SharedInternetConnection;

/**
 * Basisklasse f&uuml;r alle DAOs welche Zugriff auf LPIS ben&ouml;tigen.
 * Enth&auml;lt auch eine Funktion zum Pr&uuml;fen des Benutzernamens und
 * Passworts.
 * 
 * @author kerrim
 */
public class DAOWebLPIS extends DAOWeb {
    private static final String TAG = DAOWebLPIS.class.getSimpleName();

    private static String       lpisLink;
    private static Date         lpisLinkSet;
    private UniversityUser      user;
    private University          university;

    /**
     * Stellt die DAO f&uuml;r LPIS richtig ein.
     * 
     * @param user
     *            G&uuml;ltiger LPIS Benutzer.
     * @param universityKey
     *            Universit&auml;tskey zum Eindeutigen identifizieren der
     *            Universit&auml;t
     */
    public DAOWebLPIS(final UniversityUser user, final University university) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
        this.university = university;
    }

    /**
     * Pr&uuml;ft ob eine Anmeldung f&uuml;r LPIS n&ouml;tig ist und meldet sich
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
        if (lpisLinkSet != null
                && lpisLinkSet.getTime() + 8 * 60 * 1000 < (new Date())
                        .getTime()) {
            lpisLink = null;
        }

        if (!getSharedInternetConnection().cookieExists("LPIS")
                || lpisLink == null) {
            try {
                String loginLink = null;
                String userFormName = null;
                String passwordFormName = null;
                String seedValue = null;

                /** Für Login nötige Informationen holen **/
                HttpGet httpget = new HttpGet("https://lpis.wu.ac.at/lpis");
                HttpResponse response = getSharedInternetConnection().execute(
                        httpget);

                lpisLink = getSharedInternetConnection()
                        .getLastHeaderLocation();
                lpisLink = lpisLink.substring(0, lpisLink.indexOf("ID?R="));
                lpisLinkSet = new Date();
                loginLink = lpisLink + "CID";

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = null;
                Pattern userFormNamePattern = Pattern
                        .compile(".*<input type=\"text\" accesskey=\"u\" .*");
                Pattern passwordFormNamePattern = Pattern
                        .compile(".*<input class=\"disableAutoComplete\" type=\"password\" accesskey=\"p\" .*");
                Pattern seedValuePattern = Pattern
                        .compile(".*<input type=\"hidden\" name=\"seed\" .*");

                while ((line = br.readLine()) != null) {
                    if (userFormNamePattern.matcher(line).matches()) {
                        userFormName = line.substring(62, line.length() - 9);
                    }
                    if (passwordFormNamePattern.matcher(line).matches()) {
                        passwordFormName = line
                                .substring(94, line.length() - 9);
                    }
                    if (seedValuePattern.matcher(line).matches()) {
                        seedValue = line.substring(46, line.length() - 4);
                    }
                }

                Log.d(TAG, userFormName + "=" + user.getUsername());
                Log.d(TAG, passwordFormName + "=" + "*****");
                Log.d(TAG, "Seed=" + seedValue);

                List<Cookie> cookies = SharedInternetConnection
                        .getSharedInternetConnection().getCookies();
                for (Cookie cookie : cookies) {
                    Log.d(TAG, "Cookie: " + cookie.toString());
                }

                Log.d(TAG, "Daten übertragen an: " + loginLink);

                /** Anmeldung bei LPIS **/
                HttpPost httppost = new HttpPost(loginLink);

                ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
                nameValuePairs.add(new BasicNameValuePair(userFormName, user
                        .getUsername()));
                nameValuePairs.add(new BasicNameValuePair(passwordFormName,
                        user.getPassword()));
                nameValuePairs.add(new BasicNameValuePair("seed", seedValue));
                nameValuePairs.add(new BasicNameValuePair("self", "login"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                getSharedInternetConnection().execute(httppost).getEntity()
                        .consumeContent();

                for (Cookie cookie : cookies) {
                    Log.d(TAG, "Cookie: " + cookie.toString());
                }

            } catch (IOException e) {
                throw new IOException();
            }
        }
        if (!getSharedInternetConnection().cookieExists("LPIS")) {
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
     * Liefert den LPIS Link mit richtigen Port zur&uuml;ck. Dieser muss dann
     * f&uuml;r die weitere Kommunikation mit LPIS genutzt werden.
     * 
     * @return LPIS Link mit korrektem Port.
     */
    public final String getLpisLink() {
        return lpisLink;
    }

    /**
     * @return the university
     */
    public University getUniversity() {
        return university;
    }
}
