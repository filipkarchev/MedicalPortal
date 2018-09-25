package bg.tu.thesis.medicalportal.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.CategoriesAdapter;
import bg.tu.thesis.medicalportal.adapters.DoctorsAdapter;
import bg.tu.thesis.medicalportal.items.Doctor;
import bg.tu.thesis.medicalportal.items.Category;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 20.05.16.
 */
public class FragmentAskQuestion extends Fragment {
    private Button btnPublication,btnDoctor;
    private RelativeLayout rlAskDoctor,rlPublicationType;
    int questionType = 1;

    private ImageView ivShowHide;
    private GridView gvCategories;
    public static ArrayList<Category> listCategories = new ArrayList<>();
    CategoriesAdapter catAdapter;
    private Button btnPublish;
    private EditText etPublicationText;
    private EditText etPublicationTitle;
    private TextView txtCatTitle;
    int category = -1;

    private Button btnSelectDoctor,btnAskDoctor;
    private ImageView ivCloseIcon,ivSelectImage;
    private TextView txtDoctorNames,txtDoctorSpeciality;
    private RatingBar rbDoctorRating;
    private RelativeLayout rlSelectedDoctor;
    private EditText etQuestion,etQuestionTitle;

    private Bitmap bitmap;

    static FragmentAskQuestion  fragment;
    public static FragmentAskQuestion getInstance()
    {
        if(fragment==null) {
            fragment = new FragmentAskQuestion();
        }
       return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ask, container, false);

        btnDoctor = (Button) rootView.findViewById(R.id.btn_doctor);
        btnPublication = (Button) rootView.findViewById(R.id.btn_publication);
        rlAskDoctor = (RelativeLayout) rootView.findViewById(R.id.rl_ask_doctor);
        rlPublicationType = (RelativeLayout) rootView.findViewById(R.id.rl_publish_question);

        btnPublish = (Button) rootView.findViewById(R.id.btn_publish);
        etPublicationText = (EditText) rootView.findViewById(R.id.et_publication_text);
        etPublicationTitle = (EditText) rootView.findViewById(R.id.et_publication_title);
        gvCategories = (GridView) rootView.findViewById(R.id.gv_categories);
        ivShowHide = (ImageView) rootView.findViewById(R.id.iv_arrow);
        txtCatTitle = (TextView) rootView.findViewById(R.id.txt_cat_title);

        btnSelectDoctor = (Button) rootView.findViewById(R.id.btn_select_doctor);
        btnAskDoctor = (Button) rootView.findViewById(R.id.btn_ask_question);
        ivCloseIcon = (ImageView) rootView.findViewById(R.id.iv_cancel_doctor);
        ivSelectImage = (ImageView) rootView.findViewById(R.id.iv_picture);
        txtDoctorNames = (TextView) rootView.findViewById(R.id.txt_doctor_name);
        txtDoctorSpeciality = (TextView) rootView.findViewById(R.id.txt_doctor_speciality);
        rbDoctorRating = (RatingBar) rootView.findViewById(R.id.rb_doctor_rating);
        rlSelectedDoctor = (RelativeLayout) rootView.findViewById(R.id.rl_doctor_profile);
        etQuestion = (EditText) rootView.findViewById(R.id.et_question_text);
        etQuestionTitle = (EditText) rootView.findViewById(R.id.et_question_title);


        listCategories = new ArrayList<>();
        listCategories = Utils.getListCategories(true);
        catAdapter = new CategoriesAdapter(getActivity(),0,listCategories,2);
        gvCategories.setAdapter(catAdapter);


        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionType = 2;
                handleChangeQuestionType();
            }
        });

        btnPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionType = 1;
                handleChangeQuestionType();
            }
        });

        setPublicationType();

        setAskDoctorType();

        return rootView;
    }

    private void setAskDoctorType() {

        ivCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSelectedDoctor.setVisibility(View.GONE);
                selectedDoctor = null;
            }
        });

        btnSelectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opedDialogSelectDoctor();
            }
        });

        ivSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Utils.SELECT_IMAGE);
            }
        });

        btnAskDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewQuestion();
            }
        });

    }

    private void sendNewQuestion() {

        if(etQuestion.getText().toString().equals("") || selectedDoctor==null || etQuestionTitle.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(),"Please fill in all fields",Toast.LENGTH_LONG).show();
            return;
        }

        String url = Utils.baseUrl + "message_sent.php";

        //If there is selected image, make it as base64 encoded string
        String image_str="";
        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        }

        HashMap<String,String> params = new HashMap<>();
        params.put("text",etQuestion.getText().toString());
        params.put("sender_id",""+Utils.getUserId());
        params.put("receiver_id",""+selectedDoctor.getId());
        params.put("date",Utils.getCurrentDate());
        params.put("title",etQuestionTitle.getText().toString());
        params.put("sender_name",Utils.getUserNames());
        params.put("receiver_name",selectedDoctor.getFirstname() + " " + selectedDoctor.getLastname());
        params.put("type",Utils.getProfileType()+"");


        //if string image is not empty add it to parameters
        if(!image_str.equals(""))
        params.put("image",image_str);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("FragmentAsk","onResponse: " + response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(getActivity(),"Message is sent",Toast.LENGTH_LONG).show();
                    etQuestion.setText("");
                    etQuestionTitle.setText("");
                    selectedDoctor = null;
                    rlSelectedDoctor.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    ArrayList<Doctor> listDoctors = new ArrayList<Doctor>();
    public Doctor selectedDoctor;
    DoctorsAdapter doctorAdapter;
    CategoriesAdapter catsQuestionAdapter;
    GridView gvCategoriesDoctorCategory;
    ImageView ivShowHideDoctorCategory;
    TextView txtCatTitleDoctorCategory;

    private void opedDialogSelectDoctor() {

        final Dialog dialogSelectDoctor = new Dialog(getActivity(),
                R.style.DialogTheme);
        dialogSelectDoctor
                .setContentView(R.layout.dialog_select_doctor);
        gvCategoriesDoctorCategory = (GridView) dialogSelectDoctor.findViewById(R.id.gv_categories);
        txtCatTitleDoctorCategory= (TextView) dialogSelectDoctor.findViewById(R.id.txt_cat_title);
        ivShowHideDoctorCategory = (ImageView) dialogSelectDoctor.findViewById(R.id.iv_arrow);
        listCategories = Utils.getListCategories(true);
        catsQuestionAdapter = new CategoriesAdapter(getActivity(),0,listCategories,3);
        gvCategoriesDoctorCategory.setAdapter(catsQuestionAdapter);


        ListView lvDoctors = (ListView) dialogSelectDoctor
                .findViewById(R.id.lv_doctors);
        doctorAdapter = new DoctorsAdapter(getActivity(),0,listDoctors);
        lvDoctors.setAdapter(doctorAdapter);

        lvDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDoctor = listDoctors.get(position);
                doctorAdapter.notifyDataSetChanged();
            }
        });

        ivShowHideDoctorCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gvCategoriesDoctorCategory.getVisibility()==View.VISIBLE)
                {
                    gvCategoriesDoctorCategory.setVisibility(View.GONE);
                    ivShowHideDoctorCategory.setImageResource(R.drawable.ic_down);
                }
                else
                {
                    listCategories = Utils.getListCategories(true);
                    catsQuestionAdapter.notifyDataSetChanged();
                    gvCategoriesDoctorCategory.setVisibility(View.VISIBLE);
                    ivShowHideDoctorCategory.setImageResource(R.drawable.ic_up);
                }
            }
        });

        Button btnInfo = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_info);
        // if button is clicked, start profile Activity
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedDoctor==null)
                {
                    Toast.makeText(getActivity(),"No selected doctor",Toast.LENGTH_LONG).show();
                    return;
                }

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

                dialogSelectDoctor.dismiss();

            }
        });

        Button btnSave = (Button) dialogSelectDoctor
                .findViewById(R.id.btn_select);

        // if button is clicked, close the custom dialog
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDoctor==null)
                {
                    return;
                }
                dialogSelectDoctor.dismiss();

                rlSelectedDoctor.setVisibility(View.VISIBLE);
                txtDoctorNames.setText(selectedDoctor.getFirstname() + " " + selectedDoctor.getLastname());
                txtDoctorSpeciality.setText(selectedDoctor.getSpeciality());
                rbDoctorRating.setRating((float) selectedDoctor.getRating());
            }


        });

        dialogSelectDoctor.show();
    }

    public void downloadDoctrosList(int position) {
        ivShowHideDoctorCategory.performClick();
        txtCatTitleDoctorCategory.setText(listCategories.get(position).getName());

        String url = Utils.baseUrl + "list_doctors_get.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("category","" + listCategories.get(position).getId());

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
                        doctorAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    listDoctors.clear();
                    doctorAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void setPublicationType() {

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
                    listCategories = Utils.getListCategories(true);
                    catAdapter.notifyDataSetChanged();
                    gvCategories.setVisibility(View.VISIBLE);
                    ivShowHide.setImageResource(R.drawable.ic_up);
                }
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    @Override
                    public void run() {
                        publishNewQuestion();
                        super.run();
                    }
                };

            }
        });

    }

    public void callOnCategoryClicked(int position)
    {
        category = listCategories.get(position).getId();
        ivShowHide.performClick();
        txtCatTitle.setText(listCategories.get(position).getName());
    }



    private void publishNewQuestion() {
        String title = etPublicationTitle.getText().toString();
        String text = etPublicationText.getText().toString();

        //Check for full information
        if(title.equals("") || text.equals("") || category == -1)
        {
            Toast.makeText(getActivity(),"Please fill in all fields",Toast.LENGTH_LONG).show();
            return;
        }

        String url = Utils.baseUrl  + "publication_add.php";


        HashMap<String,String> params = new HashMap<>();
        params.put("description",text);
        params.put("title",title);
        params.put("creator_id",""+Utils.getUserId());
        params.put("date",Utils.getCurrentDate());
        params.put("category",""+category);

        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Fragment search","search response: " + response.toString());
                boolean isok = response.optBoolean("isok",false);
                if(isok==true)
                {
                    Toast.makeText(getActivity(),"Your question in published",Toast.LENGTH_LONG).show();

                    Fragment nextFrag= new HomeFragment();
                    getActivity().getFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)
                            .replace(R.id.container, nextFrag,"home")
                            .addToBackStack("tag")
                            .commit();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },params,false,false);
    }

    private void handleChangeQuestionType() {

        if(questionType==1)
        {
            btnPublication.setBackgroundColor(getResources().getColor(R.color.blue));
            btnPublication.setTextColor(getResources().getColor(R.color.white));

            btnDoctor.setBackgroundColor(getResources().getColor(R.color.white));
            btnDoctor.setTextColor(getResources().getColor(R.color.black));

            rlAskDoctor.setVisibility(View.GONE);
            rlPublicationType.setVisibility(View.VISIBLE);
        }
        else
        {
            btnPublication.setBackgroundColor(getResources().getColor(R.color.white));
            btnPublication.setTextColor(getResources().getColor(R.color.black));

            btnDoctor.setBackgroundColor(getResources().getColor(R.color.blue));
            btnDoctor.setTextColor(getResources().getColor(R.color.white));

            rlAskDoctor.setVisibility(View.VISIBLE);
            rlPublicationType.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("FragmentAskquestion","onActivityResult");
        switch (requestCode) {
            case Utils.SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        bitmap = BitmapFactory.decodeFile(filePath);
                        ivSelectImage.setImageBitmap(bitmap);
                    } else {
                    }
                }

        }
    }
}
