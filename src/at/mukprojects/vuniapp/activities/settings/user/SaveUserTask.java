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
import android.widget.Toast;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;

/**
 * Der Task SaveUser speichert einen User in die Datenbank.
 * 
 * @author Mathias
 */
class SaveUserTask extends BaseTask<UniversityUser, UniversityUser> {
    private static final String TAG = SaveUserTask.class.getSimpleName();
    private Activity            activity;
    private UserFragment        fragment;

    /**
     * Erstellt einen SaveUserTask, um einen bestimmten Benutzer zu speichern.
     * 
     * @param activity
     *            Activity, welche den Task aufgerufen hat.
     * @param fragment
     *            UserFragment, welche den Task aufgerufen hat.
     */
    public SaveUserTask(final Activity activity, final UserFragment fragment) {
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
                Log.d(TAG, "Benutzer wird gespeichert.");
                service.createOrUpdateUser(params[0]);
            } catch (CreateException createException) {
                super.createException = createException;
            } catch (ReadException readException) {
                super.readException = readException;
            } catch (UpdateException updateException) {
                super.updateException = updateException;
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
            fragment.sendAddListItemRequest(result);
            Toast.makeText(activity,
                    activity.getString(R.string.activity_user_saveToast),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
