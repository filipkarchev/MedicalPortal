package bg.tu.thesis.medicalportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.tu.thesis.medicalportal.items.Category;

/**
 * Created by filip on 19.05.16.
 */
public class Utils {
    public final static String baseUrl = "http://78.90.52.167/MedicalPortal/ctrl/";

    public static final int DRAWER_MY_PUBLICATIONS = 1;
    public static final int DRAWER_MESSAGES = 2;
    public static final int DRAWER_CONSULTATIONS = 3;
    public static final int DRAWER_LOG_OUT = 4;
    public static final int DRAWER_HOME = 5;
    public static final int DRAWER_PROFILE = 6;



    public static final int TAKE_PICTURE = 100;
    public static final int SELECT_IMAGE = 101;


    private static  int userId;
    private static String userNames;
    private static int profileType;

    public static int getProfileType() {
        return profileType;
    }

    public static void setProfileType(int profileType) {
        Utils.profileType = profileType;
    }

    public static String getUserNames() {
        return userNames;
    }

    public static void setUserNames(String userNames) {
        Utils.userNames = userNames;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNoNetworkDialog(final Context context,
                                           final Runnable retryRunnable) {
        try {
            //Get android version and make dialog in light theme
            ContextThemeWrapper themedContext;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            } else {
                themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Light_NoTitleBar);
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    themedContext);

            // set title
            alertDialogBuilder.setTitle(R.string.internet_dialog_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.internet_dialog_message)
                    .setCancelable(false)

                    .setNegativeButton(R.string.internet_dialog_exit,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    System.exit(0);
                                }
                            })
                    .setPositiveButton(R.string.internet_dialog_retry,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (!Utils.isNetworkAvailable(context)) {
                                        Utils.showNoNetworkDialog(context,
                                                retryRunnable);
                                    } else {
                                        retryRunnable.run();
                                    }
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            if (context != null) {
                Log.e("Utils", "Show no network dialog");
                alertDialog.show();
            } else {
                System.exit(0);
            }

        } catch (Exception e) {
        }
    }

    public static ArrayList<Category> getListCategories(boolean areEmpty)
    {
        ArrayList<Category> list = new ArrayList<>();
            Category cat1 = new Category("Dermatology",!areEmpty,1);
            list.add(cat1);
            Category cat2 = new Category("Neurology",!areEmpty,2);
            list.add(cat2);
            Category cat3 = new Category("Pediatrics",!areEmpty,3);
            list.add(cat3);
            Category cat4 = new Category("Surgery",!areEmpty,4);
            list.add(cat4);
            Category cat5 = new Category("Plastic Surgery",!areEmpty,5);
            list.add(cat5);
            Category cat6 = new Category("Orthopedic surgery",!areEmpty,6);
            list.add(cat6);
            Category cat7 = new Category("Internal Medicine",!areEmpty,7);
            list.add(cat7);
            Category cat8 = new Category("Gynecology",!areEmpty,8);
            list.add(cat8);

        return list;
    }

    public static String getCurrentDate()
    {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String DateToStr = format.format(curDate);
        return DateToStr;
    }

    public static Bitmap getQuadradBitmap(Bitmap srcBmp) {

        Bitmap dstBmp;

        if (srcBmp.getWidth() >= srcBmp.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        } else {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    //Crop bitmap to circle view(used in profile picture)
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
