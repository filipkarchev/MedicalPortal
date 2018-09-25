package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by filip on 21.05.16.
 */
public class Publication implements Serializable{
    private String title="";
    private String description ="";
    private String date ="";
    private int id = 0;
    private int creator_id = 0;
    private int seen = 0 ;
    private int category  = 0;
    private int votes  = 0;
    private int comments  = 0;

    public Publication(JSONObject json) {
        title = json.optString("title","");
        description = json.optString("description","");
        creator_id = json.optInt("creator_id",0);
        seen = json.optInt("seen",0);
        id = json.optInt("id",0);
        votes = json.optInt("votes",0);
        category = json.optInt("category",0);
        date  = json.optString("data","");
        comments = json.optInt("comments",0);
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
