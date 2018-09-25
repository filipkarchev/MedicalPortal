package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.items.Publication;

/**
 * Created by filip on 21.05.16.
 */
public class PublicationsAdapter extends ArrayAdapter<Publication> {
    Context context;
    List<Publication> objects;

    public PublicationsAdapter(Context context, int resource,
                             List<Publication> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Publication item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_publication_search, parent, false);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_publication_title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.txt_publication_desc);
        TextView txtSeen = (TextView) rowView.findViewById(R.id.txt_seen_count);
        TextView txtAnswers = (TextView) rowView.findViewById(R.id.txt_answers_count);

        txtTitle.setText(item.getTitle());
        txtDesc.setText(item.getDescription());
        txtSeen.setText(""+item.getSeen());
        txtAnswers.setText(""+item.getComments());

        return rowView;
    }
}
