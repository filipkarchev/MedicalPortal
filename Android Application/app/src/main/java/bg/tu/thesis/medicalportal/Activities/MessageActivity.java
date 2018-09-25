package bg.tu.thesis.medicalportal.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import bg.tu.thesis.medicalportal.items.Message;
import bg.tu.thesis.medicalportal.items.User;
import bg.tu.thesis.medicalportal.volley.MyVolley;

/**
 * Created by filip on 22.05.16.
 */


public class MessageActivity extends Activity {
    Message message;

    private ImageView ivBack, ivImage;
    private Button btnAnswer;
    private TextView txtSender, txtReceiver, txtDate, txtMessage, txtMessageTitle;
    private EditText etResposne, etResponseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = (Message) getIntent().getExtras().getSerializable("message");
        setContentView(R.layout.activity_message);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.onBackPressed();
            }
        });

        btnAnswer = (Button) findViewById(R.id.btn_resposne);
        etResposne = (EditText) findViewById(R.id.et_resposne);
        etResponseTitle = (EditText) findViewById(R.id.et_resposne_title);
        txtSender = (TextView) findViewById(R.id.txt_sender);
        txtReceiver = (TextView) findViewById(R.id.txt_receiver);
        txtDate = (TextView) findViewById(R.id.txt_date);
        txtMessage = (TextView) findViewById(R.id.txt_message_text);
        txtMessageTitle = (TextView) findViewById(R.id.txt_message_title);
        ivImage = (ImageView) findViewById(R.id.iv_picture);

        txtSender.setText(message.getSenderName());
        txtReceiver.setText(message.getReceiverName());
        txtDate.setText(message.getDate());
        txtMessage.setText(message.getText());
        txtMessageTitle.setText(message.getTheme());

        if (!message.getImage().equals("") && !message.getImage().equals("null") && message.getImage() != null) {
            Log.i("MessageActivity", "message image " + message.getImage());
            ivImage.setVisibility(View.VISIBLE);
            new GetAsyncPicture(ivImage, message.getImage()).execute();
        }

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogImage();
            }
        });


        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etResponseTitle.getText().toString().equals("") || etResposne.getText().toString().equals("")) {
                    Toast.makeText(MessageActivity.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                String url = Utils.baseUrl + "message_sent.php";

                HashMap<String, String> params = new HashMap<>();
                params.put("text", etResposne.getText().toString());
                params.put("sender_id", "" + Utils.getUserId());
                params.put("receiver_id", "" + message.getSenderId());
                params.put("date", Utils.getCurrentDate());
                params.put("title", etResponseTitle.getText().toString());
                params.put("sender_name", Utils.getUserNames());
                params.put("receiver_name", message.getSenderName());
                params.put("type", Utils.getProfileType() + "");


                MyVolley.requestJSONObject(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("FragmentAsk", "onResponse: " + response.toString());
                        boolean isok = response.optBoolean("isok", false);
                        if (isok == true) {
                            Toast.makeText(MessageActivity.this, "Message is sent", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, params, false, false);

            }
        });

        if (Utils.getUserId() == message.getReceiverId() && message.getSeen() == 0) {
            Log.i("MessageActivity", "start message seen task");
            sendMessageSeen();
        }
    }


    private void openDialogImage() {
        final Dialog dialogImage = new Dialog(MessageActivity.this,
                R.style.DialogTheme);
        dialogImage
                .setContentView(R.layout.dialog_message_image);

        final Button btnCancel = (Button) dialogImage
                .findViewById(R.id.btn_close);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImage.dismiss();
            }
        });

        ImageView ivImage = (ImageView) dialogImage.findViewById(R.id.iv_image);
        new GetAsyncPicture(ivImage,
                message.getImage()).execute();

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCancel.getVisibility() == View.VISIBLE) {
                    btnCancel.setVisibility(View.GONE);
                } else {
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogImage.show();
    }


    private void sendMessageSeen() {

    String url = Utils.baseUrl + "message_seen.php";
    HashMap<String, String> params = new HashMap<>();
    params.put("message_id",message.getId()+"");
    MyVolley.requestJSONObject(url,new Response.Listener<JSONObject>()

    {
        @Override
        public void onResponse (JSONObject response){
        Log.i("MessageActivity", "onResponse: " + response);


    }
    }

    ,new Response.ErrorListener()

    {
        @Override
        public void onErrorResponse (VolleyError error){

    }
    }
    ,params,false,false);

}
}
