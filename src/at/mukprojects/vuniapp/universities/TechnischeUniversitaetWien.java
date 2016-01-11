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
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import at.mukprojects.vuniapp.dao.CanteenDAOCache;
import at.mukprojects.vuniapp.dao.CanteenDAOWebRSS;
import at.mukprojects.vuniapp.dao.CertificateDAOCache;
import at.mukprojects.vuniapp.dao.CertificateDAOWebTISS;
import at.mukprojects.vuniapp.dao.HtmlDataDAOCache;
import at.mukprojects.vuniapp.dao.HtmlDataDAOWebTUWien;
import at.mukprojects.vuniapp.dao.ProfessorDAOWebTUWien;
import at.mukprojects.vuniapp.dao.RoomDAOMuKServer;
import at.mukprojects.vuniapp.dao.SubjectDAOAndroidDB;
import at.mukprojects.vuniapp.dao.SubjectDAOCache;
import at.mukprojects.vuniapp.dao.SubjectDAOFilterDB;
import at.mukprojects.vuniapp.dao.SubjectDAOWebTISS;
import at.mukprojects.vuniapp.dao.base.DAOWebTISS;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Canteen;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.HtmlData;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.CanteenService;
import at.mukprojects.vuniapp.services.CanteenServiceCaching;
import at.mukprojects.vuniapp.services.CertificateService;
import at.mukprojects.vuniapp.services.CertificateServiceCaching;
import at.mukprojects.vuniapp.services.HtmlDataService;
import at.mukprojects.vuniapp.services.HtmlDataServiceCaching;
import at.mukprojects.vuniapp.services.ProfessorService;
import at.mukprojects.vuniapp.services.ProfessorServiceStd;
import at.mukprojects.vuniapp.services.RoomService;
import at.mukprojects.vuniapp.services.RoomServiceStd;
import at.mukprojects.vuniapp.services.SubjectService;
import at.mukprojects.vuniapp.services.SubjectServiceCachingFiltered;
import at.mukprojects.vuniapp.services.SubjectServiceFiltered;
import at.mukprojects.vuniapp.universities.interfaces.packages.CompleteUniversityInterfaces;

/**
 * Die Klasse kapselt alle Funktionen f&uuml;r die Technichen Universit&auml;t
 * Wien.
 * 
 * @author Mathias
 * @author kerrim
 */
public class TechnischeUniversitaetWien extends University implements
        CompleteUniversityInterfaces {
    private static final long        serialVersionUID = 1L;

    /** Name der Universit&auml;t. */
    public static final String       NAME             = "Technische Universität Wien";
    public static final String       KEY              = "tuwien";

    private static final String      TAG              = TechnischeUniversitaetWien.class
                                                              .getSimpleName();

    private static final String      UKZID            = "zid";

    private HashMap<String, Integer> canteenList;
    private HashMap<String, String>  userKeys;

    private HashMap<String, Integer> mapValueToGrade;
    private HashMap<String, String>  mapDisplayedNameToGrade;

    /**
     * Erstellt ein neues TechnischeUniversitaetWien Object.
     */
    public TechnischeUniversitaetWien() {
        super(NAME, KEY);

        canteenList = new HashMap<String, Integer>();
        canteenList.put("TU Wien Mensa Markt & M-Cafe Freihaus", 9);
        canteenList.put("TU Wien Cafe Schrödinger im Freihaus", 52);

        userKeys = new HashMap<String, String>();
        userKeys.put(UKZID, "Zentraler Informatikdienst");

        mapValueToGrade = new HashMap<String, Integer>();
        mapValueToGrade.put("sehr gut", 1);
        mapValueToGrade.put("gut", 2);
        mapValueToGrade.put("befriedigend", 3);
        mapValueToGrade.put("genügend", 4);
        mapValueToGrade.put("nicht genügened", 5);
        mapValueToGrade.put("1", 1);
        mapValueToGrade.put("2", 2);
        mapValueToGrade.put("3", 3);
        mapValueToGrade.put("4", 4);
        mapValueToGrade.put("5", 5);
        mapValueToGrade.put("mit Erfolg teilgenommen", null);
        mapValueToGrade.put("ohne Erfolg teilgenommen", null);

        mapDisplayedNameToGrade = new HashMap<String, String>();
        mapDisplayedNameToGrade.put("sehr gut", "Sehr Gut");
        mapDisplayedNameToGrade.put("gut", "Gut");
        mapDisplayedNameToGrade.put("befriedigend", "Befriedigend");
        mapDisplayedNameToGrade.put("genügend", "Genügend");
        mapDisplayedNameToGrade.put("nicht genügened", "Nicht Genügened");
        mapDisplayedNameToGrade.put("1", "Sehr Gut");
        mapDisplayedNameToGrade.put("2", "Gut");
        mapDisplayedNameToGrade.put("3", "Befriedigend");
        mapDisplayedNameToGrade.put("4", "Genügend");
        mapDisplayedNameToGrade.put("5", "Nicht Genügened");
        mapDisplayedNameToGrade.put("mit Erfolg teilgenommen",
                "mit Erfolg teilgenommen");
        mapDisplayedNameToGrade.put("ohne Erfolg teilgenommen",
                "ohne Erfolg teilgenommen");
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
        ArrayList<String> canteens = new ArrayList<String>();
        canteens.addAll(canteenList.keySet());
        return canteens;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.CanteenInterface#readCanteen
     * (java.lang.String)
     */
    @Override
    public final Canteen getCanteen(final String canteen) throws IOException {
        CanteenService canteenService = new CanteenServiceCaching(
                new CanteenDAOWebRSS(), new CanteenDAOCache());
        Canteen output = canteenService.read(canteen, canteenList.get(canteen));

        return output;
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
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        RoomService service = new RoomServiceStd(new RoomDAOMuKServer());
        ArrayList<Room> output = service.searchRooms(this, input);
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return output;
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
        Log.i(TAG, "Methode: searchRooms wird gestartet.");
        RoomService service = new RoomServiceStd(new RoomDAOMuKServer());
        ArrayList<Room> output = service.searchRooms(this, input);
        Log.i(TAG, "Methode: searchRooms wird verlassen.");
        return output;
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
        Log.i(TAG, "Methode: searchProfessor wird gestartet.");
        ProfessorService service = new ProfessorServiceStd(
                new ProfessorDAOWebTUWien(this));
        ArrayList<Professor> output = service.searchProfessor(input);
        Log.i(TAG, "Methode: searchProfessor wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface#getDetails
     * (at.mukprojects.vuniapp.models.Professor)
     */
    @Override
    public final Professor getProfessorDetails(final Professor professor)
            throws IOException {
        Log.i(TAG, "Methode: getDetails wird gestartet.");
        ProfessorService service = new ProfessorServiceStd(
                new ProfessorDAOWebTUWien(this));
        Professor output = service.getDetails(professor);
        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#
     * getSubjectKeys()
     */
    @Override
    public final ArrayList<String> getSubjectKeys() {
        Log.i(TAG, "Methode: getSubjectKeys wird gestartet.");
        ArrayList<String> subjectKeys = new ArrayList<String>();
        subjectKeys.add(UKZID);
        Log.i(TAG, "Methode: getSubjectKeys wird verlassen.");
        return subjectKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#readSubjects
     * ()
     */
    @Override
    public final ArrayList<Subject> getSubjects(
            final HashMap<String, UniversityUser> users, final Context context)
            throws IOException, InvalidLoginException, ConnectionException {
        Log.i(TAG, "Methode: getSubjects wird gestartet.");
        UniversityUser user = users.get(UKZID);
        SubjectService service = new SubjectServiceCachingFiltered(
                new SubjectDAOWebTISS(user, this), new SubjectDAOCache(KEY),
                new SubjectDAOFilterDB(context));
        ArrayList<Subject> subjects = service.readSubjects();

        // TODO K: Sollte in die Service Schicht runterwandern
        service = new SubjectServiceFiltered(new SubjectDAOAndroidDB(context),
                new SubjectDAOFilterDB(context));
        ArrayList<Subject> additionalSubjects = service.readSubjects();
        for (Subject as : additionalSubjects) {
            if (getKeyName().equals(as.getUniversity().getKeyName())) {
                boolean isNew = true;
                for (Subject s : subjects) {
                    if (s.getId().equals(as.getId())) {
                        isNew = false;
                        s.setName(as.getName());
                        s.setLvanr(as.getLvanr());
                        s.setType(as.getType());
                        s.setSemester(as.getSemester());
                        s.setEcts(as.getEcts());
                        s.setSemester(as.getSemester());
                    }
                }
                if (isNew) {
                    subjects.add(as);
                }
            }
        }

        Log.i(TAG, "Methode: getSubjects wird verlassen.");
        return subjects;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.SubjectInterface#readDetails
     * (at.mukprojects.vuniapp.models.Subject)
     */
    @Override
    public final Subject getSubjectDetails(
            final HashMap<String, UniversityUser> users, final Subject subject,
            final Context context) throws IOException, InvalidLoginException,
            ConnectionException {
        Subject newSubject = subject;
        if (subject.getLink() != null) {
            UniversityUser user = users.get(UKZID);
            SubjectService service = new SubjectServiceCachingFiltered(
                    new SubjectDAOWebTISS(user, this),
                    new SubjectDAOCache(KEY), new SubjectDAOFilterDB(context));
            newSubject = service.readDetails(subject);
        }
        return newSubject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getCertificateKeys()
     */
    @Override
    public final ArrayList<String> getCertificateKeys() {
        Log.i(TAG, "Methode: getCertificateKeys wird gestartet.");
        ArrayList<String> certificateKeys = new ArrayList<String>();
        certificateKeys.add(UKZID);
        Log.i(TAG, "Methode: getCertificateKeys wird verlassen.");
        return certificateKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * readCertificates()
     */
    @Override
    public final ArrayList<Certificate> getCertificates(
            final HashMap<String, UniversityUser> users) throws IOException,
            InvalidLoginException {
        UniversityUser user = users.get(UKZID);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        CertificateService service = new CertificateServiceCaching(
                new CertificateDAOWebTISS(user, this), new CertificateDAOCache(
                        KEY));
        ArrayList<Certificate> certificates = service.readCertificates();
        return certificates;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.LoginInterface#checkLoginData
     * (at.mukprojects.vuniapp.models.User)
     */
    @Override
    public final boolean checkLoginData(final UniversityUser user)
            throws IOException {
        DAOWebTISS dwt = new DAOWebTISS(user, this);
        return dwt.validLogin();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.universities.interfaces.UserInterface#getUserKeys
     * ()
     */
    @Override
    public final HashMap<String, String> getUserKeys() {
        Log.i(TAG, "Methode: getUserKeys wird gestartet.");
        HashMap<String, String> userKeys = new HashMap<String, String>();
        userKeys.putAll(this.userKeys);
        Log.i(TAG, "Methode: getUserKeys wird verlassen.");
        return userKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.DatesInterface#
     * getUniversityDates()
     */
    @Override
    public final HtmlData getUniversityDates() throws IOException {
        Log.i(TAG, "Methode: getUniversityDates wird gestartet.");
        String datesUrl = "http://www.tuwien.ac.at/dle/studienabteilung/akademischer_kalender";

        HtmlDataService service = new HtmlDataServiceCaching(
                new HtmlDataDAOWebTUWien(), new HtmlDataDAOCache());
        HtmlData universityDates = service.readHtmlData(datesUrl);
        Log.i(TAG, "Methode: getUniversityDates wird verlassen.");
        return universityDates;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getValueToGrade(java.lang.String)
     */
    @Override
    public final Integer getValueToGrade(final String grade) {
        return mapValueToGrade.get(grade);
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.universities.interfaces.CertificateInterface#
     * getDisplayedNameToGrade(java.lang.String)
     */
    @Override
    public final String getDisplayedNameToGrade(final String grade) {
        if (mapDisplayedNameToGrade.get(grade) != null) {
            return mapDisplayedNameToGrade.get(grade);
        }
        return grade;
    }
}
