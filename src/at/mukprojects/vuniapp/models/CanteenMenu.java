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
 * CanteenMenu in dem alle Mahlzeiten eines Menus gespeichert werden.
 * 
 * @author kerrim
 */
public class CanteenMenu {
    private String                name;
    private HashMap<Long, String> meals;

    /**
     * Erstellt ein neues MensaMenu Objekt.
     * 
     * @param name
     *            Name des Mensa Menus.
     * @param meals
     *            Mahlzeiten aus diesem Menu. Aufbau: Id, MensaMenu
     */
    public CanteenMenu(final String name, final HashMap<Long, String> meals) {
        super();
        this.name = name;
        this.meals = meals;
    }

    /**
     * Liefert den Namen des CanteenMenus zur&uuml;ck.
     * 
     * @return Name des Mensa Menus.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert die Mahlzeiten des CanteenMenus zur&uuml;ck.
     * 
     * @return HashMap mit Mahlzeiten des CanteenMenus. Aufbau: Id, MensaMenu
     */
    public final HashMap<Long, String> getMeals() {
        return meals;
    }

    // CHECKSTYLE OFF
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((meals == null) ? 0 : meals.hashCode());
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
        CanteenMenu other = (CanteenMenu) obj;
        if (meals == null) {
            if (other.meals != null)
                return false;
        } else if (!meals.equals(other.meals))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    // CHECKSTYLE ON
}
