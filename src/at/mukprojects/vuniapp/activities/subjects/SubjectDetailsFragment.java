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

package at.mukprojects.vuniapp.activities.subjects;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.ExportEventsActivity;
import at.mukprojects.vuniapp.activities.searchprofessor.ProfessorDetailsFragment;
import at.mukprojects.vuniapp.activities.searchprofessor.SearchProfessorActivity;
import at.mukprojects.vuniapp.activities.searchroom.SearchRoomActivity;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.LoadFragmentRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.RemoveListItemRequest;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.exceptions.NoTwoPaneException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.Event;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.SubjectInterface;

/**
 * Fragment zum Anzeigen eines Faches. Dieses bekommt ein Subject Objekt und
 * zeigt dieses an.
 * 
 * @author Mathias
 */
public class SubjectDetailsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = SubjectDetailsFragment.class
                                                         .getSimpleName();

    /** Tag Parameter. */
    private static final String HIDE             = "hide";
    private static final String SHOW             = "show";

    /** Tab Parameter. */
    private static final int    INFO             = 0;
    private static final int    DATES            = 1;
    private static final int    EXAMS            = 2;

    /** Content IDs. */
    private Subject             subject;

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
        return inflater.inflate(R.layout.fragment_subjects_subjectdetails,
                container, false);
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
        Bundle bundle = getArguments();
        subject = (Subject) bundle.getSerializable(Subject.SUBJECT);

        ((TextView) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_lvaText))
                .setText(((subject.getType() != null) ? (subject.getType() + " ")
                        : "")
                        + subject.getName());
        ((TextView) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_lvaDetail))
                .setText(subject.getInfo());

        /** Ausganspostion herstellen */
        setContentVisibility(
                getView().findViewById(
                        R.id.fragment_subjects_subjectsdetails_infoLayout),
                HIDE);
        setContentVisibility(
                getView().findViewById(
                        R.id.fragment_subjects_subjectsdetails_datesList), SHOW);
        setContentVisibility(
                getView().findViewById(
                        R.id.fragment_subjects_subjectsdetails_examsList), HIDE);

        /** Listener setzten. */
        setContentListener();

        if (subject.isContainsDetails()) {
            showSubjectDetails(subject);
        } else {
            (new LoadSubjectDetailsTask()).execute();
        }

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /**
     * Setzt die Listener des Contents.
     */
    private void setContentListener() {
        Log.d(TAG, "Listener des Contents werden gesetzt.");
        setTabListener();
    }

    /**
     * Setzt die TabListener des Contents.
     */
    private void setTabListener() {
        ((Button) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabButtonInfo))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeTabActiv(INFO);
                    }
                });

        ((Button) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabButtonDates))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeTabActiv(DATES);
                    }
                });

        ((Button) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabButtonExams))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeTabActiv(EXAMS);
                    }
                });
    }

    /**
     * &Auml;ndert die grafische Aktivierung eines Tabs.
     * 
     * @param tab
     *            Gibt an welches Tab aktiviert werden soll.
     */
    private void changeTabActiv(final int tab) {
        ArrayList<View> tabViews = new ArrayList<View>();
        tabViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabActiveInfo));
        tabViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabActiveDates));
        tabViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_tabActiveExams));

        ArrayList<View> contentViews = new ArrayList<View>();
        contentViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_infoLayout));
        contentViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_datesLayout));
        contentViews.add(getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_examsLayout));

        for (int i = 0; i < tabViews.size(); i++) {
            if (tab == i) {
                tabViews.get(i).setBackgroundResource(R.color.LightGreen);
                setContentVisibility(contentViews.get(i), SHOW);
            } else {
                tabViews.get(i).setBackgroundResource(R.color.Green);
                setContentVisibility(contentViews.get(i), HIDE);
            }
        }
    }

    /**
     * Setzt die Sichtbarkeit eines Views entweder mit einem &uuml;bergebenen
     * Tag oder mit dem Tag des Views.
     * 
     * @param view
     *            Der zu &auml;ndernde View.
     * @param tag
     *            Der Tag, welche entweder hide oder show sein muss, bzw. null
     *            falls der Tag des Views verwendet werden soll.
     */
    private void setContentVisibility(final View view, final String tag) {
        Log.d(TAG, "Sichtbarkeit des Views (" + view.getId()
                + ") wird geändert.");
        String setTag;

        if (tag == null) {
            setTag = view.getTag().toString();
        } else {
            setTag = tag;
        }
        Log.d(TAG, "Tag: " + setTag);
        if (setTag.equals(HIDE)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        view.setTag(setTag);
    }

    /**
     * Zeigt die Informationen des Faches im Fragment an.
     * 
     * @param subject
     *            Fach dessen Informationen angezeigt werden soll.
     */
    private void showSubjectDetails(final Subject subject) {
        Log.d(TAG, "SubjectDetails werden eingefügt.");

        setUpInfoTab(subject);
        setUpDatesTab(subject);
        setUpExamsTab(subject);
    }

    /**
     * Erstellt den Content des ExamsTabs.
     * 
     * @param subject
     *            Das Fach mit den Daten.
     */
    private void setUpExamsTab(final Subject subject) {
        Log.d(TAG, "Prüfungen werden geladen");
        if (subject.getExams() != null) {
            setUpDatesAndExams(
                    subject.getExams(),
                    (ListView) getView().findViewById(
                            R.id.fragment_subjects_subjectsdetails_examsList),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_examsMenuButtonExportView),
                    getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_examsMenuButtonExportLayout),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_examsMenuButtonExport),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_examsMenuButtonCancel));
        }

        /** ListView Bug! Eigentlich sollte dieser Befehlt nicht notwendig sein. */
        ((ListView) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_examsList))
                .setVisibility(ListView.VISIBLE);
    }

    /**
     * Erstellt den Content des DatesTabs.
     * 
     * @param subject
     *            Das Fach mit den Daten.
     */
    private void setUpDatesTab(final Subject subject) {
        Log.d(TAG, "Termien werden geladen");
        if (subject.getAppointments() != null) {
            setUpDatesAndExams(
                    subject.getAppointments(),
                    (ListView) getView().findViewById(
                            R.id.fragment_subjects_subjectsdetails_datesList),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_datesMenuButtonExportView),
                    getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_datesMenuButtonExportLayout),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_datesMenuButtonExport),
                    (Button) getView()
                            .findViewById(
                                    R.id.fragment_subjects_subjectsdetails_datesMenuButtonCancel));
        }
    }

    /**
     * Managt die Termin ListViews.
     * 
     * @param eventList
     *            Termine des Views.
     * @param datesList
     *            ListView der verwendet werden soll.
     * @param exportViewButton
     *            Termin ExportView Button.
     * @param exportButtonLayout
     *            ButtonLayout der Options Buttons.
     * @param exportButton
     *            Export Button.
     * @param exportCancelButton
     *            AbbruchButton.
     */
    private void setUpDatesAndExams(final ArrayList<Event> eventList,
            final ListView datesList, final Button exportViewButton,
            final View exportButtonLayout, final Button exportButton,
            final Button exportCancelButton) {

        Log.d(TAG, "ListView: " + datesList.getId());
        Log.d(TAG, "Events: " + eventList.toString());

        Collections.sort(eventList);

        if (eventList != null) {
            datesList.setAdapter(new DatesAdapter(getActivity(), eventList,
                    R.layout.adapter_subject_subjectdetails_dateslist_normal));

            registerForContextMenu(datesList);

            exportViewButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    datesList
                            .setAdapter(new DatesAdapter(
                                    getActivity(),
                                    eventList,
                                    R.layout.adapter_subject_subjectdetails_dateslist_checkbox));
                    exportButtonLayout.setVisibility(View.VISIBLE);
                    exportViewButton.setVisibility(Button.INVISIBLE);
                }
            });

            exportCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    datesList
                            .setAdapter(new DatesAdapter(
                                    getActivity(),
                                    eventList,
                                    R.layout.adapter_subject_subjectdetails_dateslist_normal));
                    exportButtonLayout.setVisibility(View.INVISIBLE);
                    exportViewButton.setVisibility(Button.VISIBLE);
                }
            });

            exportButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DatesAdapter adapter = (DatesAdapter) datesList
                            .getAdapter();
                    ArrayList<Event> events = adapter.getCheckedEvents();
                    Event[] eventArray = new Event[events.size()];
                    for (int i = 0; i < events.size(); i++) {
                        eventArray[i] = events.get(i);
                    }
                    datesList
                            .setAdapter(new DatesAdapter(
                                    getActivity(),
                                    eventList,
                                    R.layout.adapter_subject_subjectdetails_dateslist_normal));
                    exportButtonLayout.setVisibility(View.INVISIBLE);
                    exportViewButton.setVisibility(Button.VISIBLE);

                    Intent intent = new Intent(getActivity(),
                            ExportEventsActivity.class);
                    intent.putExtra(ExportEventsActivity.EXTRA_EVENTS, events);
                    startActivity(intent);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu
     * , android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v,
            final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(getString(R.string.fragment_subject_details_contextmenu_search));
        menu.add(getString(R.string.fragment_subject_details_contextmenu_export));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem
     * )
     */
    @Override
    public final boolean onContextItemSelected(final MenuItem item) {
        ListView datesList = (ListView) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_datesList);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        int index = info.position;
        DatesAdapter datesAdapter = (DatesAdapter) datesList.getAdapter();
        Event event = (Event) datesAdapter.getItem(index);
        if (item.getTitle()
                .equals(getString(R.string.fragment_subject_details_contextmenu_search))) {
            Log.d(TAG, "Raum suche wurde gewählt. (Event: " + event.getDate()
                    + "");
            Intent intent = new Intent(getActivity(), SearchRoomActivity.class);
            intent.putExtra(Room.ROOM_EXTRA, event.getRoom());
            startActivity(intent);
        } else if (item
                .getTitle()
                .equals(getString(R.string.fragment_subject_details_contextmenu_export))) {
            Log.d(TAG, "Termin export wurde gewählt.(Event: " + event.getDate()
                    + ")");
            Intent intent = new Intent(getActivity(),
                    ExportEventsActivity.class);
            intent.putExtra(ExportEventsActivity.EXTRA_EVENT, event);
            startActivity(intent);
        }
        return true;
    }

    /**
     * Erstellt den Content des InfoTabs.
     * 
     * @param subject
     *            Das Fach mit den Daten.
     */
    private void setUpInfoTab(final Subject subject) {
        setUpWebView(subject);
        setUpProfList(subject.getProfessors());
    }

    /**
     * Bereitet die Daten f&uuml;r den WebView auf.
     * 
     * @param subject
     *            Das Fach mit den Daten.
     */
    private void setUpWebView(final Subject subject) {
        Log.d(TAG, "Der WebView wird erstellt.");
        WebView fragmentSubjectsSubjectsdetailsInfoWebViewGoals = (WebView) getView()
                .findViewById(
                        R.id.fragment_subjects_subjectsdetails_infoWebViewGoals);
        String htmlCode = "";
        htmlCode += "<h3>Ziele</h3>";
        htmlCode += "</hr>";
        htmlCode += subject.getGoals();
        htmlCode += "</hr>";
        htmlCode += "<h3>Beschreibung</h3>";
        htmlCode += subject.getContent();

        fragmentSubjectsSubjectsdetailsInfoWebViewGoals.loadDataWithBaseURL(
                "http://goals", htmlCode, "text/html", "utf-8", "");
    }

    /**
     * Bereitet die Daten f&uuml;r die ListView auf.
     * 
     * @param profList
     *            Liste an Professoren.
     */
    private void setUpProfList(final ArrayList<Professor> profList) {
        Log.d(TAG, "Professoren werden eingefügt.");
        final ListView profListView = (ListView) getView().findViewById(
                R.id.fragment_subjects_subjectsdetails_profList);
        profListView.setAdapter(new ProfAdapter(getActivity(), profList));
        profListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> adapter,
                    final View view, final int position, final long arg) {
                Professor profItem = (Professor) profListView
                        .getItemAtPosition(position);
                Log.d(TAG, "Ein Professor (" + profItem.getName()
                        + ") wurde angeklickt, das Detail"
                        + " Fragment wird gestartet.");

                if (isTwoPaneWithListener()) {
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Professor.PROFESSOR, profItem);
                        getFragmentListener()
                                .invoke(new LoadFragmentRequest(
                                        new ProfessorDetailsFragment(), bundle));
                    } catch (NoTwoPaneException noTwoPaneException) {
                        Log.e(TAG, noTwoPaneException.getMessage(),
                                noTwoPaneException);
                    }
                } else {
                    Intent intent = new Intent(getActivity(),
                            SearchProfessorActivity.class);
                    intent.putExtra(Professor.PROFESSOR, profItem);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Adapter zum Anzeigen der Termine in einer ListView.
     * 
     * @author Mathias
     */
    class DatesAdapter extends MainListBaseAdapter<Event> {
        private int id;

        /**
         * Erzeugt einen neuen DatesAdapter f&uuml;r die Anzeige der Termine in
         * der ListView.
         * 
         * @param context
         *            Context der Activity.
         * @param dates
         *            Liste der anzuzeigenden Termine.
         * @param id
         *            ID des XML Files.
         */
        public DatesAdapter(final Context context,
                final ArrayList<Event> dates, final int id) {
            super(context, dates);
            Log.v(TAG, "Neuer Dates Adapter wird erstellt: " + dates.toString());
            this.id = id;
        }

        /**
         * Liefert alle makierten Events.
         * 
         * @return Alle relevanten Events.
         */
        public ArrayList<Event> getCheckedEvents() {
            Log.d(TAG, "Events werden ausgelesen");
            ArrayList<Event> checkedEvents = new ArrayList<Event>();
            for (int i = 0; i < list.size(); i++) {
                Event event = list.get(i);
                Log.d(TAG, "Event(Date: " + event.getDate() + "Index: " + i
                        + ")");
                View currentView = getView(i, null, null);
                ViewHolderDatesAdapter holder = (ViewHolderDatesAdapter) currentView
                        .getTag();
                if (holder.checkbox != null && holder.checkbox.isChecked()) {
                    checkedEvents.add(event);
                }
            }

            return checkedEvents;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(final int position, final View cView,
                final ViewGroup parent) {
            final ViewHolderDatesAdapter holder;
            View convertView = cView;

            convertView = inflator.inflate(id, parent, false);

            holder = new ViewHolderDatesAdapter();
            holder.date = (TextView) convertView
                    .findViewById(R.id.adapter_subjects_subject_date);
            holder.description = (TextView) convertView
                    .findViewById(R.id.adapter_subjects_subject_description);
            holder.room = (TextView) convertView
                    .findViewById(R.id.adapter_subjects_subject_room);
            holder.checkbox = (CheckBox) convertView
                    .findViewById(R.id.adapter_subjects_subject_box);

            convertView.setTag(holder);

            Event event = (Event) getItem(position);
            Log.d(TAG, "Event " + event.getName() + " wird angezeigt.");
            if (!event.isWholeDay()) {
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                if (event.getDate() != null) {
                    holder.date.setText(df.format(event.getDate()));
                }
            } else {
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                if (event.getDate() != null) {
                    holder.date.setText(df.format(event.getDate()));
                }
            }
            holder.room.setText("Raum: " + event.getPlace());
            holder.description.setText(event.getDescription());
            if (event.getDate() != null) {
                if (event.getDate().getTime() < new Date().getTime()) {
                    holder.date.setTextColor(getResources().getColor(
                            R.color.LightGrey));
                    holder.room.setTextColor(getResources().getColor(
                            R.color.LightGrey));
                    holder.description.setTextColor(getResources().getColor(
                            R.color.LightGrey));
                }
            }
            return convertView;
        }

        /**
         * ViewHolder f&uuml;r die anzuzeigenden Termine.
         * 
         * @author Mathias
         */
        class ViewHolderDatesAdapter {
            private TextView date;
            private TextView description;
            private TextView room;
            private CheckBox checkbox;
        }
    }

    /**
     * Adapter zum Anzeigen der Professoren in einer ListView.
     * 
     * @author Mathias
     */
    class ProfAdapter extends MainListBaseAdapter<Professor> {
        /**
         * Erzeugt einen neuen ProfAdapter f&uuml;r die Anzeige der Professoren
         * in der ListView.
         * 
         * @param context
         *            Context welcher vom Adapter genutzt werden soll.
         * @param list
         *            Ergebnisliste der Professoren.
         */
        public ProfAdapter(final Context context,
                final ArrayList<Professor> list) {
            super(context, list);
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, final View cView,
                final ViewGroup parent) {
            final ViewHolderProfAdapter holder;
            View convertView = cView;

            convertView = inflator.inflate(
                    R.layout.adapter_subject_subjectdetails_proflist, parent,
                    false);
            holder = new ViewHolderProfAdapter();
            holder.name = (TextView) convertView
                    .findViewById(R.id.adapter_subjects_subjectdetails_profName);
            convertView.setTag(holder);

            Professor prof = (Professor) getItem(position);
            holder.name.setText(prof.getName());
            return convertView;
        }

        /**
         * ViewHolder f&uuml;r die anzuzeigenden Professoren.
         * 
         * @author Mathias
         */
        class ViewHolderProfAdapter {
            private TextView name;
        }
    }

    /**
     * Laden der Informationen eines Faches.
     * 
     * @author kerrim
     * @author Mathias
     */
    class LoadSubjectDetailsTask extends BaseTask<Void, Subject> {

        /**
         * Erstellt einen neuen Task.
         */
        public LoadSubjectDetailsTask() {
            super(getResources().getString(R.string.fragment_subject_details),
                    getResources().getString(
                            R.string.fragment_subject_details_loading), false,
                    getActivity());
        }

        @Override
        protected Subject doInBackground(final Void... params) {
            UniversityUserService userService = null;
            try {
                userService = new UniversityUserServiceStd(
                        new UniversityUserDAOAndroidDB(getActivity()));
            } catch (ConnectionException e1) {
                Log.e(TAG, e1.getMessage());
            }

            University university = subject.getUniversity();

            if (university instanceof SubjectInterface) {

                List<String> userKeys = ((SubjectInterface) university)
                        .getSubjectKeys();
                HashMap<String, UniversityUser> users = new HashMap<String, UniversityUser>();

                try {
                    users = userService.getUsersFromUniversity(university,
                            userKeys);
                } catch (ReadException e1) {
                    e1.printStackTrace();
                } catch (NotInitializedException e) {
                    SecurityHelper.initialize(((VUniAppActivity) getActivity())
                            .getPrefFileConfig());
                    try {
                        users = userService.getUsersFromUniversity(university,
                                userKeys);
                    } catch (ReadException readException) {
                        super.readException = readException;
                    } catch (NotInitializedException notInitializedException) {
                        super.notInitializedException = notInitializedException;
                    }
                }

                try {
                    if (users.size() > 0) {
                        subject = ((SubjectInterface) university)
                                .getSubjectDetails(users, subject, activity);
                    } else {
                        Log.d(TAG, "Für "
                                + subject.getUniversity().getKeyName()
                                + " gibt es keine Benutzer.");
                    }
                } catch (IOException e) {
                    super.ioException = e;
                } catch (InvalidLoginException e) {
                    super.invalidLoginException = e;
                } catch (ConnectionException e) {
                    super.connectionException = e;
                }
            }

            return subject;
        }

        @Override
        protected void onPostExecute(final Subject result) {
            super.onPostExecute(result);
            if (super.noSuperError) {
                showSubjectDetails(result);
            }
            Log.i(TAG, "ASyncTask: LoadSubjectDetailsTask wird verlassen.");
        }
    }
}
