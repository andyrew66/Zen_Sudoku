package com.example.zensudoku.viewmodel;


import androidx.lifecycle.ViewModel;

import com.example.zensudoku.game.SudokuGame;


public class PlaySudokuViewModel extends ViewModel {
    public SudokuGame sudokuGame = new SudokuGame();
}

