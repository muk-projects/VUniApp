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

package at.mukprojects.vuniapp.helper;

/**
 * Diese Helper Klasse beinhaltet Methoden, um String miteinander zu
 * vergleichen.
 * 
 * @author Mathias
 */
public abstract class TextCompareHelper {

    /**
     * Liefert das Minumum dreier Zahlen.
     * 
     * @param a
     *            Erste Zahl.
     * @param b
     *            Zweite Zahl.
     * @param c
     *            Dritte Zahl.
     * @return Kleinste Zahl.
     */
    private static int getMinimum(final int a, final int b, final int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * Liefert die Levenshtein Distanz zweier Strings.
     * 
     * @param strOne
     *            Der erste String.
     * @param strTwo
     *            Der zweite String.
     * @return Die Levenshtein Distanz.
     */
    public static final int getLevenshteinDistance(final String strOne,
            final String strTwo) {

        int[][] distance = new int[strOne.length() + 1][strTwo.length() + 1];

        for (int i = 0; i <= strOne.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= strTwo.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= strOne.length(); i++) {
            for (int j = 1; j <= strTwo.length(); j++) {
                distance[i][j] = getMinimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                                + ((strOne.charAt(i - 1) == strTwo
                                        .charAt(j - 1)) ? 0 : 1));
            }
        }

        return distance[strOne.length()][strTwo.length()];
    }
}
