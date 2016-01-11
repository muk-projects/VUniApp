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
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWeb;
import at.mukprojects.vuniapp.models.HtmlData;

/**
 * DAO zum Holen von HTML Daten aus der <a
 * href="http://studentpoint.univie.ac.at">Studentpoint</a> Seite der Uni Wien.
 * 
 * @author kerrim
 */
public class HtmlDataDAOWebStudentPointUniWien extends DAOWeb implements
        HtmlDataDAO {
    private static final String TAG = HtmlDataDAOWebStudentPointUniWien.class
                                            .getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.HtmlDataDAO#readHtmlData(java.lang.String)
     */
    @Override
    public final HtmlData readHtmlData(final String url) throws IOException {
        Log.i(TAG, "Methode: readHtmlData wird gestartet.");

        Log.d(TAG, "HTTP Request wird erstellt.");
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern contentStart = Pattern.compile(".*<!-- Middle Content Area start -->");
        Pattern contentEnd = Pattern.compile(".*<!-- Middle Content Area End -->");

        String data = "";

        String line = null;

        while ((line = br.readLine()) != null) {
            if (contentStart.matcher(line).matches()) {
                while ((line = br.readLine()) != null) {
                    if (!contentEnd.matcher(line).matches()) {
                        data += line;
                    } else {
                        break;
                    }

                }

            }

        }

        Log.i(TAG, "Methode: readHtmlData wird verlassen.");
        return new HtmlData(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.HtmlDataDAO#writeHtmlData(java.lang.String,
     * at.mukprojects.vuniapp.models.HtmlData)
     */
    @Override
    public final void writeHtmlData(final String url, final HtmlData htmlData)
            throws IOException {
        throw new UnsupportedOperationException();
    }

}
