package com.pengpeng.elifelistenapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pengpeng.elifelistenapp.common.AudioListManager;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.presenter.AudioDataPresenter;
import com.pengpeng.elifelistenapp.presenter.AudioDataPresenterImpl;
import com.pengpeng.elifelistenapp.view.AudioAnswerView;
import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.utils.Resource;
import com.pengpeng.elifelistenapp.widget.WordWrapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment implements AudioAnswerView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private int mPart;
    private Audio audioData;

    private AudioDataPresenter mAudioDataPresenter;
    private List<List<EditText>> editTextList = new ArrayList<List<EditText>>();

    private WordWrapView mWVExercise;
    private int index;
    private List<String> strList = new ArrayList<String>();
    private List<String> strArray = new ArrayList<String>();
    private String[] str = new String[100];
    private String[] string = new String[100];
//    private AudioListManager mManager;
    private Audio mAudio;
    private List<Integer> blanksList0 = new ArrayList<Integer>();
    private List<Integer> blanksList1 = new ArrayList<Integer>();
    private List<Integer> blanksList2 = new ArrayList<Integer>();
    private List<Integer> blanks = new ArrayList<Integer>();
   private int[] blanksInt = new int[50];
    private String[] stringAnswer = new String[50];

    public ExerciseFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param part the number of Exercise Part.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(int part) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt("part", part);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPart = getArguments().getInt("part");
        }
        if (audioData == null) {
            audioData = new Audio();
        }

        mAudioDataPresenter = new AudioDataPresenterImpl(this);


        Intent intent = getActivity().getIntent();

        index = intent.getIntExtra(Resource.ParamsKey.AUDIO_TEXT, -1);

        mAudio = AudioListManager.getInstance().getCurrentAudio();

        mAudioDataPresenter.loadAudioData(mAudio.getAudioId(), "123");
        // mAudioAnswerPresenter.loadAudioAnswer(mAudio.getAudioId(),mAudio.getAudioPartEndTime(),"123",);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        mWVExercise = (WordWrapView) view.findViewById(R.id.wwv_exercise_text);
        return view;
    }

    @Override

    public void addAudioData(Audio audioData) {
        strList = audioData.getAudioText();
        str[mPart] = strList.get(mPart);
        handleText(str[mPart]);
        blanksList0 = audioData.getAudioTextBlankIndex().get(0);
        blanksList1 = audioData.getAudioTextBlankIndex().get(1);
        blanksList2 = audioData.getAudioTextBlankIndex().get(2);
        Collections.addAll(strArray, string);
        mWVExercise.initExercise(strArray, blanksList0, mWVExercise);

        mWVExercise.initExercise(strArray, blanksList1, mWVExercise);
        mWVExercise.initExercise(strArray, blanksList2, mWVExercise);

   }
    public void handleText(String str) {

        string=str.split(" ");
    }

}
