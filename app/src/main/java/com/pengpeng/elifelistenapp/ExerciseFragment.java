package com.pengpeng.elifelistenapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private int mPart;


    private WordWrapView mWVExercise;
    private List<String> strList = new ArrayList<String>();
    private String[] ss = new String[]{"My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct","My", "name", "is", "pengpeng",
            "lalala", "This", "is", "the", "first", "time", "Thanks",
            "bye.", "hello", "howareyou", "correct"};
    private List<Integer> blanksList = new ArrayList<Integer>();
    private int[] blanks = new int[]{2, 4, 5, 8, 10};

    public ExerciseFragment() {
        // Required empty public constructor
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
        Collections.addAll(strList, ss);
        for (int blank : blanks) {
            blanksList.add(blank);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_exercise, container, false);
        mWVExercise = (WordWrapView)view.findViewById(R.id.wwv_exercise_text);
        mWVExercise.initExercise(strList, blanksList, mWVExercise);

        return view;
    }

}
