package com.example.zensudoku.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board implements Serializable {
    static int size;
    boolean boardComplete = false;
    Cell[][] board = new Cell[size][size];
    Integer[][] origBoard;

    Cell cell;
    Cell[][] answer = new Cell[size][size];

    {
        size = 9;
    }


    public Board(int size, Integer[][] board) {
        this.origBoard = board.clone();
        Board.size = size;
        this.board = setCells(board);

        SudokuSolver ss = new SudokuSolver(origBoard);
        this.answer = setCells(ss.getAnswerBoard());
    }

    public Board() {

    }

    private Cell[][] setCells(Integer[][] boardLayout) {


        Cell[][] boards = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cell = new Cell(boardLayout[i][j]);
                boards[i][j] = cell;
            }
        }
        return boards;
    }

    public int getCell(int r, int c) {
        return board[r][c].getValue();
    }

    public boolean getOriginal(int r, int c) {

        return board[r][c].getOriginalValue();
    }

    public Integer[][] getOrigBoard() {
        return origBoard;
    }

    public void setCell(int r, int c, int value) {

        board[r][c].setValue(value);
        checkBoard();


    }

    public void addNotes(int r, int c, int value) {
        board[r][c].addNotes(value);
    }

    public boolean getInvalidCell(int r, int c) {
        return board[r][c].isInvalidValue();
    }


    public Set getCellNotes(int row, int col) {
        if (row == -1 && col == -1) {
            return new HashSet<Integer>();
        }
        return board[row][col].getNotes();

    }

    public void removeNotes(int row, int col, int value) {
        board[row][col].removeNotes(value);
    }

    public void removeAllNotes(int row, int col) {
        board[row][col].removeAllNotes();
    }

    private boolean checkBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].getValue() != answer[i][j].getValue()) return false;
            }
        }
        return true;
    }

    public Boolean getBoardComplete() {
        boardComplete = checkBoard();
        return boardComplete;
    }

    public Boolean getHint(int row, int col) {
        return board[row][col].isHintvalue();
    }

    public void getHint() {

        if (getBoardComplete()) {
            return;
        }
        int max = 9;
        int min = 0;
        Boolean origCell = true;
        Boolean hintCell = false;
        Random r = new Random();
        r.nextInt(max - min);
        int rowPos = 1;
        int colPos = 1;
        while (origCell && !hintCell) {
            rowPos = r.nextInt((max - min) - min);
            colPos = r.nextInt((max - min) - min);
            origCell = board[rowPos][colPos].originalValue;
            if (origCell) {
                hintCell = board[rowPos][colPos].hintvalue;
            }
        }
        setCell(rowPos, colPos, answer[rowPos][colPos].getValue());
        board[rowPos][colPos].setHintvalue();
    }


}

