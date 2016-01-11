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
import at.mukprojects.vuniapp.dao.base.DAOWebTISS;
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
public class CertificateDAOWebTISS extends DAOWebTISS implements CertificateDAO {
    private static final String TAG = CertificateDAOWebTISS.class
                                            .getSimpleName();

    /**
     * Erzeugt eine neue CertificateDAO, die TISS als Datenquelle nutzt.
     * 
     * @param user
     *            G&uuml;ltiger TISS Benutzer Account.
     * @param university
     *            Key zum Eindeutigen identifizieren der Universit&auml;t.
     */
    public CertificateDAOWebTISS(final UniversityUser user,
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
                    "https://tiss.tuwien.ac.at/graduation/certificates.xhtml");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            Pattern lineWithCertificateTable = Pattern
                    .compile(".*ui-datatable-tablewrapper.*");

            String line = null;

            while ((line = br.readLine()) != null) {
                if (lineWithCertificateTable.matcher(line).matches()) {
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
     * Generiert aus der HTML Tabelle aus TISS eine ArrayList aller Zeugnisse.
     * 
     * @param input
     *            HTML Tabelle aus TISS mit Zeugnissen.
     * @return Eine ArrayList mit allen gefundenen Zeugnissen.
     */
    private ArrayList<Certificate> generateCertificatesOutOfHTMLTable(
            final String input) {
        ArrayList<Certificate> output = new ArrayList<Certificate>();

        ArrayList<ArrayList<String>> content = super
                .extractStringArrayListFromHTMLTable(input);
        // String[] content = super.extractContentFromHTML(input);

        for (ArrayList<String> row : content) {
            try {
                Log.d(TAG, row.toString());
                Log.d(TAG, "size=" + row.size());
                String lvanr = super.extractContentFromHTML(row.get(0))[0];
                String lvatype = super.extractContentFromHTML(row.get(1))[0];
                String lvatitle = super.extractContentFromHTML(row.get(2))[0];
                float hours = Float.parseFloat(super.extractContentFromHTML(row
                        .get(3))[0]);
                float ects = Float.parseFloat(super.extractContentFromHTML(row
                        .get(4))[0]);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
                        Locale.getDefault());
                Date date;
                try {
                    date = sdf
                            .parse(super.extractContentFromHTML(row.get(5))[0]);
                } catch (ParseException e) {
                    date = null;
                    Log.e(TAG,
                            "Datum konnte nicht in ein Date Format umgewandelt werden.");
                }

                String stnr = super.extractContentFromHTML(row.get(6))[0];

                String grade = super.extractContentFromHTML(row.get(7))[0];
                String professor = super.extractContentFromHTML(row.get(8))[0];
                boolean active = true;

                if (row.size() > 9) {
                    String anmerkungen = super.extractContentFromHTML(row
                            .get(9))[0];
                    if (anmerkungen.contains("3")) {
                        active = false;
                    }
                }

                output.add(new Certificate(super.getUniversity(), lvanr,
                        lvatype, lvatitle, hours, ects, date, stnr, grade,
                        professor, active));
            } catch (Exception e) {
                // TODO K: Vielleicht später differenzierter lösen
                Log.e(TAG, "Ein Zeugnis konnte nicht geparsed werden.");
                Log.e(TAG, e.toString());
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
