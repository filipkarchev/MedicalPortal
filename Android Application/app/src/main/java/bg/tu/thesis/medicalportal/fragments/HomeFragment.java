package bg.tu.thesis.medicalportal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import bg.tu.thesis.medicalportal.Preferences;
import bg.tu.thesis.medicalportal.R;
import bg.tu.thesis.medicalportal.Utils;

/**
 * Created by filip on 20.05.16.
 */
public class HomeFragment extends Fragment {

    private Button btnLogIn,btnSearch,btnAskQuestion,btnSaveConsulation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btnLogIn = (Button) rootView.findViewById(R.id.btn_log_in);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        btnAskQuestion = (Button) rootView.findViewById(R.id.btn_ask_question);
        btnSaveConsulation = (Button) rootView.findViewById(R.id.btn_save_consultation);

        String username = Preferences.getString(getActivity(),"username","");
        if(!username.equals(""))
        btnLogIn.setVisibility(View.INVISIBLE);

        //iF USER IS NOT LOGGED IN
        if(Utils.getUserId()==0)
        {
           btnAskQuestion.setVisibility(View.GONE);
            btnSaveConsulation.setVisibility(View.GONE);
        }



        //Start icon_new activity for log in and registration
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFrag= new FragmentSearch();
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag,"search")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Start icon_new activity for log in and registration
        btnAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFrag= FragmentAskQuestion.getInstance();
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag,"ask")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Start icon_new activity for log in and registration
        btnSaveConsulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFrag= FragmentSaveConsultation.getInstance();
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Start icon_new activity for log in and registration
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFrag= new FragmentLogin();
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }


}
