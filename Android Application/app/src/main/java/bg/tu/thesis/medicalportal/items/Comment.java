package bg.tu.thesis.medicalportal.items;

import org.json.JSONObject;

/**
 * Created by filip on 21.05.16.
 */
public class Comment {
    private String text = "";
    private int votes=0;
    private int id=0;
    private int creator_id=0;
    private int publication_id=0;

    public Comment(JSONObject jsonObject) {
        text = jsonObject.optString("comment");
        votes = jsonObject.optInt("votes");
        id = jsonObject.optInt("id");
        creator_id = jsonObject.optInt("creator_id");
        publication_id = jsonObject.optInt("publication_id");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
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

    public int getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }
}
