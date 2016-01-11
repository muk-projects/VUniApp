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

package at.mukprojects.vuniapp.enumeration;

import java.util.ArrayList;

/**
 * Die Enum beschreibt alle m&ouml;glichen Farben eines Tasks.
 * 
 * @author Mathias
 */
public enum TaskColor {
    BLUE("#3971C6"), DARKBLUE("#3262AC"), RED("#932A1C"), DARKRED("#79281D"), GREEN(
            "#A4C639"), DARKGREEN("#627917"), WHITE("white");

    private String                      colorValue;
    private static ArrayList<TaskColor> colorList;

    /**
     * Erstellt eine neue TaskColor.
     * 
     * @param colorValue
     *            Die Value der Farbe.
     */
    TaskColor(final String colorValue) {
        this.colorValue = colorValue;
        addColor(this);
    }

    /**
     * Speichert die Farbe in die ColorList.
     * 
     * @param color
     *            Die zu speichernde Farbe.
     */
    private static void addColor(final TaskColor color) {
        if (colorList == null) {
            colorList = new ArrayList<TaskColor>();
        }
        colorList.add(color);
    }

    /**
     * Liefert die Value der Enum.
     * 
     * @return Die Value der Enum.
     */
    public String displayValue() {
        return colorValue;
    }

    /**
     * Liefert die TaskColor zu einer ColorValue zur&uuml;ck.
     * 
     * @param colorValue
     *            Die ColorValue.
     * @return Die TaskColor mit entsprechender ColorValue.
     */
    public static TaskColor valueOfTaskColor(final String colorValue) {
        for (TaskColor color : colorList) {
            if (colorValue.equals(color.displayValue())) {
                return color;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Liefert alle Farben der Enum.
     * 
     * @return Alle Farben der Enum.
     */
    public static ArrayList<TaskColor> getAllColors() {
        return (ArrayList<TaskColor>) colorList.clone();
    }
}
