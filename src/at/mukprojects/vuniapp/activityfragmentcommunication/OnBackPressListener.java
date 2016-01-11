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

package at.mukprojects.vuniapp.activityfragmentcommunication;

/**
 * Dieses Interface wird von Fragments implementiert, welche beim Dr&uuml;cken
 * des BackButtons nicht einfach geschlossen werden sollen.
 * 
 * @author Mathias
 */
public interface OnBackPressListener {
    /**
     * Die Methode gibt an, ob das Fragment eine onBackPressAktion hat oder
     * geschlossen werden darf.
     * 
     * @return Liefert True, falls das Fragment nicht geschlossen werden soll
     *         und stattdessen die Aktion Methode ausgef&uuml;hrt werden soll.
     *         Andernfalls es wird False ausgegeben und das Fragment kann
     *         geschlossen werden.
     */
    boolean needBackPressAktion();

    /**
     * Die Methode wird implementiert und ausgef&uuml;hrt, wenn das Fragment
     * nicht geschlossen werden soll, sondern eine andere Aufgabe
     * ausgef&uuml;hrt werden soll.
     */
    void onBackPressAktion();
}
