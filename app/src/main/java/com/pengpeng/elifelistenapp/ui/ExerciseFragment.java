package com.pengpeng.elifelistenapp.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.pengpeng.elifelistenapp.common.AudioListManager;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.presenter.AudioDataPresenter;
import com.pengpeng.elifelistenapp.presenter.AudioDataPresenterImpl;
import com.pengpeng.elifelistenapp.utils.LogUtil;
import com.pengpeng.elifelistenapp.utils.Tools;
import com.pengpeng.elifelistenapp.view.AudioAnswerView;
import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.widget.WordWrapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    //    private Audio audioData;
    private String TAG = "ExerciseFragment";

    private AudioDataPresenter mAudioDataPresenter;
    private List<List<EditText>> editTextList = new ArrayList<List<EditText>>();
//    ProgressBar dialog;

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
//        if (audioData == null) {
//            audioData = new Audio();
//        }

        mAudioDataPresenter = new AudioDataPresenterImpl(this);

//
//        Intent intent = getActivity().getIntent();
//
//        index = intent.getIntExtra(Resource.ParamsKey.AUDIO_TEXT, -1);

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
//        dialog = new ProgressBar(getActivity());
//        getActivity().setProgressBarIndeterminateVisibility(true);
        return view;
    }

    public void fillExercise() {
        if (mTextMap != null && mTextMap.size() > 0) {
            if (mWVExercise.getChildCount() > 0) {
                return;
            }
            mWVExercise.initExercise(mTextMap.get(mPart).mTextList, mTextMap.get(mPart).mBlanksIdList, mWVExercise);
        }
    }

    class TextHolder {
        public List<String> mTextList;
        public List<Integer> mBlanksIdList;
    }

    private Map<Integer, TextHolder> mTextMap = new HashMap<>();

    @Override
    public void addAudioData(Audio audio) {
        List<String> textList = audio.getAudioText(); //strList
        List<List<Integer>> blanksList = audio.getAudioTextBlankIndex();
        List<List<Integer>> adaptBlanksList = convertBlanksListToSuitableList(blanksList, textList);
//        str[mPart] = textList.get(mPart);
        if (textList != null && adaptBlanksList != null) {
            mTextMap.clear();
            LogUtil.e(TAG, "pp addAudioData");
            for (int i = 0; i < textList.size(); i++) {
                TextHolder holder = new TextHolder();
                holder.mTextList = convertStringToWordList(textList.get(i));
                holder.mBlanksIdList = adaptBlanksList.get(i);
                mTextMap.put(i, holder);
            }
        }
        fillExercise();
//        dialog.dismiss();
//        blanksList0 = audio.getAudioTextBlankIndex().get(0);
//        blanksList1 = audio.getAudioTextBlankIndex().get(1);
//        blanksList2 = audio.getAudioTextBlankIndex().get(2);
//        Collections.addAll(strArray, string);
//        mWVExercise.initExercise(strArray, blanksList0, mWVExercise);
//
//        mWVExercise.initExercise(strArray, blanksList1, mWVExercise);
//        mWVExercise.initExercise(strArray, blanksList2, mWVExercise);

    }

    private List<List<Integer>> convertBlanksListToSuitableList(List<List<Integer>> blanksList, List<String> textList) {
        LogUtil.e(TAG, "pp blanksList is " + blanksList.toArray().toString());
        List<List<Integer>> adaptList = new ArrayList<>();
        adaptList.add(blanksList.get(0));
        List<Integer> tmpList = new ArrayList<>();
        int sum = 0;
        for (int i = 1; i < blanksList.size(); i++) {
            LogUtil.e(TAG, "pp textList is " + textList.get(i - 1).length());
            List<String> wordList = convertStringToWordList(textList.get(i - 1));
            LogUtil.e(TAG, "pp wordList is " + wordList.size());
            sum += wordList.size();
            List<Integer> partBlanks = blanksList.get(i);
            tmpList.clear();
            for (int pos = 0; pos < partBlanks.size(); pos++) {
                LogUtil.e(TAG, " pp partBlanks is " + partBlanks.get(pos).toString());
                tmpList.add(partBlanks.get(pos) - sum); //将后台得到的按顺序的挖空下标，转换为从1开始的相对下标

            }
            LogUtil.e(TAG, "pp list is " + Tools.convertListToString(tmpList));
            adaptList.add(tmpList);
        }

        return adaptList;
    }

    private List<String> convertStringToWordList(String text) {
        String[] str = text.split(" ");
        LogUtil.e(TAG, "pp convertStringToWordList str is " + str.length);
        List<String> strList = new ArrayList<>();
        Collections.addAll(strList, str);
        LogUtil.e(TAG, "pp convertStringToWordList strList is " + strList.size());
        return strList;
    }

}
