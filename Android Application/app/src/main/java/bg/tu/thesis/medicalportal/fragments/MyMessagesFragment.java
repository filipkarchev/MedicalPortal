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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.Activities.BaseActivity;
import bg.tu.thesis.medicalportal.Activities.MessageActivity;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.MessagesAdapter;
import bg.tu.thesis.medicalportal.items.Message;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 22.05.16.
 */
public class MyMessagesFragment extends Fragment {
    private Button btnSentMessages,btnReceivedMessages;
    private ListView lvMessages;
    private int messagesType=1;

    ArrayList<Message> listReceivedMessages = new ArrayList<>();
    ArrayList<Message> listSentMessages = new ArrayList<>();
    ArrayList<Message> listSelectedMessages = new ArrayList<>();


    MessagesAdapter adapterReceivedMessages;
    MessagesAdapter adapterSentMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_lists,container,false);

        btnSentMessages = (Button) rootView.findViewById(R.id.btn_second);
        btnReceivedMessages = (Button) rootView.findViewById(R.id.btn_first);
        lvMessages = (ListView) rootView.findViewById(R.id.lv_items);

     /*   //Get All Messages
        getAllMessages();*/

        adapterReceivedMessages = new MessagesAdapter(getActivity(),0,listReceivedMessages);
        adapterSentMessages = new MessagesAdapter(getActivity(),0,listSentMessages);

        lvMessages.setAdapter(adapterReceivedMessages);
        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent messageIntent = new Intent(getActivity(), MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("message",listSelectedMessages.get(position));
                messageIntent.putExtras(bundle);
                startActivity(messageIntent);
            }
        });

        btnSentMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesType = 2;
                handleChangeMessagesType();
            }
        });

        btnReceivedMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesType = 1;
                handleChangeMessagesType();
            }
        });
        return rootView;
    }


    private void getAllMessages() {

        String url = Utils.baseUrl + "messages_get.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",""+Utils.getUserId());

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("MyMessagesFragment","onResponse: "+response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listReceivedMessages.clear();
                    listSentMessages.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            Message item = new Message((JSONObject) array.get(i));

                         //   if(item.getSenderId()==Utils.getUserId())
                            if(item.getType()==Utils.getProfileType())
                            {
                                listSentMessages.add(item);
                            }
                            else
                            {
                                listReceivedMessages.add(item);
                            }

                        }
                        if(messagesType==1)
                           listSelectedMessages =listReceivedMessages;
                        else
                            listSelectedMessages =listSentMessages;

                        adapterReceivedMessages.notifyDataSetChanged();
                        adapterSentMessages.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    listSelectedMessages.clear();
                    listSentMessages.clear();
                    listReceivedMessages.clear();
                    adapterReceivedMessages.notifyDataSetChanged();
                    adapterSentMessages.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }


    private void handleChangeMessagesType() {
        if(messagesType==1)
        {
            btnReceivedMessages.setBackgroundColor(getResources().getColor(R.color.blue));
            btnReceivedMessages.setTextColor(getResources().getColor(R.color.white));

            btnSentMessages.setBackgroundColor(getResources().getColor(R.color.white));
            btnSentMessages.setTextColor(getResources().getColor(R.color.black));

            lvMessages.setAdapter(adapterReceivedMessages);
            listSelectedMessages = listReceivedMessages;
        }
        else
        {
            btnReceivedMessages.setBackgroundColor(getResources().getColor(R.color.white));
            btnReceivedMessages.setTextColor(getResources().getColor(R.color.black));

            btnSentMessages.setBackgroundColor(getResources().getColor(R.color.blue));
            btnSentMessages.setTextColor(getResources().getColor(R.color.white));

            lvMessages.setAdapter(adapterSentMessages);
            listSelectedMessages = listSentMessages;
        }
    }

    @Override
    public void onResume() {
        Log.i("MyMessagesFragment","OnResume");

        getAllMessages();
        ((BaseActivity) getActivity()).drawerListCat.get(0).setNewItem(0);
        ((BaseActivity) getActivity()).adapter.notifyDataSetChanged();
        super.onResume();
    }
}
