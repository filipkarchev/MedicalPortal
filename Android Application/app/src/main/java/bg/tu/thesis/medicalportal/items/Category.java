package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

/**
 * Created by filip on 21.05.16.
 */
public class Category {
    private String name="";
    private int id=0;
    private boolean isChecked  = false;

    public Category(String name, boolean checked, int id) {
        this.name = name;
        isChecked = checked;
        this.id= id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
