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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.AddListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.RemoveListItemRequest;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.exceptions.NoTwoPaneException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.models.base.User;
import at.mukprojects.vuniapp.storages.Universities;

/**
 * Dieses Fragment zeigt die Details zu einem Raum an.
 * 
 * @author Mathias
 */
public class UserFragment extends VUniAppFragment {
    private static final long                            serialVersionUID = 1L;

    private static final String                          TAG              = UserFragment.class
                                                                                  .getSimpleName();
    private UserFragment                                 fragment         = this;
    private UniversityUser                               user;
    private boolean                                      testOK;
    private boolean                                      needToSave;

    private HashMap<University, HashMap<String, String>> universityWithKeys;
    private ArrayList<String>                            universityNames;
    private ArrayList<String>                            universityKeys;
    private ArrayList<String>                            userKeyNames;
    private ArrayList<String>                            userKeys;

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();

        universityNames = new ArrayList<String>();
        universityKeys = new ArrayList<String>();
        userKeyNames = new ArrayList<String>();
        userKeys = new ArrayList<String>();

        universityWithKeys = Universities.getInstance()
                .getUniversitiesAndUserKeys();
        for (University university : universityWithKeys.keySet()) {
            universityNames.add(university.getName());
            universityKeys.add(university.getKeyName());
        }

        testOK = false;
        needToSave = false;

        setSpinner();
        setEditTextListener();
        setButtonListener();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(User.USER_EXTRA) != null) {
            user = (UniversityUser) bundle.getSerializable(User.USER_EXTRA);

            /** User Daten einbinden. */
            ((EditText) getView().findViewById(
                    R.id.activity_user_usernameEditText)).setText(user
                    .getUsername());
            ((EditText) getView().findViewById(
                    R.id.activity_user_passwordEditText)).setText(user
                    .getPassword());

            setUniversitySpinner(
                    (Spinner) getView().findViewById(
                            R.id.activity_user_university), user
                            .getUniversity().getKeyName());

            ((Button) getView().findViewById(R.id.activity_user_deleteButton))
                    .setEnabled(true);
        } else {
            ((Button) getView().findViewById(R.id.activity_user_deleteButton))
                    .setEnabled(false);
        }

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /**
     * Setzt die EditTextListener.
     */
    private void setEditTextListener() {
        ((EditText) getView().findViewById(R.id.activity_user_usernameEditText))
                .addTextChangedListener(new TextWatcher() {
                    private String beforeTextChanged = "";

                    @Override
                    public void onTextChanged(final CharSequence charSequence,
                            final int start, final int before, final int count) {
                    }

                    @Override
                    public void beforeTextChanged(
                            final CharSequence charSequence, final int start,
                            final int count, final int after) {
                        beforeTextChanged = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {
                        if (!beforeTextChanged.equals(editable.toString())) {
                            testOK = false;
                            needToSave = true;

                            ((Button) getView().findViewById(
                                    R.id.activity_user_deleteButton))
                                    .setEnabled(false);
                            getView().findViewById(
                                    R.id.activity_user_testProgressBar)
                                    .setVisibility(ProgressBar.INVISIBLE);
                            getView().findViewById(R.id.activity_testButton)
                                    .setVisibility(ImageView.VISIBLE);
                            ((EditText) getView().findViewById(
                                    R.id.activity_user_usernameEditText))
                                    .setBackgroundResource(android.R.drawable.editbox_background_normal);
                            ((EditText) getView().findViewById(
                                    R.id.activity_user_passwordEditText))
                                    .setBackgroundResource(android.R.drawable.editbox_background_normal);
                        }
                    }
                });

        ((EditText) getView().findViewById(R.id.activity_user_passwordEditText))
                .addTextChangedListener(new TextWatcher() {
                    private String beforeTextChanged = "";

                    @Override
                    public void onTextChanged(final CharSequence charSequence,
                            final int start, final int before, final int count) {
                    }

                    @Override
                    public void beforeTextChanged(
                            final CharSequence charSequence, final int start,
                            final int count, final int after) {
                        beforeTextChanged = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {
                        if (!beforeTextChanged.equals(editable.toString())) {
                            testOK = false;
                            needToSave = true;

                            ((Button) getView().findViewById(
                                    R.id.activity_user_deleteButton))
                                    .setEnabled(false);
                            getView().findViewById(
                                    R.id.activity_user_testProgressBar)
                                    .setVisibility(ProgressBar.INVISIBLE);
                            getView().findViewById(R.id.activity_testButton)
                                    .setVisibility(ImageView.VISIBLE);
                            ((EditText) getView().findViewById(
                                    R.id.activity_user_usernameEditText))
                                    .setBackgroundResource(android.R.drawable.editbox_background_normal);
                            ((EditText) getView().findViewById(
                                    R.id.activity_user_passwordEditText))
                                    .setBackgroundResource(android.R.drawable.editbox_background_normal);
                        }
                    }
                });

    }

    /**
     * Diese Methode setzt den Spinner auf die &uuml;bergebene Universit&auml;t
     * und sperrt den Spinner anschlie&szlig;end.
     * 
     * @param universitySpinner
     *            Spinner der Universit&auml;t.
     * @param universityKey
     *            Key der Universit&auml;t.
     */
    private void setUniversitySpinner(final Spinner universitySpinner,
            final String universityKey) {
        for (int i = 0; i < universityKeys.size(); i++) {
            if (universityKeys.get(i).equals(universityKey)) {
                universitySpinner.setSelection(i);

                universitySpinner.setEnabled(false);
                ((Spinner) getView().findViewById(R.id.activity_user_userKey))
                        .setEnabled(false);
                break;
            }
        }

    }

    /**
     * Diese Methode setzt alle ben&ouml;tigten Ressourcen der Spinner.
     */
    private void setSpinner() {
        final Spinner university = (Spinner) getView().findViewById(
                R.id.activity_user_university);
        final Spinner userKey = (Spinner) getView().findViewById(
                R.id.activity_user_userKey);

        ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                universityNames);
        universityAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        university.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                String universityKey = universityKeys.get(position);
                University selectedUniversity = Universities.getInstance()
                        .getUniversityFromKey(universityKey);

                HashMap<String, String> userKeysMap = universityWithKeys
                        .get(selectedUniversity);
                userKeyNames.clear();
                userKeys.clear();
                for (String name : userKeysMap.keySet()) {
                    userKeyNames.add(userKeysMap.get(name));
                    userKeys.add(name);
                }

                if (userKeyNames.isEmpty()) {
                    ArrayList<String> empty = new ArrayList<String>();
                    empty.add(getString(R.string.activity_user_emptyUserKey));
                    ArrayAdapter<String> userKeyAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item, empty);
                    userKeyAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userKey.setAdapter(userKeyAdapter);
                    userKey.setSelection(0);
                    userKey.setEnabled(false);
                } else {
                    ArrayAdapter<String> userKeyAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item, userKeyNames);
                    userKeyAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userKey.setAdapter(userKeyAdapter);
                    userKey.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                university.setSelection(0);
            }
        });
        university.setAdapter(universityAdapter);
        university.setSelection(0);
    }

    /**
     * Diese Methode setzt alle ButtonListener des Fragments.
     */
    private void setButtonListener() {
        ((Button) getView().findViewById(R.id.activity_testButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        (new TestUserTask(getActivity(), Universities
                                .getInstance().getUniversitiesWithLogin()
                                .get(getSelectedUniversity()),
                                (Button) getView().findViewById(
                                        R.id.activity_testButton),
                                (ProgressBar) getView().findViewById(
                                        R.id.activity_user_testProgressBar),
                                getSelectedUser(),
                                (EditText) getView().findViewById(
                                        R.id.activity_user_usernameEditText),
                                (EditText) getView().findViewById(
                                        R.id.activity_user_passwordEditText),
                                false, fragment)).execute();
                    }
                });

        ((Button) getView().findViewById(R.id.activity_user_saveButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (testOK) {

                            (new SaveUserTask(getActivity(), fragment))
                                    .execute(getSelectedUser());
                        } else {
                            (new TestUserTask(
                                    getActivity(),
                                    Universities.getInstance()
                                            .getUniversitiesWithLogin()
                                            .get(getSelectedUniversity()),
                                    (Button) getView().findViewById(
                                            R.id.activity_testButton),
                                    (ProgressBar) getView().findViewById(
                                            R.id.activity_user_testProgressBar),
                                    getSelectedUser(),
                                    (EditText) getView()
                                            .findViewById(
                                                    R.id.activity_user_usernameEditText),
                                    (EditText) getView()
                                            .findViewById(
                                                    R.id.activity_user_passwordEditText),
                                    true, fragment)).execute();
                        }
                    }
                });

        ((Button) getView().findViewById(R.id.activity_user_deleteButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        (new DeleteUserTask(getActivity(), fragment))
                                .execute(getSelectedUser());
                    }
                });

    }

    /**
     * Liefert den eingegebenen User zur&uuml;ck.
     * 
     * @return Den eingegebenen User.
     */
    private UniversityUser getSelectedUser() {
        String userKey = null;
        if (((Spinner) getView().findViewById(R.id.activity_user_userKey))
                .isEnabled()) {
            userKey = getUserKeyFromUserKeyName((String) ((Spinner) getView()
                    .findViewById(R.id.activity_user_userKey))
                    .getSelectedItem());
        }
        return new UniversityUser(((EditText) getView().findViewById(
                R.id.activity_user_usernameEditText)).getText().toString(),
                ((EditText) getView().findViewById(
                        R.id.activity_user_passwordEditText)).getText()
                        .toString(), getSelectedUniversity(), userKey);
    }

    /**
     * Methode zum Ermitteln des richtigen UserKeys zum Namen des UserKeys.
     * 
     * @param name
     *            Anzeigbarer Name des UserKeys.
     * @return Der gesuchte UserKey.
     */
    private String getUserKeyFromUserKeyName(final String name) {
        int pos = -1;
        while (pos < userKeyNames.size()) {
            pos++;
            if (name.equals(userKeyNames.get(pos))) {
                break;
            }
        }
        return userKeys.get(pos);
    }

    /**
     * Liefert die eingegebenen Universit&auml;t zur&uuml;ck.
     * 
     * @return Die eingegebenen Universit&auml;t.
     */
    private University getSelectedUniversity() {
        String universityName = (String) ((Spinner) getView().findViewById(
                R.id.activity_user_university)).getSelectedItem();
        return Universities.getInstance().getUniversityFromName(
                (universityName));
    }

    /**
     * Diese Methode setzt den Parameter testOK. Dieser gibt an, ob der User
     * postiv getestet ist.
     * 
     * @param testOK
     *            Gibt an ob der User postiv getestet ist.
     */
    public final void setTestParameter(final boolean testOK) {
        this.testOK = testOK;
    }

    /**
     * Wird aufgerufen, wenn der Test vor dem Speichern fehlgeschlagen ist.
     */
    public final void testWithSaveHasFailed() {
        createMissingTestSave().show();
    }

    /**
     * Diese Methode setzt den Parameter needToSave. Dieser gibt an, ob der User
     * gespeichert ist.
     * 
     * @param needToSave
     *            Gibt an ob der User gespeichert ist.
     */
    public final void setSaveParameter(final boolean needToSave) {
        this.needToSave = needToSave;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.VUniAppFragment#needBackPressAktion
     * ()
     */
    @Override
    public final boolean needBackPressAktion() {
        return needToSave;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.VUniAppFragment#onBackPressAktion
     * ()
     */
    @Override
    public final void onBackPressAktion() {
        createSaveOnExit().show();
    }

    /**
     * Sendet wenn m&ouml;glich ein ListItemRequest an die ParrentActivity.
     * 
     * @param user
     *            Der User welcher die Liste updaten soll.
     */
    public final void sendAddListItemRequest(final UniversityUser user) {
        if (isTwoPaneWithListener()) {
            try {
                getFragmentListener().invoke(
                        new AddListItemRequest<UniversityUser>(user));
            } catch (NoTwoPaneException noTwoPaneException) {
                Log.e(TAG, noTwoPaneException.getMessage(), noTwoPaneException);
            }
        }
    }
    
    /**
     * Sendet wenn m&ouml;glich ein ListItemRequest an die ParrentActivity.
     * 
     * @param user
     *            Der User welcher die Liste updaten soll.
     */
    public final void sendRemoveListItemRequest(final UniversityUser user) {
        if (isTwoPaneWithListener()) {
            try {
                getFragmentListener().invoke(
                        new RemoveListItemRequest<UniversityUser>(user));
            } catch (NoTwoPaneException noTwoPaneException) {
                Log.e(TAG, noTwoPaneException.getMessage(), noTwoPaneException);
            }
        }
    }
    
    /**
     * Die Methode beendet das Fragment.
     */
    public final void preformBackPress() {
        getActivity().onBackPressed();
    }

    /**
     * Die Methode erstellt einen Dialog, welcher den Anwender darauf hinweist,
     * dass der zu speichernde Benutzer noch nicht getestet wurde oder der Test
     * nicht erfolgreich verlaufen ist.
     * 
     * @return Liefert den AlterDialog zur&uuml;ck.
     */
    private AlertDialog createMissingTestSave() {
        Log.d(TAG, "CreateMissingTestSave Dialog wird erstellt.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.activity_user_missingTestDialog_title));
        builder.setMessage(getString(R.string.activity_user_missingTestDialog_text));
        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.dialog_yesButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        SaveUserTask saveUserTask = new SaveUserTask(
                                getActivity(), fragment);
                        saveUserTask.execute(getSelectedUser());
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.dialog_noButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    /**
     * Die Methode CreateSaveOnExit erstellt einen AlertDialog, welcher den User
     * darauf hinweist, dass eine oder mehrere Eingaben nicht gespeichert
     * wurden. Der User kann dann bestimmen ob die Eingaben gespeichert werden
     * sollen.
     * 
     * @return Liefert einen CreateSaveOnExit Dialog zur&uuml;ck.
     */
    private AlertDialog createSaveOnExit() {
        Log.d(TAG, "CreateSaveOnExit Dialog wird erstellt.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.activity_user_saveOnExitDialog_title));
        builder.setMessage(getString(R.string.activity_user_saveOnExitDialog_text));
        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.dialog_yesButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        SaveUserTask saveUserTask = new SaveUserTask(
                                getActivity(), fragment);
                        saveUserTask.execute(getSelectedUser());
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.dialog_noButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                        needToSave = false;
                        getActivity().onBackPressed();
                    }
                });

        return builder.create();
    }
}