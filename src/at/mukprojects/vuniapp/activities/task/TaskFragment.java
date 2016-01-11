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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.searchprofessor.ProfessorDetailsFragment;
import at.mukprojects.vuniapp.activities.searchprofessor.SearchProfessorActivity;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.LoadFragmentRequest;
import at.mukprojects.vuniapp.activityfragmentcommunication.request.ReloadListRequest;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.enumeration.TaskColor;
import at.mukprojects.vuniapp.exceptions.NoTwoPaneException;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.Task;
import at.mukprojects.vuniapp.models.TaskDate;
import at.mukprojects.vuniapp.widget.task.TaskWidget;

/**
 * Mit Hilfe des TaskFragments k&ouml;nnen neue Task angelegt und alte
 * bearbeitet werden.
 * 
 * @author Mathias
 */
public class TaskFragment extends VUniAppFragment {
    private static final long                  serialVersionUID      = 1L;
    private static final String                TAG                   = TaskFragment.class
                                                                             .getSimpleName();
    private static final int                   REQUEST_IMAGE_CAPTURE = 1;
    public static final String                 IMAGE                 = "image";

    private TaskFragment                       fragment              = this;
    private Task                               bundleTask;
    private TaskDate                           taskDate;
    private String                             currentPhotoPath;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

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
        return inflater.inflate(R.layout.fragment_task, container, false);
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
        if (bundle != null && bundle.getSerializable(Task.TASK) != null) {
            bundleTask = (Task) bundle.getSerializable(Task.TASK);
            setUpContentWithBundleTask(bundleTask);
        } else {
            ((Button) getView().findViewById(R.id.fragment_task_deleteButton))
                    .setEnabled(false);
        }

        TaskColor stdColor = TaskColor.WHITE;
        if (bundleTask != null && bundleTask.getColor() != null) {
            stdColor = bundleTask.getTaskColor();
        }

        getView().findViewById(R.id.fragment_task_colorView)
                .setBackgroundColor(Color.parseColor(stdColor.displayValue()));

        /*
         * FIXME M: NullPointer TaskColorAdapter colorAdapter = (TaskColorAdapter)
         * ((Spinner) getView()
         * .findViewById(R.id.fragment_task_colorSpinner)).getAdapter();
         * List<TaskColor> colorList = colorAdapter.getItemList(); for (int i =
         * 0; i < colorList.size(); i++) { if
         * (colorList.get(i).equals(bundleTask.getTaskColor())) { ((Spinner)
         * getView().findViewById(
         * R.id.fragment_task_colorSpinner)).setSelection(i); break; } }
         */

        TaskColorAdapter colorSpinnerAdapter = new TaskColorAdapter(
                getActivity(), TaskColor.getAllColors());
        ((Spinner) getView().findViewById(R.id.fragment_task_colorSpinner))
                .setAdapter(colorSpinnerAdapter);

        ((Button) getView().findViewById(R.id.fragment_task_saveButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        SaveTaskTask saveTaskTask = new SaveTaskTask(
                                getActivity(), fragment, getInputTask());
                        saveTaskTask.execute();
                    }
                });

        final Calendar calendar = Calendar.getInstance();
        ((Button) getView().findViewById(R.id.fragment_task_pickTimeButton))
                .setEnabled(false);

        ((Button) getView().findViewById(R.id.fragment_task_pickDateButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        (new DatePickerDialog(getActivity(), dateSetListener,
                                calendar.get(Calendar.YEAR), calendar
                                        .get(Calendar.MONTH), calendar
                                        .get(Calendar.DAY_OF_MONTH))).show();
                    }
                });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year,
                    final int monthOfYear, final int dayOfMonth) {
                ((Button) getView().findViewById(
                        R.id.fragment_task_pickTimeButton)).setEnabled(true);
                taskDate = new TaskDate(year, monthOfYear, dayOfMonth);
                ((TextView) getView().findViewById(
                        R.id.fragment_task_dateShowText)).setText(taskDate
                        .toString());
            }
        };

        ((Button) getView().findViewById(R.id.fragment_task_pickTimeButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        (new TimePickerDialog(getActivity(), timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY), calendar
                                        .get(Calendar.MINUTE), true)).show();
                    }
                });

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker view, final int hourOfDay,
                    final int minute) {
                taskDate.setHour(hourOfDay);
                taskDate.setMinute(minute);
                ((TextView) getView().findViewById(
                        R.id.fragment_task_dateShowText)).setText(taskDate
                        .toString());
            }
        };

        ((Button) getView().findViewById(R.id.fragment_task_addImageButton))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        dispatchTakePictureIntent();
                    }
                });

        ((ImageView) getView().findViewById(R.id.fragment_task_imageShow))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (isTwoPaneWithListener()) {
                            try {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(IMAGE, currentPhotoPath);
                                getFragmentListener()
                                        .invoke(new LoadFragmentRequest(
                                                new TaskImageFragment(), bundle));
                            } catch (NoTwoPaneException noTwoPaneException) {
                                Log.e(TAG, noTwoPaneException.getMessage(),
                                        noTwoPaneException);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    TaskImageActivity.class);
                            intent.putExtra(IMAGE, currentPhotoPath);
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * Liefert den InputTask.
     * 
     * @return Den InputTask.
     */
    private Task getInputTask() {
        long taskTime = 0;
        if (taskDate != null) {
            taskTime = taskDate.getDate().getTime();
        }
        Task inputTask = new Task(((EditText) getView().findViewById(
                R.id.fragment_task_titleEditText)).getText().toString(),
                ((EditText) getView().findViewById(
                        R.id.fragment_task_descriptionEditText)).getText()
                        .toString(),
                ((TaskColor) ((Spinner) getView().findViewById(
                        R.id.fragment_task_colorSpinner)).getSelectedItem())
                        .displayValue(), taskTime, currentPhotoPath);
        if (bundleTask != null && bundleTask.getId() != null) {
            inputTask.setId(bundleTask.getId());
        }
        return inputTask;
    }

    /**
     * Der Content wird mit Hilfe des BundleTaks bef&uuml;llt.
     * 
     * @param bundleTask
     *            Der BundleTask zum bef&uuml;llen des Contents.
     */
    private void setUpContentWithBundleTask(final Task bundleTask) {
        Log.d(TAG, "Fragment wird mit Hilfe des BundleTasks befüllt.");
        if (bundleTask.getId() != null) {
            Log.d(TAG, "Der BundleTask enthält eine ID - DatenspeicherTask.");
            ((Button) getView().findViewById(R.id.fragment_task_deleteButton))
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            DeleteTaskTask deleteTaskTask = new DeleteTaskTask(
                                    getActivity(), fragment, bundleTask.getId());
                            deleteTaskTask.execute();
                        }
                    });
        } else {
            ((Button) getView().findViewById(R.id.fragment_task_deleteButton))
                    .setEnabled(false);
        }

        ((EditText) getView().findViewById(R.id.fragment_task_titleEditText))
                .setText(bundleTask.getTitle());
        ((EditText) getView().findViewById(
                R.id.fragment_task_descriptionEditText)).setText(bundleTask
                .getDescription());

        if (bundleTask.getDeadlineDate() != null) {
            Date date = new Date(bundleTask.getDeadlineDate());
            taskDate = new TaskDate(date.getYear(), date.getMonth(),
                    date.getDay(), date.getHours(), date.getMinutes());
            ((TextView) getView().findViewById(R.id.fragment_task_dateShowText))
                    .setText(taskDate.toString());
        }

        if (bundleTask.getImagePath() != null) {
            currentPhotoPath = bundleTask.getImagePath();
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            ((ImageView) getView().findViewById(R.id.fragment_task_imageShow))
                    .setImageBitmap(bitmap);
        }
    }

    /**
     * Sendet wenn m&ouml;glich ein ListItemRequest an die ParrentActivity.
     */
    public final void sendRealodListRequest() {
        /* FIXME M: ...*/
        /** Update Widget. */
        /*
        Intent intent = new Intent(getActivity(), TaskWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = { R.xml.widget_task };
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent); */
        
        if (isTwoPaneWithListener()) {
            try {
                getFragmentListener().invoke(new ReloadListRequest());
            } catch (NoTwoPaneException noTwoPaneException) {
                Log.e(TAG, noTwoPaneException.getMessage(), noTwoPaneException);
            }
        } else {
            getActivity().finish();
        }
    }

    /**
     * Erstellt das ImageFile.
     * 
     * @return Das ImageFile.
     * @throws IOException
     *             Wird geworfen, wenn es beim Erstellen des Files zu einem
     *             Fehler kommt.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "VUniApp_TaskImage_" + timeStamp;
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "Image gespeichert unter: " + currentPhotoPath);
        return image;
    }

    /**
     * Schickt ein Intent, um ein Foto aufzunehmen.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent
                .resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
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
        if (requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "Foto erhalten. - " + currentPhotoPath);
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            ((ImageView) getView().findViewById(R.id.fragment_task_imageShow))
                    .setImageBitmap(bitmap);
        }
    }
}
