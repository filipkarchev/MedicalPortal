package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by filip on 20.05.16.
 */
public class Doctor extends User implements Serializable{

    private String speciality="";
    private String description="";
    private String hospital_name = "";
    private String city="";
    private String address="";
    private int rating_count = 0;
    private double rating = 0;
    private int isConfirmed = 0;


    public Doctor()
    {}

   public Doctor(JSONObject response)
   {
      super(response);
       speciality = response.optString("speciality","");
       description = response.optString("description","");
       hospital_name = response.optString("hospital_name","");
       city = response.optString("city","");
       address = response.optString("address","");
       rating = response.optDouble("rating",0);
       rating_count = response.optInt("rating_count",0);
       isConfirmed = response.optInt("isConfirmed",0);
   }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}
