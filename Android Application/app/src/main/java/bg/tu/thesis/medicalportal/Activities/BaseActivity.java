package bg.tu.thesis.medicalportal.Activities;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bg.tu.thesis.medicalportal.GetAsyncPicture;
import bg.tu.thesis.medicalportal.Preferences;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;
import bg.tu.thesis.medicalportal.adapters.NavigationDrawerAdapter;
import bg.tu.thesis.medicalportal.fragments.FragmentMyConsultations;
import bg.tu.thesis.medicalportal.fragments.FragmentMyPublications;
import bg.tu.thesis.medicalportal.fragments.HomeFragment;
import bg.tu.thesis.medicalportal.fragments.MyMessagesFragment;
import bg.tu.thesis.medicalportal.fragments.ProfileFragment;
import bg.tu.thesis.medicalportal.items.Doctor;
import bg.tu.thesis.medicalportal.items.DrawerItem;
import bg.tu.thesis.medicalportal.items.User;
import bg.tu.thesis.medicalportal.volley.MyVolley;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by filip on 19.05.16.
 */
public class BaseActivity extends FragmentActivity {

    public List<DrawerItem> drawerListCat;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ImageView imgDrawer;
    Context mContext;
    View listHeaderView;
    private User user;
    CircleImageView iv;
    public NavigationDrawerAdapter adapter;

    private String LOG_TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getSerializable("user")!=null)
                user = (User) extras.getSerializable("user");
            else if (extras.getSerializable("doctor")!=null)
                user = (Doctor) extras.getSerializable("doctor");
        }


        mContext=BaseActivity.this;
        setContentView(R.layout.activity_base);
        imgDrawer =(ImageView) findViewById(R.id.img_nav_drawer_open);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //add profile picture and profile name as header
        LayoutInflater inflater = getLayoutInflater();
        listHeaderView = inflater.inflate(R.layout.header_view, null, false);
        TextView profileName=(TextView) listHeaderView.findViewById(R.id.txt_profile_name);
        profileName.setText(user.getFirstname() + " " + user.getLastname());

         iv=(CircleImageView) listHeaderView.findViewById(R.id.img_profile_icon);
        new GetAsyncPicture(iv,user.getIconUrl()).execute();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take picture from camera
//                Intent intent = icon_new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, Utils.TAKE_PICTURE);

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Utils.TAKE_PICTURE);
            }
        });

        mDrawerList.addHeaderView(listHeaderView);

        drawerListCat=getDrawerItems();
        adapter = new NavigationDrawerAdapter(BaseActivity.this,R.layout.item_nav_drawer,drawerListCat);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        Fragment fragment=new Fragment();
        Log.i("BaseActivity","profile type: " + Utils.getProfileType());
        if(Utils.getProfileType()==1)
        {
            fragment = new HomeFragment();
        }
        else
        {
            fragment = new ProfileFragment();
            Bundle b = new Bundle();
            b.putSerializable("profile",user);
            fragment.setArguments(b);
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.container, fragment,"home")
                .addToBackStack(null)
                .commit();


    }
    private void toggleDrawer()
    {
        Log.e("MainActivity", "toogleDrawer");

        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)==true)
            mDrawerLayout.closeDrawers();
        else mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private List<DrawerItem> getDrawerItems()
    {
        List<DrawerItem> list = new ArrayList<DrawerItem>();

        list.add(new DrawerItem("Messages", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_messages), Utils.DRAWER_MESSAGES,user.getMessagesUnread()));
        list.add(new DrawerItem("Consultations", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_consultations),Utils.DRAWER_CONSULTATIONS,user.getConsultationsUnread()));
        if(Utils.getProfileType()==1)
        {
            list.add(new DrawerItem("My publications", BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_publications),Utils.DRAWER_MY_PUBLICATIONS,0));

            list.add(new DrawerItem("Home", BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_home),Utils.DRAWER_HOME,0));
        }
        else if (Utils.getProfileType()==2)
        {

            list.add(new DrawerItem("Profile", BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_home),Utils.DRAWER_PROFILE,0));
        }

        list.add(new DrawerItem("Log Out", BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_log_out),Utils.DRAWER_LOG_OUT,0));
        return list;
    }




    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Log.i("MainActivity","position: " + position);
        if (position > 0 && position < (drawerListCat.size()+1)) {
            // Create a icon_new fragment and specify the planet to show based on position
            int id = drawerListCat.get(position - 1).getId();
            Log.i("MainActivity","drawer position: " + position + "with id: " + id);
            Fragment fragment = null;
            //log out profile
            if (id == Utils.DRAWER_LOG_OUT) {
                Preferences.putString(BaseActivity.this,"username","");
                Preferences.putString(BaseActivity.this,"pass","");
                Preferences.deleteKey(BaseActivity.this,"type");
                Preferences.putString(BaseActivity.this,"names","");
                Utils.setUserId(0);
                Utils.setUserNames("");
                Utils.setProfileType(0);
                Intent loginInten = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(loginInten);
                finish();
            } else if (id == Utils.DRAWER_MY_PUBLICATIONS) {
                fragment = new FragmentMyPublications();

            } else if (id == Utils.DRAWER_MESSAGES){
                fragment = new MyMessagesFragment();
               // fragment = icon_new Fragment();
            }
            else if (id == Utils.DRAWER_CONSULTATIONS){
                fragment = new FragmentMyConsultations();

            }
            else if (id == Utils.DRAWER_HOME){
                //  fragment = icon_new MyConsultationsFragment();
                fragment = new HomeFragment();
            }
            else if (id == Utils.DRAWER_PROFILE){
                fragment = new ProfileFragment();
                Bundle b = new Bundle();
                b.putSerializable("profile",user);
                fragment.setArguments(b);
            }


            if(fragment!=null) {
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Utils.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.e(LOG_TAG, "picture 1");
                    if (data != null) {
                      /*  Log.e(LOG_TAG, "picture 2");
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        photo = Utils.getQuadradBitmap(photo);
                        photo = Utils.getCroppedBitmap(photo);
                        sendPictureToServer(photo);*/

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        sendPictureToServer(BitmapFactory.decodeFile(filePath));
                    } else {
                    }
                }

        }
    }

    String encoded_string,image_name;
    File file;
    Uri fileUri;

    private void sendPictureToServer(final Bitmap photo) {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                doFileUpload(photo);

            }
        });
        serverThread.start();
    }

    private void doFileUpload(Bitmap photo) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        Log.i(LOG_TAG,"image_str: " + image_str);
        String url = Utils.baseUrl + "upload_profile_image.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("image",image_str);
        params.put("name",System.currentTimeMillis()+".png");
        params.put("id",""+Utils.getUserId());

        if(user instanceof Doctor)
        {
            params.put("type","1");
        }
        else
        {
            params.put("type","2");
        }
        MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(LOG_TAG,"onResposne: " + response.toString());
                if(response.optBoolean("isok",false)==true)
                {
                    new GetAsyncPicture(iv,response.optString("data","")).execute();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG,"onErrorResponse: " + error.toString());
            }
        },params,false,false);

    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

}

