package com.example.zensudoku.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.zensudoku.R;
import com.example.zensudoku.SettingsActivity;
import com.example.zensudoku.view.MainMenu.MainMenuActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PauseMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PauseMenu extends DialogFragment {

    ImageButton resume;
    ImageButton exitBtn;
    ImageButton optnsBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PauseMenu() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment PauseMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static PauseMenu newInstance() {
        PauseMenu fragment = new PauseMenu();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pause_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resume = (ImageButton) view.findViewById(R.id.resumePause);
        exitBtn = (ImageButton) view.findViewById(R.id.exit);
        optnsBtn = (ImageButton) view.findViewById(R.id.optnsPause);

        resume.setOnClickListener(v -> {
            getDialog().dismiss();
        });
        exitBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MainMenuActivity.class);
            startActivity(i);
            getActivity().finish();
        });
        optnsBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().alpha = 0.9f;
    }

}