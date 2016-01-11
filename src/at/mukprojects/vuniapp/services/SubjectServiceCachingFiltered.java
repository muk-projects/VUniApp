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
 * TODO K: Beschreibung
 * 
 * @author kerrim
 */
public class SubjectServiceCachingFiltered implements SubjectService {
    private static final String TAG = SubjectServiceCachingFiltered.class
                                            .getSimpleName();

    private SubjectService      subjectService;
    private SubjectDAO          filter;

    /**
     * Erzeugt einen neuen Subject Service welches Caching und einen Filter
     * unterst&uuml;tzt.
     * 
     * @param subjectDAO
     *            SubjectDAO aus der die Daten gelesen werden sollen.
     * @param cache
     *            SubjectDAO in welche gecacht wird.
     * @param filter
     *            SubjectDAO welche die F&auml;cher
     */
    public SubjectServiceCachingFiltered(final SubjectDAO subjectDAO,
            final SubjectDAO cache, final SubjectDAO filter) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        subjectService = new SubjectServiceCaching(subjectDAO, cache);
        this.filter = filter;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.SubjectService#readSubjects()
     */
    @Override
    public final ArrayList<Subject> readSubjects() throws IOException,
            InvalidLoginException {
        Log.i(TAG, "Methode: readSubjects wird gestartet.");
        ArrayList<Subject> temp = subjectService.readSubjects();
        ArrayList<Subject> output = new ArrayList<Subject>();
        ArrayList<Subject> toFilter = filter.readSubjects();

        for (Subject subject : temp) {
            boolean shouldBeFiltered = false;
            for (Subject subjectToFilter : toFilter) {
                if (subjectToFilter.getId().equals(subject.getId())) {
                    shouldBeFiltered = true;
                }
            }
            if (!shouldBeFiltered) {
                output.add(subject);
            }
        }

        Log.i(TAG, "Methode: readSubjects wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.SubjectService#readDetails(at.mukprojects
     * .vuniapp.models.Subject)
     */
    @Override
    public final Subject readDetails(final Subject subject) throws IOException,
            InvalidLoginException {
        return subjectService.readDetails(subject);
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
        subjectService.writeSubjects(subjects);
        Log.i(TAG, "Methode: writeSubjects wird verlassen.");
    }

    /* (non-Javadoc)
     * @see at.mukprojects.vuniapp.services.SubjectService#removeSubjects(java.util.ArrayList)
     */
    @Override
    public final void removeSubjects(final ArrayList<Subject> subjects) throws IOException {
        Log.i(TAG, "Methode: removeSubjects wird gestartet.");
        subjectService.removeSubjects(subjects);
        Log.i(TAG, "Methode: removeSubjects wird verlassen.");
        
    }

}
