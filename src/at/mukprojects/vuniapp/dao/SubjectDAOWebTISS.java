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
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWebTISS;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.helper.EventHandlingHelper;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der SubjectDAO f&uuml;r <a
 * href="https://tiss.tuwien.ac.at/">TISS</a> der TU Wien.
 * 
 * @author kerrim
 */
public class SubjectDAOWebTISS extends DAOWebTISS implements SubjectDAO {
    private static final String           TAG          = SubjectDAOWebTISS.class
                                                               .getSimpleName();

    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat(
                                                               "dd.MM.yyyy HH:mm",
                                                               Locale.getDefault());
    private static final SimpleDateFormat SDF_DATE     = new SimpleDateFormat(
                                                               "dd.MM.yyyy",
                                                               Locale.getDefault());

    /**
     * Erstellt einen neues TISS SubjectDAO.
     * 
     * @param user
     *            Benutzer des TISS Accounts.
     * @param universityKey
     *            Universit&auml;tskey zum Eindeutigen identifizieren der
     *            Universit&auml;t
     */
    public SubjectDAOWebTISS(final UniversityUser user,
            final University university) {
        super(user, university);
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        SDF_DATETIME.setTimeZone(TimeZone.getTimeZone("CET"));
        SDF_DATE.setTimeZone(TimeZone.getTimeZone("CET"));
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

        ArrayList<Subject> output = new ArrayList<Subject>();
        try {
            HttpGet httpget = new HttpGet(
                    "https://tiss.tuwien.ac.at/education/favorites.xhtml");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            Pattern lineWithSubjectName = Pattern
                    .compile(".*class=\"favoritesTitleCol\".*");
            Pattern subjectTaken = Pattern.compile(".*title=\"angemeldet\".*");

            String line = null;

            while ((line = br.readLine()) != null) {

                if (lineWithSubjectName.matcher(line).matches()) {
                    br.readLine();
                    br.readLine();
                    line += br.readLine();
                    line += br.readLine();

                    String[] content = extractContentFromHTML(line);

                    output.add(new Subject(super.getUniversity(),
                            content[0], subjectTaken.matcher(line).matches(),
                            "https://tiss.tuwien.ac.at/course/educationDetails.xhtml?semester="
                                    + content[4] + "&courseNr="
                                    + content[2].substring(0, 3)
                                    + content[2].substring(4), content[2],
                            content[3].substring(2, 4), content[4], Float
                                    .parseFloat(content[7]), Float
                                    .parseFloat(content[6])));
                }

            }
        } catch (IOException e) {
            throw new IOException();
        }

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

        login();

        try {
            Log.d(TAG, "Informationen werden geholt: " + subject.getLink());
            HttpGet httpget = new HttpGet(subject.getLink());
            Log.d(TAG, "Anfrage wurde erstellt und wird jetzt durchgeführt.");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);
            Log.d(TAG, "Daten erfolgreich vom Internet geholt.");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            Pattern startProfs = Pattern.compile("<h2>Vortragende</h2>");
            Pattern startGoals = Pattern
                    .compile("<h2>Ziele der Lehrveranstaltung</h2>");
            Pattern startContent = Pattern
                    .compile("<h2>Inhalt der Lehrveranstaltung</h2>");
            Pattern startExams = Pattern.compile(".*<h2>Prüfungen</h2>");
            Pattern startCourses = Pattern.compile(".*<h2>LVA Termine</h2>");

            String line;
            while ((line = br.readLine()) != null) {

                if (startProfs.matcher(line).matches()) {
                    Log.d(TAG, "Liste mit Vortragenden gefunden.");
                    Pattern endProfs = Pattern.compile(".*</ul>");
                    Pattern prof = Pattern.compile(".*<li>.*");
                    while ((line = br.readLine()) != null) {
                        if (endProfs.matcher(line).matches()) {
                            break;
                        } else if (prof.matcher(line).matches()) {
                            if (subject.getProfessors() == null) {
                                subject.setProfessors(new ArrayList<Professor>());
                            }
                            String url = line.substring(line.indexOf("http"),
                                    line.indexOf("\" styleClass"));
                            Professor professor = new Professor(
                                    extractContentFromHTML(line)[0],
                                    super.getUniversity(),
                                    url);
                            subject.getProfessors().add(professor);
                            Log.d(TAG,
                                    "Gefundener Professor: "
                                            + professor.getName() + " ("
                                            + professor.getUrlToDetails() + ")");
                        }
                    }
                } else if (startGoals.matcher(line).matches()) {
                    Log.d(TAG, "Ziele der Lehrveranstaltung gefunden.");
                    Pattern endGoals = Pattern.compile("</div>");
                    String goals = "";
                    while ((line = br.readLine()) != null) {
                        goals += line;
                        if (endGoals.matcher(line).matches()) {
                            break;
                        }
                    }
                    subject.setGoals(goals);
                    Log.d(TAG, "Ziele: " + goals);
                } else if (startContent.matcher(line).matches()) {
                    Log.d(TAG, "Inhalt der Lehrveranstaltung gefunden.");
                    Pattern endContent = Pattern.compile("</div>");
                    String content = "";
                    while ((line = br.readLine()) != null) {
                        content += line;
                        if (endContent.matcher(line).matches()) {
                            break;
                        }
                    }
                    subject.setContent(content);
                    Log.d(TAG, "Inhalt: " + content);
                } else if (startExams.matcher(line).matches()) {
                    subject.setExams(getExamAppointments(subject, br));
                } else if (startCourses.matcher(line).matches()) {
                    subject.setAppointments(getCourseAppointments(subject, br));
                }

            }
            subject.setContainsDetails(true);

        } catch (IOException e) {
            throw new IOException();
        }

        Log.i(TAG, "Methode: readDetails wird verlassen.");
        return subject;
    }

    /**
     * Liest die Pr&uuml;fungstermine aus der TISS Seite einer LVA aus.
     * 
     * @param subject
     *            Fach welches gerade ausgelesen werden soll.
     * @param bufferedReader
     *            BufferedReader der den HTML Code durchgeht.
     * @return ArrayList mit den Pr&uuml;fungsterminen.
     * @throws IOException
     *             Wird geworfen falls beim Lesen der Daten ein Fehler auftrat.
     */
    private ArrayList<Event> getExamAppointments(final Subject subject,
            final BufferedReader bufferedReader) throws IOException {

        Log.d(TAG, "Prüfungen der Lehrveranstaltung gefunden.");
        Pattern endExams = Pattern.compile(".*</table>.*");
        String dateData = "";
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            dateData += line;
            if (endExams.matcher(line).matches()) {
                break;
            }
        }

        ArrayList<ArrayList<String>> exams = super
                .extractStringArrayListFromHTMLTable(dateData);
        ArrayList<Event> events = new ArrayList<Event>();

        for (int i = 2; i < exams.size(); i++) {
            for (int j = 0; j < exams.get(i).size(); j++) {
                Log.v(TAG, "Exam Row " + i + " Col " + j + ": "
                        + exams.get(i).get(j));
            }
            String name = subject.getName() + " Prüfung";
            String description = "Anmerkung: " + exams.get(i).get(7)
                    + "\nAnmeldezeitraum: "
                    + super.extractContentFromHTML(exams.get(i).get(5))[0];
            Date date = null;
            long duration = 0;
            if (exams.get(i).get(1).length() != 0
                    && exams.get(i).get(1).length() < 13) {
                try {
                    date = SDF_DATETIME.parse(exams.get(i).get(2)
                            .substring(0, 10)
                            + " " + exams.get(i).get(1).substring(0, 5));
                    Date endDate = SDF_DATETIME.parse(exams.get(i).get(2)
                            .substring(0, 10)
                            + " " + exams.get(i).get(1).substring(8, 13));

                    duration = endDate.getTime() - date.getTime();
                } catch (ParseException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                } catch (NullPointerException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                }
            } else {
                try {
                    date = SDF_DATE.parse(exams.get(i).get(2));
                    duration = -1;
                } catch (ParseException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                } catch (NullPointerException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                }
            }
            String place = super.extractContentFromHTML(exams.get(i).get(3))[0];

            Event newEvent;
            if (duration == -1) {
                // wholeDay Event erzeugen, da keine Zeitangabe auf
                // der Seite steht.
                newEvent = new Event(name, description, date, place, true,
                        super.getUniversity(), null);
            } else {
                newEvent = new Event(name, description, date, duration, place,
                        super.getUniversity());
            }
            events.add(newEvent);

        }
        for (Event e : events) {
            Log.d(TAG, "Event: " + e);
        }
        return events;
    }

    /**
     * Liest die Lehrveranstaltungstermine aus der TISS Seite einer LVA aus.
     * 
     * @param subject
     *            Fach welches gerade ausgelesen werden soll.
     * @param bufferedReader
     *            BufferedReader der den HTML Code durchgeht.
     * @return ArrayList mit den LVA Terminen.
     * @throws IOException
     *             Wird geworfen falls beim Lesen der Daten ein Fehler auftrat.
     */
    private ArrayList<Event> getCourseAppointments(final Subject subject,
            final BufferedReader bufferedReader) throws IOException {
        Log.d(TAG, "Kurs Termine der Lehrveranstaltung gefunden.");
        Pattern endCourses = Pattern.compile(".*</table>.*");
        String coursesData = "";
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            coursesData += line;
            if (endCourses.matcher(line).matches()) {
                break;
            }
        }

        ArrayList<ArrayList<String>> courses = super
                .extractStringArrayListFromHTMLTable(coursesData);
        ArrayList<Event> events = new ArrayList<Event>();

        for (int i = 2; i < courses.size(); i++) {
            for (int j = 0; j < courses.get(i).size(); j++) {
                Log.v(TAG, "Courses Row " + i + " Col " + j + ": "
                        + courses.get(i).get(j));
            }

            String name = subject.getName();
            String description = "Anmerkung: " + courses.get(i).get(4);
            String location = super.extractContentFromHTML(courses.get(i)
                    .get(3))[0];
            Date firstDate = null;
            long duration = 0;
            try {
                firstDate = SDF_DATETIME.parse(courses.get(i).get(2)
                        .substring(0, 10)
                        + " " + courses.get(i).get(1).substring(0, 5));
                Date firstDateEnd = SDF_DATETIME.parse(courses.get(i).get(2)
                        .substring(0, 10)
                        + " " + courses.get(i).get(1).substring(8, 13));
                duration = firstDateEnd.getTime() - firstDate.getTime();
            } catch (ParseException e) {
                Log.e(TAG,
                        "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
            } catch (NullPointerException e) {
                Log.e(TAG,
                        "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
            }

            if (courses.get(i).get(2).charAt(11) == '-') {
                Date lastDate = null;
                try {
                    lastDate = SDF_DATETIME.parse(courses.get(i).get(2)
                            .substring(13, 23)
                            + " " + courses.get(i).get(1).substring(8, 13));
                } catch (ParseException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                } catch (NullPointerException e) {
                    Log.e(TAG,
                            "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
                }
                if (lastDate != null) {
                    events.addAll(EventHandlingHelper.generateWeeklyEvents(
                            firstDate, duration, lastDate, name, location,
                            description, super.getUniversity()));
                } else {
                    events.add(new Event(name, description, firstDate,
                            duration, location, super.getUniversity(), null));
                }
            } else {
                events.add(new Event(name, description, firstDate, duration,
                        location, super.getUniversity(), null));
            }
        }
        for (Event e : events) {
            Log.d(TAG, "Event: " + e);
        }
        return events;
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(ArrayList<Subject> subjects) throws IOException {
        throw new UnsupportedOperationException();
    }
}
