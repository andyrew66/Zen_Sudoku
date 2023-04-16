package com.example.zensudoku.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.zensudoku.R;
import com.example.zensudoku.view.SudokuActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelComplete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelComplete extends DialogFragment {

    static String timerText;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String levelDifficulty = "";
    public String emptyTimer = "";

    ImageButton nextLevelButton;

    public LevelComplete(String levelDifficulty, String timer) {
        // Required empty public constructor
        LevelComplete.levelDifficulty = levelDifficulty;
        timerText = timer;
    }


    // TODO: Rename and change types and number of parameters
    public static LevelComplete newInstance() {
        LevelComplete fragment = new LevelComplete(levelDifficulty, timerText);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setEnterTransition(@Nullable Object transition) {
        super.setEnterTransition(transition);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().alpha = 0.9f;
        setCancelable(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView timer = view.findViewById(R.id.timer);
        CharSequence s = "Time Taken: " + timerText;
        timer.setText(s);
        nextLevelButton = view.findViewById(R.id.nextLevelButton);
        nextLevelButton.setOnClickListener(v -> {
                nextLevelButtonClick();
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_level_complete, container, false);
    }

    private void nextLevelButtonClick() {

        Intent i = new Intent(getActivity(), SudokuActivity.class);
        i.putExtra("Difficulty", levelDifficulty);
        startActivity(i);
        getActivity().finish();
    }


}