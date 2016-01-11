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

package at.mukprojects.vuniapp.activities.info.help;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.HomeActivity;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;

/**
 * Die HelpBaseActivity ist die Basis f&uuml;r alle Hilfs- Activities.
 * 
 * @author Mathias
 */
public abstract class HelpBaseActivity extends FragmentActivity {
    private static final String TAG      = HelpBaseActivity.class
                                                 .getSimpleName();
    private Activity            activity = this;

    /**
     * Setzt den Skip Button.
     */
    protected final void setSkipButton() {
        Button skip = (Button) findViewById(R.id.activity_help_skip);
        skip.setVisibility(Button.VISIBLE);
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                setPreference();
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        updateMenuBar();
    }

    /**
     * Setzt den Next Button.
     * 
     * @param activityToStart
     *            Activity, welche durch den Klick gestartet werden soll.
     * @param lastHelpActivity
     *            Gibt an, ob es sich um die letzte Hilfs Activity handelt.
     */
    protected final void setNextButton(
            final Class<? extends Activity> activityToStart,
            final boolean lastHelpActivity) {
        Button next = (Button) findViewById(R.id.activity_help_next);
        next.setVisibility(Button.VISIBLE);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (lastHelpActivity) {
                    setPreference();
                }
                Intent intent = new Intent(activity, activityToStart);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        updateMenuBar();
    }

    /**
     * Setzt die Einstellung.
     */
    private void setPreference() {
        CheckBox checkbox = (CheckBox) findViewById(R.id.activity_help_checkbox);

        SharedPreferences prefFileGlobal = getSharedPreferences(
                VUniAppActivity.PREF_FILE_GLOBAL, 0);
        SharedPreferences.Editor editorGlobal = prefFileGlobal.edit();

        if (checkbox.isChecked()) {
            editorGlobal.putBoolean(VUniAppActivity.HELP_INTRODUKTION, false);
            editorGlobal.commit();
        } else {
            editorGlobal.putBoolean(VUniAppActivity.HELP_INTRODUKTION, true);
            editorGlobal.commit();
        }
    }

    /**
     * Aktualisiert die Men&uuml;bar.
     */
    private void updateMenuBar() {
        Button skip = (Button) findViewById(R.id.activity_help_skip);
        Button next = (Button) findViewById(R.id.activity_help_next);
        if (skip.getVisibility() == Button.GONE
                && next.getVisibility() == Button.VISIBLE) {
            skip.setVisibility(Button.INVISIBLE);
        }
        if (next.getVisibility() == Button.GONE
                && skip.getVisibility() == Button.VISIBLE) {
            next.setVisibility(Button.INVISIBLE);
        }
    }
}
