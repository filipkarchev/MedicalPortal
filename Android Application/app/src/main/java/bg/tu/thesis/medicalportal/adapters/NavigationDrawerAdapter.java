package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.items.DrawerItem;

public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {
    List<DrawerItem> items;

    public NavigationDrawerAdapter(Context context, int resource, List<DrawerItem> items) {
        super(context, resource,items);
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(view==null)
        {
            LayoutInflater inflater=null;
            inflater= LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.item_nav_drawer,null);
        }

        if(position%2==0)
        {view.setBackgroundColor(getContext().getResources().getColor(R.color.blue));}
        else view.setBackgroundColor(getContext().getResources().getColor(R.color.sky_blue));

        DrawerItem item = getItem(position);

        ImageView img
                = (ImageView) view.findViewById(R.id.img_drawer_icon);
        TextView txtItemTitle = (TextView) view.findViewById(R.id.txt_drawer_item_text);

        ImageView imgNew
                = (ImageView) view.findViewById(R.id.iv_new);
        if(item.getNewItem()!=0)
        {
            imgNew.setVisibility(View.VISIBLE);
        }

        if (img != null) {
            img.setImageBitmap(item.getImage());
        }

        if (txtItemTitle != null) {
            txtItemTitle.setText(item.getText());
        }
        return view;
    }

    @Override
    public DrawerItem getItem(int position) {
      return  items.get(position);
    }
}
