package bg.tu.thesis.medicalportal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.items.Comment;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 21.05.16.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    Context context;
    List<Comment> objects;

    public CommentsAdapter(Context context, int resource,
                             List<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       Comment item = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_comment, parent, false);

        TextView txtText = (TextView) rowView.findViewById(R.id.txt_comment_title);
        TextView txtVotes = (TextView) rowView.findViewById(R.id.txt_vote);
        ImageView ivLike = (ImageView) rowView.findViewById(R.id.iv_like);
        ImageView ivDislike = (ImageView) rowView.findViewById(R.id.iv_dislike);

        txtText.setText(item.getText());
        Log.i("CommentsAdapter","votes " + item.getVotes());
        txtVotes.setText(""+item.getVotes());

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getUserId()==0)
                {
                    Toast.makeText(context,"You should log in",Toast.LENGTH_LONG).show();
                    return;
                }

                sendVote(1,position);
            }
        });

        ivDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getUserId()==0)
                {
                    Toast.makeText(context,"You should log in",Toast.LENGTH_LONG).show();
                    return;
                }

                sendVote(-1,position);
            }
        });


        return rowView;
    }

    private void sendVote(final int vote,final int position) {

        String url= Utils.baseUrl + "comment_rating_add.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("comment_id",""+objects.get(position).getId());
        params.put("user_id",""+Utils.getUserId());
        params.put("vote",""+vote);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("CommentsAdapter","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(context,"Your vote is sent",Toast.LENGTH_LONG).show();
                    objects.get(position).setVotes(objects.get(position).getVotes() + vote);
                    notifyDataSetChanged();
                }else if(response.optString("msg","").equals("you have alrady voted"))
                {
                    Toast.makeText(context,"Your have already voted",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }
}