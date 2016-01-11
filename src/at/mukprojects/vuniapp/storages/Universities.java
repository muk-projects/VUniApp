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
import java.util.List;

import android.util.Log;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.universities.interfaces.CanteenInterface;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;
import at.mukprojects.vuniapp.universities.interfaces.DatesInterface;
import at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface;
import at.mukprojects.vuniapp.universities.interfaces.RoomInterface;
import at.mukprojects.vuniapp.universities.interfaces.SubjectInterface;
import at.mukprojects.vuniapp.universities.interfaces.base.LoginInterface;

/**
 * Die Klasse Universities verwaltet alle unterst&uuml;tzten Universit&auml;ten
 * von VUniApp.
 * 
 * @author Mathias
 * @author kerrim
 */
public final class Universities {
    private static final String         TAG = Universities.class
                                                    .getSimpleName();
    private static Universities         instance;
    private HashMap<String, University> universities;
    private HashMap<String, University> universitiesKey;

    /**
     * Erstellt einen neuen Storage.
     * 
     * @throws NotInitializedException
     *             Die Exception wird geworfen, wenn es nicht m&ouml;glich war
     *             die Universit&auml;ten aus dem UniversitySettingsStorage zu
     *             laden.
     */
    private Universities() throws NotInitializedException {
        ArrayList<University> tempUniversities = UniversitySettings
                .getInstance().getCopyOfActiveUniversities();

        universities = new HashMap<String, University>();
        universitiesKey = new HashMap<String, University>();
        for (University university : tempUniversities) {
            universities.put(university.getName(), university);
            universitiesKey.put(university.getKeyName(), university);
            Log.d(TAG, university.getName() + " (" + university.getKeyName()
                    + "): " + university);
        }
    }

    /**
     * Liefert die einzige Instanz von Universities zur&uuml;ck.
     * 
     * @return Einzige Instanz von Universities. Es wird null
     *         zur&uuml;ckgeliefert, wenn w&auml;hrend des erstellens oder
     *         aktualisieren der Klasse ein Fehler auftritt.
     */
    public static Universities getInstance() {
        try {
            if (instance == null) {
                instance = new Universities();
            } else {
                instance.updateUniversities();
            }
        } catch (NotInitializedException notInitializedException) {
            Log.e(TAG, notInitializedException.getMessage(),
                    notInitializedException);
        }
        return instance;
    }

    /**
     * Aktualisiert die Liste an Universit&auml;ten.
     * 
     * @throws NotInitializedException
     *             Die Exception wird geworfen, wenn es nicht m&ouml;glich war
     *             die Universit&auml;ten aus dem UniversitySettingsStorage zu
     *             laden.
     */
    private synchronized void updateUniversities()
            throws NotInitializedException {
        ArrayList<University> tempUniversities = UniversitySettings
                .getInstance().getCopyOfActiveUniversities();

        universities.clear();
        universitiesKey.clear();
        for (University university : tempUniversities) {
            universities.put(university.getName(), university);
            universitiesKey.put(university.getKeyName(), university);
            Log.d(TAG, university.getName() + " (" + university.getKeyName()
                    + "): " + university);
        }
    }

    /**
     * Liefert eine Liste mit allen vorhanden Universit&auml;ten zur&uuml;ck.
     * 
     * @return Liste mit allen Universit&auml;ten.
     */
    public synchronized List<University> getUniversities() {
        ArrayList<University> tempUniversities = new ArrayList<University>();
        for (String universityName : universities.keySet()) {
            tempUniversities.add(universities.get(universityName));
        }
        return tempUniversities;
    }

    /**
     * Liefert die Universit&auml;t zu einem Namen.
     * 
     * @param name
     *            Namen der Universit&auml;t.
     * @return Universit&auml;t, welche den &uuml;bergeben Namen enth&auml;t
     *         oder null falls die Universit&auml;t nicht existiert.
     */
    public synchronized University getUniversityFromName(final String name) {
        return universities.get(name);
    }

    /**
     * Liefert die Universit&auml;t zu einem Key.
     * 
     * @param key
     *            Key der Universit&auml;t.
     * @return Universit&auml;t, welche den &uuml;bergeben Key enth&auml;t oder
     *         null falls die Universit&auml;t nicht existiert.
     */
    public synchronized University getUniversityFromKey(final String key) {
        return universitiesKey.get(key);
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * CanteenInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface CanteenInterface. In
     *         diesem Fall ist der String nicht der Name der Universit&auml;t
     *         sondern der, der Mensa.
     */
    public synchronized HashMap<String, CanteenInterface> getUniversitiesWithCanteens() {
        HashMap<String, CanteenInterface> tempUniversities = new HashMap<String, CanteenInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof CanteenInterface) {
                CanteenInterface canteenInterface = (CanteenInterface) universities
                        .get(universityName);
                for (String canteen : canteenInterface.getCanteens()) {
                    tempUniversities.put(canteen, canteenInterface);
                }
            }
        }
        return tempUniversities;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * RoomInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface RoomInterface.
     */
    public synchronized HashMap<String, RoomInterface> getUniversitiesWithRooms() {
        HashMap<String, RoomInterface> tempUniversities = new HashMap<String, RoomInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof RoomInterface) {
                tempUniversities.put(universityName,
                        (RoomInterface) universities.get(universityName));
            }
        }
        return tempUniversities;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * ProfessorInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface ProfessorInterface.
     */
    public synchronized HashMap<String, ProfessorInterface> getUniversitiesWithProfs() {
        HashMap<String, ProfessorInterface> tempUniversities = new HashMap<String, ProfessorInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof ProfessorInterface) {
                tempUniversities.put(universityName,
                        (ProfessorInterface) universities.get(universityName));
            }
        }
        return tempUniversities;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * SubjectInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface SubjectInterface.
     */
    public synchronized HashMap<String, SubjectInterface> getUniversitiesWithSubjects() {
        HashMap<String, SubjectInterface> tempUniversities = new HashMap<String, SubjectInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof SubjectInterface) {
                tempUniversities.put(universityName,
                        (SubjectInterface) universities.get(universityName));
            }
        }
        return tempUniversities;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * SubjectInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface SubjectInterface.
     */
    public synchronized HashMap<String, CertificateInterface> getUniversitiesWithCertificates() {
        HashMap<String, CertificateInterface> tempUniversities = new HashMap<String, CertificateInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof CertificateInterface) {
                tempUniversities
                        .put(universityName,
                                (CertificateInterface) universities
                                        .get(universityName));
            }
        }
        return tempUniversities;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * LoginInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface LoginInterface.
     */
    public synchronized HashMap<University, LoginInterface> getUniversitiesWithLogin() {
        HashMap<University, LoginInterface> tempUniversities = new HashMap<University, LoginInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof LoginInterface) {
                tempUniversities.put(universities.get(universityName),
                        (LoginInterface) universities.get(universityName));
            }
        }
        return tempUniversities;
    }

    /**
     * Diese Methode liefert eine Liste aller Universit&auml;ten zur&uuml;ck,
     * welche das Interface LoginInterface implementiert haben. Zu jeder
     * Universit&auml;t wird eine ArrayList mit allen ben&auml;tigten UserKeys
     * mitgeliefert. Wenn die Universit&auml;t nur einen UserKey hat so wird
     * eine leere Liste mitgeliefert.
     * 
     * @return Map mit Universit&auml;t und den dazugeh&ouml;rigen UserKeys.
     */
    public synchronized HashMap<University, HashMap<String, String>> getUniversitiesAndUserKeys() {
        HashMap<University, HashMap<String, String>> tempMap = new HashMap<University, HashMap<String, String>>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof LoginInterface) {
                HashMap<String, String> userKeys = new HashMap<String, String>();
                if (universities.get(universityName) instanceof SubjectInterface) {
                    userKeys.putAll(((SubjectInterface) universities
                            .get(universityName)).getUserKeys());
                }
                if (universities.get(universityName) instanceof CertificateInterface) {
                    userKeys.putAll(((CertificateInterface) universities
                            .get(universityName)).getUserKeys());
                }
                tempMap.put(universities.get(universityName), userKeys);
            }
        }
        return tempMap;
    }

    /**
     * Liefert alle Universit&auml;ten zur&uuml;ck, welche das Interface
     * DatesInterface implementiert haben.
     * 
     * @return Alle Universit&auml;ten mit dem Interface DatesInterface.
     */
    public synchronized HashMap<String, DatesInterface> getUniversitiesWithDates() {
        HashMap<String, DatesInterface> tempUniversities = new HashMap<String, DatesInterface>();
        for (String universityName : universities.keySet()) {
            if (universities.get(universityName) instanceof DatesInterface) {
                tempUniversities.put(universityName,
                        (DatesInterface) universities.get(universityName));
            }
        }
        return tempUniversities;
    }
}
