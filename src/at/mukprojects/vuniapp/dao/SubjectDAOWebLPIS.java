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
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;
import at.mukprojects.vuniapp.dao.base.DAOWebLPIS;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;

/**
 * Implementierung der SubjectDAO f&uuml;r LPIS der WU Wien.
 * 
 * @author kerrim
 */
public class SubjectDAOWebLPIS extends DAOWebLPIS implements SubjectDAO {
    private static final String           TAG          = SubjectDAOWebLPIS.class
                                                               .getSimpleName();

    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat(
                                                               "dd.MM.yyyy HH:mm",
                                                               Locale.getDefault());

    /**
     * Erstellt einen neues LPIS SubjectDAO.
     * 
     * @param user
     *            Benutzer des LPIS Accounts.
     * @param university
     *            Universit&auml;t
     */
    public SubjectDAOWebLPIS(final UniversityUser user,
            final University university) {
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

        ArrayList<Subject> output = new ArrayList<Subject>();

        try {
            HttpGet httpget = new HttpGet(super.getLpisLink() + "AL");
            HttpResponse response = getSharedInternetConnection().execute(
                    httpget);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "iso-8859-1"));

            Pattern startOfSubjectTable = Pattern
                    .compile(".*table class=\"b3k-data\".*");

            String line = null;

            while ((line = br.readLine()) != null) {
                if (startOfSubjectTable.matcher(line).matches()) {
                    Pattern endOfSubjectTable = Pattern.compile(".*</table>");
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        line += temp;
                        if (endOfSubjectTable.matcher(line).matches()) {
                            break;
                        }
                    }
                    output = generateSubjectsOutOfHTMLTable(line);
                    break;
                }

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

    /**
     * Generiert aus der HTML Tabelle aus LPIS eine ArrayList aller F&auml;cher.
     * 
     * @param input
     *            HTML Tabelle aus LPIS mit F&auml;chern.
     * @return Eine ArrayList mit allen gefundenen F&auml;chern.
     */
    private ArrayList<Subject> generateSubjectsOutOfHTMLTable(final String input) {
        ArrayList<Subject> output = new ArrayList<Subject>();
        ArrayList<ArrayList<String>> content = super
                .extractStringArrayListFromHTMLTable(input);

        for (int i = 2; i < content.size(); i++) {
            for (int j = 0; j < content.get(i).size(); j++) {
                Log.v(TAG, "Zeile " + i + " Spalte " + j + ": "
                        + content.get(i).get(j));
            }

            String name = super.extractContentFromHTML(content.get(i).get(2))[3];
            try {
                String professorNames = super.extractContentFromHTML(content
                        .get(i).get(2))[0];
                String lvanr = super.extractContentFromHTML(content.get(i).get(
                        0))[0];
                boolean taken = content.get(i).get(4).contains("<a href");
                String link = content.get(i).get(0);
                link = link.substring(link.indexOf("http"));
                link = link.substring(0, link.indexOf('\"'));
                String type = super.extractContentFromHTML(content.get(i)
                        .get(1))[1];
                String semester = super.extractContentFromHTML(content.get(i)
                        .get(0))[2];
                float hours = Float.parseFloat(super
                        .extractContentFromHTML(content.get(i).get(1))[4]);
                float ects = hours * 2F;
                Subject temp = new Subject(super.getUniversity(), name,
                        taken, link, lvanr, type, semester, ects, hours);

                temp.setProfessors(extractProfessorsFromString(professorNames));

                Log.v(TAG, temp.toString());

                output.add(temp);
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "Fach (" + name + ") konnte nicht gelesen werden.");
            }

        }

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
                    response.getEntity().getContent(), "iso-8859-1"));

            Pattern startGoals = Pattern
                    .compile(".*<b>Lernergebnisse (Learning Outcomes):</b>.*");
            Pattern startContent = Pattern
                    .compile(".*<b>Inhalte der LV:</b>.*");
            Pattern startCourses = Pattern
                    .compile(".*<td class=\"thdb\" colspan=\"4\">Termine</td>.*");

            String line;
            String data = "";
            while ((line = br.readLine()) != null) {
                data += line;
            }

            String[] tables = super.extractDataBetweenStartAndEndString(data,
                    "<table", "</table>");
            for (String table : tables) {
                if (startCourses.matcher(table).matches()) {
                    Log.d(TAG, "Tabelle mit LVA Terminen gefunden.");
                    ArrayList<Event> events = extractCoursesFromLPISTable(
                            subject, table);
                    subject.setAppointments(events);
                } else if (startContent.matcher(table).matches()
                        || startGoals.matcher(table).matches()) {
                    Log.d(TAG, "Tabelle mit LVA Details gefunden.");
                    Log.v(TAG, table);
                    HashMap<String, String> content = extractContentFromLPISTable(table);
                    subject.setContent(content.get("content"));
                    subject.setGoals(content.get("goals"));
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
     * Extrahiert die LVA Termine aus einer LPIS Tabelle welche in der
     * Veranstaltungsdetailsseite angezeigt wird.
     * 
     * @param subject
     *            Das Fach f&uuml;r welches die Termine extrahiert werden.
     * @param input
     *            LPIS Tabelle aus die extrahiert werden soll.
     * @return Liste mit den gefundenen Terminen.
     */
    private ArrayList<Event> extractCoursesFromLPISTable(final Subject subject,
            final String input) {
        ArrayList<Event> output = new ArrayList<Event>();
        String[] rows = extractDataBetweenStartAndEndString(input, "<tr",
                "</tr>");
        for (int i = 1; i < (rows.length - 1); i++) {
            Log.v(TAG, "Termineintrag: " + rows[i]);

            String[] data = super.extractContentFromHTML(rows[i]);

            String name = subject.getName();
            String description = "";
            Date startDate = null;
            long duration = 0;
            try {
                startDate = SDF_DATETIME.parse(data[1] + " "
                        + data[2].substring(0, 5));
                Date endDate = null;
                endDate = SDF_DATETIME.parse(data[1] + " "
                        + data[2].substring(6, 11));
                duration = endDate.getTime() - startDate.getTime();
            } catch (ParseException e) {
                Log.e(TAG,
                        "Es ist ein Fehler beim Parsen der Daten aufgetreten.");
            }
            String room = data[3];
            String placeUrl = super.extractDataBetweenStartAndEndString(
                    rows[i], "http://", "\"")[0];
            placeUrl.subSequence(0, placeUrl.length() - 1);

            Event event = new Event(name, description, startDate, duration,
                    room, super.getUniversity(), placeUrl);
            Log.d(TAG, "LVA Termin: " + event);

            output.add(event);

        }
        return output;
    }

    /**
     * Extrahiert aus einer LPIS Tabelle der Veranstaltungsdetailsseite
     * gefundene Informationen und legt sie in eine HashMap. Diese hat die Form
     * <"Informationsname", "Information">. Zur Zeit gibt es die
     * Informationsnamen "content" und "goals".
     * 
     * @param input
     *            LPIS Tabelle aus die extrahiert werden soll.
     * @return HashMap mit den Informationsnamen und den zugeh&ouml;rigen
     *         Informationen.
     */
    private HashMap<String, String> extractContentFromLPISTable(
            final String input) {
        HashMap<String, String> output = new HashMap<String, String>();
        String[] rows = extractDataBetweenStartAndEndString(input, "<tr",
                "</tr>");
        Pattern startGoals = Pattern.compile(".*<b>Lernergebnisse.*</b>.*");
        Pattern startContent = Pattern.compile(".*<b>Inhalte der LV:</b>.*");
        for (int i = 0; i < rows.length; i++) {
            Log.v(TAG, "Row: " + rows[i]);
            if (startGoals.matcher(rows[i]).matches()) {
                output.put(
                        "goals",
                        extractDataBetweenStartAndEndString(rows[++i], "<td",
                                "</td>")[0]);
            } else if (startContent.matcher(rows[i]).matches()) {
                output.put(
                        "content",
                        extractDataBetweenStartAndEndString(rows[++i], "<td",
                                "</td>")[0]);

            }
        }
        return output;
    }

    /**
     * Diese Methode extrahiert Professoren aus einem &uuml;bergebenen String.
     * 
     * @param input
     *            String welcher genutzt werden soll. Professoren m&uuml;ssen
     *            mit ", " getrennt werden.
     * @return Liste der gefundenen Professoren.
     */
    private ArrayList<Professor> extractProfessorsFromString(final String input) {
        ArrayList<Professor> professors = new ArrayList<Professor>();
        String professorNames = input;
        while (professorNames.contains(", ")) {
            professors.add(new Professor(professorNames.substring(0,
                    professorNames.indexOf(", ")), super.getUniversity(),
                    null));
            professorNames = professorNames.substring(professorNames
                    .indexOf(", ") + 2);
        }
        professors.add(new Professor(professorNames, super.getUniversity(),
                null));
        return professors;
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.dao.SubjectDAO#removeSubjects(java.util.ArrayList)
     */
    @Override
    public void removeSubjects(ArrayList<Subject> subjects) throws IOException {
        throw new UnsupportedOperationException();
    }
}
