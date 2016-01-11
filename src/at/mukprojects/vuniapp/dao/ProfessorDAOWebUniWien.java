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
 * Implementierung der ProfessorDAO f&uuml;r die TU Wien. Als Suche wird die von
 * TISS genutzt. Auch erreichbar unter
 * https://tiss.tuwien.ac.at/adressbuch/adressbuch
 * 
 * @author kerrim
 */
public class ProfessorDAOWebUniWien extends DAOWeb implements ProfessorDAO {
    private static final String TAG = ProfessorDAOWebUniWien.class
                                            .getSimpleName();
    private University              university;

    /**
     * Erstellt eine ProfessorDAO Implementierung f&uuml;r die TU Wien.
     * 
     * @param universityKey
     *            Key zur Identifikationen der Universit&auml;t.
     */
    public ProfessorDAOWebUniWien(final University university) {
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
    public final ArrayList<Professor> searchProfessor(final String tInput)
            throws IOException {
        Log.i(TAG, "Methode: searchProfessor wird gestartet.");
        ArrayList<Professor> output = new ArrayList<Professor>();

        String input = "";
        for (int i = 0; i < tInput.length(); i++) {
            if (tInput.charAt(i) == ' ') {
                input += '+';
            } else {
                input += tInput.charAt(i);
            }
        }

        Log.d(TAG, "HTTP Request wird erstellt.");
        String url = "http://online.univie.ac.at/pers?zuname=" + input;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent(), "ISO-8859-15"));
        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern startProfessor = Pattern
                .compile(".*pers_vorname.*pers_zuname.*");
        Pattern endNewProfessor = Pattern
                .compile(".*<div class=\"pers_name\">.*");
        Pattern startFooter = Pattern.compile(".*<div id=\"footer\">.*");

        String line = null;

        while ((line = br.readLine()) != null) {

            if (startProfessor.matcher(line).matches()) {
                String name = "";
                String details = "";
                String[] nameLine = extractContentFromHTML(line);
                for (String nameParts : nameLine) {
                    name += nameParts;
                }

                details += line;

                while ((line = br.readLine()) != null) {
                    if (endNewProfessor.matcher(line).matches()
                            || startFooter.matcher(line).matches()) {
                        break;
                    } else {
                        details += line;
                    }

                }
                Professor professor = new Professor(name, university, url);
                professor.setDetails(details);
                output.add(professor);
            }

        }

        Log.i(TAG, "Methode: searchProfessor wird verlassen.");
        return output;
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
        Log.d(TAG,
                "Professoren Details werden hier bereits bei der Suche eingesetzt.");
        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return professor;
    }

}
