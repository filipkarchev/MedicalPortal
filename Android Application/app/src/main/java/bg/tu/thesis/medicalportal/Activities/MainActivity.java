package bg.tu.thesis.medicalportal.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bg.tu.thesis.medicalportal.Preferences;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.fragments.FragmentLogin;
import bg.tu.thesis.medicalportal.fragments.HomeFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent();
    }

    private void setContent() {
        setContentView(R.layout.activity_main);

        Fragment fragment;
        String username = Preferences.getString(MainActivity.this,"username","");
                if(!username.equals(""))
                {
                    fragment = new FragmentLogin();
                }
        else
                {
                    fragment = new HomeFragment();
                }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.container, fragment,"home")
                .commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
