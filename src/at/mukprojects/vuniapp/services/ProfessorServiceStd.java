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
import at.mukprojects.vuniapp.dao.ProfessorDAO;
import at.mukprojects.vuniapp.models.Professor;

/**
 * Standardimplementierung des ProfessorService Interfaces.
 * 
 * @author kerrim
 */
public class ProfessorServiceStd implements ProfessorService {
    private static final String         TAG = ProfessorServiceStd.class.getSimpleName();
    private ProfessorDAO professorDAO;

    /**
     * Erzeugt eine Standard ProfessorService Implementierung.
     * 
     * @param professorDAO
     *            Zu nutzende DAO.
     */
    public ProfessorServiceStd(final ProfessorDAO professorDAO) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.professorDAO = professorDAO;
        Log.i(TAG, "Methode: Konstruktor wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.ProfessorService#searchProfessor(java
     * .lang.String)
     */
    @Override
    public final ArrayList<Professor> searchProfessor(final String input)
            throws IOException {
        Log.i(TAG, "Methode: searchProfessor wird gestartet.");
        ArrayList<Professor> output = professorDAO.searchProfessor(input);
        Log.i(TAG, "Methode: searchProfessor wird verlassen.");
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.ProfessorService#getDetails(at.mukprojects
     * .vuniapp.models.Professor)
     */
    @Override
    public final Professor getDetails(final Professor professor)
            throws IOException {
        Log.i(TAG, "Methode: getDetails wird gestartet.");
        Professor output = professorDAO.getDetails(professor);
        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return output;
    }

}
