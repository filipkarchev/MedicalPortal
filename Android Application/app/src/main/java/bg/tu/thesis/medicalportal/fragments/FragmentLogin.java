package bg.tu.thesis.medicalportal.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import bg.tu.thesis.medicalportal.Activities.BaseActivity;
import bg.tu.thesis.medicalportal.Preferences;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.items.Doctor;
import bg.tu.thesis.medicalportal.items.User;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 20.05.16.
 */
public class FragmentLogin extends Fragment {

    private int profileType = 1;
    private Button btnUser,btnDoctor;
    private Button btnLogin,btnSignUp;
    private EditText etUsername,etPass;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_log_in, container, false);

        MyVolley.init(getActivity());

        btnUser = (Button) rootView.findViewById(R.id.btn_user);
        btnDoctor = (Button) rootView.findViewById(R.id.btn_doctor);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        btnSignUp = (Button) rootView.findViewById(R.id.btn_signup);

        etUsername = (EditText) rootView.findViewById(R.id.et_username);
        etPass = (EditText) rootView.findViewById(R.id.et_pass);

        profileType = Preferences.getInt(getActivity(),"type",1);
        if(profileType==2)
        {
            btnUser.setBackgroundColor(getResources().getColor(R.color.white));
            btnUser.setTextColor(getResources().getColor(R.color.black));

            btnDoctor.setBackgroundColor(getResources().getColor(R.color.blue));
            btnDoctor.setTextColor(getResources().getColor(R.color.white));
        }

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileType =1;

                btnUser.setBackgroundColor(getResources().getColor(R.color.blue));
                btnUser.setTextColor(getResources().getColor(R.color.white));

                btnDoctor.setBackgroundColor(getResources().getColor(R.color.white));
                btnDoctor.setTextColor(getResources().getColor(R.color.black));

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

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFrag= new FragmentSignUp();

                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag,"sign_up")
                        .addToBackStack("tag")
                        .commit();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        checkIsLoggedIn();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return rootView;
    }

    private void checkIsLoggedIn() {
        String username = Preferences.getString(getActivity(),"username","");
        String pass = Preferences.getString(getActivity(),"pass","");
        int id = Preferences.getInt(getActivity(),"id",0);
        String names = Preferences.getString(getActivity(),"names","");


        //if username and pass are not empty, login
        if(!username.equals("") && !pass.equals(""))
        {
            etUsername.setText(username);
            etPass.setText(pass);
            Utils.setUserId(id);
            Utils.setUserNames(names);
            Utils.setProfileType(profileType);

            startLogin();
        }
    }

    ProgressDialog dialog;
    private void startLogin() {

        dialog = new ProgressDialog(getActivity());
        final String username = etUsername.getText().toString();
        final String pass = etPass.getText().toString();

        if(username.equals("") || pass.equals(""))
        {return;
        }

        dialog.show();
        String url = Utils.baseUrl + "user_login.php";

        HashMap<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",pass);
        params.put("type",""+profileType);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LoginActivity","login onresponse " + response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    try {
                        JSONObject data =(JSONObject) response.getJSONArray("data").get(0);

                        //Create the object, depends on profileType
                        if(profileType==1)
                            user = new User(data);
                        else
                            user = new Doctor(data);

                        //Save username and password
                        Preferences.putString(getActivity(),"username",username);
                        Preferences.putString(getActivity(),"pass",pass);
                        Preferences.putInt(getActivity(),"id",user.getId());
                        Preferences.putString(getActivity(),"names",user.getFirstname() + " " + user.getLastname());
                        Preferences.putInt(getActivity(),"type",profileType);
                        Utils.setProfileType(profileType);
                        Utils.setUserId(user.getId());
                        Utils.setUserNames(user.getFirstname() + " " + user.getLastname());

                        //start base activity
                        Intent startIntent  = new Intent(getActivity(),BaseActivity.class);
                        Bundle bundle = new Bundle();
                        if(profileType==1)
                            bundle.putSerializable("user",user);
                        else
                            bundle.putSerializable("doctor",user);

                        startIntent.putExtras(bundle);
                        startActivity(startIntent);
                        getActivity().finish();

                        //Hide progress dialog
                        if(dialog!=null && dialog.isShowing()==true)
                        {
                            dialog.dismiss();
                        }
                       //finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    //Hide progress dialog
                    if(dialog!=null && dialog.isShowing()==true)
                    {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), R.string.wrong_credentials,Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }
}
