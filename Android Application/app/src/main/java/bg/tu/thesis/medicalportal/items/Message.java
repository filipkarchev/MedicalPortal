package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by filip on 22.05.16.
 */
public class Message implements Serializable{
    private int senderId=0;
    private String senderName = "";
    private String receiverName = "";
    private int receiverId = 0;
    private String theme = "";
    private String date="";
    private String text="";
    private int seen=0;
    private String image="";
    private int id=0;
    private int type=0;

    public Message()
    {}

    public Message(JSONObject jsonObject) {
        senderId = jsonObject.optInt("sender_id");
        senderName = jsonObject.optString("sender_name");
        receiverName = jsonObject.optString("receiver_name");
        receiverId = jsonObject.optInt("receiver_id");
        theme = jsonObject.optString("title");
        date = jsonObject.optString("date");
        seen = jsonObject.optInt("seen");
        text = jsonObject.optString("text");
        image = jsonObject.optString("image");
        id = jsonObject.optInt("id");
        type = jsonObject.optInt("type",0);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
