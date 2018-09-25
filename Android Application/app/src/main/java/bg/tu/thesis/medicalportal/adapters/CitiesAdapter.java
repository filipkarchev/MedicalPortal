package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.fragments.FragmentSaveConsultation;
import bg.tu.thesis.medicalportal.items.City;

/**
 * Created by filip on 23.05.16.
 */
public class CitiesAdapter extends ArrayAdapter<City> {
    Context context;
    List<City> objects;

    public CitiesAdapter(Context context, int resource,
                         List<City> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        City item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_categories, parent, false);

        TextView txtName = (TextView) rowView.findViewById(R.id.txt_cat_name);
        CheckBox cbCat = (CheckBox) rowView.findViewById(R.id.cb_cat);

        txtName.setText(item.getName());
        cbCat.setChecked(item.isSelected());

        cbCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FragmentSaveConsultation.getInstance().citySelected(position);
            }
        });

        return rowView;
    }
}