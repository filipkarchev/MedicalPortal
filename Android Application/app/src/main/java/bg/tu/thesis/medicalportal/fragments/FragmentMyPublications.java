package bg.tu.thesis.medicalportal.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.Activities.MessageActivity;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.MessagesAdapter;
import bg.tu.thesis.medicalportal.adapters.PublicationsAdapter;
import bg.tu.thesis.medicalportal.items.Message;
import bg.tu.thesis.medicalportal.items.Publication;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 23.05.16.
 */
public class FragmentMyPublications extends Fragment {
        private Button btnPublished,btnCommented;
        private ListView lvPublications;
        private int publicationType=1;

        ArrayList<Publication> listMyPublications = new ArrayList<>();
        ArrayList<Publication> listCommentedPublications = new ArrayList<>();
        ArrayList<Publication> listSelectedPublications = new ArrayList<>();


        PublicationsAdapter adapterMyPublications;
        PublicationsAdapter adapterCommentedPublications;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_lists,container,false);

            btnCommented = (Button) rootView.findViewById(R.id.btn_second);
            btnPublished = (Button) rootView.findViewById(R.id.btn_first);
            lvPublications = (ListView) rootView.findViewById(R.id.lv_items);
            TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);


            txtTitle.setText("Publications");
            btnCommented.setText("Commented");
            btnPublished.setText("Published");

            //Get All Messages
            getAllMyPublications();

            adapterMyPublications = new PublicationsAdapter(getActivity(),0,listMyPublications);
            adapterCommentedPublications = new PublicationsAdapter(getActivity(),0,listSelectedPublications);

            lvPublications.setAdapter(adapterMyPublications);
            lvPublications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("publication", listSelectedPublications.get(position));

                    Fragment publicationDetails = new FragmentPublication();
                    publicationDetails.setArguments(bundle);
                    getActivity().getFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)
                            .replace(R.id.container, publicationDetails,"publication")
                            .addToBackStack("tag")
                            .commit();
                }
            });

            btnCommented.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publicationType = 2;
                    handleChangePublicationsType();
                }
            });

            btnPublished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publicationType = 1;
                    handleChangePublicationsType();
                }
            });
            return rootView;
        }


        private void getAllMyPublications() {

            String url = Utils.baseUrl + "publications_mine_get.php";

            HashMap<String,String> params = new HashMap<>();
            params.put("user_id",""+Utils.getUserId());

            MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("MyMessagesFragment","onResponse: "+response.toString());
                    boolean isok = response.optBoolean("isok",false);
                    if(isok==true)
                    {
                        listMyPublications.clear();
                        listCommentedPublications.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for(int i=0;i<array.length();i++)
                            {
                                Publication item = new Publication((JSONObject) array.get(i));

                                if(item.getCreator_id()==Utils.getUserId())
                                {
                                    listMyPublications.add(item);
                                }
                                else
                                {
                                    listCommentedPublications.add(item);
                                }

                            }
                            listSelectedPublications =listMyPublications;
                            adapterMyPublications.notifyDataSetChanged();
                            adapterCommentedPublications.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        listMyPublications.clear();
                        listCommentedPublications.clear();
                        listSelectedPublications.clear();
                        adapterMyPublications.notifyDataSetChanged();
                        adapterCommentedPublications.notifyDataSetChanged();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            },params,false,false);
        }


        private void handleChangePublicationsType() {
            if(publicationType==1)
            {
                btnPublished.setBackgroundColor(getResources().getColor(R.color.blue));
                btnPublished.setTextColor(getResources().getColor(R.color.white));

                btnCommented.setBackgroundColor(getResources().getColor(R.color.white));
                btnCommented.setTextColor(getResources().getColor(R.color.black));

                lvPublications.setAdapter(adapterMyPublications);
                listSelectedPublications = listMyPublications;
            }
            else
            {
                btnPublished.setBackgroundColor(getResources().getColor(R.color.white));
                btnPublished.setTextColor(getResources().getColor(R.color.black));

                btnCommented.setBackgroundColor(getResources().getColor(R.color.blue));
                btnCommented.setTextColor(getResources().getColor(R.color.white));

                lvPublications.setAdapter(adapterCommentedPublications);
                listSelectedPublications = listCommentedPublications;
            }
        }
    }