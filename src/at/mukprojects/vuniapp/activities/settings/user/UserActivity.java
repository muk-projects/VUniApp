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

import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.AddListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.RemoveListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.Request;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.Response;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.exceptions.NoUniversitiesException;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.models.base.User;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Die UserActivity bietet Funktionen zum Erstellen und Testen von
 * Benutzerdaten.
 * 
 * @author Mathias
 */
public class UserActivity extends VUniAppActivityWithFragment {
    private static final String TAG            = UserActivity.class
                                                       .getSimpleName();

    /** Preference. */
    private static final String PREF_FILE      = "PrefFileUser";
    private SharedPreferences   prefFile       = null;
    private Editor              prefFileEditor = null;

    /** Content IDs. */
    private Button              activityUserAddButton;
    private ListView            activityUserListView;
    private TextView            activityUserHint;

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
        setContentView(R.layout.activity_user);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_user));
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.onCreate(savedInstanceState);

        prefFile = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        prefFileEditor = prefFile.edit();

        /** Element- IDs setzten. */
        activityUserAddButton = (Button) findViewById(R.id.activity_user_addButton);
        activityUserListView = (ListView) findViewById(R.id.activity_user_listView);
        activityUserHint = (TextView) findViewById(R.id.activity_user_hint);

        activityUserAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                loadUserFragment(null);
            }
        });

        activityUserListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                UniversityUser clickedUser = (UniversityUser) activityUserListView
                        .getAdapter().getItem(position);
                loadUserFragment(clickedUser);
            }
        });

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.activities.base.VUniAppActivity#onResume()
     */
    @Override
    protected final void onResume() {
        Log.i(TAG, "Methode: onResume wird gestartet.");
        super.onResume();
        reloadUserList();
        Log.i(TAG, "Methode: onResume wird verlassen.");
    }

    /**
     * Mit der Methode loadUserFragment kann ein User in das UserFragment
     * geladen werden. Wenn null &uuml;bergeben wird so wird ein UserFragment
     * ohne User geladen.
     * 
     * @param user
     *            Der zu ladende User.
     */
    private void loadUserFragment(final UniversityUser user) {
        Log.d(TAG, "Der User (" + user + ") wird geladen.");
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(User.USER_EXTRA, user);
        loadFragment(fragment, bundle);
    }

    /**
     * Diese Methode sorgt daf&uuml;rt, dass die ListView der vorhanden User neu
     * geladen wird.
     */
    public final void reloadUserList() {
        HashMap<University, HashMap<String, String>> universityWithKeys = Universities
                .getInstance().getUniversitiesAndUserKeys();
        if (universityWithKeys.isEmpty()) {
            activityUserAddButton.setEnabled(false);
            activityUserListView.setVisibility(ListView.INVISIBLE);
            activityUserHint.setVisibility(TextView.VISIBLE);
            activityUserHint
                    .setText(getString(R.string.activity_user_hint_noUniversity));
        } else {
            activityUserAddButton.setEnabled(true);
            FindUserTask findUserTask = new FindUserTask(this,
                    activityUserListView, activityUserHint);
            findUserTask.execute();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.activities.base.VUniAppActivityWithFragment#invoke
     * (at.mukprojects.vuniapp.activities.base.fragmentlistener.Request)
     */
    @Override
    public final Response invoke(final Request request) {
        Log.d(TAG, "Request erhalten: " + request.toString());
        Response response = super.invoke(request);

        if (response == null) {
            if (request instanceof AddListItemRequest
                    || request instanceof RemoveListItemRequest) {
                ListAdapter adapter = activityUserListView.getAdapter();
                if (adapter instanceof UserListAdapter) {
                    UserListAdapter userListAdapter = (UserListAdapter) adapter;
                    if (request instanceof AddListItemRequest) {
                        Log.d(TAG,
                                "AddListItemRequest Item der Liste hinzufügen..");
                        UniversityUser userItem = (UniversityUser) ((AddListItemRequest<UniversityUser>) request)
                                .getItem();
                        userListAdapter.remove(userItem);
                        userListAdapter.add(userItem);
                        userListAdapter.notifyDataSetChanged();
                    } else if (request instanceof RemoveListItemRequest) {
                        Log.d(TAG,
                                "RemoveListItemRequest Item aus der Liste löschen..");
                        UniversityUser userItem = (UniversityUser) ((RemoveListItemRequest<UniversityUser>) request)
                                .getItem();
                        userListAdapter.remove(userItem);
                        userListAdapter.notifyDataSetChanged();
                    }

                    // FIXME M>K: Code müsste auch ohne diese Zeile
                    // funktionieren!
                    // Remove reloadUserList and still work!
                    // http://vuniapp.mukprojects.at/mithelfen/support/?ticket=34
                    reloadUserList();

                    if (userListAdapter.getCount() == 0) {
                        activityUserListView.setVisibility(ListView.INVISIBLE);
                        activityUserHint.setVisibility(TextView.VISIBLE);
                    } else {
                        activityUserListView.setVisibility(ListView.VISIBLE);
                        activityUserHint.setVisibility(TextView.INVISIBLE);
                    }

                    response = null;
                }
            } else {
                Log.d(TAG, "Request ist nicht bekannt.");
                response = null;
            }
        }

        return response;
    }
}
