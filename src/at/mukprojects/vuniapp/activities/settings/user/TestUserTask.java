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

package at.mukprojects.vuniapp.activities.settings.user;

import java.io.IOException;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.universities.interfaces.base.LoginInterface;

/**
 * Die AsyncTask Klasse UserTester stellt eine Methode bereit mit dessen Hilfe
 * Benutzerdaten auf ihre Korrektheit &uuml;berpr&uuml;ft werden k&ouml;nnen.
 * Somit kann der User sicherstellen das die Eingabe korrekt war. Weiters kann
 * der User nach einem postiven Test auch gleich gespeichert werden.
 * 
 * @author Mathias
 */
class TestUserTask extends BaseTask<Void, Boolean> {
    private static final String TAG = TestUserTask.class.getSimpleName();
    private LoginInterface      university;
    private Activity            activity;
    private Button              testButton;
    private ProgressBar         testProgressBar;
    private UniversityUser      user;
    private EditText            username;
    private EditText            password;
    private UserFragment        fragment;

    private boolean             testAndSave;

    /**
     * Erstellt einen neuen UserTester.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param university
     *            Mit Hilfe der University wird die Testmethode bestimmt.
     * @param testButton
     *            Button, welcher den Test ausgel&ouml;st hat.
     * @param testProgressBar
     *            Progress, welcher f&uuml;r die Dauer des Test angezeigt werden
     *            soll.
     * @param user
     *            User, welcher getestet werdem soll.
     * @param username
     *            EditText mit dem Usernamen.
     * @param password
     *            EditText mit dem Passwort.
     * @param testAndSave
     *            Gibt an, ob der User nur getestet oder auch gespeichert werden
     *            soll, falls der Test g&uuml;tig ist.
     * @param fragment
     *            UserFragment, welche den Task aufgerufen hat.
     */
    public TestUserTask(final Activity activity,
            final LoginInterface university, final Button testButton,
            final ProgressBar testProgressBar, final UniversityUser user,
            final EditText username, final EditText password,
            final boolean testAndSave, final UserFragment fragment) {
        super(activity);
        this.university = university;
        this.activity = activity;
        this.testButton = testButton;
        this.testProgressBar = testProgressBar;
        this.user = user;
        this.username = username;
        this.password = password;
        this.testAndSave = testAndSave;
        this.fragment = fragment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Progress- Anzeige wird sichtbar.");
        testButton.setVisibility(Button.INVISIBLE);
        testProgressBar.setVisibility(ProgressBar.VISIBLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(final Void... params) {
        Log.d(TAG, "Benutzerdaten werden getestet.");
        try {
            return university.checkLoginData(user);
        } catch (IOException ioException) {
            super.ioException = ioException;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final Boolean result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            testButton.setVisibility(Button.VISIBLE);
            testProgressBar.setVisibility(ProgressBar.INVISIBLE);
            fragment.setTestParameter(result);

            if (result) {
                Log.d(TAG, "Test hat ein postives Ergebnis geliefert.");
                username.setBackgroundResource(R.color.LightGreen);
                password.setBackgroundResource(R.color.LightGreen);
                if (testAndSave) {
                    SaveUserTask saveUserTask = new SaveUserTask(activity,
                            fragment);
                    saveUserTask.execute(user);
                }
            } else {
                Log.d(TAG, "Test hat ein negatives Ergebnis geliefert.");
                username.setBackgroundResource(R.color.DarkRed);
                password.setBackgroundResource(R.color.DarkRed);
                if (testAndSave) {
                    fragment.testWithSaveHasFailed();
                }
            }
        }
    }
}
