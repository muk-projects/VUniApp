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

package at.mukprojects.vuniapp.universities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Canteen;
import at.mukprojects.vuniapp.models.CanteenMenu;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.HtmlData;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.universities.interfaces.packages.CompleteUniversityInterfaces;

// CHECKSTYLE OF
/**
 * Universit&auml;t zum Testen der App.
 * 
 * @author Mathias
 */
public class TestUniversity extends University implements
        CompleteUniversityInterfaces {
    private static final long       serialVersionUID        = 1L;

    /** Name der Universit&auml;t. */
    public static final String      NAME                    = "Test Universität";
    public static final String      KEY                     = "test";

    private static final String     TAG                     = TestUniversity.class
                                                                    .getSimpleName();

    private HashMap<String, String> userKeys;

    /** Stabile statische Anzeige aller Daten. */
    public static final String      KEY_ALL                 = "all";
    /** Bei jedem Aufruf wird ein neues stabiles Zeugnis erstellt. */
    public static final String      KEY_NEW_CERTIFICATES    = "newcertificates";
    /** Liste aller moeglichen Zeugnisse. */
    public static final String      KEY_STATIC_CERTIFICATES = "staticcertificates";
    /** Bei jedem Aufruf wird ein neues stabiles Fach erstellt. */
    public static final String      KEY_NEW_SUBJECTS        = "newsubjects";
    /** Liste aller moeglichen Faecher. */
    public static final String      KEY_STATIC_SUBJECTS     = "staticsubjects";

    private ArrayList<Certificate>  certificates            = new ArrayList<Certificate>();
    private int                     testCertificateNr       = 0;

    private ArrayList<Subject>      subjects                = new ArrayList<Subject>();
    private int                     testSubjectNr           = 0;

    private ArrayList<String>       canteenList;

    /** G&uuml;ltiger Login Name. */
    public static final String      USERNAME                = "testUser";
    /** G&uuml;ltiges Login Passwort. */
    public static final String      PASSWORT                = "testPW";

    /**
     * Erstellt eine neue Test Uni.
     */
    public TestUniversity() {
        super(NAME, KEY);

        userKeys = new HashMap<String, String>();
        userKeys.put(KEY_ALL, "Benutzer für alle Daten");
        userKeys.put(KEY_NEW_CERTIFICATES, "Immer neue Zeugnisse");
        userKeys.put(KEY_STATIC_CERTIFICATES, "Statische Zeugnisliste");
        userKeys.put(KEY_NEW_SUBJECTS, "Immer neue Fächer");
        userKeys.put(KEY_STATIC_SUBJECTS, "Statische Fächerliste");

        canteenList = new ArrayList<String>();
        canteenList.add("Test Canteen 1");
        canteenList.add("Test Canteen 2");
    }

    /*
     * G&uuml;ltige Login Daten: Name: testUser Passwort: testPW
     */
    @Override
    public final boolean checkLoginData(final UniversityUser user)
            throws IOException {
        if (user.getUsername().endsWith(USERNAME)
                && user.getPassword().equals(PASSWORT)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.base.UserInterface#getUserKeys
     * ()
     */
    @Override
    public final HashMap<String, String> getUserKeys() {
        return userKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getCertificates(java.util.HashMap)
     */
    @Override
    public final ArrayList<Certificate> getCertificates(
            final HashMap<String, UniversityUser> users) throws IOException,
            InvalidLoginException {
        ArrayList<String> userKeys = new ArrayList<String>();
        for (String key : users.keySet()) {
            userKeys.add(key);
        }

        ArrayList<Certificate> output = new ArrayList<Certificate>();
        InvalidLoginException invalidLoginException = null;

        for (String key : userKeys) {
            if (key.equals(KEY_ALL)) {
                output.add(new Certificate(this, "1", "VO", "Testfach All", 2,
                        3, new Date(), "1", "sehr gut", "MuK", true));
            } else if (key.equals(KEY_NEW_CERTIFICATES)) {
                certificates.add(new Certificate(this, "1", "VO",
                        "Testfach New: " + testCertificateNr++, 2, 3,
                        new Date(), "1", "sehr gut", "MuK", true));
                output.addAll(certificates);
            } else if (key.equals(KEY_STATIC_CERTIFICATES)) {
                Certificate certificateWithValidData1 = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithValidData1);
                Certificate certificateWithValidData2 = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", false);
                output.add(certificateWithValidData2);
                Certificate certificateWithUniversityNull = new Certificate(
                        null, "1", "VO", "Testfach Static", 2, 3, new Date(),
                        "1", "sehr gut", "MuK", true);
                output.add(certificateWithUniversityNull);
                Certificate certificateWithNrNull = new Certificate(this, null,
                        "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithNrNull);
                Certificate certificateWithInvalidNr1 = new Certificate(this,
                        "~", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidNr1);
                Certificate certificateWithInvalidNr2 = new Certificate(this,
                        "?", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidNr2);
                Certificate certificateWithTypeNull = new Certificate(this,
                        "1", null, "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithTypeNull);
                Certificate certificateWithInvalidType1 = new Certificate(this,
                        "1", "$", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidType1);
                Certificate certificateWithInvalidType2 = new Certificate(this,
                        "1", "/", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidType2);
                Certificate certificateWithTitleNull = new Certificate(this,
                        "1", "VO", null, 2, 3, new Date(), "1", "sehr gut",
                        "MuK", true);
                output.add(certificateWithTitleNull);
                Certificate certificateWithInvalidHours = new Certificate(this,
                        "1", "VO", "Testfach Static", -1, 3, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidHours);
                Certificate certificateWithInvalidECTS = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, -1, new Date(), "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidECTS);
                Certificate certificateWithDateNull = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, null, "1",
                        "sehr gut", "MuK", true);
                output.add(certificateWithDateNull);
                Certificate certificateWithStNrNull = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), null,
                        "sehr gut", "MuK", true);
                output.add(certificateWithStNrNull);
                Certificate certificateWithInvalidStNr = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "?",
                        "sehr gut", "MuK", true);
                output.add(certificateWithInvalidStNr);
                Certificate certificateWithGradeNull = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        null, "MuK", true);
                output.add(certificateWithGradeNull);
                Certificate certificateWithInvalidGrade = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "?", "MuK", true);
                output.add(certificateWithInvalidGrade);
                Certificate certificateWithProfNull = new Certificate(this,
                        "1", "VO", "Testfach Static", 2, 3, new Date(), "1",
                        "sehr gut", null, true);
                output.add(certificateWithProfNull);
            } else {
                invalidLoginException = new InvalidLoginException();
            }
        }

        if (output.isEmpty() && invalidLoginException != null) {
            throw invalidLoginException;
        }
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getCertificateKeys()
     */
    @Override
    public final ArrayList<String> getCertificateKeys() {
        ArrayList<String> certificateKeys = new ArrayList<String>();
        certificateKeys.add(KEY_ALL);
        certificateKeys.add(KEY_NEW_CERTIFICATES);
        certificateKeys.add(KEY_STATIC_CERTIFICATES);
        return certificateKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.CanteenInterface#getCanteens
     * ()
     */
    @Override
    public final ArrayList<String> getCanteens() {
        return canteenList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.CanteenInterface#getCanteen
     * (java.lang.String)
     */
    @Override
    public final Canteen getCanteen(final String canteen) throws IOException {
        HashMap<Long, String> meals = new HashMap<Long, String>();
        HashMap<String, CanteenMenu> menuMap = new HashMap<String, CanteenMenu>();

        if (canteen.equals("Test Canteen 1")) {
            meals.put(10L, "Test Canteen 1: Menü 1");
            meals.put(20L, "Test Canteen 1: Menü 2");
            CanteenMenu menu1 = new CanteenMenu("Test Canteen 1: Menü 1", meals);
            CanteenMenu menu2 = new CanteenMenu("Test Canteen 1: Menü 2", meals);
            menuMap.put("Test Canteen 1: Menü 1", menu1);
            menuMap.put("Test Canteen 1: Menü 2", menu2);

            Canteen retCanteen = new Canteen("Test Canteen 1", menuMap);
            return retCanteen;
        } else {
            meals.put(10L, "Test Canteen 2: Menü 1");

            Canteen retCanteen = new Canteen("Test Canteen 2", menuMap);
            return retCanteen;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface#
     * searchProfessor(java.lang.String)
     */
    @Override
    public final ArrayList<Professor> searchProfessor(final String input)
            throws IOException {
        ArrayList<Professor> retProfList = new ArrayList<Professor>();

        if (input.toLowerCase().equals("karl")) {
            retProfList.add(new Professor("Karl", this, "testUrlToDetails"));
        } else {
            retProfList.add(new Professor("TestProf 1", this,
                    "testUrlToDetails"));
            retProfList.add(new Professor("TestProf 2", this,
                    "testUrlToDetails"));
            retProfList.add(new Professor("TestProf 3", this,
                    "testUrlToDetails"));
        }

        return retProfList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface#
     * getProfessorDetails(at.mukprojects.vuniapp.models.Professor)
     */
    @Override
    public final Professor getProfessorDetails(final Professor professor)
            throws IOException {
        professor.setDetails("Link zu "
                + "<a href='http://vuniapp.mukprojects.at'>VUniApp</a>"
                + "<br> <br>" + "Mail: "
                + "<a href='mailto:mail@mail.at'>Mail</a>");
        return professor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.RoomInterface#searchRooms
     * (java.lang.String)
     */
    @Override
    public final ArrayList<Room> searchRooms(final String input)
            throws JSONException, IOException {
        ArrayList<Room> retRoomList = new ArrayList<Room>();
        Room room1 = new Room("TestRoom1", this,
                "https://maps.google.at/maps?hl=de&tab=wl", "Wien",
                "Mitten in Wien");
        Room room2 = new Room("TestRoom1", this,
                "https://maps.google.at/maps?hl=de&tab=wl", "Wien",
                "Mitten in Wien");
        Room room3 = new Room("TestRoom1", this,
                "https://maps.google.at/maps?hl=de&tab=wl", "Wien",
                "Mitten in Wien");

        if (input.equals("TestRoom1")) {
            retRoomList.add(room1);
        } else {
            retRoomList.add(room1);
            retRoomList.add(room2);
            retRoomList.add(room3);
        }
        return retRoomList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.RoomInterface#searchRooms
     * (at.mukprojects.vuniapp.models.Room)
     */
    @Override
    public final ArrayList<Room> searchRooms(final Room input)
            throws JSONException, IOException {
        ArrayList<Room> retRoomList = new ArrayList<Room>();
        Room room1 = new Room("TestRoom1", this,
                "https://maps.google.at/maps?hl=de&tab=wl", "Wien",
                "Mitten in Wien");
        retRoomList.add(room1);
        return retRoomList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#getSubjects
     * (java.util.HashMap)
     */
    @Override
    public final ArrayList<Subject> getSubjects(
            final HashMap<String, UniversityUser> users, final Context context) throws IOException,
            InvalidLoginException {
        ArrayList<String> userKeys = new ArrayList<String>();
        for (String key : users.keySet()) {
            userKeys.add(key);
        }

        ArrayList<Subject> output = new ArrayList<Subject>();
        InvalidLoginException invalidLoginException = null;

        for (String key : userKeys) {
            if (key.equals(KEY_ALL)) {
                Subject subjectAll = new Subject(this, "Test Fach All", true,
                        "testLink", "test", "testType");
                output.add(subjectAll);
            } else if (key.equals(KEY_NEW_SUBJECTS)) {
                subjects.add(new Subject(this,
                        "Test Fach New " + testSubjectNr, true, "testLink",
                        "test", "testType"));
                output.addAll(subjects);
            } else if (key.equals(KEY_STATIC_SUBJECTS)) {
                Subject subjectValidDataWithTrue = new Subject(this,
                        "Test Fach Static", true, "testLink", "test",
                        "testType");
                output.add(subjectValidDataWithTrue);
                Subject subjectValidDataWithFalse = new Subject(this,
                        "Test Fach Static", false, "testLink", "test",
                        "testType");
                output.add(subjectValidDataWithFalse);
                Subject subjectWithUniversityNull = new Subject(null,
                        "Test Fach Static", false, "testLink", "test",
                        "testType");
                output.add(subjectWithUniversityNull);
                Subject subjectWithNameNull = new Subject(this, null, false,
                        "testLink", "test", "testType");
                output.add(subjectWithNameNull);
                Subject subjectWithLinkNull = new Subject(this,
                        "Test Fach Static", true, null, "test", "testType");
                output.add(subjectWithLinkNull);
                Subject subjectWithInvalidLink = new Subject(this,
                        "Test Fach Static", true, "~", "test", "testType");
                output.add(subjectWithInvalidLink);
                Subject subjectWithLvaNull = new Subject(this,
                        "Test Fach Static", true, "testLink", null, "testType");
                output.add(subjectWithLvaNull);
                Subject subjectWithInvalidLva = new Subject(this,
                        "Test Fach Static", true, "testLink", "#", "testType");
                output.add(subjectWithInvalidLva);
                Subject subjectWithTypeNull = new Subject(this,
                        "Test Fach Static", true, "testLink", "test", null);
                output.add(subjectWithTypeNull);
                Subject subjectWithInvalidType = new Subject(this,
                        "Test Fach Static", true, "testLink", "test", "=");
                output.add(subjectWithInvalidType);
            } else {
                invalidLoginException = new InvalidLoginException();
            }
        }

        if (output.isEmpty() && invalidLoginException != null) {
            throw invalidLoginException;
        }
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#
     * getSubjectDetails(java.util.HashMap,
     * at.mukprojects.vuniapp.models.Subject)
     */
    @Override
    public final Subject getSubjectDetails(
            final HashMap<String, UniversityUser> users, final Subject subject, final Context context)
            throws IOException, InvalidLoginException, ConnectionException {
        subject.setContainsDetails(true);
        ArrayList<String> userKeys = new ArrayList<String>();
        for (String key : users.keySet()) {
            userKeys.add(key);
        }

        ArrayList<Event> events = new ArrayList<Event>();
        ArrayList<Professor> profs = new ArrayList<Professor>();
        InvalidLoginException invalidLoginException = null;

        for (String key : userKeys) {
            if (key.equals(KEY_ALL)) {
                events.add(new Event("Test Event All", "Test Beschreibung",
                        new Date(), 1000, "TestRoom1", this));
                subject.setAppointments(events);
                subject.setExams(events);
                profs.add(new Professor("karl", this, "testUrlToDetails"));
                subject.setProfessors(profs);
                subject.setGoals("testGoals All");
                subject.setContent("TestContent All");
            } else if (key.equals(KEY_NEW_SUBJECTS)) {
                if (testSubjectNr % 2 == 0) {
                    events.add(new Event("Test Event " + testSubjectNr,
                            "Test Beschreibung", new Date(), 1000, "TestRoom1",
                            this));
                    subject.setExams(events);
                    profs.add(new Professor("karl", this, "testUrlToDetails"));
                    subject.setProfessors(profs);
                    subject.setGoals("testGoals All");
                    subject.setContent("TestContent All");
                } else {
                    events.add(new Event("Test Event " + testSubjectNr,
                            "Test Beschreibung", new Date(), 1000, "TestRoom1",
                            this));
                    subject.setAppointments(events);
                    profs.add(new Professor("karl", this, "testUrlToDetails"));
                    subject.setProfessors(profs);
                    subject.setGoals("testGoals All");
                    subject.setContent("TestContent All");
                }
            } else if (key.equals(KEY_STATIC_SUBJECTS)) {
                Event eventWithValidData = new Event("Test Event All",
                        "Test Beschreibung", new Date(), 1000, "TestRoom1",
                        this);
                events.add(eventWithValidData);
                // TODO K: Ich weis nicht genau, welche Eingaben fuer
                // Professoren oder Event moeglich sind also schau, dass du alle
                // Moeglichekeiten abdeckst.
                subject.setAppointments(events);
                subject.setExams(events);

                Professor profWithValidData = new Professor("karl", this,
                        "testUrlToDetails");
                profs.add(profWithValidData);
                // TODO K: Ich weis nicht genau, welche Eingaben fuer
                // Professoren oder Event moeglich sind also schau, dass du alle
                // Moeglichekeiten abdeckst.
                subject.setProfessors(profs);

                subject.setGoals(null);
                subject.setContent(null);
            } else {
                invalidLoginException = new InvalidLoginException();
            }
        }

        if (invalidLoginException != null) {
            throw invalidLoginException;
        }
        return subject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#
     * getSubjectKeys()
     */
    @Override
    public final ArrayList<String> getSubjectKeys() {
        ArrayList<String> subjectKeys = new ArrayList<String>();
        subjectKeys.add(KEY_ALL);
        subjectKeys.add(KEY_NEW_SUBJECTS);
        subjectKeys.add(KEY_STATIC_SUBJECTS);
        return subjectKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.DatesInterface#
     * getUniversityDates()
     */
    @Override
    public final HtmlData getUniversityDates() throws IOException {
        return new HtmlData("TestHtmlData");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getValueToGrade(java.lang.String)
     */
    @Override
    public final Integer getValueToGrade(final String grade) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("sehr gut", 1);
        map.put("gut", 2);
        map.put("befriedigend", 3);
        map.put("genügend", 4);
        map.put("nicht genügened", 5);
        map.put("sehr gut", 1);
        map.put("mit Erfolg teilgenommen", null);
        map.put("ohne Erfolg teilgenommen", null);
        return map.get(grade);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getDisplayedNameToGrade(java.lang.String)
     */
    @Override
    public final String getDisplayedNameToGrade(final String grade) {
        return grade;
    }
}
