package at.mukprojects.vuniapp.activities.subjects;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.models.Subject;

/**
 * Adapter zum Anzeigen der F&auml;cher in einer ListView.
 * 
 * @author kerrim
 * @author Mathias
 */
public class SubjectAdapter extends MainListBaseAdapter<Subject> {

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Ergebnisliste der Subjects.
     */
    public SubjectAdapter(final Context context, final ArrayList<Subject> list) {
        super(context, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        convertView = inflator.inflate(R.layout.adapter_subjects_subjectlist,
                parent, false);

        holder = new ViewHolder();
        holder.subject = (TextView) convertView
                .findViewById(R.id.adapter_subjects_subject_Subject);
        holder.info = (TextView) convertView
                .findViewById(R.id.adapter_subjects_subject_Info);

        convertView.setTag(holder);

        Subject subject = (Subject) getItem(position);

        holder.subject.setText(((subject.getType() != null) ? (subject
                .getType() + " ") : "") + subject.getName());
        holder.info.setText(subject.getInfo());

        return convertView;
    }

    /**
     * ViewHolder f&uuml;r die anzuzeigenden F&auml;cher.
     * 
     * @author kerrim
     */
    static class ViewHolder {
        private TextView subject;
        private TextView info;
    }
}