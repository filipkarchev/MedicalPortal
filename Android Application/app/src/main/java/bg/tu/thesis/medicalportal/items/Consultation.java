package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by filip on 23.05.16.
 */
public class Consultation implements Serializable{
    private int id=0;
    private int userId = 0;
    private int doctorId=0;
    private String doctor_name ="";
    private String address ="";
    private String date = "";
    private int hour =0 ;
    private int seen = 0;

    public Consultation(JSONObject jsonObject) {
        id = jsonObject.optInt("id");
        userId = jsonObject.optInt("user_id");
        doctorId = jsonObject.optInt("doctor_id");
        doctor_name = jsonObject.optString("firstname") + " " + jsonObject.optString("lastname");
        address = jsonObject.optString("city") + " " + jsonObject.optString("address");
        date = jsonObject.optString("date");
        hour = jsonObject.optInt("hour");
        seen = jsonObject.optInt("seen",0);
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
