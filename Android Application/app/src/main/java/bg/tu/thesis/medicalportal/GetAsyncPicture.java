package bg.tu.thesis.medicalportal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by filip on 27.09.15.
 */
public class GetAsyncPicture extends AsyncTask<String, Void, Bitmap> {
   // CircleImageView iv;
    ImageView iv;
    String url;
    private Exception exception;

public GetAsyncPicture(ImageView iv,String url)
{
    this.iv=iv;
    this.url = url;
}
    protected Bitmap doInBackground(String... urls) {
        try {

            return getPicture(Utils.baseUrl+url);

        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Bitmap feed) {

        if(iv==null)
        {
            Log.e("'","iv is null");
        }

        if(feed==null)
        {
            Log.e("'","feed is null");
        }

        iv.setImageBitmap(feed);
    }

    public Bitmap getPicture(String strImageURL){
        try {
            URL imageURL = new URL(strImageURL);
            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            return bitmap;
        }
        catch (MalformedURLException ex)
        {
            Log.e("MainActivity", "MalformedURLException");}
        catch (IOException ex)
        {
            Log.e("MainActivity","IOException");}
        return null;
    }
}