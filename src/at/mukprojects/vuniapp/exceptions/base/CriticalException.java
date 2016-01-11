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

package at.mukprojects.vuniapp.exceptions.base;

/**
 * Dieses Interface wird von allen Exceptions implementiert, welche einen
 * fatalen Fehler ausl&ouml;ssen k&ouml;nnen.
 * 
 * @author Mathias
 */
public interface CriticalException {
    /**
     * Gibt an ob es sich um einen fatalen Fehler handelt.
     * 
     * @return Liefert einen Wahrheitswert, welcher den Fehler klassifiziert.
     */
    boolean isCreiticalException();
}
