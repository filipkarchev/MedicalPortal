package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by filip on 20.05.16.
 */
public class User implements Serializable{
    private String username = "";
    private String password = "";
    private String firstname = "";
    private String lastname = "";
    private String phone = "";
    private String iconUrl="";
    private int id =0;
    private int messagesUnread = 0;
    private int consultationsUnread = 0;

    public User()
    {}

    public User(JSONObject response)
    {
        username = response.optString("username","");
        id = response.optInt("id",0);
        password = response.optString("password","");
        firstname = response.optString("firstname","");
        lastname = response.optString("lastname","");
        phone = response.optString("phone","");
        iconUrl = response.optString("icon_url","");
        messagesUnread = response.optInt("messages",0);
        consultationsUnread = response.optInt("consultations",0);
    }

    public int getMessagesUnread() {
        return messagesUnread;
    }

    public void setMessagesUnread(int messagesUnread) {
        this.messagesUnread = messagesUnread;
    }

    public int getConsultationsUnread() {
        return consultationsUnread;
    }

    public void setConsultationsUnread(int consultationsUnread) {
        this.consultationsUnread = consultationsUnread;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
