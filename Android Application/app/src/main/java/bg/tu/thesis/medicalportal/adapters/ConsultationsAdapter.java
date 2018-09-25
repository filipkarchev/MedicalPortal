package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.items.Comment;
import bg.tu.thesis.medicalportal.items.Consultation;

/**
 * Created by filip on 23.05.16.
 */
public class ConsultationsAdapter extends ArrayAdapter<Consultation> {
    Context context;
    List<Consultation> objects;

    public ConsultationsAdapter(Context context, int resource,
                           List<Consultation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Consultation item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_consultation, parent, false);

        TextView txtDoctorName = (TextView) rowView.findViewById(R.id.txt_doctor_name);
        TextView txtAddress = (TextView) rowView.findViewById(R.id.txt_address);
        TextView txtDate = (TextView) rowView.findViewById(R.id.txt_date);
        TextView txtHour = (TextView) rowView.findViewById(R.id.txt_hour);

        txtDoctorName.setText(item.getDoctor_name());
        txtAddress.setText(item.getAddress());
        txtDate.setText(item.getDate());
        txtHour.setText(item.getHour()  +":00");

        return rowView;
    }
}
