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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.base.LoginInterface;

/**
 * Die Klasse UserFinder sucht alle relevanten Benutzerdaten aus der Datenbank
 * und bef&uult; anschlie&szlig;end die EditText Felder mit den bestehenden
 * Daten.
 * 
 * @author Mathias
 */
class FindUserTask extends BaseTask<Void, ArrayList<UniversityUser>> {
    private static final String TAG = FindUserTask.class.getSimpleName();
    private Activity            activity;
    private ListView            userList;
    private TextView            hint;
    private float               progress;
    private float               currentProgress;

    /**
     * Erstellt einen neuen UserFinder.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param userList
     *            ListView indem die User ausgegeben werden sollen.
     * @param activityUserHint
     *            TextView, welcher angezeigt wird, wenn keine User vorhanden
     *            sind.
     */
    public FindUserTask(final Activity activity, final ListView userList,
            final TextView activityUserHint) {
        super(activity.getString(R.string.activity_user), activity
                .getString(R.string.activity_user_userLoadingDialog_text),
                true, activity);
        this.activity = activity;
        this.userList = userList;
        this.hint = activityUserHint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected ArrayList<UniversityUser> doInBackground(final Void... params) {
        Log.d(TAG, "Benutzerdaten werden aus der Datenbank geladen.");
        ArrayList<UniversityUser> retUserList = new ArrayList<UniversityUser>();
        UniversityUserService service = null;

        try {
            service = new UniversityUserServiceStd(
                    new UniversityUserDAOAndroidDB(activity));
        } catch (ConnectionException connectionException) {
            super.connectionException = connectionException;
        }

        if (service != null) {
            HashMap<University, LoginInterface> universityList = Universities
                    .getInstance().getUniversitiesWithLogin();
            float eventSize = universityList.size();
            progress = 100 / eventSize;
            currentProgress = 0;

            for (University university : universityList.keySet()) {
                Log.d(TAG, university.getName()
                        + " User suchen und hinzufügen.");
                try {
                    for (Entry<String, UniversityUser> user : service
                            .getUsersFromUniversity(university).entrySet()) {
                        retUserList.add(user.getValue());
                    }
                } catch (ReadException readException) {
                    super.readException = readException;
                } catch (NotInitializedException e) {
                    super.notInitializedException = notInitializedException;
                }

                int value = Math.round(currentProgress + progress);
                publishProgress(value);
                currentProgress += progress;
            }
            publishProgress(100);
        }
        return retUserList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final ArrayList<UniversityUser> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            Log.d(TAG, "Benutzerdaten werden in den ListView geladen.");
            if (result.isEmpty()) {
                userList.setVisibility(ListView.INVISIBLE);
                hint.setVisibility(TextView.VISIBLE);
                hint.setText(activity.getString(R.string.activity_user_hint_noUser));
            } else {
                userList.setVisibility(ListView.VISIBLE);
                hint.setVisibility(TextView.INVISIBLE);
                UserListAdapter adapter = new UserListAdapter(activity, result);
                userList.setAdapter(adapter);
            }
        }
    }
}
