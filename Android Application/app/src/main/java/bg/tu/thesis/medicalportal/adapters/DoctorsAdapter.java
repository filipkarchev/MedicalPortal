package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import bg.tu.thesis.medicalportal.GetAsyncPicture;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.fragments.FragmentAskQuestion;
import bg.tu.thesis.medicalportal.fragments.FragmentSaveConsultation;
import bg.tu.thesis.medicalportal.items.Doctor;

/**
 * Created by filip on 22.05.16.
 */
public class DoctorsAdapter extends ArrayAdapter<Doctor> {
    Context context;
    List<Doctor> objects;

    public DoctorsAdapter(Context context, int resource,
                          List<Doctor> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Doctor item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_doctor_list, parent, false);

        if((FragmentAskQuestion.getInstance().selectedDoctor!=null && item.getId() == FragmentAskQuestion.getInstance().selectedDoctor.getId()) || (FragmentSaveConsultation.getInstance().selectedDoctor!=null && item.getId() == FragmentSaveConsultation.getInstance().selectedDoctor.getId()))
        {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }

        TextView txtName = (TextView) rowView.findViewById(R.id.txt_doctor_name);
        TextView txtSpeciality = (TextView) rowView.findViewById(R.id.txt_doctor_speciality);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rb_doctor_rating);
        ImageView ivDoctorPicture = (ImageView) rowView.findViewById(R.id.iv_doctor_picture);

        txtName.setText(item.getFirstname() + " " + item.getLastname());
        txtSpeciality.setText(item.getSpeciality());
        ratingBar.setRating((float) item.getRating());

        new GetAsyncPicture(ivDoctorPicture,item.getIconUrl()).execute();

        return rowView;
    }
}