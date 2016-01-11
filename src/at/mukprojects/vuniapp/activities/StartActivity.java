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

package at.mukprojects.vuniapp.activities;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.info.help.HelpStartActivity;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Die StartActivity ist die erste Activity der VUniApp und pr&uuml;ft die
 * Einstellungen, ob eine Pinanfrage n&ouml;tig ist. Wenn die Einstellung
 * gesetzt ist, wird ein Dialog erzeugt und der Pin &uumlberpr&uuml;ft.
 * 
 * @author Mathias
 */
public class StartActivity extends VUniAppActivity {
    private static final String TAG                   = StartActivity.class
                                                              .getSimpleName();

    /** Extra Key. */
    public static final String  START_ACTIVITY        = "startActivity";

    /** Preferences. */
    private static final String PREF_FILE             = "PrefFileStart";
    private SharedPreferences   prefFile              = null;

    /** Gibt an, ob ein weiterer Pin Eingabeversuch unternommen werden darf. */
    private static final String INCORRECT_PIN_TIMEOUT = "PinTimeout";

    /** Gibt an, wie viele weiterer Pin Eingabeversuch unternommen werden darf. */
    private static final String INCORRECT_PIN_COUNTER = "PinCounter";

    /** Dialogs. */
    private static final int    PIN_DIALOG            = 1;
    private static final int    PIN_DIALOG_RETRY      = 2;

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.VUniAppActivity#onCreate(android.os
     * .Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);

        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        prefFile = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();

        /** Winzard. */
        if (!PrefFileConfig.getBoolean(PREF_PIN, false)
                && PrefFileGlobal.getBoolean(HELP_INTRODUKTION, true)) {
            Log.d(TAG, "VUniApp start die Hilfe.");
            Intent intent = new Intent(this, HelpStartActivity.class);
            startActivity(intent);
        } else {
            /** Login Pr&uuml;fen. */
            Log.d(TAG, "Pin Einstellung Prüfen.");

            /** Timeout, falls der Pin drei Mal falsch eingegeben wurde. */
            long timeout = prefFile.getLong(INCORRECT_PIN_TIMEOUT, -1);
            Log.d(TAG, "Timeout: " + timeout + " Date: " + new Date().getTime());
            if (timeout == -1 || timeout < (new Date()).getTime()) {
                Log.d(TAG, "Timeout: Ok");
                checkEncryption();
            } else {
                int difInMin = (int) ((timeout / 60000) - (((new Date())
                        .getTime()) / 60000));
                String toastText = "Der Pin wurde zu oft falsch eingegeben!"
                        + "\n Timeout (" + difInMin + " min)";
                Log.d(TAG, toastText);
                Toast.makeText(getCurrentActivity(), toastText,
                        Toast.LENGTH_LONG).show();
                unsuccessfulLogin();
            }
        }
        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected final void onResume() {
        /** Clock auf aktuelle Uhrzeit setzten. */
        lastPauseFromActivity = new Date();
        super.onResume();
    }

    /**
     * Die Methode pr&uuml;ft ob der eingegebene Pin korrekt ist.
     */
    @SuppressWarnings("deprecation")
    private void checkEncryption() {
        Log.d(TAG, "Verschlüsselungs- Einstellungen prüfen.");
        boolean securityConfig = PrefFileConfig.getBoolean(PREF_PIN, false);

        Log.d(TAG, "SecurityConfig: " + securityConfig);
        if (!securityConfig) {
            Log.d(TAG, "Einstellung: Kein Pin - "
                    + "Passwörter werden in Klartext gespeichert.");
            startVUniApp();
        } else if (securityConfig) {
            Log.d(TAG, "Einstellung: Pin - "
                    + "Passwörter werden mittels Pin verschlüsselt.");
            showDialog(PIN_DIALOG);
        }
    }

    /**
     * Vergleicht den Pin mit dem gespeicherten Hashwert.
     * 
     * @param enteredPin
     *            Der eingegebene Pin.
     */
    @SuppressWarnings("deprecation")
    private void pinEqualsHash(final String enteredPin) {
        if (SecurityHelper.equalsStringWithHashString(enteredPin,
                PrefFileGlobal.getString(PREF_SAVED_PIN_HASH, ""))) {
            Log.d(TAG, "Pin Eingabe gültig!");

            Log.d(TAG, "Pin Counter & Timeout zurücksetzten.");
            Editor editor = prefFile.edit();
            editor.putLong(INCORRECT_PIN_TIMEOUT, -1);
            editor.commit();
            editor.putInt(INCORRECT_PIN_COUNTER, 0);
            editor.commit();

            Log.d(TAG, "Cache Pin.");
            Cache.getCache().cacheData(CACHE_PIN, enteredPin, null);

            startVUniApp();
        } else {
            Log.d(TAG, "Pin Eingabe ungültig!");

            int counter = prefFile.getInt(INCORRECT_PIN_COUNTER, 0);
            /** Pin wurde 3 Mal falsch eingeben - Timeout 5 min */
            Log.d(TAG, "Pin Counter: " + counter);
            if (counter >= 2) {
                Editor editor = prefFile.edit();
                String message = "Timeout: "
                        + (new Date(((new Date()).getTime()) + (5 * 60000)))
                                .toString();
                Log.d(TAG, message);
                Toast.makeText(getCurrentActivity(), message, Toast.LENGTH_LONG)
                        .show();
                editor.putLong(INCORRECT_PIN_TIMEOUT, (new Date().getTime())
                        + (5 * 60000));
                editor.commit();
                editor.putInt(INCORRECT_PIN_COUNTER, 0);
                editor.commit();
                unsuccessfulLogin();

            } else {
                counter++;
                Editor editor = prefFile.edit();
                editor.putInt(INCORRECT_PIN_COUNTER, counter);
                editor.commit();
                showDialog(PIN_DIALOG_RETRY);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    @Deprecated
    protected final Dialog onCreateDialog(final int id) {
        Log.i(TAG, "Methode: onCreateDialog wird gestartet.");

        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getCurrentActivity());
        switch (id) {
            case PIN_DIALOG:
                builder.setTitle(getString(R.string.dialog_pin_confirmation_titel));
                builder.setMessage(getString(R.string.dialog_pin_confirmation_text));
                builder.setCancelable(false);

                final EditText input = new EditText(getCurrentActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton(
                        getString(R.string.dialog_pin_confirmation_oKButton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int whichButton) {
                                String value = input.getText().toString();
                                pinEqualsHash(value);
                                dialog.cancel();
                            }
                        });
                dialog = builder.create();
                break;
            case PIN_DIALOG_RETRY:
                builder.setTitle(getString(R.string.dialog_pin_confirmation_titel));
                String tries = (3 - prefFile.getInt(INCORRECT_PIN_COUNTER, 0))
                        + "";
                builder.setMessage(getString(
                        R.string.dialog_pin_confirmation_retryText, tries));
                builder.setCancelable(false);

                final EditText inputRetry = new EditText(getCurrentActivity());
                inputRetry.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(inputRetry);

                builder.setPositiveButton(
                        getString(R.string.dialog_pin_confirmation_oKButton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int whichButton) {
                                String value = inputRetry.getText().toString();
                                pinEqualsHash(value);
                                dialog.cancel();
                            }
                        });
                dialog = builder.create();
                break;
            default:
                dialog = super.onCreateDialog(id);
                break;
        }

        Log.i(TAG, "Methode: onCreateDialog wird verlassen.");
        return dialog;
    }

    /**
     * Startet die VUniApp.
     */
    private void startVUniApp() {
        Log.d(TAG, "VUniApp wird gestartet");

        if (isOnline()) {
            startOtherActivity();
        } else {
            Log.d(TAG, "Keine Internet Settings gesetzt.");
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getCurrentActivity());
            builder.setTitle(getString(R.string.activity_start_dialog_internet_titel));
            builder.setMessage(getString(R.string.activity_start_dialog_internet_message));
            builder.setCancelable(false);

            builder.setPositiveButton(
                    getCurrentActivity().getString(
                            R.string.activity_start_dialog_internet_positiv),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                final int whichButton) {
                            dialog.cancel();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.phone",
                                    "com.android.phone.Settings");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getCurrentActivity().finish();
                        }
                    });
            builder.setNegativeButton(
                    getCurrentActivity().getString(
                            R.string.activity_start_dialog_internet_negativ),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                final int whichButton) {
                            dialog.cancel();
                            startOtherActivity();
                        }
                    });

            builder.create().show();
        }
    }

    /**
     * Startet entweder den HomeScreen oder die Activity im Bundle.
     */
    private void startOtherActivity() {
        Bundle bundle = getIntent().getExtras();
        boolean normalStart = false;
        if (bundle != null) {
            if (bundle.get(START_ACTIVITY) instanceof Class) {
                Log.d(TAG, "StartActivity leitet direkt weiter.");
                Class<? extends Activity> activityToStart = (Class<? extends Activity>) bundle
                        .get(START_ACTIVITY);
                Intent intent = new Intent(this, activityToStart);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                normalStart = true;
            }
        } else {
            normalStart = true;
        }
        if (normalStart) {
            Log.d(TAG, "StartActivity startet die HomeActivity.");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }

    /**
     * Diese Methode beendet VUniApp, falls der Pin inkorrekt war.
     */
    private void unsuccessfulLogin() {
        Log.d(TAG, "UnsuccessfulLogin: App wird geschlossen.");
        /**
         * Da der Pin nur beim Starten getestet wird, wird im Fall einer
         * inkorrekten Eingabe die App geschlossen.
         */
        finish();
    }

    /**
     * Pr&uuml;ft, ob eine Internetverbindung aufgebaut werden kann.
     * 
     * @return True, falls Internet vorhanden ist, andernfalls False.
     */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
