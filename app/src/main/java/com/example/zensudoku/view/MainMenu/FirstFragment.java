package com.example.zensudoku.view.MainMenu;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.zensudoku.*;
import com.example.zensudoku.view.SudokuActivity;

import java.util.HashSet;

public class FirstFragment extends Fragment {

    HashSet<AnimatedVectorDrawable> drawableAnimation = new HashSet<>();


    private void playPop() {

        ((MainMenuActivity) getActivity()).popPlay();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawableAnimation.add((AnimatedVectorDrawable) view.findViewById(R.id.Start).getBackground());
        drawableAnimation.add((AnimatedVectorDrawable) view.findViewById(R.id.Resume).getBackground());
        drawableAnimation.add((AnimatedVectorDrawable) view.findViewById(R.id.OptnBttn).getBackground());

        buttonAnimator();

        view.findViewById(R.id.Start).setOnClickListener(view1 -> {
            playPop();

            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
        });

        view.findViewById(R.id.Resume);
        view.setOnClickListener(v -> {
            playPop();
            Intent i = new Intent(getActivity(), SudokuActivity.class);
            i.putExtra("Resume", true);
            startActivity(i);

        });


        view.findViewById(R.id.OptnBttn).setOnClickListener(v -> {
            playPop();
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);

        });
    }

    private void buttonAnimator() {

        for (AnimatedVectorDrawable animatedVectorDrawable : drawableAnimation) {
            animatedVectorDrawable.start();
        }
    }
}