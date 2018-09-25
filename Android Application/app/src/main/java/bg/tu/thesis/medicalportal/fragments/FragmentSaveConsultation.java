package bg.tu.thesis.medicalportal.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import bg.tu.thesis.medicalportal.adapters.CategoriesAdapter;
import bg.tu.thesis.medicalportal.adapters.CitiesAdapter;
import bg.tu.thesis.medicalportal.adapters.DoctorsAdapter;
import bg.tu.thesis.medicalportal.items.City;
import bg.tu.thesis.medicalportal.items.Doctor;
import bg.tu.thesis.medicalportal.items.Category;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 20.05.16.
 */
public class FragmentSaveConsultation extends Fragment {
    private Button btnSelectedCategories,btnSelectCity,btnSelectDoctor,btnSelectDate,btnSelectHour,btnSaveConsultation;
    private TextView txtSelectCity, txtSelectDoctor,txtSelectHour,txtSelectedCategory,txtSelectedDate;

    ArrayList<City> listCities =new ArrayList<>();
    ArrayList<Doctor> listDoctors =new ArrayList<>();
    ArrayList<Integer> listHours =new ArrayList<>();
    
    private ProgressDialog pdLoad;

    static FragmentSaveConsultation  fragment;
    public static FragmentSaveConsultation getInstance()
    {
        if(fragment==null) {
            fragment = new FragmentSaveConsultation();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_consultation,container,false);

        pdLoad = new ProgressDialog(getActivity());


        btnSelectedCategories = (Button) rootView.findViewById(R.id.btn_select_category);
        btnSelectCity = (Button) rootView.findViewById(R.id.btn_select_city);
        btnSelectDoctor = (Button) rootView.findViewById(R.id.btn_select_doctor);
        btnSelectDate  = (Button) rootView.findViewById(R.id.btn_select_date);
        btnSelectHour = (Button) rootView.findViewById(R.id.btn_select_hour);
        btnSaveConsultation = (Button) rootView.findViewById(R.id.btn_save_hour);

        txtSelectCity = (TextView) rootView.findViewById(R.id.txt_selected_city);
        txtSelectedDate = (TextView) rootView.findViewById(R.id.txt_selected_date);
        txtSelectDoctor = (TextView) rootView.findViewById(R.id.txt_selected_doctor);
        txtSelectHour = (TextView) rootView.findViewById(R.id.txt_selected_hour);
        txtSelectedCategory = (TextView) rootView.findViewById(R.id.txt_selected_category);

        btnSelectedCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectCategory();
            }
        });

        btnSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectCity();
            }
        });

        btnSelectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectDoctor();
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectDate();
            }
        });

        btnSelectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectHour();
            }
        });

        btnSaveConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveConsultation();
            }
        });
        return rootView;
    }

    String selectedDate="";
    private void openDialogSelectDate() {
        final Dialog dialogSelectDate = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogSelectDate
                .setContentView(R.layout.dialog_select_date);

        Button btnCancel = (Button) dialogSelectDate
                .findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectDate.dismiss();
            }
        });

        final DatePicker dpDate = (DatePicker)  dialogSelectDate.findViewById(R.id.dp_consultation);
        dpDate.setMinDate(System.currentTimeMillis() - 1000);
       // dpDate.setMaxDate(System.currentTimeMillis() + 1000*60*60*24*30);

        Button btnSave = (Button) dialogSelectDate
                .findViewById(R.id.btn_select);

        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = dpDate.getDayOfMonth();
                int month = dpDate.getMonth() + 1;
                int year = dpDate.getYear();

                selectedDate = year+"-" + month+"-"+day;
                txtSelectedDate.setText(selectedDate);
                txtSelectedDate.setVisibility(View.VISIBLE);

                pdLoad.show();
                downloadHours(selectedDate);

                dialogSelectDate.dismiss();
            }
        });

        dialogSelectDate.show();
    }

    private void downloadHours(String selectedDate) {
        String url = Utils.baseUrl + "consultation_hour_get.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("date",selectedDate);
        params.put("doctor_id",selectedDoctor.getId()+"");

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentAsk","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listHours.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            int item = ((JSONObject) array.get(i)).optInt("hour");
                            listHours.add(item);
                        }

                        btnSelectHour.setVisibility(View.VISIBLE);
                       if(pdLoad!=null && pdLoad.isShowing())
                           pdLoad.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(pdLoad!=null && pdLoad.isShowing())
                            pdLoad.dismiss();
                    }
                }
                else
                {
                    listHours.clear();
                    if(pdLoad!=null && pdLoad.isShowing())
                        pdLoad.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    CategoriesAdapter adapter;
    ArrayList<Category> listCategories;
    private void openDialogSelectCategory() {

        final Dialog dialogCategoryConsultation = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogCategoryConsultation
                .setContentView(R.layout.dialog_categories_consultation);

        Button btnCancel = (Button) dialogCategoryConsultation
                .findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCategoryConsultation.dismiss();
            }
        });

        Button btnSave = (Button) dialogCategoryConsultation
                .findViewById(R.id.btn_select);
        final ListView lvCategories = (ListView) dialogCategoryConsultation
                .findViewById(R.id.lv_categories);
        listCategories = Utils.getListCategories(true);
         adapter = new CategoriesAdapter(getActivity(),0,listCategories,4);
        lvCategories.setAdapter(adapter);

        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCategory==-1)
                {Toast.makeText(getActivity(),"No selected category",Toast.LENGTH_LONG).show();
                return;}

                txtSelectedCategory.setText(listCategories.get(selectedCategory).getName());
                txtSelectedCategory.setVisibility(View.VISIBLE);
                pdLoad.show();
                downloadCities(listCategories.get(selectedCategory).getId());
                dialogCategoryConsultation.dismiss();
            }


        });

        dialogCategoryConsultation.show();
    }

    private void downloadCities(int category)
    {
        String url = Utils.baseUrl + "cities_get.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("category",""+category);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentAsk","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listCities.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            listCities.add(new City(((JSONObject) array.get(i)).optString("city",""),false));
                        }
                        
                        btnSelectCity.setVisibility(View.VISIBLE);
                        if(pdLoad!=null && pdLoad.isShowing())
                        pdLoad.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(pdLoad!=null && pdLoad.isShowing())
                            pdLoad.dismiss();
                    }
                }
                else
                {
                    listCities.clear();
                    if(pdLoad!=null && pdLoad.isShowing())
                        pdLoad.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pdLoad!=null && pdLoad.isShowing())
                    pdLoad.dismiss();
            }
        },params,false,false);
    }

    public void downloadDoctorsList(String city) {

        String url = Utils.baseUrl + "list_doctors_get.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("city",city);
        params.put("category",listCategories.get(selectedCategory).getId()+"");

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentAsk","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    listDoctors.clear();
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            Doctor item = new Doctor((JSONObject) array.get(i));
                            listDoctors.add(item);
                        }

                        btnSelectDoctor.setVisibility(View.VISIBLE);
                        if(pdLoad!=null && pdLoad.isShowing())
                            pdLoad.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(pdLoad!=null && pdLoad.isShowing())
                            pdLoad.dismiss();
                    }
                }
                else
                {
                    listDoctors.clear();
                    if(pdLoad!=null && pdLoad.isShowing())
                        pdLoad.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    CitiesAdapter citiesAdapter;
    private void openDialogSelectCity() {
        final Dialog dialogCitiesConsultation = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogCitiesConsultation
                .setContentView(R.layout.dialog_categories_consultation);

        Button btnCancel = (Button) dialogCitiesConsultation
                .findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCitiesConsultation.dismiss();
            }
        });

        Button btnSave = (Button) dialogCitiesConsultation
                .findViewById(R.id.btn_select);
        final ListView lvCities = (ListView) dialogCitiesConsultation
                .findViewById(R.id.lv_categories);
        citiesAdapter = new CitiesAdapter(getActivity(),0,listCities);
        lvCities.setAdapter(citiesAdapter);


        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCity==-1)
                {Toast.makeText(getActivity(),"No selected city",Toast.LENGTH_LONG).show();
                    return;}

                txtSelectCity.setText(listCities.get(selectedCity).getName());
                txtSelectCity.setVisibility(View.VISIBLE);
                pdLoad.show();
                downloadDoctorsList(listCities.get(selectedCity).getName());
                dialogCitiesConsultation.dismiss();
            }


        });

        dialogCitiesConsultation.show();
    }

    public Doctor selectedDoctor;
    private void openDialogSelectDoctor() {

        final Dialog dialogSelectDoctor = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogSelectDoctor
                .setContentView(R.layout.dialog_categories_consultation);

        Button btnInfo = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_cancel);
        btnInfo.setText("Info");

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogSelectDoctor.dismiss();
                Fragment nextFrag= new ProfileFragment();
                Bundle b = getActivity().getIntent().getExtras();
                b.putSerializable("profile",selectedDoctor);
                nextFrag.setArguments(b);
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag,"profile")
                        .addToBackStack("tag")
                        .commit();
            }
        });

        Button btnSave = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_select);
        final ListView lvDoctors = (ListView) dialogSelectDoctor
                .findViewById(R.id.lv_categories);
        final DoctorsAdapter  doctorsAdapter = new DoctorsAdapter(getActivity(),0,listDoctors);
        lvDoctors.setAdapter(doctorsAdapter);

        lvDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDoctor = listDoctors.get(position);
                doctorsAdapter.notifyDataSetChanged();
            }
        });


        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDoctor==null)
                {Toast.makeText(getActivity(),"No selected doctor",Toast.LENGTH_LONG).show();
                    return;}

                txtSelectDoctor.setText(selectedDoctor.getFirstname() + " " + selectedDoctor.getLastname());
                txtSelectDoctor.setVisibility(View.VISIBLE);

                btnSelectDate.setVisibility(View.VISIBLE);
                dialogSelectDoctor.dismiss();
            }
        });

        dialogSelectDoctor.show();
    }


    int selectedHour;
    private void openDialogSelectHour() {

        final Dialog dialogSelectHour = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogSelectHour
                .setContentView(R.layout.dialog_select_hour);
        final String[] freeHours = getFreeHours();
        final NumberPicker npHours = (NumberPicker) dialogSelectHour.findViewById(R.id.np_hours);
        npHours.setMinValue(0);
        npHours.setMaxValue(freeHours.length - 1);
        npHours.setDisplayedValues(freeHours);


        Button btnCancel = (Button) dialogSelectHour
                .findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectHour.dismiss();
            }
        });

        Button btnSave = (Button) dialogSelectHour
                .findViewById(R.id.btn_select);

        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String selectedHoursStr= freeHours[npHours.getValue()];
                    txtSelectHour.setText(selectedHoursStr);
                    txtSelectHour.setVisibility(View.VISIBLE);
                    btnSaveConsultation.setVisibility(View.VISIBLE);

                    selectedHour =Integer.parseInt(selectedHoursStr.substring(0,selectedHoursStr.indexOf(":")));
                    dialogSelectHour.dismiss();
            }


        });

        dialogSelectHour.show();
    }

    private String[] getFreeHours() {
        String[] freeHours;
        ArrayList<Integer> fullList = new ArrayList<>();
        fullList.add(8);
        fullList.add(9);
        fullList.add(10);
        fullList.add(11);
        fullList.add(13);
        fullList.add(14);
        fullList.add(15);
        fullList.add(16);
        fullList.add(17);

        for(int i=0;i<listHours.size();i++)
        {
            if(fullList.contains(listHours.get(i)))
            {
                fullList.remove(listHours.get(i));
            }
        }

        freeHours = new String[fullList.size()];

        for(int j=0;j<fullList.size();j++)
        {
            freeHours[j] = fullList.get(j) + ":00";
        }

        return freeHours;
    }

    private void saveConsultation() {
        String url = Utils.baseUrl + "consultation_add.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("date",selectedDate);
        params.put("doctor_id",selectedDoctor.getId()+"");
        params.put("user_id",Utils.getUserId()+"");
        params.put("hour",selectedHour+"");

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentAsk","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(getActivity(),"Consultation is reserved. Wait for confirmation call",Toast.LENGTH_LONG).show();
                    btnSelectCity.setVisibility(View.GONE);
                    btnSelectDoctor.setVisibility(View.GONE);
                    btnSelectDate.setVisibility(View.GONE);
                    btnSelectHour.setVisibility(View.GONE);
                    btnSaveConsultation.setVisibility(View.GONE);

                    txtSelectedCategory.setVisibility(View.GONE);
                    txtSelectCity.setVisibility(View.GONE);
                    txtSelectDoctor.setVisibility(View.GONE);
                    txtSelectedDate.setVisibility(View.GONE);
                    txtSelectHour.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getActivity(),"Consultation is not reserved.",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    int selectedCategory=-1;
    public void categorySelected(int position)
    {
        selectedCategory = position;

        //Clear all selections except the last
        for(int i=0 ;i<listCategories.size();i++)
        {
            if(i==selectedCategory)
            {
                listCategories.get(i).setChecked(true);
            }
            else
            {
                listCategories.get(i).setChecked(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    int selectedCity=-1;
    public void citySelected(int position)
    {
        selectedCity = position;

        //Clear all selections except the last
        for(int i=0 ;i<listCities.size();i++)
        {
            if(i==selectedCity)
            {
                listCities.get(i).setSelected(true);
            }
            else
            {
                listCities.get(i).setSelected(false);
            }
        }
        citiesAdapter.notifyDataSetChanged();
    }
}
