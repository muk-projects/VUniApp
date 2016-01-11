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

package at.mukprojects.vuniapp.baseclasses.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.BrowserActivity;
import at.mukprojects.vuniapp.activities.HomeActivity;
import at.mukprojects.vuniapp.activities.StartActivity;
import at.mukprojects.vuniapp.activities.canteen.CanteenActivity;
import at.mukprojects.vuniapp.activities.certificates.CertificatesActivity;
import at.mukprojects.vuniapp.activities.dates.DatesActivity;
import at.mukprojects.vuniapp.activities.info.help.HelpStartActivity;
import at.mukprojects.vuniapp.activities.searchprofessor.SearchProfessorActivity;
import at.mukprojects.vuniapp.activities.searchroom.SearchRoomActivity;
import at.mukprojects.vuniapp.activities.services.ServicesActivity;
import at.mukprojects.vuniapp.activities.settings.security.SecurityActivity;
import at.mukprojects.vuniapp.activities.settings.university.UniversitySettingActivity;
import at.mukprojects.vuniapp.activities.settings.user.UserActivity;
import at.mukprojects.vuniapp.activities.subjects.SubjectsActivity;
import at.mukprojects.vuniapp.activities.task.TaskActivity;
import at.mukprojects.vuniapp.helper.DialogHelper;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.storages.UniversitySettings;

/**
 * Activity mit implementierter VUniApp Navigation. Alle Activities sollten von
 * dieser Activity erben. F&uuml;r weitere Informationen &uuml;ber das das
 * Navigationsmenu: <a href=
 * "http://developer.android.com/training/implementing-navigation/nav-drawer.html"
 * > Creating a Navigation Drawer</a>
 * 
 * @author kerrim
 * @author Mathias
 */
public abstract class VUniAppActivity extends FragmentActivity {
    private static final String                                                  TAG                  = VUniAppActivity.class
                                                                                                              .getSimpleName();

    // CHECKSTYLE OFF
    public static final String                                                   PREF_FILE_GLOBAL     = "PrefFileGlobal";
    protected SharedPreferences                                                  PrefFileGlobal       = null;
    public static final String                                                   PREF_FILE_CONFIG     = "PrefFileConfig";
    protected SharedPreferences                                                  PrefFileConfig       = null;

    /** Config. */
    public static final String                                                   PREF_PIN             = "SettingPin";
    public static final String                                                   PREF_SAVED_PIN_HASH  = "SavedPinHash";
    public static final String                                                   CACHE_PIN            = "Pin";
    /* Global. */
    public static final String                                                   HELP_INTRODUKTION    = "helpOnStart";

    private ActionBarDrawerToggle                                                drawerToggler;
    private DrawerLayout                                                         drawerLayout;

    private String                                                               activeNavigationItem;
    private VUniAppActivity                                                      currentActivity;

    protected String                                                             language;
    public static final String                                                   ENGLISH              = "English";
    public static final String                                                   GERMAN               = "German";

    protected static Date                                                        lastPauseFromActivity;

    private Class<? extends Activity>                                            activityClass;
    private static HashMap<Class<? extends Activity>, ArrayList<NavigationItem>> subnavigationStorage = new HashMap<Class<? extends Activity>, ArrayList<NavigationItem>>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        onCreate(savedInstanceState, null, null);

        /** Inizialisiert UniversitySettings. */
        UniversitySettings.initialize(PrefFileConfig);
        /** Inizialisiert SecurityHelper. */
        SecurityHelper.initialize(PrefFileConfig);
    }

    /**
     * Wird beim Erstellen der Activity aufgerufen.
     * 
     * @param savedInstanceState
     *            Bundle welches an die ActivityKlasse weitergereicht wird.
     * @param activityClass
     *            Klasse der Activity, welche f&uuml;r die Daten zust&auml;ndig
     *            ist. Diese bestimmt unter anderem welche Navigation angezeigt
     *            wird.
     */
    protected final void onCreate(final Bundle savedInstanceState,
            final Class<? extends Activity> activityClass) {
        onCreate(savedInstanceState, activityClass, null);
    }

    /**
     * Wird beim Erstellen der Activity aufgerufen. Diese Methode sollte nicht
     * mehr von den erbenden Klassen genutzt werden, da wir solche
     * Subnavigationen nicht mehr unterst&uuml;tzen wollen.
     * 
     * @param savedInstanceState
     *            Bundle welches an die ActivityKlasse weitergereicht wird.
     * @param activityClass
     *            Klasse der Activity, welche f&uuml;r die Daten zust&auml;ndig
     *            ist. Unter diesem Klassennamen wird die Navigation falls
     *            mitgegeben gespeichert. Sonst wird versucht eine Navigation
     *            mit identen Namen zu laden.
     * @param subnavigation
     *            Navigationsitems welche zur Navigationsleiste hinzugef&uuml;gt
     *            werden sollen.
     */
    private final void onCreate(final Bundle savedInstanceState,
            final Class<? extends Activity> activityClass,
            final ArrayList<NavigationItem> subnavigation) {
        // CHECKSTYLE ON
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        super.onCreate(savedInstanceState);

        Log.d(TAG, "PrefFile Config & Global erstellen.");
        PrefFileGlobal = getSharedPreferences(PREF_FILE_GLOBAL, 0);
        PrefFileConfig = getSharedPreferences(PREF_FILE_CONFIG, 0);

        Log.d(TAG, "Navigation wird erstellt.");

        ((ImageButton) findViewById(R.id.actionbar_navigation))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        } else {
                            drawerLayout.openDrawer(Gravity.LEFT);
                        }
                    }
                });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        this.activityClass = activityClass;
        if (activityClass != null && subnavigation != null) {
            subnavigationStorage.put(activityClass, subnavigation);
        }

        ListView navigationList = (ListView) findViewById(R.id.drawer_layout_list);
        putNavigationInListView(
                navigationList,
                createNavigationOutOfNavigationItems(createNavigationItems(subnavigation)));

        drawerToggler = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_button_navigation_in, R.string.navigation_open,
                R.string.navigation_close) {
            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(final View view) {
                ImageButton navigationButton = (ImageButton) findViewById(R.id.actionbar_navigation);
                navigationButton
                        .setImageResource(R.drawable.ic_button_navigation_in);
            }

            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(final View drawerView) {
                ImageButton navigationButton = (ImageButton) findViewById(R.id.actionbar_navigation);
                navigationButton
                        .setImageResource(R.drawable.ic_button_navigation_out);
            }
        };

        drawerLayout.setDrawerListener(drawerToggler);

        Log.d(TAG, "Navigation wurde erstellt.");

        Log.i(TAG, "Methode: onCreate wird verlassen.");

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    // CHECKSTYLE OFF
    @Override
    protected void onResume() {
        // CHECKSTYLE ON
        super.onResume();
        Date time = new Date();
        if (lastPauseFromActivity == null
                || lastPauseFromActivity.getTime() + 60 * 1000 < time.getTime()) {
            Log.d(TAG, "ClockTime überschritten LoginScreen.");
            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra(StartActivity.START_ACTIVITY, activityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected final void onPause() {
        super.onPause();
        lastPauseFromActivity = new Date();
    }

    /**
     * Erstellt die Standardnavigation und f&uuml;gt gegebenfalls eine
     * Spezialnavigation am Anfang dieser ein.
     * 
     * @param customNavigation
     *            Navigation die zus&auml;tzlich zur Standardnavigation genutzt
     *            werden soll.
     * @return ArrayList mit allen Navigationsitems.
     */
    private ArrayList<NavigationItem> createNavigationItems(
            final ArrayList<NavigationItem> customNavigation) {
        ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

        if (customNavigation != null) {
            navigationItems.addAll(customNavigation);
        }

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_home), HomeActivity.class, getResources()
                .getString(R.string.navigation_category_general)));

        navigationItems
                .add(new NavigationItem(getResources().getString(
                        R.string.activity_subjects), SubjectsActivity.class,
                        getResources().getString(
                                R.string.navigation_category_general)));

        navigationItems
                .add(new NavigationItem(getResources().getString(
                        R.string.activity_certificates),
                        CertificatesActivity.class, getResources().getString(
                                R.string.navigation_category_general)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_mensa), CanteenActivity.class, getResources()
                .getString(R.string.navigation_category_general)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_dates), DatesActivity.class, getResources()
                .getString(R.string.navigation_category_general)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_searchprofessor),
                SearchProfessorActivity.class, getResources().getString(
                        R.string.navigation_category_general)));

        navigationItems
                .add(new NavigationItem(getResources().getString(
                        R.string.activity_searchroom),
                        SearchRoomActivity.class, getResources().getString(
                                R.string.navigation_category_general)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_task), TaskActivity.class, getResources()
                .getString(R.string.navigation_category_general)));
        /* */
        navigationItems
                .add(new NavigationItem(getResources().getString(
                        R.string.activity_services), ServicesActivity.class,
                        getResources().getString(
                                R.string.navigation_category_settings)));
        /* */
        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_user), UserActivity.class, getResources()
                .getString(R.string.navigation_category_settings)));

        navigationItems
                .add(new NavigationItem(getResources().getString(
                        R.string.activity_security), SecurityActivity.class,
                        getResources().getString(
                                R.string.navigation_category_settings)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_universitysettings),
                UniversitySettingActivity.class, getResources().getString(
                        R.string.navigation_category_settings)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.info_vuniapp), BrowserActivity.class, getResources()
                .getString(R.string.navigation_category_info)) {
            @Override
            public void execute() {
                Intent i = new Intent(getCurrentActivity(), getActivity());
                i.putExtra("url",
                        "http://www.mukprojects.at/vuniapp/appsite/vuniapp.html");
                i.putExtra("limitedHost", "www.mukprojects.at");
                i.putExtra("navigationItem",
                        getResources().getString(R.string.info_vuniapp));
                startActivity(i);
            }
        });

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.info_feedback), BrowserActivity.class, getResources()
                .getString(R.string.navigation_category_info)) {
            @Override
            public void execute() {
                DialogHelper.getFeedbackDialog(getCurrentActivity()).show();
            }
        });

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.activity_help), HelpStartActivity.class,
                getResources().getString(R.string.navigation_category_info)));

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.info_contribute), BrowserActivity.class,
                getResources().getString(R.string.navigation_category_info)) {
            @Override
            public void execute() {
                Intent i = new Intent(getCurrentActivity(), getActivity());
                i.putExtra("url",
                        "http://www.mukprojects.at/vuniapp/appsite/contribute.html");
                i.putExtra("limitedHost", "www.mukprojects.at");
                i.putExtra("navigationItem",
                        getResources().getString(R.string.info_contribute));
                startActivity(i);
            }
        });

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.info_contributors), BrowserActivity.class,
                getResources().getString(R.string.navigation_category_info)) {
            @Override
            public void execute() {
                Intent i = new Intent(getCurrentActivity(), getActivity());
                i.putExtra("url",
                        "http://www.mukprojects.at/vuniapp/appsite/contributors.html");
                i.putExtra("limitedHost", "www.mukprojects.at");
                i.putExtra("navigationItem",
                        getResources().getString(R.string.info_contributors));
                startActivity(i);
            }
        });

        navigationItems.add(new NavigationItem(getResources().getString(
                R.string.info_impressum), BrowserActivity.class, getResources()
                .getString(R.string.navigation_category_info)) {
            @Override
            public void execute() {
                Intent i = new Intent(getCurrentActivity(), getActivity());
                i.putExtra("url",
                        "http://www.mukprojects.at/vuniapp/appsite/impressum.html");
                i.putExtra("limitedHost", "www.mukprojects.at");
                i.putExtra("navigationItem",
                        getResources().getString(R.string.info_impressum));
                startActivity(i);
            }
        });

        return navigationItems;
    }

    /**
     * Nimmt eine Liste von NavigationItems und wandelt sie in eine in
     * Kategorien geordnete NavigationsListe f&uuml;r den NavigationDrawer um.
     * 
     * @param navigationItems
     *            Liste der Items die zur Navigation genutzt werden sollen.
     * @return Liste bestehend aus Kategorien (Strings) und Navigationsitems.
     */
    private ArrayList<Object> createNavigationOutOfNavigationItems(
            final ArrayList<NavigationItem> navigationItems) {
        ArrayList<Object> navigation = new ArrayList<Object>();
        String category = "";
        for (NavigationItem navigationItem : navigationItems) {
            if (!navigationItem.getCategory().equals(category)) {
                category = navigationItem.getCategory();
                navigation.add(new String(category));
            }
            navigation.add(navigationItem);
        }
        return navigation;

    }

    /**
     * F%uuml;gt einer ListView einen ArrayAdapter mit der Navigation hinzu.
     * 
     * @param navigationListView
     *            ListView, welche f&uuml;r die Navigation genutzt werden soll.
     * @param navigation
     *            ArrayList mit den Strings f&uuml;r den Kategorien und
     *            NavigationItems f&uuml;r die einzelnen Navigationspunkte.
     */
    private void putNavigationInListView(final ListView navigationListView,
            final ArrayList<Object> navigation) {
        navigationListView.setAdapter(new ArrayAdapter<Object>(this,
                R.layout.adapter_navigation_navigationlist_item, navigation) {
            @Override
            public boolean isEnabled(final int position) {
                if (navigation.get(position) instanceof String) {
                    return false;
                }
                if (navigation.get(position) instanceof NavigationItem) {
                    if (((NavigationItem) navigation.get(position)).getTitle()
                            .equals(activeNavigationItem)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public View getView(final int position, final View cView,
                    final ViewGroup parent) {
                ViewHolder holder = null;
                View convertView = null;

                if (navigation.get(position) instanceof NavigationItem) {
                    convertView = LayoutInflater
                            .from(getCurrentActivity())
                            .inflate(
                                    R.layout.adapter_navigation_navigationlist_item,
                                    parent, false);

                    RelativeLayout item = (RelativeLayout) convertView
                            .findViewById(R.id.adapter_navigation_navigationlist_item);

                    if (!isEnabled(position)) {
                        item.setEnabled(false);
                    }

                    holder = new ViewHolder();
                    holder.item = (TextView) convertView
                            .findViewById(R.id.adapter_navigation_navigationlist_item_text);

                    convertView.setTag(holder);
                } else if (navigation.get(position) instanceof String) {
                    convertView = LayoutInflater
                            .from(getCurrentActivity())
                            .inflate(
                                    R.layout.adapter_navigation_navigationlist_category,
                                    parent, false);

                    RelativeLayout item = (RelativeLayout) convertView
                            .findViewById(R.id.adapter_navigation_navigationlist_category);

                    item.setEnabled(false);

                    holder = new ViewHolder();
                    holder.item = (TextView) convertView
                            .findViewById(R.id.adapter_navigation_navigationlist_category_text);

                    convertView.setTag(holder);
                }

                holder.item.setText(navigation.get(position).toString());

                return convertView;
            }

            class ViewHolder {
                private TextView item;
            }
        });

        navigationListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> arg0, final View arg1,
                    final int position, final long arg3) {
                ((NavigationItem) navigation.get(position)).execute();
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    /**
     * Setzt in der Navigation den aktuellen Navigationslink, in dem sich die
     * App befindet.
     * 
     * @param activeNavigationItem
     *            Der Aktuelle Name des Navigationsitems indem sich die App
     *            befindet.
     */
    protected final void setActiveNavigationItem(
            final String activeNavigationItem) {
        this.activeNavigationItem = activeNavigationItem;
        // Falls onCreate() bereits durchgelaufen ist und das
        // activeNavigationItem geändert wurde, Navigation neu erzeugen.
        if (drawerToggler != null) {
            ListView navigationList = (ListView) findViewById(R.id.drawer_layout_list);
            putNavigationInListView(
                    navigationList,
                    createNavigationOutOfNavigationItems(createNavigationItems(subnavigationStorage
                            .get(activityClass))));
        }
    }

    /**
     * Mit Hilfe dieser Methode kann der Hintergrund der Aktionbar ge&aouml;nder
     * werden.
     * 
     * @param drawableBackground
     *            Der neue Hintergrund.
     */
    protected final void setActionbarBackground(final int drawableBackground) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.actionbar);
        if (layout != null) {
            layout.setBackgroundResource(drawableBackground);
        } else {
            Log.e(TAG, "Actionbar ist null, Content muss zuerst erzeugt werden");
        }
    }

    /**
     * Liefert die Klasse der aktuellen Activity zur&uuml;ck.
     * 
     * @return Klasse der aktuellen Activity.
     */
    public final Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    /**
     * Liefert die aktuell laufende Activity zur&uuml;ck. Diese muss zuerst mit
     * setCurrentActivity(Activity) gesetzt werden.
     * 
     * @return Aktuell laufende Activity.
     */
    public final Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * Setzt die aktuell laufende Activity.
     * 
     * @param currentActivity
     *            Aktuell laufende Activity.
     */
    public final void setCurrentActivity(final VUniAppActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /**
     * Liefert PrefFileGlobal.
     * 
     * @return PrefFileGlobal
     */
    public final SharedPreferences getPrefFileGlobal() {
        return PrefFileGlobal;
    }

    /**
     * Liefert PrefFilConfig.
     * 
     * @return PrefFilConfig
     */
    public final SharedPreferences getPrefFileConfig() {
        return PrefFileConfig;
    }

    /**
     * Repr&auml;sentiert ein Navigations Link in der Sidebar. Falls der
     * Navigationslink nicht einfach die Activity starten soll, sollte die
     * execute() Methode &uuml;berschrieben werden.
     * 
     * @author kerrim
     */
    protected class NavigationItem implements Comparable<NavigationItem> {
        private String category;
        private String title;
        @SuppressWarnings("rawtypes")
        private Class  activity;

        /**
         * Erzeug ein neues NavigationItem.
         * 
         * @param title
         *            Name der Activity zu der gesprungen werden soll.
         * @param activity
         *            Activity zu der gesprungen werden soll.
         * @param category
         *            Speichert die Kategorie zu der dieses Item eingeordnet
         *            werden soll.
         */
        @SuppressWarnings("rawtypes")
        public NavigationItem(final String title, final Class activity,
                final String category) {
            this.title = title;
            this.activity = activity;
            this.category = category;
        }

        /**
         * Liefert den Namen des Navigationsitems.
         * 
         * @return Name des Navigationsitems.
         */
        public final String getTitle() {
            return title;
        }

        /**
         * Liefert die Kategorie des Items zur&uuml;ck.
         * 
         * @return Kategorie des Items.
         */
        public final String getCategory() {
            return category;
        }

        /**
         * Liefert den Namen der Activity zu die gesprungen werden soll
         * zur&uuml;ck.
         * 
         * @return Name der Activity.
         */
        @Override
        public final String toString() {
            return title;
        }

        /**
         * Liefert die Activity zu die gesprungen werden soll zur&uuml;ck.
         * 
         * @return Activity zu der gesprungen werden soll.
         */
        @SuppressWarnings("rawtypes")
        public final Class getActivity() {
            return activity;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public final int compareTo(final NavigationItem another) {
            return category.compareTo(another.getCategory());
        }

        /**
         * F&uuml;hrt dieses Navigationsitem aus. Um eine von der
         * Standardausf&uuml;hrung abweichende Ausf&uuml;hrung zu
         * implementieren, kann diese Methode &uuml;berschrieben werden.
         */
        // CHECKSTYLE OFF
        public void execute() {
            // CHECKSTYLE ON
            Intent i = new Intent(getCurrentActivity(), getActivity());
            startActivity(i);
        }
    }
}
