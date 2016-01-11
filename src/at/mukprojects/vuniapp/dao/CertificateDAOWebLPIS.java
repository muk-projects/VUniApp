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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWebLPIS;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der CertificateDAO mit <a
 * href="https://tiss.tuwien.ac.at/">TISS</a> als Datenquelle.
 * 
 * @author kerrim
 */
public class CertificateDAOWebLPIS extends DAOWebLPIS implements CertificateDAO {
    private static final String TAG = CertificateDAOWebLPIS.class
                                            .getSimpleName();

    /**
     * Erzeugt eine neue CertificateDAO, die LPIS als Datenquelle nutzt.
     * 
     * @param user
     *            G&uuml;ltiger LPIS Benutzer Account.
     * @param university
     *            Key zum Eindeutigen identifizieren der Universit&auml;t.
     */
    public CertificateDAOWebLPIS(final UniversityUser user,
            final University university) {
        super(user, university);
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.CertificateDAO#readCertificates()
     */
    @Override
    public final ArrayList<Certificate> readCertificates() throws IOException,
            InvalidLoginException {
        Log.i(TAG, "Methode: readCertificates wird gestartet.");

        login();

        ArrayList<Certificate> output = new ArrayList<Certificate>();

        try {
            HttpGet httpget = new HttpGet(super.getLpisLink() + "NT");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "iso-8859-1"));

            Pattern startOfCertificateTable = Pattern
                    .compile(".*table class=\"b3k-data\".*");

            String line = null;

            while ((line = br.readLine()) != null) {
                if (startOfCertificateTable.matcher(line).matches()) {
                    Pattern endOfCertificateTable = Pattern
                            .compile(".*</table>");
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        line += temp;
                        if (endOfCertificateTable.matcher(line).matches()) {
                            break;
                        }
                    }
                    output = generateCertificatesOutOfHTMLTable(line);
                    break;
                }
            }
        } catch (IOException e) {
            throw new IOException();
        }

        Log.d(TAG, "Es wurden " + output.size() + " Zeugnisse ausgelesen.");
        for (Certificate c : output) {
            Log.d(TAG, c.toString());
        }

        Log.i(TAG, "Methode: readCertificates wird verlassen.");

        return output;
    }

    /**
     * Generiert aus der HTML Tabelle aus LPIS eine ArrayList aller Zeugnisse.
     * 
     * @param input
     *            HTML Tabelle aus LPIS mit Zeugnissen.
     * @return Eine ArrayList mit allen gefundenen Zeugnissen.
     */
    private ArrayList<Certificate> generateCertificatesOutOfHTMLTable(
            final String input) {
        ArrayList<Certificate> output = new ArrayList<Certificate>();
        ArrayList<ArrayList<String>> content = super
                .extractStringArrayListFromHTMLTable(input);

        for (int i = 2; i < content.size(); i++) {
            for (int j = 0; j < content.get(i).size(); j++) {
                Log.v(TAG, "Zeile " + i + " Spalte " + j + ": "
                        + content.get(i).get(j));
            }

            String subjectType = super.extractContentFromHTML(content.get(i)
                    .get(0))[1];

            String subjectName = super.extractContentFromHTML(content.get(i)
                    .get(0))[3];

            String profName = content.get(i).get(0)
                    .substring(content.get(i).get(0).lastIndexOf("<br />") + 6);
            while (profName.charAt(0) == ' ') {
                profName = profName.substring(1);
            }
            while (profName.charAt(profName.length() - 1) == ' ') {
                profName = profName.substring(0, profName.length() - 2);
            }
            profName = profName.replace("&nbsp;", " ");

            float hours = Float.parseFloat(super.extractContentFromHTML(content
                    .get(i).get(1))[0]);

            float ects = Float.parseFloat(super.extractContentFromHTML(content
                    .get(i).get(1))[2]);

            String grade = super.extractContentFromHTML(content.get(i).get(2))[0];

            Date date;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
                    Locale.getDefault());
            try {
                date = sdf.parse(super.extractContentFromHTML(content.get(i)
                        .get(2))[2]);
            } catch (ParseException e) {
                date = null;
                Log.e(TAG,
                        "Datum konnte nicht in ein Date Format umgewandelt werden.");
            }

            String curnr = content.get(i).get(3);

            output.add(new Certificate(super.getUniversity(), null,
                    subjectType, subjectName, hours, ects, date, curnr, grade,
                    profName, true));

        }

        Collections.sort(output);

        // Nicht mehr gültige Zeugnisse markieren
        for (int i = 0; i < output.size(); i++) {
            for (int j = (i + 1); j < output.size(); j++) {
                if (output.get(i).getTitle().equals(output.get(j).getTitle())) {
                    output.get(i).setActive(false);
                }
            }
        }

        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.CertificateDAO#writeCertificates(java.util
     * .ArrayList)
     */
    @Override
    public final void writeCertificates(
            final ArrayList<Certificate> certificates) throws IOException {
        throw new UnsupportedOperationException();
    }

}
