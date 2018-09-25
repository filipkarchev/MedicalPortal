package bg.tu.thesis.medicalportal.items;

import android.graphics.Bitmap;

public class DrawerItem {
    String text;
    Bitmap image;
    int id;
    private int newItem=0;

    public DrawerItem(String name, Bitmap image, int id,int newItem)
    {
        this.text=name;
        this.image=image;
        this.id=id;
        this.newItem=newItem;
    }

    public DrawerItem()
    {}

    public int getNewItem() {
        return newItem;
    }

    public void setNewItem(int newItem) {
        this.newItem = newItem;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }






}
