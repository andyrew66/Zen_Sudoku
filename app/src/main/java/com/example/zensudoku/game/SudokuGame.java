package com.example.zensudoku.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.HashSet;
import java.util.Set;


public class SudokuGame {
    public MutableLiveData<Boolean> isComplete = new MutableLiveData<>();
    public MutableLiveData<Board> cellsLiveData = new MutableLiveData<>();
    public MutableLiveData<Set<Integer>> notesLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isTakingNotes = new MutableLiveData<>();
    public MutableLiveData<Pair<Integer, Integer>> selectedCellLiveData = new MutableLiveData<>();
    Context context;
    public Board board;
    SharedPreferences mPrefs;
    Board savedBoard;
    Integer[][] game;


    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean takingNotes = false;

    {
        isTakingNotes.setValue(false);
        isComplete.setValue(false);

        selectedCellLiveData.postValue(Pair.create(selectedRow, selectedCol));

    }

    public SudokuGame() {

    }

    public void handleInput(int value) {

        if (selectedRow == -1 || selectedCol == -1) return;
        Set returnValues = new HashSet<>();
        if (isTakingNotes == null) return;
        if (!isTakingNotes.getValue()) {
            board.setCell(selectedRow, selectedCol, value);
        } else {
            returnValues = board.getCellNotes(selectedRow, selectedCol);
            if (board.getCellNotes(selectedRow, selectedCol).contains(value)) {
                board.removeNotes(selectedRow, selectedCol, value);
            } else {
                board.addNotes(selectedRow, selectedCol, value);
            }
        }
        cellsLiveData.postValue(board);
        notesLiveData.postValue(returnValues);
        isComplete.postValue(board.getBoardComplete());

    }

    public void setNotesMode() {

        takingNotes = !takingNotes;
        isTakingNotes.postValue(takingNotes);
        if (takingNotes) notesLiveData.postValue(board.getCellNotes(selectedRow, selectedCol));
        else notesLiveData.postValue(new HashSet<>());

    }


    public void getHint() {
        board.getHint();
        cellsLiveData.postValue(board);
        isComplete.postValue(board.getBoardComplete());
    }

    public void setContext(Context context) {
        this.context = context;
        context.getSharedPreferences("savedData", Context.MODE_PRIVATE);

    }

    public void updateSelectedCellLiveData(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        selectedCellLiveData.postValue(Pair.create(row, col));
        if (board.getCell(row, col) != 0) {
            Set<Integer> empty = new HashSet<>();
            notesLiveData.postValue(empty);
        }
        assert isTakingNotes != null;
        if (isTakingNotes.getValue()) {
            notesLiveData.postValue(board.getCellNotes(row, col));
        }
        isComplete.postValue(board.getBoardComplete());
    }

    public void setGame(Integer[][] game) {
        this.game = game;
        board = new Board(9, game);
        cellsLiveData.setValue(board);
        selectedCellLiveData.postValue(Pair.create(selectedRow, selectedCol));
    }


    public void delete() {
        if (selectedCol == -1 || selectedRow == -1 || isTakingNotes == null) return;
        if (isTakingNotes.getValue()) {
            board.removeAllNotes(selectedRow, selectedCol);
            notesLiveData.postValue(new HashSet<>());

        }
        handleInput(0);
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
