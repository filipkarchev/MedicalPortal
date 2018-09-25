package bg.tu.thesis.medicalportal.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.CategoriesAdapter;
import bg.tu.thesis.medicalportal.adapters.PublicationsAdapter;
import bg.tu.thesis.medicalportal.items.Category;
import bg.tu.thesis.medicalportal.items.Publication;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 20.05.16.
 */
public class FragmentSearch extends Fragment {

    private ListView lvPublications;
    private GridView gvCategories;
    private Button btnSearch;
    private EditText etSearch;
    private ImageView ivShowHide;

    private ArrayList<Publication> listPublications = new ArrayList<>();
    public static ArrayList<Category> listCategories = new ArrayList<>();
    CategoriesAdapter catAdapter;
    PublicationsAdapter publicationsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        lvPublications = (ListView) rootView.findViewById(R.id.lv_publications);
        gvCategories = (GridView) rootView.findViewById(R.id.gv_categories);
        ivShowHide = (ImageView) rootView.findViewById(R.id.iv_arrow);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        etSearch =  (EditText) rootView.findViewById(R.id.et_search);

        listCategories = new ArrayList<>();
        listCategories = Utils.getListCategories(false);
        catAdapter = new CategoriesAdapter(getActivity(),0,listCategories,1);
        gvCategories.setAdapter(catAdapter);

        publicationsAdapter = new PublicationsAdapter(getActivity(),0,listPublications);
        lvPublications.setAdapter(publicationsAdapter);

        ivShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gvCategories.getVisibility()==View.VISIBLE)
                {
                    gvCategories.setVisibility(View.GONE);
                    ivShowHide.setImageResource(R.drawable.ic_down);
                }
                else
                {
                    gvCategories.setVisibility(View.VISIBLE);
                    ivShowHide.setImageResource(R.drawable.ic_up);
                }
            }
        });

        startNewSearch("",listCategories);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewSearch(etSearch.getText().toString(),listCategories);

                gvCategories.setVisibility(View.GONE);
                ivShowHide.setImageResource(R.drawable.ic_down);

                //Hide keybord
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        //When user on some publication
        lvPublications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open details page for this publication
                Bundle bundle = new Bundle();
                bundle.putSerializable("publication", listPublications.get(position));

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
        return rootView;
    }

    private void startNewSearch(String text, ArrayList<Category> itemCategories) {

        String url = Utils.baseUrl  + "publication_get.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("text",text);
        String strCategories="";

        for (int i=0;i<itemCategories.size();i++) {
            if(itemCategories.get(i).isChecked()==true)
            {
              strCategories = strCategories + itemCategories.get(i).getId() + ",";
            }
        }

        //Remove last ,
        if(!strCategories.equals(""))
        strCategories = strCategories.substring(0, strCategories.length()-1);
        else  strCategories ="-1";

        params.put("categories",strCategories);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            Log.i("Fragment search","search response: " + response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listPublications.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            Publication item = new Publication((JSONObject) array.get(i));
                            listPublications.add(item);

                        }
                        publicationsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    listPublications.clear();
                    publicationsAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }
}
