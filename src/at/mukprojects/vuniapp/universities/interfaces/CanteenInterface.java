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

package at.mukprojects.vuniapp.universities.interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import at.mukprojects.vuniapp.models.Canteen;

/**
 * Das Interface CanteenInterface wird von allen Universit&auml;ten
 * implementiert, welche eine Mensa implementiert haben.
 * 
 * @author Mathias
 */
public interface CanteenInterface extends Serializable {
    /**
     * Liefert die Mensen der Universit&auml;t zur&uuml;ck.
     * 
     * @return Mensen der Universit&auml;t.
     */
    ArrayList<String> getCanteens();

    /**
     * Die Methode liest die Men&uuml;s der Mensa aus.
     * 
     * @param canteen
     *            Name, welcher angibt, welche Mensa ausgelesen werden soll.
     * @return Liefert die ausgelesene Mensa zur&uuml;ck.
     * @throws IOException
     *             Die Exception wird geworfen, wenn es beim Auslesen der Menus
     *             zu einem Fehler kommt.
     */
    Canteen getCanteen(String canteen) throws IOException;
}
