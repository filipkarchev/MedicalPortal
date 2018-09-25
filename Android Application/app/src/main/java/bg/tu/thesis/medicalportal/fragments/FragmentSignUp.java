package bg.tu.thesis.medicalportal.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.Activities.SelectAddressActivity;
import bg.tu.thesis.medicalportal.Preferences;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.items.Category;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 20.05.16.
 */
public class FragmentSignUp extends Fragment {
    private final String LOG_TAG = "SignUpActivity";
    int profileType=1;
    ArrayList<Category> listCategories;
    int selectedCategory = -1;
    private Double latitude=0.0,longitude=0.0;
    String address="";

    private Button btnUser,btnDoctor;
    private Button btnSignUp,btnSelectCat,btnSelectAddress;
    private EditText etUsername,etPass,etPassRepeat,etPhoneNumber,etFirstName,etLastName;
    private EditText etSpeciality,etCity,etHospitalName,etDescription,etHospitalAddress;
    private LinearLayout llProfessionalInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_sign_up, container, false);

        btnUser = (Button) rootView.findViewById(R.id.btn_user);
        btnDoctor = (Button) rootView.findViewById(R.id.btn_doctor);
        btnSelectCat = (Button) rootView.findViewById(R.id.btn_select_cat);
        btnSignUp = (Button) rootView.findViewById(R.id.btn_sign_up);
        btnSelectAddress = (Button) rootView.findViewById(R.id.btn_select_address);

        etUsername = (EditText) rootView.findViewById(R.id.et_username);
        etPass= (EditText) rootView.findViewById(R.id.et_pass);
        etPassRepeat= (EditText) rootView.findViewById(R.id.et_repeat_pass);
        etPhoneNumber= (EditText) rootView.findViewById(R.id.et_phone_number);
        etFirstName= (EditText) rootView.findViewById(R.id.et_first_name);
        etLastName= (EditText) rootView.findViewById(R.id.et_last_name);
        etSpeciality= (EditText) rootView.findViewById(R.id.et_speciality);
        etCity= (EditText) rootView.findViewById(R.id.et_city);
        etHospitalName= (EditText) rootView.findViewById(R.id.et_hospital_name);
        etDescription = (EditText) rootView.findViewById(R.id.et_professional_desc);
        etHospitalAddress = (EditText) rootView.findViewById(R.id.et_address);
        llProfessionalInfo = (LinearLayout) rootView.findViewById(R.id.ll_professional_info);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileType =1;

                btnUser.setBackgroundColor(getResources().getColor(R.color.blue));
                btnUser.setTextColor(getResources().getColor(R.color.white));

                btnDoctor.setBackgroundColor(getResources().getColor(R.color.white));
                btnDoctor.setTextColor(getResources().getColor(R.color.black));

                llProfessionalInfo.setVisibility(View.GONE);
            }
        });

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileType = 2;

                btnUser.setBackgroundColor(getResources().getColor(R.color.white));
                btnUser.setTextColor(getResources().getColor(R.color.black));

                btnDoctor.setBackgroundColor(getResources().getColor(R.color.blue));
                btnDoctor.setTextColor(getResources().getColor(R.color.white));

                llProfessionalInfo.setVisibility(View.VISIBLE);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etPass.getText().toString().equals(etPassRepeat.getText().toString()))
                {
                    Toast.makeText(getActivity(), R.string.passwords_not_match,Toast.LENGTH_LONG).show();
                }
                if(profileType==1)
                {
                    registerUser();
                }
                else
                {
                    registerDoctor();
                }
            }
        });

        btnSelectCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSelectCategory();
            }
        });

        btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMap = new Intent(getActivity(), SelectAddressActivity.class);
                startActivityForResult(openMap,100);
            }
        });

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return rootView;
    }

    private void openDialogSelectCategory() {

        final Dialog dialogSelectDoctor = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogSelectDoctor
                .setContentView(R.layout.dialog_select_category);

        ListView lvCats = (ListView) dialogSelectDoctor
                .findViewById(R.id.lv_categories);
        listCategories = Utils.getListCategories(true);
       final CategoriesAdapterListView catsAdapter = new CategoriesAdapterListView(getActivity(),0,listCategories);
        lvCats.setAdapter(catsAdapter);

        lvCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = listCategories.get(position).getId();
                catsAdapter.notifyDataSetChanged();
            }
        });



        Button btnCancel = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_info);
        btnCancel.setText("Cancel");
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectDoctor.dismiss();
            }
        });

        Button btnSaveTurnOff = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_select);

        // if button is clicked, close the custom dialog
        btnSaveTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCategory== -1)
                {
                    Toast.makeText(getActivity(),"Select category",Toast.LENGTH_LONG).show();
                }
                else
                {
                    dialogSelectDoctor.dismiss();
                }
            }


        });

        dialogSelectDoctor.show();

    }

    private void registerDoctor() {
        String url = Utils.baseUrl + "doctor_create.php";

        String username = etUsername.getText().toString();
        String pass = etPass.getText().toString();
        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String phone = etPhoneNumber.getText().toString();

        String speciality = etSpeciality.getText().toString();
        String desc = etDescription.getText().toString();
        String hospital_name = etHospitalName.getText().toString();
        String city = etCity.getText().toString();
        //String address = etHospitalAddress.getText().toString();
        int category = selectedCategory;

        HashMap<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",pass);
        params.put("firstname",firstname);
        params.put("lastname",lastname);
        params.put("phone",phone);

        params.put("speciality",speciality);
        params.put("description",desc);
        params.put("hospital_name",hospital_name);
        params.put("city",city);
        params.put("address",address);
        params.put("category",""+category);
        params.put("latitude",""+latitude);
        params.put("longitude",""+longitude);


        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(LOG_TAG,"register doctor onRespnse: " + response.toString());
                boolean isok = response.optBoolean("isok",false);

                if(isok==true)
                {
                    enterWithUsername();
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.error_in_registration,Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void registerUser() {
        String url = Utils.baseUrl + "user_create.php";

        String username = etUsername.getText().toString();
        String pass = etPass.getText().toString();
        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String phone = etPhoneNumber.getText().toString();

        HashMap<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",pass);
        params.put("firstname",firstname);
        params.put("lastname",lastname);
        params.put("phone",phone);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean isok = response.optBoolean("isok",false);

                if(isok==true)
                {
                    enterWithUsername();
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.error_in_registration,Toast.LENGTH_LONG).show();
                }
                Log.i(LOG_TAG,"register user onRespnse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void enterWithUsername() {
        Toast.makeText(getActivity(), R.string.user_created,Toast.LENGTH_LONG).show();
        //Save username and password
        Preferences.putString(getActivity(),"username",etUsername.getText().toString());
        Preferences.putString(getActivity(),"pass",etPass.getText().toString());

        /*//start login activity
        Intent startIntent  = icon_new Intent(getActivity(),LoginActivity.class);
        startActivity(startIntent);*/
        Fragment nextFrag= new FragmentLogin();
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.container, nextFrag,"login")
                .addToBackStack("tag")
                .commit();

       // finish();
    }


    private class CategoriesAdapterListView extends ArrayAdapter<Category> {
        Context context;
        ArrayList<Category> objects;

        public CategoriesAdapterListView(Context context, int resource,
                                 ArrayList<Category> objects) {
            super(context, resource, objects);
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Category item = objects.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_categories_list, parent, false);

            TextView txtName = (TextView) rowView.findViewById(R.id.txt_cat_name);
            txtName.setText(item.getName());

            ImageView ivChecked = (ImageView) rowView.findViewById(R.id.iv_checked);

            if(selectedCategory==listCategories.get(position).getId())
            {
                ivChecked.setVisibility(View.VISIBLE);
            }

            return rowView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("FragmentSignUp","onActivityResult requestCode:"+ requestCode + " resultCode:" + resultCode);
        switch(requestCode) {
            case (100) : {
                if (resultCode == Activity.RESULT_OK) {
                    latitude = data.getExtras().getDouble("latitude");
                    longitude = data.getExtras().getDouble("longitude");
                    address = data.getExtras().getString("address");
                    Log.i("FragmentSignUp","latitude: " + latitude + " longitude: " +longitude + " address: " + address);
                }
                break;
            }
        }
    }
}
