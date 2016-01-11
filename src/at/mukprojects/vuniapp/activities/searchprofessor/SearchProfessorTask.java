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

package at.mukprojects.vuniapp.activities.searchprofessor;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface;

/**
 * Professoren suchen und Listview updaten.
 * 
 * @author Mathias
 * @author kerrim
 */
class SearchProfessorTask extends InternetBaseTask<Void, ArrayList<Professor>> {
    private static final String     TAG = SearchProfessorTask.class
                                                .getSimpleName();

    private SearchProfessorActivity professorActivity;
    private ProfessorInterface      university;
    private String                  input;
    private TextView                activityResultsText;
    private ListView                activityResults;

    /**
     * Erstellt einen neuen Task. Es kann entweder ein input oder ein room
     * übergeben werden.
     * 
     * @param title
     *            Titel des Dialogs.
     * @param message
     *            Message des Dialogs.
     * @param activity
     *            Activity, welche den Task ausf&uuml;hrt.
     * @param input
     *            Input des Tasks.
     * @param university
     *            Universit&auml;t zum ausf&uuml;hren der Raumsuche.
     * @param activityResultsText
     *            Der ResultText.
     * @param activityResults
     *            Der ResultListView.
     */
    public SearchProfessorTask(final String title, final String message,
            final SearchProfessorActivity activity, final String input,
            final ProfessorInterface university,
            final TextView activityResultsText, final ListView activityResults) {
        super(title, message, false, activity);
        this.professorActivity = activity;
        this.university = university;
        this.activityResultsText = activityResultsText;
        this.activityResults = activityResults;
        this.input = input;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.baseclasses.InternetBaseTask#executeBackgroundMethod
     * (I[])
     */
    @Override
    protected ArrayList<Professor> executeBackgroundMethod(final Void... params) {
        ArrayList<Professor> output = new ArrayList<Professor>();

        try {
            output = university.searchProfessor(input);
        } catch (IOException e) {
            super.ioException = e;
        }

        return output;
    }

    @Override
    protected void onPostExecute(final ArrayList<Professor> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            if (result == null || result.isEmpty()) {
                Log.d(TAG, "Kein passendes Element gefunden.");
                activityResultsText.setVisibility(TextView.VISIBLE);
                activityResults.setVisibility(ListView.INVISIBLE);
            } else {
                Log.d(TAG, "Elemente gefunden.");
                activityResultsText.setVisibility(TextView.INVISIBLE);
                activityResults.setVisibility(ListView.VISIBLE);
                professorActivity.showResult(result);
            }
        }
    }
}
