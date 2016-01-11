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

package at.mukprojects.vuniapp.activities.task;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.settings.user.UserFragment;
import at.mukprojects.vuniapp.activities.settings.user.UserListAdapter;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.AddListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.ReloadListRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.RemoveListItemRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.Request;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.Response;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.enumeration.TaskSortArg;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.User;

/**
 * Die TaskActivity bietet die grafische Oberfl&auml;che um Task zu verwalten.
 * 
 * @author Mathias
 */
public class TaskActivity extends VUniAppActivityWithFragment {
    private static final String TAG            = TaskActivity.class
                                                       .getSimpleName();
    /** Preference. */
    private static final String PREF_FILE      = "PrefFileTask";
    private static final String PREF_LAST_SORT = "lastSort";

    private SharedPreferences   prefFile       = null;
    private Editor              prefFileEditor = null;

    private TaskFragment        fragment;

    /** Content IDs. */
    private Button              activityTaskCreateButton;
    private ListView            activityTaskListView;
    private Spinner             activitySortSpinner;

    private int                 lastSort;

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
        setContentView(R.layout.activity_task);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_task));
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.onCreate(savedInstanceState);
        super.setActionbarBackground(R.drawable.actionbar_background_blue);

        prefFile = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        prefFileEditor = prefFile.edit();

        /** Element- IDs setzten. */
        activityTaskCreateButton = (Button) findViewById(R.id.activity_task_createButton);
        activityTaskListView = (ListView) findViewById(R.id.activity_task_taskList);
        activitySortSpinner = (Spinner) findViewById(R.id.activity_task_sortSpinner);

        ArrayAdapter<String> sortSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                TaskSortArg.getStringList(language));
        activitySortSpinner.setAdapter(sortSpinnerAdapter);
        activitySortSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> arg0,
                            final View arg1, final int arg2, final long arg3) {
                        reloadTaskList();
                    }

                    @Override
                    public void onNothingSelected(final AdapterView<?> arg0) {
                    }
                });

        lastSort = prefFile.getInt(PREF_LAST_SORT, 0);
        activitySortSpinner.setSelection(lastSort);

        activityTaskCreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                loadTaskFragment(null);
            }
        });

        activityTaskListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                Task clickedTask = (Task) activityTaskListView.getAdapter()
                        .getItem(position);
                loadTaskFragment(clickedTask);
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
        // reloadTaskList();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(Task.TASK) != null) {
            Log.d(TAG, "Es wurde ein Task übergeben.");
            loadTaskFragment((Task) bundle.get(Task.TASK));
        }
        Log.i(TAG, "Methode: onResume wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStop()
     */
    @Override
    protected final void onStop() {
        Log.i(TAG, "Methode: onStop wird gestartet.");
        super.onStop();
        prefFileEditor.putInt(PREF_LAST_SORT,
                activitySortSpinner.getSelectedItemPosition());
        Log.i(TAG, "Methode: onStop wird verlassen.");
    }

    /**
     * Mit der Methode loadTaskFragment kann ein Task in das TaskFragment
     * geladen werden. Wenn null &uuml;bergeben wird so wird ein TaskFragment
     * ohne Task geladen.
     * 
     * @param task
     *            Der zu ladende Task.
     */
    private void loadTaskFragment(final Task task) {
        Log.d(TAG, "Der Task (" + task + ") wird geladen.");
        fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Task.TASK, task);
        loadFragment(fragment, null, bundle,
                R.drawable.actionbar_background_blue);
    }

    /**
     * Diese Methode sorgt daf&uuml;rt, dass die ListView der vorhanden Tasks
     * neu geladen wird.
     */
    public final void reloadTaskList() {
        TaskSortArg arg = TaskSortArg
                .valueOfTaskSortArg((String) activitySortSpinner
                        .getSelectedItem());
        FindTasksTask findTasksTask = new FindTasksTask(this,
                activityTaskListView, arg);
        findTasksTask.execute();
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
            if (request instanceof ReloadListRequest) {
                reloadTaskList();
            } else {
                Log.d(TAG, "Request ist nicht bekannt.");
                response = null;
            }
        }

        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    public final void onActivityResult(final int requestCode,
            final int resultCode, final Intent data) {
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
