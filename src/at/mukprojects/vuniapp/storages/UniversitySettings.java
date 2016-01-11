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

package at.mukprojects.vuniapp.storages;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.universities.TechnischeUniversitaetWien;
import at.mukprojects.vuniapp.universities.TestUniversity;
import at.mukprojects.vuniapp.universities.UniversitaetWien;
import at.mukprojects.vuniapp.universities.WirtschaftsuniversitaetWien;

/**
 * Die Klasse UniversitySettings verwaltet alle unterst&uuml;tzten
 * Universit&auml;ten von VUniApp.
 * 
 * @author Mathias
 */
public final class UniversitySettings {
    private static final String       TAG = UniversitySettings.class
                                                  .getSimpleName();
    private static UniversitySettings instance;

    private ArrayList<University>     availableUniversities;
    private ArrayList<University>     activeUniversities;

    /**
     * Erstellt einen neuen Storage.
     * 
     * @param preferences
     *            Sharedpreference mit aktiven Universit&auml;ten.
     */
    private UniversitySettings(final SharedPreferences preferences) {
        availableUniversities = new ArrayList<University>();

        /** Neue Uni- Implentierungen nur an dieser Stelle einf&uuml;gen. */
        availableUniversities.add(new UniversitaetWien());
        availableUniversities.add(new TechnischeUniversitaetWien());
        availableUniversities.add(new WirtschaftsuniversitaetWien());
        availableUniversities.add(new TestUniversity());

        activeUniversities = new ArrayList<University>();
        for (University university : availableUniversities) {
            if (preferences.getBoolean(university.getKeyName(), false)) {
                activeUniversities.add(university);
            }
        }
    }

    /**
     * Liefert die einzige Instanz von UniversitySettings zur&uuml;ck.
     * 
     * @return Einzige Instanz von UniversitySettings.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn UniversitySettings nicht
     *             inizialisiert wurde.
     */
    public static UniversitySettings getInstance()
            throws NotInitializedException {
        if (instance == null) {
            throw new NotInitializedException();
        }
        return instance;
    }

    /**
     * Erstellt die einzige Instanz.
     * 
     * @param preferences
     *            Sharedpreference mit aktiven Universit&auml;ten.
     * @return Einzige Instanz von UniversitySettings.
     */
    public static UniversitySettings initialize(
            final SharedPreferences preferences) {
        if (instance == null) {
            instance = new UniversitySettings(preferences);
        }
        return instance;
    }

    /**
     * Liefert eine Kopie aller aktiven Universit&auml;ten.
     * 
     * @return Eine Kopie der Liste.
     */
    public synchronized ArrayList<University> getCopyOfActiveUniversities() {
        ArrayList<University> output = new ArrayList<University>();
        for (University university : activeUniversities) {
            output.add(university);
        }
        return output;
    }

    /**
     * Liefert eine Kopie aller verf&uuml;gbaren Universit&auml;ten.
     * 
     * @return Eine Kopie der Liste.
     */
    public synchronized ArrayList<University> getCopyOfAvailableUniversities() {
        ArrayList<University> output = new ArrayList<University>();
        for (University university : availableUniversities) {
            output.add(university);
        }
        return output;
    }

    /**
     * F&uuml;gt eine Universit&auml;t zu den aktiven Universit&auml;ten hinzu.
     * 
     * @param university
     *            Universit&auml;t, welche hinzugef&uuml;gt werden soll.
     * @return Liefert True, wenn die Universit&auml;t hinzugef&uuml;gt werden
     *         konnte oder falls, wenn dies nicht m&ouml;glich war, weil die
     *         Universit&auml;t zum Beispiel ung&uuml;ltig war.
     */
    public synchronized boolean addUniversityToActiv(final University university) {
        if (availableUniversities.contains(university)) {
            Log.d(TAG, university.getName() + " ist nun aktiv.");
            return activeUniversities.add(university);
        } else {
            return false;
        }
    }

    /**
     * Entfernt eine Universit&auml;t aus der aktiven Liste.
     * 
     * @param university
     *            Universit&auml;t, welche gel&ouml;scht werden soll.
     */
    public synchronized void removeUniversityFromActiv(
            final University university) {
        activeUniversities.remove(university);
    }
}