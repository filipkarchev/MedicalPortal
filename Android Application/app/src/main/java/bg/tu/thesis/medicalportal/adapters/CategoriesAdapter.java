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
import bg.tu.thesis.medicalportal.fragments.FragmentAskQuestion;
import bg.tu.thesis.medicalportal.fragments.FragmentSaveConsultation;
import bg.tu.thesis.medicalportal.fragments.FragmentSearch;
import bg.tu.thesis.medicalportal.items.Category;

/**
 * Created by filip on 21.05.16.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {
    Context context;
    List<Category> objects;
    int type;

    public CategoriesAdapter(Context context, int resource,
                             List<Category> objects, int type) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.type=type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Category item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_categories, parent, false);

        TextView txtName = (TextView) rowView.findViewById(R.id.txt_cat_name);
        CheckBox cbCat = (CheckBox) rowView.findViewById(R.id.cb_cat);

        txtName.setText(item.getName());
        cbCat.setChecked(item.isChecked());

        cbCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(type==1) {
                    FragmentSearch.listCategories.get(position).setChecked(isChecked);
                }
                else if(type==2)
                {
                    FragmentAskQuestion.getInstance().callOnCategoryClicked(position);
                }
                else if(type==3)
                {
                    FragmentAskQuestion.getInstance().downloadDoctrosList(position);
                }
                else if(type==4)
                {
                    FragmentSaveConsultation.getInstance().categorySelected(position);
                }
            }
        });

        return rowView;
    }
}