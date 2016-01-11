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

package at.mukprojects.vuniapp.activities.settings.security;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Das Fragment dient zum &Auml;ndern des Passworts.
 * 
 * @author Mathias
 */
public class SecurityFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;
    private static final String TAG              = SecurityFragment.class
                                                         .getSimpleName();
    private String              appPin;

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_security, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    public final void onStart() {
        super.onStart();

        ((Button) getView().findViewById(R.id.fragment_security_button))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        managePasswordChange();
                    }
                });

        /** Testen, ob ein Pin vorhanden ist. */
        if (Cache.getCache().containsData(VUniAppActivity.CACHE_PIN)) {
            appPin = (String) Cache.getCache().getCachedData(
                    VUniAppActivity.CACHE_PIN);

        }
        if (appPin == null) {
            Log.d(TAG, "Kein Pin vorhanden.");
            ((TextView) getView()
                    .findViewById(R.id.fragment_security_oldPWText))
                    .setVisibility(TextView.INVISIBLE);
            ((EditText) getView().findViewById(R.id.fragment_security_oldPW))
                    .setVisibility(EditText.INVISIBLE);
        }
    }

    /**
     * Die Methode wickelt das Passwort &auml;ndern ab.
     */
    private void managePasswordChange() {
        Log.d(TAG, "Es wird geprüft, ob das Passwort geändert werden kann.");
        boolean changeOk = true;
        if (appPin != null) {
            Log.d(TAG, "Altes Passwort prüfen.");
            if (!(((EditText) getView().findViewById(
                    R.id.fragment_security_oldPW)).getText().toString()
                    .equals(appPin))) {
                Toast toast = Toast.makeText(getActivity(),
                        getString(R.string.activity_security_oldPinFalse),
                        Toast.LENGTH_SHORT);
                Log.d(TAG, getString(R.string.activity_security_oldPinFalse));
                toast.show();
                changeOk = false;
            }
        }

        if (changeOk) {
            if (((EditText) getView().findViewById(
                    R.id.fragment_security_newPW1))
                    .getText()
                    .toString()
                    .equals(((EditText) getView().findViewById(
                            R.id.fragment_security_newPW2)).getText()
                            .toString())) {
                Log.d(TAG, "Beide neuen Passwörter identisch");
            } else {
                Toast toast = Toast.makeText(getActivity(),
                        getString(R.string.activity_security_newPinFalse),
                        Toast.LENGTH_SHORT);
                Log.d(TAG, getString(R.string.activity_security_newPinFalse));
                toast.show();
                changeOk = false;
            }
        }

        UniversityUserService userService = null;
        try {
            userService = new UniversityUserServiceStd(
                    new UniversityUserDAOAndroidDB(getActivity()));
        } catch (ConnectionException connectionException) {
            Log.e(TAG, connectionException.getMessage(), connectionException);
            changeOk = false;
            Toast toast = Toast.makeText(getActivity(),
                    getString(R.string.activity_security_serviceFalse),
                    Toast.LENGTH_SHORT);
            Log.d(TAG, getString(R.string.activity_security_serviceFalse));
            toast.show();
        }
        try {
            if (changeOk && userService != null) {
                SharedPreferences prefFileGlobal = getActivity()
                        .getSharedPreferences(VUniAppActivity.PREF_FILE_GLOBAL,
                                0);
                SharedPreferences.Editor prefFileGlobalEditor = prefFileGlobal
                        .edit();
                SharedPreferences prefFileConfig = getActivity()
                        .getSharedPreferences(VUniAppActivity.PREF_FILE_CONFIG,
                                0);
                SharedPreferences.Editor prefFileConfigEditor = prefFileConfig
                        .edit();

                if (!((EditText) getView().findViewById(
                        R.id.fragment_security_newPW1)).getText().toString()
                        .equals("")) {
                    Log.d(TAG, "Passwort der App ändern.");

                    Log.d(TAG, "Neues Passwort cachen.");
                    Cache.getCache().cacheData(
                            VUniAppActivity.CACHE_PIN,
                            ((EditText) getView().findViewById(
                                    R.id.fragment_security_newPW1)).getText()
                                    .toString(), null);
                    Log.d(TAG, "Hash Passwort im PrefFile abspeichern.");
                    prefFileGlobalEditor
                            .putString(
                                    VUniAppActivity.PREF_SAVED_PIN_HASH,
                                    SecurityHelper
                                            .getMD5Hash(((EditText) getView()
                                                    .findViewById(
                                                            R.id.fragment_security_newPW1))
                                                    .getText().toString()));
                    prefFileGlobalEditor.commit();

                    Log.d(TAG, "Datenbank mit neuem Passwort verschlüsseln.");

                    userService.encryptionUsers(((EditText) getView()
                            .findViewById(R.id.fragment_security_newPW1))
                            .getText().toString(), appPin);

                    Log.d(TAG, "Einstellung ändern.");
                    prefFileConfigEditor.putBoolean(VUniAppActivity.PREF_PIN,
                            true);
                    prefFileConfigEditor.commit();
                } else {
                    Log.d(TAG, "Passwort der App löschen.");

                    Log.d(TAG, "Alte Passwort aus dem Chace entfernen.");
                    Cache.getCache().removeData(VUniAppActivity.CACHE_PIN);
                    Log.d(TAG, "Hash Passwort aus dem PrefFile löschen.");
                    prefFileGlobalEditor
                            .remove(VUniAppActivity.PREF_SAVED_PIN_HASH);
                    prefFileGlobalEditor.commit();

                    Log.d(TAG, "Datenbank entschlüsseln.");
                    if (appPin != null) {
                        userService.encryptionUsers(null, appPin);
                    }

                    Log.d(TAG, "Einstellung ändern.");
                    prefFileConfigEditor.putBoolean(VUniAppActivity.PREF_PIN,
                            false);
                    prefFileConfigEditor.commit();
                }
                Toast toast = Toast.makeText(getActivity(),
                        getString(R.string.activity_security_changed),
                        Toast.LENGTH_SHORT);
                Log.d(TAG, getString(R.string.activity_security_changed));
                toast.show();
            }
        } catch (ReadException e) {
            Log.e(TAG, e.getMessage(), e);
            changeError(e);
        } catch (UpdateException e) {
            Log.e(TAG, e.getMessage(), e);
            changeError(e);
        } catch (NotInitializedException e) {
            Log.e(TAG, e.getMessage(), e);
            changeError(e);
        }
    }

    /**
     * Wird aufegrufen, wenn es w&auml;hrend der Passwort&auml;nderung zu einer
     * Exception kommt.
     * 
     * @param e
     *            Aufgetrettene Exception.
     */
    private void changeError(final Exception e) {
        Toast toast = Toast
                .makeText(getActivity(),
                        getString(R.string.activity_security_error),
                        Toast.LENGTH_SHORT);
        toast.show();
    }
}
