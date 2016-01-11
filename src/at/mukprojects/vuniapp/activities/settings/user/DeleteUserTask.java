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

import android.app.Activity;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;

/**
 * Der Task DeleteUserTask l&ouml;scht einen User in die Datenbank.
 * 
 * @author Mathias
 */
class DeleteUserTask extends BaseTask<UniversityUser, UniversityUser> {
    private static final String TAG = SaveUserTask.class.getSimpleName();
    private Activity            activity;
    private UserFragment        fragment;

    /**
     * Erstellt einen DeleteUserTask, um einen bestimmten Benutzer zu
     * l&ouml;schen.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param fragment
     *            UserFragment, welche den Task aufgerufen hat.
     */
    public DeleteUserTask(final Activity activity, final UserFragment fragment) {
        super(activity.getResources().getString(R.string.activity_user),
                activity.getResources().getString(
                        R.string.activity_user_userSaveingDialog_text), false,
                activity);
        this.activity = activity;
        this.fragment = fragment;

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected UniversityUser doInBackground(final UniversityUser... params) {
        UniversityUserService service = null;

        try {
            service = new UniversityUserServiceStd(
                    new UniversityUserDAOAndroidDB(activity));
        } catch (ConnectionException e) {
            super.connectionException = e;
        }

        if (service != null) {
            try {
                UniversityUser deleteUser = new UniversityUser(null, null,
                        params[0].getUniversity(), params[0].getUserKey());
                Log.d(TAG, "Benutzer wird gelöscht.");
                service.deleteUser(deleteUser);
            } catch (ReadException readException) {
                super.readException = readException;
            } catch (DeleteException deleteException) {
                super.deleteException = deleteException;
            } catch (NotInitializedException e) {
                super.notInitializedException = notInitializedException;
            }
        }
        return params[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final UniversityUser result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            fragment.setSaveParameter(false);
            fragment.sendRemoveListItemRequest(result);
            fragment.preformBackPress();
        }
    }
}
