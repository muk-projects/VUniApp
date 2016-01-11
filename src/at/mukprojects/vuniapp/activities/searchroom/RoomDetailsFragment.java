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

package at.mukprojects.vuniapp.activities.searchroom;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.storages.Universities;

/**
 * Dieses Fragment zeigt die Details zu einem Raum an.
 * 
 * @author Mathias
 */
public class RoomDetailsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = RoomDetailsFragment.class
                                                         .getSimpleName();

    /** Tag Parameter. */
    private static final String HIDE             = "hide";
    private static final String SHOW             = "show";

    /** Tab Parameter. */
    private static final int    DETAIL           = 0;
    private static final int    MAP              = 1;

    private Room                room;

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searchroom_room, container,
                false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();
        Bundle bundle = getArguments();
        room = (Room) bundle.getSerializable(Room.ROOM_EXTRA);

        ((TextView) getView().findViewById(
                R.id.fragment_searchroom_room_roomName)).setText(room
                .getRoomName());

        /** Ausganspostion herstellen */
        setContentVisibility(
                getView().findViewById(
                        R.id.fragment_searchroom_room_DetailLayout), null);
        setContentVisibility(
                getView().findViewById(R.id.fragment_searchroom_room_MapLayout),
                null);

        /** Listener setzten. */
        setContentListener();

        showDetail();

        Log.i(TAG, "Methode: onStart wird verlassen.");
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
                R.id.fragment_searchroom_room_tabButtonDetail))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeTabActiv(DETAIL);
                    }
                });

        ((Button) getView().findViewById(
                R.id.fragment_searchroom_room_tabButtonMap))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeTabActiv(MAP);
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
                R.id.fragment_searchroom_room_tabViewDetail));
        tabViews.add(getView().findViewById(
                R.id.fragment_searchroom_room_tabViewMap));

        ArrayList<View> contentViews = new ArrayList<View>();
        contentViews.add(getView().findViewById(
                R.id.fragment_searchroom_room_DetailLayout));
        contentViews.add(getView().findViewById(
                R.id.fragment_searchroom_room_MapLayout));

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
     * Zeigt die Details des Raums an.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void showDetail() {
        /** Info Details. */
        ((TextView) getView().findViewById(
                R.id.fragment_searchroom_room_DetailTextUni)).setText(room
                .getUniversity().getName());
        ((TextView) getView().findViewById(
                R.id.fragment_searchroom_room_DetailTextRoom)).setText(room
                .getRoomName());

        TextView address = (TextView) getView().findViewById(
                R.id.fragment_searchroom_room_DetailTextAddress);
        if (room.getAddress() != null) {
            address.setText(room.getAddress());
        } else {
            address.setText(getString(R.string.fragment_searchroom_room_noData));
        }

        TextView info = (TextView) getView().findViewById(
                R.id.fragment_searchroom_room_DetailTextInfo);
        if (room.getDetailInfo() != null) {
            info.setText(room.getDetailInfo());
        } else {
            info.setText(getString(R.string.fragment_searchroom_room_noData));
        }

        /** Map Details. */
        WebView webview = (WebView) getView().findViewById(
                R.id.fragment_searchroom_room_MapWebView);

        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        if (room.getUrlToDetails() != null) {
            webview.loadUrl(room.getUrlToDetails());
        } else {
            webview.loadUrl("https://maps.google.at/");
        }
    }
}
