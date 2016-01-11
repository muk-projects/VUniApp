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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWebMOODLE;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.helper.EventHandlingHelper;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der SubjectDAO f&uuml;r Moodle der TU Wien.
 * 
 * @author kerrim
 */
public class SubjectDAOWebMOODLE extends DAOWebMOODLE implements SubjectDAO {
    private static final String           TAG          = SubjectDAOWebMOODLE.class
                                                               .getSimpleName();

    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat(
                                                               "dd.MM.yyyy HH.mm",
                                                               Locale.getDefault());

    /**
     * Erstellt einen neues Moodle SubjectDAO.
     * 
     * @param user
     *            Benutzer des Moodle Accounts.
     * @param universityKey
     *            Universit&auml;t
     */
    public SubjectDAOWebMOODLE(final UniversityUser user, final University university) {
        super(user, university);
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#readSubjects()
     */
    @Override
    public final ArrayList<Subject> readSubjects() throws IOException,
            InvalidLoginException {
        Log.i(TAG, "Methode: readSubjects wird gestartet.");

        login();

        Log.d(TAG, "Login sollte erfolgreich beendet sein.");

        ArrayList<Subject> output = new ArrayList<Subject>();

        try {
            HttpGet httpget = new HttpGet("https://moodle.univie.ac.at/my/");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "utf-8"));

            String line = null;
            String data = "";

            while ((line = br.readLine()) != null) {
                data += line + "\n";
            }

            String[] courses = extractDataBetweenStartAndEndString(data,
                    "<a title=", "</a>");

            Log.d(TAG, "Rohdaten der gefundenen Fächer:");
            for (String course : courses) {
                String name = extractContentFromHTML(course)[0];
                Log.d(TAG, "name=" + name);
                String semester = null;
                String link = null;
                String lvanr = null;
                if (name.indexOf("S") == 4 || name.indexOf("W") == 4) {
                    semester = name.substring(0, 5);
                    name = name.substring(6);

                    if (name.indexOf("-") == 6) {
                        lvanr = name.substring(0, 6);
                        link = "http://online.univie.ac.at/vlvz?lvnr=" + lvanr
                                + "&semester=" + semester.charAt(4)
                                + semester.substring(0, 4);
                        name = name.substring(9);
                    }
                }
                Log.d(TAG, "semester=" + semester);
                Log.d(TAG, "lvanr=" + lvanr);
                Log.d(TAG, "name=" + name);
                output.add(new Subject(super.getUniversity(), name,
                        true, link, lvanr, semester));
            }

        } catch (IOException e) {
            throw new IOException();
        }

        Log.d(TAG, "Es wurden " + output.size() + " Fächer ausgelesen.");
        for (Subject s : output) {
            Log.d(TAG, s.toString());
        }

        Log.i(TAG, "Methode: readSubjects wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#writeSubjects(java.util.ArrayList)
     */
    @Override
    public final void writeSubjects(final ArrayList<Subject> subjects) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.dao.SubjectDAO#readDescription(at.mukprojects.
     * vuniapp.models.Subject)
     */
    @Override
    public final Subject readDetails(final Subject subject) throws IOException,
            InvalidLoginException {
        Log.i(TAG, "Methode: readDetails wird gestartet.");
        if (subject.getLink() != null && !subject.isContainsDetails()) {

            HttpGet httpget = new HttpGet(subject.getLink());
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "iso-8859-15"));

            String line = null;
            String data = "";
            boolean subjectData = false;

            Pattern courseDates = Pattern.compile(".*wtl.*von.*bis.*-.*Ort:.*");

            while ((line = br.readLine()) != null) {
                if (line.contains("vlvz_langtitel")) {
                    subjectData = true;
                } else if (line.contains("<div id=\"footer\">")) {
                    subjectData = false;
                }

                if (subjectData) {
                    data += line + "\n";

                    if (courseDates.matcher(line).matches()) {
                        ArrayList<Event> courseEvents = new ArrayList<Event>();

                        Log.v(TAG, "Daten können hier abgelesen werden: "
                                + line);
                        String from = line.substring(line.indexOf("von") + 4,
                                line.indexOf("von") + 14);
                        String till = line.substring(line.indexOf("bis") + 4,
                                line.indexOf("bis") + 14);
                        String startTime = line.substring(
                                line.indexOf("-") - 5, line.indexOf("-"));
                        String endTime = line.substring(line.indexOf("-") + 1,
                                line.indexOf("-") + 6);
                        int endOfLocation = line.indexOf("<br />");
                        if (endOfLocation < 0) {
                            endOfLocation = line.indexOf("</div>");
                        }
                        if (endOfLocation < 0) {
                            endOfLocation = line.length();
                        }

                        String location = line.substring(line.indexOf("Ort: ")
                                + ("Ort: ").length(), endOfLocation);

                        try {
                            Date fromStartTime = SDF_DATETIME.parse(from + " "
                                    + startTime);
                            Date fromEndTime = SDF_DATETIME.parse(from + " "
                                    + endTime);
                            long duration = fromEndTime.getTime()
                                    - fromStartTime.getTime();
                            Date tillEndTime = SDF_DATETIME.parse(till + " "
                                    + endTime);
                            
                            courseEvents.addAll(EventHandlingHelper
                                    .generateWeeklyEvents(fromStartTime,
                                            duration, tillEndTime,
                                            subject.getName(), location, "",
                                            super.getUniversity()));
                        } catch (ParseException e) {
                            Log.e(TAG, "Fehler beim Parsen der Daten.");
                        }

                        subject.setAppointments(courseEvents);
                    }
                }
            }

            subject.setContainsDetails(true);
            subject.setContent(data);
        }
        Log.i(TAG, "Methode: readDetails wird verlassen.");
        return subject;
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(ArrayList<Subject> subjects) throws IOException {
        throw new UnsupportedOperationException();
    }
}
