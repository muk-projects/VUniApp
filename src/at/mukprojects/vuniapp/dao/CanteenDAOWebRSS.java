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
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWeb;
import at.mukprojects.vuniapp.models.Canteen;
import at.mukprojects.vuniapp.models.CanteenMenu;

/**
 * Implementierung der MensaDAO f&uuml;r den RSS Feed von mensen.at.
 * 
 * @author kerrim
 */
public class CanteenDAOWebRSS extends DAOWeb implements CanteenDAO {
    private static final String         TAG = CanteenDAOWebRSS.class.getSimpleName();

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.MensaDAO#read(java.lang.String)
     */
    @Override
    public final Canteen read(final String mensaName, final int mensaId) throws IOException {
        Log.i(TAG, "Methode: read wird gestartet.");
        Log.d(TAG, "Folgende Mensa wird geholt: " + mensaName);

        Log.d(TAG, "HTTP Request wird erstellt.");
        HttpGet httpget = new HttpGet("http://menu.mensen.at/index/rss/locid/"
                + mensaId);
        HttpResponse response = getSharedInternetConnection().execute(httpget);
        BufferedReader br = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        Log.d(TAG, "Daten aus dem Internet geholt.");

        Pattern menuStart = Pattern.compile(".*<item>");
        Pattern menuEnd = Pattern.compile(".*</item>");
        Pattern menuName = Pattern
                .compile(".*<title>.*(Menü|Brainfood|Vegetarisch|Tagesteller).*");
        Pattern menuTextStart = Pattern.compile("<menu>");
        Pattern menuTextEnd = Pattern.compile(".*</menu>.*");

        Canteen output = null;
        HashMap<String, CanteenMenu> tMenuMap = new HashMap<String, CanteenMenu>();

        String line = null;

        boolean getMeals = false;
        String tMenuName = null;
        HashMap<Long, String> tMenuMeals = null;

        while ((line = br.readLine()) != null) {
            if (menuStart.matcher(line).matches()) {
                line = br.readLine();
                if (menuName.matcher(line).matches()) {
                    tMenuName = line.substring(22, line.length() - 11);
                    tMenuMeals = new HashMap<Long, String>();
                    getMeals = true;
                }
            } else if (menuEnd.matcher(line).matches()) {
                if (tMenuName != null) {
                    tMenuMap.put(tMenuName,
                            new CanteenMenu(tMenuName, tMenuMeals));
                }
                getMeals = false;
            } else if (getMeals && menuTextStart.matcher(line).matches()) {
                do {
                    line += br.readLine() + " ";
                } while (!menuTextEnd.matcher(line).matches());

                String[] temp = super.extractContentFromHTML(line);

                String tMeal = "";
                for (int i = 0; i < temp.length; i++) {
                    if (i != 0) {
                        tMeal += " ";
                    }
                    tMeal += temp[i];
                }

                int day = 0;
                if (tMeal.substring(0, 2).equals("Mo")) {
                    day = 0;
                } else if (tMeal.substring(0, 2).equals("Di")) {
                    day = 1;
                } else if (tMeal.substring(0, 2).equals("Mi")) {
                    day = 2;
                } else if (tMeal.substring(0, 2).equals("Do")) {
                    day = 3;
                } else if (tMeal.substring(0, 2).equals("Fr")) {
                    day = 4;
                }

                tMenuMeals.put((long) day, tMeal);

            }

        }

        output = new Canteen(mensaName, tMenuMap);
        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.MensaDAO#write(int,
     * at.mukprojects.vuniapp.models.Mensa)
     */
    @Override
    public final void write(final String mensaName, final Canteen mensa)
            throws IOException {
        throw new UnsupportedOperationException();
    }

}
