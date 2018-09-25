package bg.tu.thesis.medicalportal.items;

/**
 * Created by filip on 23.05.16.
 */
public class City {
    private String name="";
    private boolean selected = false;

    public City(String name,boolean iSelected)
    {
        this.name = name;
        selected = iSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
