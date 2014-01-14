package lia.customized;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.lia.balancednewsdumb.R;
import lia.constants.Constants;
import lia.model.Answer;


/**
 * Created by xtang on 13-12-10.
 */
public class QuestionFragment extends Fragment {

    public static final String TAGS = "TAGS";
    public static final String TOPIC_ID = "TOPIC_ID";
    public static final String INDEX = "INDEX";

    private View rootView;
    private TextView questionSubject;
    private ArrayList<RadioButton> radioButtons;
    private RadioGroup radioGroup;
    private EditText descriptionText;
    private CheckBox checkBox;

    private int topicID;
    private Answer answer;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        //Get passed values
        Bundle args = getArguments();

        topicID = args.getInt(QuestionFragment.TOPIC_ID);
        ArrayList<String> tags = args.getStringArrayList(QuestionFragment.TAGS);
        int index = args.getInt(QuestionFragment.INDEX);

        rootView=inflater.inflate(R.layout.question_page, container, false);
        questionSubject = (TextView) rootView.findViewById(R.id.question_subject);

        String tagsStr = tags.get(0);
        for(int i=1; i<3;i++){
            tagsStr = tagsStr + ", "+tags.get(i);
        }

        String subject = "Subject "+(index+1) + "(" + tagsStr + ")";
        questionSubject.setText(subject);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.choices);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.choice_level_3 || checkedId == R.id.choice_level_4 || checkedId == R.id.choice_level_5){
                    rootView.findViewById(R.id.second_question).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.subject_description).setVisibility(View.VISIBLE);
                }else{
                    rootView.findViewById(R.id.second_question).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.subject_description).setVisibility(View.INVISIBLE);
                }
            }
        });

        checkBox = (CheckBox) rootView.findViewById(R.id.check_is_read);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rootView.findViewById(R.id.second_question).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.subject_description).setVisibility(View.INVISIBLE);
                    radioGroup.setVisibility(View.INVISIBLE);
                }else{
                    radioGroup.findViewById(R.id.choices).setVisibility(View.VISIBLE);
                    radioGroup.check(R.id.choice_level_1);
                }
            }
        });

        descriptionText = (EditText) rootView.findViewById(R.id.subject_description);

        return rootView;
    }

    @Override
    public void setUserVisibleHint (boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           if(answer == null)
               answer = new Answer(-1, null, topicID);

        }else{
            if(answer != null){
                answer.topic_id = topicID;
                answer.description = descriptionText.getText().toString();
                answer.understanding = getUnderstandingLevel();

                if(Constants.answers.contains(answer))
                    Constants.answers.remove(answer);

                Constants.answers.add(answer);
            }
        }
    }

    private int getUnderstandingLevel(){
        if(checkBox.isChecked())
            return -1;

        if(radioGroup != null){
            int checkedID = radioGroup.getCheckedRadioButtonId();
            switch (checkedID){
                case R.id.choice_level_1:
                    return 1;
                case R.id.choice_level_2:
                    return 2;
                case R.id.choice_level_3:
                    return 3;
                case R.id.choice_level_4:
                    return 4;
                case R.id.choice_level_5:
                    return 5;
                default:
                    return -1;
            }
        }else
            return -1;
    }


}
