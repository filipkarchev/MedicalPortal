package bg.tu.thesis.medicalportal.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.CommentsAdapter;
import bg.tu.thesis.medicalportal.items.Comment;
import bg.tu.thesis.medicalportal.items.Publication;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 21.05.16.
 */
public class FragmentPublication extends Fragment {
    private TextView txtTitel;
    private TextView txtDesc;
    private TextView txtVote,txtCreatorName;
    private ListView lvComments;
    private Button btnAddComment;
    private ImageView ivLike,ivDislike,ivImage;

    CommentsAdapter adapter;
    Publication publication;
    ArrayList<Comment> listComments = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_publication, container, false);
        txtTitel = (TextView) rootView.findViewById(R.id.txt_publication_title);
        txtDesc = (TextView) rootView.findViewById(R.id.txt_publication_desc);
        txtVote = (TextView) rootView.findViewById(R.id.txt_vote);
        lvComments = (ListView) rootView.findViewById(R.id.lv_comments);
        btnAddComment= (Button) rootView.findViewById(R.id.btn_add_comment);
        ivLike = (ImageView) rootView.findViewById(R.id.iv_like);
        ivDislike = (ImageView) rootView.findViewById(R.id.iv_dislike);
        ivImage = (ImageView) rootView.findViewById(R.id.iv_creator);
        txtCreatorName = (TextView) rootView.findViewById(R.id.txt_creator_name);


        publication =(Publication) getArguments().getSerializable("publication");
        txtTitel.setText(publication.getTitle());
        txtDesc.setText(publication.getDescription());
        txtVote.setText(""+publication.getVotes());


        adapter = new CommentsAdapter(getActivity(),0,listComments);
        lvComments.setAdapter(adapter);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getUserId()==0)
                {
                    Toast.makeText(getActivity(),"You should log in",Toast.LENGTH_LONG).show();
                    return;
                }
                openDialogAddNewComment();
            }
        });

        downloadComments();

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getUserId()==0)
                {
                    Toast.makeText(getActivity(),"You should log in",Toast.LENGTH_LONG).show();
                    return;
                }

                sendVote(1,publication);
            }
        });

        ivDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getUserId()==0)
                {
                    Toast.makeText(getActivity(),"You should log in",Toast.LENGTH_LONG).show();
                    return;
                }
                sendVote(-1,publication);
            }
        });

        new PublicationSeenTask().execute();

        return rootView;
    }

    private void sendVote(final int vote, final Publication publication) {
        String url= Utils.baseUrl + "publication_rating_add.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("publication_id",""+publication.getId());
        params.put("user_id",""+Utils.getUserId());
        params.put("vote",""+vote);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("CommentsAdapter","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(getActivity(),"Your vote is sent",Toast.LENGTH_LONG).show();
                    publication.setVotes(publication.getVotes() + vote);
                    txtVote.setText(""+publication.getVotes());
                }else if(response.optString("msg","").equals("you have alrady voted"))
                {
                    Toast.makeText(getActivity(),"Your have already voted",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void openDialogAddNewComment() {
        final Dialog dialogAddComment = new Dialog(getActivity(),
            R.style.DialogTheme);
        dialogAddComment
                .setContentView(R.layout.dialog_add_comment);

        Button btnCancel = (Button) dialogAddComment
                .findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddComment.dismiss();
            }
        });

        Button btnSaveTurnOff = (Button) dialogAddComment
                .findViewById(R.id.btn_save);
        final EditText etComment = (EditText) dialogAddComment
                .findViewById(R.id.et_add_comment);
        // if button is clicked, close the custom dialog
        btnSaveTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment(etComment.getText().toString());

                dialogAddComment.dismiss();
            }


        });

        dialogAddComment.show();
    }

    private void saveComment(String comment) {
        String url = Utils.baseUrl + "comment_add.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("publication_id",""+publication.getId());
        params.put("creator_id",""+Utils.getUserId());
        params.put("comment",comment);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    downloadComments();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void downloadComments() {
        String url = Utils.baseUrl + "comments_get.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("publication_id",""+publication.getId());

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentPublication","downloadComments: " + response.toString());

                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listComments.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            Comment item = new Comment((JSONObject) array.get(i));
                            listComments.add(item);

                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    listComments.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private  class PublicationSeenTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] parameters) {
            String url = Utils.baseUrl + "publication_seen.php";

            HashMap<String,String> params = new HashMap<>();
            params.put("publication_id",publication.getId()+"");

            MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("FragmentPublication","onResponse seen " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            },params,false,false);
            return null;
        }
    }
}
