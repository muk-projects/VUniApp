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

import java.io.IOException;

import at.mukprojects.vuniapp.models.Canteen;

/**
 * Interface f&uuml;r den Datenzugriff auf Mensen.
 * 
 * @author kerrim
 */
public interface CanteenDAO {

    Canteen read(final String mensaName, final int mensaId) throws IOException;

    void write(final String mensaName, final Canteen mensa)
            throws IOException;

}
