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

package at.mukprojects.vuniapp.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;

/**
 * Diese Klasse enth&auml;lt Methoden, welche fertige Dialoge zur Verf&uuml;gung
 * stellen.
 * 
 * @author Mathias
 */
public abstract class DialogHelper {
    private static final String TAG = DialogHelper.class.getSimpleName();
    private static final int FEEDBACK_VERSION = 1;

    /**
     * Diese Methode liefert einen FeedbackDialog zur&uuml;ck.
     * 
     * @param context
     *            Context des Dialogs.
     * 
     * @return Der Dialog.
     */
    public static Dialog getFeedbackDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.dialog_feedback_title));
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_feedback,
                null);
        builder.setView(dialoglayout);

        builder.setNegativeButton(
                context.getString(R.string.dialog_cancelButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        dialog.cancel();
                    }
                });

        builder.setPositiveButton(
                context.getString(R.string.dialog_feedback_sendButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {

                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { "support@mukprojects.at" });
                        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                        
                        StringBuilder text = new StringBuilder();
                        text.append("Feeback Version: " + FEEDBACK_VERSION).append("\n");
                        text.append("Android Version: " + Build.VERSION.RELEASE).append("\n");
                        text.append("Android Model: " + Build.MODEL).append("\n");
                        
                        EditText editText = (EditText) dialoglayout
                                .findViewById(R.id.dialog_feedback_description);
                        text.append("Beschreibung:").append("\n");
                        text.append(editText.getText().toString());
                        
                        CheckBox checkBox = (CheckBox) dialoglayout
                                .findViewById(R.id.dialog_feedback_logCheck);
                        if (checkBox.isChecked()) {
                            TextView textView = (TextView) dialoglayout
                                    .findViewById(R.id.dialog_feedback_log);
                            text.append("\n").append("Log:").append("\n");
                            text.append(textView.getText().toString());
                        }

                        email.putExtra(Intent.EXTRA_TEXT, text.toString());

                        email.setType("message/rfc822");

                        context.startActivity(Intent.createChooser(
                                email,
                                context.getString(R.string.dialog_feedback_chooser)));

                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        StringBuilder logs = new StringBuilder();
        int pid = android.os.Process.myPid();
        String pidPattern = String.format("%d):", pid);
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-t", "200", "-v", "time")
                    .redirectErrorStream(true).start();

            InputStream in = null;
            try {
                in = process.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(pidPattern)) {
                        logs.append(line).append("\n");
                    }
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioException) {
                        Log.e(TAG, "Stream konnte nicht geschlossen werden.", ioException);
                    }
                }
            }
        } catch (IOException ioException) {
            Log.e(TAG, "Logs können nicht gelesen werden.", ioException);
        }

        TextView textView = (TextView) dialoglayout
                .findViewById(R.id.dialog_feedback_log);
        textView.setText(logs.toString());

        return dialog;
    }
}
