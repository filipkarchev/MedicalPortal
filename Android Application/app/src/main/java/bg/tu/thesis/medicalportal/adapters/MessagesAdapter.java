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
import bg.tu.thesis.medicalportal.items.Message;

/**
 * Created by filip on 22.05.16.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {
    Context context;
    List<Message> objects;

    public MessagesAdapter(Context context, int resource,
                           List<Message> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Message item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_message, parent, false);

        //if it is not seen,color it
        if(item.getSeen()==0)
        {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }

        TextView txtSender = (TextView) rowView.findViewById(R.id.txt_sender);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_message_title);
        TextView txtDate = (TextView) rowView.findViewById(R.id.txt_date);

        txtSender.setText(item.getSenderName());
        txtTitle.setText(item.getTheme());
        txtDate.setText(item.getDate());

        return rowView;
    }
}
