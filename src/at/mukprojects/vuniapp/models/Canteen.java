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

package at.mukprojects.vuniapp.models;

import java.util.HashMap;

/**
 * Mensa Objekt mit allen Menus der Mensa.
 * 
 * @author kerrim
 */
public class Canteen {
    private String                     name;
    private HashMap<String, CanteenMenu> mensaMenus;

    /**
     * Erzeugt ein Mensa Objekt.
     * 
     * @param name
     *            Name der Mensa.
     * @param mensaMenus
     *            HashMap mit Namen und dazugeh&ouml;rigen Menus der Mensa.
     */
    public Canteen(final String name, final HashMap<String, CanteenMenu> mensaMenus) {
        super();
        this.name = name;
        this.mensaMenus = mensaMenus;
    }

    /**
     * Liefert den Namen der Mensa zur&uuml;ck.
     * 
     * @return Name der Mensa.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert die Menus der Mensa zur&uuml;ck.
     * 
     * @return HashMap der Mensa Menus. Aufbau: Name, MensaMenu
     */
    public final HashMap<String, CanteenMenu> getMensaMenus() {
        return mensaMenus;
    }

    // CHECKSTYLE OFF
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mensaMenus == null) ? 0 : mensaMenus.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Canteen other = (Canteen) obj;
        if (mensaMenus == null) {
            if (other.mensaMenus != null)
                return false;
        } else if (!mensaMenus.equals(other.mensaMenus))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    // CECKSTYLE ON

}
