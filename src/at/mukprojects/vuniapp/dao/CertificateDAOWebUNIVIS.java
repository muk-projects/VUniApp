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
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWebUNIVIS;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der CertificateDAO mit UNIVIS als Datenquelle.
 * 
 * @author kerrim
 */
public class CertificateDAOWebUNIVIS extends DAOWebUNIVIS implements
        CertificateDAO {
    private static final String TAG = CertificateDAOWebUNIVIS.class
                                            .getSimpleName();

    /**
     * Erzeugt eine neue CertificateDAO, die UNIVIS als Datenquelle nutzt.
     * 
     * @param user
     *            G&uuml;ltiger UNIVIS Benutzer Account.
     * @param university
     *            Key zum Eindeutigen identifizieren der Universit&auml;t.
     */
    public CertificateDAOWebUNIVIS(final UniversityUser user,
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
            HttpGet httpget = new HttpGet(
                    "https://univis.univie.ac.at/pruefungsleistungen/flow/leistungen-flow");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "iso-8859-1"));

            Pattern lineWithCertificateTable = Pattern
                    .compile(".*<table.*class=\"noborder max\">.*");

            String line = null;
            String tableWithCertificates = null;

            while ((line = br.readLine()) != null) {
                if (lineWithCertificateTable.matcher(line).matches()) {
                    tableWithCertificates = line;
                    Log.d(TAG, "Tabelle mit Zeugnissen gefunden.");
                    while ((line = br.readLine()) != null) {
                        tableWithCertificates += line;
                        if (line.contains("</table>")) {
                            break;
                        }
                    }
                    break;
                }
            }

            if (tableWithCertificates != null) {
                ArrayList<ArrayList<String>> tableContent = extractStringArrayListFromHTMLTable(tableWithCertificates);
                String stnr = null;
                for (ArrayList<String> list : tableContent) {
                    if (list.size() == 10 && !list.get(1).contains("Number")) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.d(TAG, "Spalte: " + i + ": " + list.get(i));
                        }
                        float hours = Float
                                .parseFloat(list.get(6).substring(1));
                        float ects = Float.parseFloat(list.get(7).substring(1));
                        Date examDate;
                        String profName = list.get(4);
                        String subject = list.get(3);
                        // String semester = list.get(2);
                        String lvanr = list.get(1);

                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "dd.MM.yyyy", Locale.getDefault());
                        try {
                            examDate = sdf.parse(list.get(8));
                        } catch (ParseException e) {
                            examDate = null;
                            Log.e(TAG,
                                    "Datum konnte nicht in ein Date Format umgewandelt werden.");
                        }

                        String gradeType = null;

                        try {
                            int grade = Integer.parseInt(list.get(9));

                            switch (grade) {
                                case 1:
                                    gradeType = "sehr gut";
                                    break;
                                case 2:
                                    gradeType = "gut";
                                    break;
                                case 3:
                                    gradeType = "befriedigend";
                                    break;
                                case 4:
                                    gradeType = "genügend";
                                    break;
                                case 5:
                                    gradeType = "nicht genügend";
                                    break;
                                default:
                                    Log.e(TAG,
                                            "Note konnte nicht geparsed werden.");
                                    break;
                            }

                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Note konnte nicht geparsed werden.");
                        }

                        Certificate certificate = new Certificate(
                                super.getUniversity(), lvanr,
                                subject.substring(0, 2), subject.substring(3,
                                        subject.length()), hours, ects,
                                examDate, stnr, gradeType, profName, true);

                        // FIXME K: Inaktive Zeugnisse ermitteln.

                        output.add(certificate);
                    } else if (list.size() == 3) {
                        stnr = list.get(2).substring(0, 5);
                    }
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
