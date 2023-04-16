package com.example.zensudoku.game;

public class SudokuSolver {
    Integer[][] answerBoard = new Integer[9][9];

    public SudokuSolver(Integer[][] board) {
        answerBoard = board;
        helper(answerBoard);
    }

    private boolean helper(Integer[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    continue;
                }

                for (Integer k = 1; k <= 9; k++) {
                    if (isValid(board, i, j, k)) {
                        board[i][j] = k;
                        if (helper(board)) {
                            return true;
                        }
                        board[i][j] = 0;
                    }
                }
                return false;
            }
        }

        return true; //return true if all cells are checked
    }

    private boolean isValid(Integer[][] board, int row, int col, Integer c) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] != 0 && board[i][col] == c) {
                return false;
            }

            if (board[row][i] != 0 && board[row][i] == c) {
                return false;
            }

            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] != 0
                    &&
                    board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) {
                return false;
            }

        }
        return true;
    }

    public Integer[][] getAnswerBoard() {
        return answerBoard;
    }
}
