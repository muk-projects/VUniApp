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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;

/**
 * Fragment, mit welchem eine TaskImage angezeigt werden kann.
 * 
 * @author Mathias
 */
public class TaskImageFragment extends VUniAppFragment {
    private static final String TAG              = TaskImageFragment.class
                                                         .getSimpleName();
    private static final long   serialVersionUID = 1L;

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
        return inflater.inflate(R.layout.fragment_task_image, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public final void onStart() {
        Log.d(TAG, "Die Methode onCreate wird gestartet.");
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null
                && bundle.getSerializable(TaskFragment.IMAGE) != null) {
            String path = (String) bundle.getSerializable(TaskFragment.IMAGE);
            
            Log.d(TAG, "Bild mit dem Pfad: " + path + " laden.");
            ImageView imageView = (ImageView) getView().findViewById(
                    R.id.fragment_task_image_Image);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }

        Log.d(TAG, "Die Methode onCreate wird verlassen.");
    }

}
