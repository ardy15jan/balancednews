package lia.customized;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.lia.balancednewsdumb.R;
import lia.Common.FontManager;
import lia.balancednewsdump.MainActivity;
import lia.constants.Constants;


/**
 * Created by xtang on 13-12-2.
 */
public class IntroFragment extends Fragment {

    public static final String LAYOUT_ID = "Layout_ID";

    private ImageView introImage;
    private TextView introText;
    private View rootView;

    private int layoutID;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){



        //Get passed values
        Bundle args = getArguments();
        layoutID = args.getInt(LAYOUT_ID);

        rootView=inflater.inflate(layoutID, container, false);

        /*
        introImage = (ImageView)rootView.findViewById(R.id.intro_image);
        introText = (TextView) rootView.findViewById(R.id.intro_text);
        introText.setTypeface(FontManager.getRobotoLight(getActivity()));
        */
        FontManager.setRobotoLight(getActivity(), rootView);


        Button startButton = (Button) rootView.findViewById(R.id.start_button);
        if(startButton != null){



            startButton.setTypeface(FontManager.getRobotoLight(getActivity()));
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText logInText = (EditText) rootView.findViewById(R.id.intro_login);

                    if(!isInteger(logInText.getText().toString())){
                        Toast.makeText(getActivity(), "User ID must be an integer", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Constants.USER_ID = logInText.getText().toString();

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.PREF, 0).edit();
                    editor.putBoolean(Constants.IS_FIRST_TIME,false);
                    editor.commit();

                    Intent intent=new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                    getActivity().finish();
                }
            });
        }



        return rootView;
    }

    private boolean isInteger(String str){
        try{
           int test = Integer.parseInt(str);
           return true;
        }catch(Exception e){
            return false;
        }
    }
}
