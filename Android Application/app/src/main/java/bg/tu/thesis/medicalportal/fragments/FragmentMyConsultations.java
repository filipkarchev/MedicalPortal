package bg.tu.thesis.medicalportal.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.ConsultationsAdapter;
import bg.tu.thesis.medicalportal.adapters.PublicationsAdapter;
import bg.tu.thesis.medicalportal.items.Consultation;
import bg.tu.thesis.medicalportal.items.Publication;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 23.05.16.
 */
public class FragmentMyConsultations extends Fragment {
    private Button btnIncomming,btnPassed;
    private ListView lvConsultations;
    private int consultationType=1;

    ArrayList<Consultation> listIncommingconsultations = new ArrayList<>();
    ArrayList<Consultation> listPassedConsultations = new ArrayList<>();
    ArrayList<Consultation> listSelectedConsultations = new ArrayList<>();


    ConsultationsAdapter adapterIncommingConsultations;
    ConsultationsAdapter adapterPassedConsultations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_lists,container,false);

        btnPassed = (Button) rootView.findViewById(R.id.btn_second);
        btnIncomming = (Button) rootView.findViewById(R.id.btn_first);
        lvConsultations = (ListView) rootView.findViewById(R.id.lv_items);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);


        txtTitle.setText("Consultations");
        btnIncomming.setText("Incommming");
        btnPassed.setText("Passed");

        //Get All Messages
        getAllMyConsultations();

        adapterIncommingConsultations = new ConsultationsAdapter(getActivity(),0,listIncommingconsultations);
        adapterPassedConsultations = new ConsultationsAdapter(getActivity(),0,listPassedConsultations);

        lvConsultations.setAdapter(adapterIncommingConsultations);
        lvConsultations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             if(Utils.getProfileType()==1)
             {
                 openDialogShowMap(listSelectedConsultations.get(position));
             }
             else {
                 makePhoneCall(listSelectedConsultations.get(position));
             }
            }
        });

        btnPassed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultationType = 2;
                handleChangePublicationsType();
            }
        });

        btnIncomming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultationType = 1;
                handleChangePublicationsType();
            }
        });
        return rootView;
    }

    Dialog dialogOpenMap;
    GoogleMap googleMap;
    private void openDialogShowMap(Consultation consultation) {
        getConsultationCoordinates(consultation);

        dialogOpenMap = new Dialog(getActivity(),R.style.DialogTheme);
        dialogOpenMap.setCancelable(false);
        dialogOpenMap.setContentView(R.layout.activity_select_address);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();

        Button btnCancel =(Button) dialogOpenMap.findViewById(R.id.btn_save);
        btnCancel.setText("Close");
        btnCancel.setVisibility(View.VISIBLE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpenMap.dismiss();

                MapFragment f = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map);
                if (f != null)
                    getFragmentManager().beginTransaction().remove(f).commit();
            }
        });

        dialogOpenMap.show();

    }

    private void getConsultationCoordinates(final Consultation consultation) {
        String url = Utils.baseUrl + "consultation_get_coordinates.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("doctor_id",""+consultation.getDoctorId());
        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentMyConsultations","onResposne: " + response.toString());
                try {
                    JSONObject object = (JSONObject) response.getJSONArray("data").get(0);
                    double latitude = object.optDouble("latitude",0);
                    double longitude = object.optDouble("longitude",0);

                      if(dialogOpenMap!=null && dialogOpenMap.isShowing()==true && googleMap!=null && latitude!=0 && longitude!=0)
                      {
                          LatLng position = new LatLng(latitude,longitude);
                          googleMap.addMarker(new MarkerOptions().position(position).title(consultation.getAddress()));
                          CameraPosition cameraPosition = new CameraPosition.Builder()
                                  .target(position)      // Sets the center of the map to location user
                                  .zoom(17)                   // Sets the zoom
                                  .bearing(90)                // Sets the orientation of the camera to east
                                  .tilt(70)                   // Sets the tilt of the camera
                                  .build();
                          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                      }
                    else
                      {
                          Toast.makeText(getActivity(),"Can not show address",Toast.LENGTH_LONG).show();
                      }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);

    }

    private void makePhoneCall(final Consultation consultation) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to call the patient")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       getPatientNumber(consultation);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();

    }

    ProgressDialog pd;
    private void getPatientNumber(Consultation consultation) {
        pd = new ProgressDialog(getActivity());
        pd.show();

        String url = Utils.baseUrl + "user_get_phone.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",""+consultation.getUserId());
        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentMyConsultations","onResposne: " + response.toString());
                try {
                    String phone =((JSONObject) response.getJSONArray("data").get(0)).optString("phone","");
                    if(!phone.equals(""))
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +phone));
                        startActivity(intent);
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }


    private void getAllMyConsultations() {

        String url = Utils.baseUrl + "list_consultations_get.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",""+Utils.getUserId());

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("MyMessagesFragment","onResponse: "+response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listIncommingconsultations.clear();
                    listPassedConsultations.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            Consultation item = new Consultation((JSONObject) array.get(i));

                            if(new SimpleDateFormat("yyyy-MM-dd").parse(item.getDate()).after(new Date()))
                            {
                                listIncommingconsultations.add(item);
                            }
                            else
                            {
                                listPassedConsultations.add(item);
                            }

                        }
                        listSelectedConsultations =listIncommingconsultations;
                        adapterIncommingConsultations.notifyDataSetChanged();
                        adapterPassedConsultations.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    listIncommingconsultations.clear();
                    listPassedConsultations.clear();
                    listSelectedConsultations.clear();
                    adapterIncommingConsultations.notifyDataSetChanged();
                    adapterPassedConsultations.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }


    private void handleChangePublicationsType() {
        if(consultationType==1)
        {
            btnIncomming.setBackgroundColor(getResources().getColor(R.color.blue));
            btnIncomming.setTextColor(getResources().getColor(R.color.white));

            btnPassed.setBackgroundColor(getResources().getColor(R.color.white));
            btnPassed.setTextColor(getResources().getColor(R.color.black));

            lvConsultations.setAdapter(adapterIncommingConsultations);
            listSelectedConsultations = listIncommingconsultations;
        }
        else
        {
            btnIncomming.setBackgroundColor(getResources().getColor(R.color.white));
            btnIncomming.setTextColor(getResources().getColor(R.color.black));

            btnPassed.setBackgroundColor(getResources().getColor(R.color.blue));
            btnPassed.setTextColor(getResources().getColor(R.color.white));

            lvConsultations.setAdapter(adapterPassedConsultations);
            listSelectedConsultations = listPassedConsultations;
        }
    }
}
