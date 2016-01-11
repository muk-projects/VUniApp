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
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWeb;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der ProfessorDAO f&uuml;r die WU Wien. Da es bei der WU keine
 * Suche gibt werden einfach alle dort eingetragenen Professoren angezeigt. Die
 * Liste der Professoren kann unter folgenden Link gefunden werden:
 * http://www.wu.ac.at/structure/departments/faculty/professors
 * 
 * @author kerrim
 */
public class ProfessorDAOWebWUWien extends DAOWeb implements ProfessorDAO {
    private static final String TAG        = ProfessorDAOWebWUWien.class
                                                   .getSimpleName();
    private University              university;

    /**
     * Erstellt eine ProfessorDAO Implementierung f&uuml;r die WU Wien.
     * 
     * @param university
     *            Universit&auml;t
     */
    public ProfessorDAOWebWUWien(final University university) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.university = university;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.ProfessorDAO#searchProfessor(java.lang.String)
     */
    @Override
    public final ArrayList<Professor> searchProfessor(final String input)
            throws IOException {
        Log.i(TAG, "Methode: searchProfessor wird gestartet.");
        ArrayList<Professor> professors = new ArrayList<Professor>();

        Log.d(TAG, "HTTP Request wird erstellt.");
        String url = "http://www.wu.ac.at/structure/departments/faculty/professors";
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern professor = Pattern
                .compile(".*<li>.*<strong>.*:.*</strong>.*<a href=.*>.*</a>.*</li>.*");

        String line = null;

        while ((line = br.readLine()) != null) {
            if (professor.matcher(line).matches()) {
                String name = "";
                String urlToDetails = line;
                String[] tempName = super.extractContentFromHTML(line);
                for (String temp : tempName) {
                    name += temp;
                }
                Log.v(TAG, name);
                if (urlToDetails.indexOf("href=\"") > 0) {
                    urlToDetails = urlToDetails.substring(urlToDetails
                            .indexOf("href=\"") + 6);
                    urlToDetails = urlToDetails.substring(0,
                            urlToDetails.indexOf("\""));
                    if (!urlToDetails.contains("http")) {
                        urlToDetails = "http://www.wu.ac.at" + urlToDetails;
                    }
                } else {
                    urlToDetails = null;
                }
                Log.v(TAG, urlToDetails);
                professors.add(new Professor(name, university, urlToDetails));
            }
        }
        Log.i(TAG, "Methode: searchProfessor wird verlassen.");
        return professors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.ProfessorDAO#getDetails(at.mukprojects.vuniapp
     * .models.Professor)
     */
    @Override
    public final Professor getDetails(final Professor professor)
            throws IOException {
        Log.i(TAG, "Methode: getDetails wird gestartet.");
        Log.d(TAG, "HTTP Request wird erstellt.");

        HttpGet httpget = new HttpGet(professor.getUrlToDetails());
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern startContact = Pattern.compile(".*<!-- content -->");
        Pattern endContact = Pattern.compile(".*bd-left-nav.*");

        String line = null;
        String data = "";

        while ((line = br.readLine()) != null) {
            if (startContact.matcher(line).matches()) {
                while ((line = br.readLine()) != null) {
                    if (endContact.matcher(line).matches()) {
                        break;
                    }
                    data += line;
                }
                break;
            }
        }

        professor.setDetails(data);

        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return professor;
    }

}
