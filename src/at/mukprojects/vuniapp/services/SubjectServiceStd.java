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
 * Standardimplementierung des SubjectServices.
 * 
 * @author kerrim
 */
public class SubjectServiceStd implements SubjectService {
    private static final String TAG = SubjectServiceStd.class.getSimpleName();

    private SubjectDAO          subjectDAO;

    /**
     * Erzeugt einen neue Standard SubjectService.
     * 
     * @param subjectDAO
     *            DAO die zum Holen der F&auml;cher genutzt werden soll.
     */
    public SubjectServiceStd(final SubjectDAO subjectDAO) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.subjectDAO = subjectDAO;
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
        ArrayList<Subject> output = subjectDAO.readSubjects();
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
        Log.i(TAG, "Methode: readDetails wird gestartet.");
        Subject output = subjectDAO.readDetails(subject);
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
