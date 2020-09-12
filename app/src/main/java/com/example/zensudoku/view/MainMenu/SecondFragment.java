package com.example.zensudoku.view.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.zensudoku.R;
import com.example.zensudoku.view.SudokuActivity;

import java.util.Objects;

public class SecondFragment extends Fragment {

    private void playPop() {

        ((MainMenuActivity) getActivity()).popPlay();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState


    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        //((MainMenuActivity)getActivity()).
        ImageButton bttn = view.findViewById(R.id.Easy);
        ImageButton bttn1 = view.findViewById(R.id.Medium);
        ImageButton bttn2 = view.findViewById(R.id.HardBttn);

        //Easy button
        bttn.setOnClickListener(v -> {
            playPop();
            ((MainMenuActivity) requireActivity()).difficulty = "Easy";
            startLevel();
            

        });

        //Medium
        bttn1.setOnClickListener(v -> {
            playPop();
            ((MainMenuActivity) requireActivity()).difficulty = "Medium";
            startLevel();
            

        });
        bttn2.setOnClickListener(v -> {
            playPop();
            ((MainMenuActivity) requireActivity()).difficulty = "Hard";
            startLevel();
            

        });


        view.findViewById(R.id.button_second).setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    private void startLevel(){

        Intent i = new Intent(getActivity(), SudokuActivity.class);
        i.putExtra("Difficulty",((MainMenuActivity)getActivity()).difficulty);
        startActivity(i);
        ((MainMenuActivity) getActivity()).musicPlayer.stop();
        getActivity().finish();
    }

}