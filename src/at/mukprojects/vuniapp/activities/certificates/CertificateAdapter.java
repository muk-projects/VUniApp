package at.mukprojects.vuniapp.activities.certificates;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Adapter zum Anzeigen der Zeugnisse in einer ListView.
 * 
 * @author kerrim
 * @author Mathias
 */
public class CertificateAdapter extends
        MainListBaseAdapter<CertificateEntry> {

    /**
     * Erzeugt einen neuen CertificateAdapter f&uuml;r die Anzeige der Zeugnisse
     * in der ListView.
     * 
     * @param context
     *            Context der Activity.
     * @param certificates
     *            Liste der anzuzeigenden Zeugnisse.
     */
    public CertificateAdapter(
            final Context context,
            final ArrayList<CertificateEntry> certificates) {
        super(context, certificates);
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
        View convertView = null;

        convertView = inflator.inflate(
                R.layout.adapter_certificates_certificatelist, parent, false);

        holder = new ViewHolder();
        holder.subject = (TextView) convertView
                .findViewById(R.id.adapter_certificates_subject);
        holder.grade = (TextView) convertView
                .findViewById(R.id.adapter_certificates_grade);

        convertView.setTag(holder);

        Entry<Certificate, CertificateInterface> certificate = (CertificateEntry) getItem(position);

        holder.subject.setText(certificate.getKey().getType() + " "
                + certificate.getKey().getTitle());
        holder.grade.setText(certificate.getValue().getDisplayedNameToGrade(
                certificate.getKey().getGrade()));

        return convertView;
    }

    /**
     * ViewHolder f&uuml;r die anzuzeigenden F&auml;cher.
     * 
     * @author kerrim
     */
    static class ViewHolder {
        private TextView subject;
        private TextView grade;
    }
}