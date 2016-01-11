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
public class ProfessorDAOWebTUWien extends DAOWeb implements ProfessorDAO {
    private static final String TAG        = ProfessorDAOWebTUWien.class
                                                   .getSimpleName();
    private University university;

    /**
     * Erstellt eine ProfessorDAO Implementierung f&uuml;r die TU Wien.
     */
    public ProfessorDAOWebTUWien(final University university) {
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
        String url = "https://tiss.tuwien.ac.at/adressbuch/adressbuch/"
                + "detailsuche?mit=1&per_page=10000&suchtext=" + input;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern startList = Pattern.compile(".*<tbody>.*");
        Pattern startContact = Pattern
                .compile("<fieldset class=\"narrow styledFieldset\">");
        Pattern endList = Pattern.compile(".*</tbody>.*");
        Pattern endContact = Pattern.compile("<script>");
        Pattern nameStart = Pattern
                .compile(".*<span class=\"name-dreizeilig-adressbuch\">.*");

        String line = null;

        while ((line = br.readLine()) != null) {

            if (startList.matcher(line).matches()) {
                // Die TISS Suche zeigt uns eine Liste an.
                Pattern profInfo = Pattern.compile(".*<a href=.*");

                while ((line = br.readLine()) != null) {

                    if (profInfo.matcher(line).matches()) {
                        // Zu bearbeitende Zeiel sieht wie folgt aus:
                        // <a
                        // href="/adressbuch/adressbuch/person/37481?suchtext=Karl">Humer
                        // <span class="markiert">Karl</span> Privatdoz.
                        // Dipl.-Ing. Mag.rer.nat. Dr.techn. (E141)</a>
                        String profUrl = "https://tiss.tuwien.ac.at"
                                + line.substring(13, line.indexOf('?'));
                        String[] tProfName = super.extractContentFromHTML(line);
                        String profName = "";
                        for (String s : tProfName) {
                            profName += s;
                        }
                        output.add(new Professor(profName, university, profUrl));
                    } else if (endList.matcher(line).matches()) {
                        break;
                    }
                }

                break;

            } else if (startContact.matcher(line).matches()) {
                // Die TISS Suche hat genau eine Person gefunden.
                String name = "";
                String details = "";
                Pattern image = Pattern.compile(".*<img.*");

                while (!endContact.matcher(line).matches()) {
                    if (!image.matcher(line).matches()) {
                        details += line;
                        if (nameStart.matcher(line).matches()) {
                            String[] nameParts = super
                                    .extractContentFromHTML(line);
                            name += nameParts[1] + nameParts[3] + " "
                                    + nameParts[0];
                        }
                    }

                    line = br.readLine();
                }

                Professor tProfessor = new Professor(name, university, url);
                tProfessor.setDetails(details);
                output.add(tProfessor);

                break;
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
        Log.d(TAG, "HTTP Request wird erstellt.");

        HttpGet httpget = new HttpGet(professor.getUrlToDetails());
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern startContact = Pattern
                .compile("<fieldset class=\"narrow styledFieldset\">");
        Pattern endContact = Pattern.compile("<script>");
        Pattern image = Pattern.compile(".*<img.*");

        String line = null;

        while ((line = br.readLine()) != null) {
            if (startContact.matcher(line).matches()) {
                String details = "";

                while (!endContact.matcher(line).matches()) {
                    if (!image.matcher(line).matches()) {
                        details += line;
                    }
                    line = br.readLine();
                }
                professor.setDetails(details);
                break;
            }

        }

        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return professor;
    }

}
