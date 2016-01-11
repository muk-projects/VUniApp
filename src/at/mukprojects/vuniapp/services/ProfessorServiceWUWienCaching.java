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
import at.mukprojects.vuniapp.dao.ProfessorDAOWebWUWien;
import at.mukprojects.vuniapp.dao.ProfessorDAOWebWUWienCache;
import at.mukprojects.vuniapp.helper.TextCompareHelper;
import at.mukprojects.vuniapp.models.Professor;

/**
 * Caching unterst&uuml;tzende Implementierung des SubjectService.
 * 
 * @author Mathias
 */
public class ProfessorServiceWUWienCaching implements ProfessorService {
    private static final String        TAG = ProfessorServiceWUWienCaching.class
                                                   .getSimpleName();

    private ProfessorDAOWebWUWien      professorWUDAO;
    private ProfessorDAOWebWUWienCache cache;

    /**
     * Erzeugt einen neuen Subject Service welches Caching unterst&uuml;tzt.
     * 
     * @param professorWUDAO
     *            ProfessorDAOWebWUWien aus der die Daten gelesen werden sollen.
     * @param cache
     *            ProfessorDAOWebWUWienCache in welche gecacht wird.
     */
    public ProfessorServiceWUWienCaching(
            final ProfessorDAOWebWUWien professorWUDAO,
            final ProfessorDAOWebWUWienCache cache) {
        Log.i(TAG, "Methode: Konstruktor wird gestartet.");
        this.professorWUDAO = professorWUDAO;
        this.cache = cache;
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
        ArrayList<Professor> daoOutput = null;

        if (cache.searchProfessor(input) != null) {
            daoOutput = cache.searchProfessor(input);
        } else {
            daoOutput = professorWUDAO.searchProfessor(input);
            cache.writeProfessor(daoOutput);
        }

        ArrayList<Professor> output = new ArrayList<Professor>();
        if (input.equals("") || input.equals("*")) {
            output = daoOutput;
        } else {
            ArrayList<Professor> category01 = new ArrayList<Professor>();
            ArrayList<Professor> category02 = new ArrayList<Professor>();
            ArrayList<Professor> category03 = new ArrayList<Professor>();
            ArrayList<Professor> category04 = new ArrayList<Professor>();

            String inputLowerCase = input.toLowerCase();
            for (Professor professor : daoOutput) {
                String profNameLowerCase = professor.getName().toLowerCase();
                int distance = TextCompareHelper.getLevenshteinDistance(
                        profNameLowerCase, inputLowerCase);

                if (inputLowerCase.contains(profNameLowerCase)
                        || profNameLowerCase.contains(inputLowerCase)
                        || distance == 0 || distance == 1) {
                    category01.add(professor);
                } else if (distance == 2 || distance == 3) {
                    category02.add(professor);
                } else if (distance == 4 || distance == 5 || distance == 6) {
                    category03.add(professor);
                } else if (distance == 7 || distance == 8 || distance == 9) {
                    category04.add(professor);
                }

                if (!category01.isEmpty()) {
                    output = category01;
                } else if (!category02.isEmpty()) {
                    output = category02;
                } else if (!category03.isEmpty()) {
                    output = category03;
                } else if (!category04.isEmpty()) {
                    output = category04;
                }
            }
        }

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
        Professor output = professorWUDAO.getDetails(professor);
        Log.i(TAG, "Methode: getDetails wird verlassen.");
        return output;
    }

}
