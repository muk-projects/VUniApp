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

package at.mukprojects.vuniapp.services;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import at.mukprojects.vuniapp.dao.SubjectDAO;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.models.Subject;

/**
 * Caching unterst&uuml;tzende Implementierung des SubjectService.
 * 
 * @author kerrim
 */
public class SubjectServiceCaching implements SubjectService {
    private static final String TAG = SubjectServiceCaching.class.getSimpleName();

    private SubjectDAO          subjectDAO;
    private SubjectDAO          cache;

    /**
     * Erzeugt einen neuen Subject Service welches Caching unterst&uuml;tzt.
     * 
     * @param subjectDAO
     *            SubjectDAO aus der die Daten gelesen werden sollen.
     * @param cache
     *            SubjectDAO in welche gecacht wird.
     */
    public SubjectServiceCaching(final SubjectDAO subjectDAO,
            final SubjectDAO cache) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.subjectDAO = subjectDAO;
        this.cache = cache;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.SubjectService#readSubjects()
     */
    @Override
    public final ArrayList<Subject> readSubjects() throws IOException, InvalidLoginException {
        Log.i(TAG, "Methode: read wird gestartet.");
        ArrayList<Subject> output = null;

        if (cache.readSubjects() != null) {
            output = cache.readSubjects();
        } else {
            output = subjectDAO.readSubjects();
            cache.writeSubjects(output);
        }

        Log.i(TAG, "Methode: read wird verlassen.");
        return output;
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.services.SubjectService#readDetails(at.mukprojects.vuniapp.models.Subject)
     */
    @Override
    public final Subject readDetails(final Subject subject) throws IOException, InvalidLoginException {
        Log.i(TAG, "Methode: readDetails wird gestartet.");
        Subject output;
        if (cache.readSubjects() != null && cache.readSubjects().contains(subject) && subject.isContainsDetails()) {
            output = subject;
        } else {
            output = subjectDAO.readDetails(subject);
        }
        Log.i(TAG, "Methode: readDetails wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.SubjectService#writeSubjects(java.util
     * .ArrayList)
     */
    @Override
    public final void writeSubjects(final ArrayList<Subject> subjects) throws IOException {
        Log.i(TAG, "Methode: writeSubjects wird gestartet.");
        subjectDAO.writeSubjects(subjects);
        Log.i(TAG, "Methode: writeSubjects wird verlassen.");
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.services.SubjectService#removeSubjects(java.util.ArrayList)
     */
    @Override
    public final void removeSubjects(final ArrayList<Subject> subjects) throws IOException {
        Log.i(TAG, "Methode: removeSubjects wird gestartet.");
        subjectDAO.removeSubjects(subjects);
        Log.i(TAG, "Methode: removeSubjects wird verlassen.");
        
    }

}
