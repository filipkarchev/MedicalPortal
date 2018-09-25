package bg.tu.thesis.medicalportal.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import bg.tu.thesis.medicalportal.GetAsyncPicture;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.items.Doctor;
import bg.tu.thesis.medicalportal.items.User;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 03.06.16.
 */
public class ProfileFragment extends Fragment {

    private ImageView ivBack;
    private ImageView ivProfilePicture;
    private TextView txtNames,txtPhone,txtSpeciality,txtCity,txtHospital,txtAddress;
    private RatingBar rbProfileRating;
    private Button btnSaveChanges;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        try{
            user  = (Doctor) getArguments().getSerializable("profile");
        }
        catch (Exception e)
        {
            user  = (User)  getArguments().getSerializable("profile");
        }


        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivProfilePicture = (ImageView) rootView.findViewById(R.id.iv_profile_picture);
        rbProfileRating = (RatingBar) rootView.findViewById(R.id.rb_profile_rating);

        txtNames = (TextView) rootView.findViewById(R.id.txt_names);
        txtPhone = (TextView) rootView.findViewById(R.id.txt_telephone);
        txtSpeciality = (TextView) rootView.findViewById(R.id.txt_speciality);
        txtCity = (TextView) rootView.findViewById(R.id.txt_city);
        txtHospital = (TextView) rootView.findViewById(R.id.txt_hospital_name);
        txtAddress = (TextView) rootView.findViewById(R.id.txt_address);
        btnSaveChanges  = (Button) rootView.findViewById(R.id.btn_save_changes);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back
                getActivity().onBackPressed();
            }
        });

        if(!user.getIconUrl().equals(""))
        {
            new GetAsyncPicture(ivProfilePicture,user.getIconUrl()).execute();
        }


        //Set info
        txtNames.setText(user.getFirstname() + " "+ user.getLastname());
        txtPhone.setText(user.getPhone());

        if(user instanceof Doctor)
        {
            Doctor doctor =(Doctor) user;
            txtSpeciality.setText(doctor.getSpeciality());
            txtCity.setText(doctor.getCity());
            txtHospital.setText(doctor.getHospital_name());
            txtAddress.setText(doctor.getAddress());
            rbProfileRating.setRating((float) doctor.getRating());
        }
        else
        {
            txtSpeciality.setVisibility(View.GONE);
            txtCity.setVisibility(View.GONE);
            txtHospital.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            rbProfileRating.setVisibility(View.GONE);
        }

        if(Utils.getProfileType() == 2 && Utils.getUserId()== user.getId())
        setDoctorEdittion();

        rbProfileRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(Utils.getProfileType() == 1)
                sendVote(rating);
            }
        });

        return rootView;
    }

    private void setDoctorEdittion() {
        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(1);
            }
        });

        txtNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(2);
            }
        });

        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(3);
            }
        });

        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(4);
            }
        });

        txtHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(5);
            }
        });

        txtSpeciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogChangeData(6);
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewDoctorInfo();
            }
        });
    }

    private void saveNewDoctorInfo() {
        Doctor doctor = (Doctor) user;
        String url = Utils.baseUrl + "doctor_update_info.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",doctor.getId()+"");
        params.put("firstname",doctor.getFirstname());
        params.put("lastname",doctor.getLastname());
        params.put("phone",doctor.getPhone());
        params.put("speciality",doctor.getSpeciality());
        params.put("city",doctor.getCity());
        params.put("address",doctor.getAddress());
        params.put("hospital_name",doctor.getHospital_name());

      MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.i("ProfileFragmnet","update info onResposne: " + response.toString());
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      },params,false,false);
    }

    private void opedDialogChangeData(final int i) {
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_enter_new_data);

        Button btnCancel =(Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnSave =(Button) dialog.findViewById(R.id.btn_save);
        TextView txtTitle =(TextView) dialog.findViewById(R.id.txt_field_title);
        final EditText etData =(EditText) dialog.findViewById(R.id.et_add_data);

        switch (i)
        {
            case 1: txtTitle.setText(txtTitle.getText() + "Hospital address"); break;
            case 2: txtTitle.setText(txtTitle.getText() + "First and Last names"); break;
            case 3: txtTitle.setText(txtTitle.getText() + "Phone number"); break;
            case 4: txtTitle.setText(txtTitle.getText() + "city"); break;
            case 5: txtTitle.setText(txtTitle.getText() + "Hospital name"); break;
            case 6: txtTitle.setText(txtTitle.getText() + "Speciality"); break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etData.getText().toString();

                if(data.equals(""))
                {
                    Toast.makeText(getActivity(),"Enter information",Toast.LENGTH_LONG).show();
                    return;
                }
                switch (i)
                {
                    case 1: ((Doctor) user).setAddress(data); break;
                    case 2: ((Doctor) user).setFirstname(data.split(" ")[0]);
                            ((Doctor) user).setLastname(data.split(" ")[1]);
                        break;
                    case 3: ((Doctor) user).setPhone(data); break;
                    case 4: ((Doctor) user).setCity(data); break;
                    case 5: ((Doctor) user).setHospital_name(data); break;
                    case 6: ((Doctor) user).setSpeciality(data); break;
                }

                btnSaveChanges.setVisibility(View.VISIBLE);

                //Update Information
                updateInfo();

                //Close the dialog
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateInfo() {
        txtNames.setText(user.getFirstname() + " "+ user.getLastname());
        txtPhone.setText(user.getPhone());

        if(user instanceof Doctor)
        {
            Doctor doctor =(Doctor) user;
            txtSpeciality.setText(doctor.getSpeciality());
            txtCity.setText(doctor.getCity());
            txtHospital.setText(doctor.getHospital_name());
            txtAddress.setText(doctor.getAddress());
        }
    }

    private void sendVote(float rating) {
        String url= Utils.baseUrl + "doctor_rating_add.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("doctor_id",""+user.getId());
        params.put("user_id",""+Utils.getUserId());
        params.put("vote",""+rating);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ProfileActivity","onResponse: " +response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(getActivity(),"Your vote is sent",Toast.LENGTH_LONG).show();
                    // rbProfileRating.setRating((float)response.optDouble("data",0));
                }else if(response.optString("msg","").equals("cannot vote"))
                {
                    Toast.makeText(getActivity(),"You cannot vote for this doctor",Toast.LENGTH_LONG).show();
                }
                else if(response.optString("msg","").equals("already voted"))
                {
                    Toast.makeText(getActivity(),"You have already voted",Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }
}
