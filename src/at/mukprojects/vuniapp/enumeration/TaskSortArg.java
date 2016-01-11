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

import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;

/**
 * Diese Enum beschreibt alle m&ouml;glichen Parameter, nach welchen die Tasks
 * sortiert werden k&ouml;nnen.
 * 
 * @author Mathias
 */
public enum TaskSortArg {
    TITLE("Title", "Titel"), DATE("Date", "Datum"), COLOR("Color", "Farbe");

    private String                        argValueEnglish;
    private String                        argValueGerman;
    private static ArrayList<TaskSortArg> argList;

    /**
     * Erstellt eine neue TaskColor.
     * 
     * @param argValueEnglish
     *            Die Value der Farbe.
     * @param argValueGerman
     *            Die Value der Farbe.
     */
    TaskSortArg(final String argValueEnglish, final String argValueGerman) {
        this.argValueEnglish = argValueEnglish;
        this.argValueGerman = argValueGerman;
        addColor(this);
    }

    /**
     * Die Value in Englisch.
     * 
     * @return Value in Englisch.
     */
    public String getArgValueEnglish() {
        return argValueEnglish;
    }

    /**
     * Die Value in Deutsch.
     * 
     * @return Value in Deutsch.
     */
    public String getArgValueGerman() {
        return argValueGerman;
    }

    /**
     * Speichert das Argument in die Liste.
     * 
     * @param arg
     *            Das zu speicherende Argument.
     */
    private static void addColor(final TaskSortArg arg) {
        if (argList == null) {
            argList = new ArrayList<TaskSortArg>();
        }
        argList.add(arg);
    }

    /**
     * Liefert die TaskSortArg zu einer argValue zur&uuml;ck.
     * 
     * @param argValue
     *            Das Argument.
     * @return Die TaskColor mit entsprechender ColorValue.
     */
    public static TaskSortArg valueOfTaskSortArg(final String argValue) {
        for (TaskSortArg arg : argList) {
            if (argValue.equals(arg.getArgValueEnglish())
                    || argValue.equals(arg.getArgValueGerman())) {
                return arg;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Liefert alle Argumente der Enum.
     * 
     * @return Alle Argumente der Enum.
     */
    public static ArrayList<TaskSortArg> getEnumList() {
        return (ArrayList<TaskSortArg>) argList.clone();
    }

    /**
     * Liefert alle Argumente der Enum.
     * 
     * @param language
     *            Sprache der Ausgabe.
     * 
     * @return Alle Argumente der Enum.
     */
    public static ArrayList<String> getStringList(final String language) {
        ArrayList<String> retList = new ArrayList<String>();

        if (language.equals(VUniAppActivity.GERMAN)) {
            for (TaskSortArg arg : (ArrayList<TaskSortArg>) argList.clone()) {
                retList.add(arg.getArgValueGerman());
            }
        } else {
            for (TaskSortArg arg : (ArrayList<TaskSortArg>) argList.clone()) {
                retList.add(arg.getArgValueEnglish());
            }
        }

        return retList;
    }
}
