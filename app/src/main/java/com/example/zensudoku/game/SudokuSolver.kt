package com.example.zensudoku.game

class SudokuSolver(board: Array<Array<Int?>>) {
    var answerBoard = Array(9) { arrayOfNulls<Int>(9) }

    init {
        answerBoard = board
        helper(answerBoard)
    }

    private fun helper(board: Array<Array<Int?>>): Boolean {
        for (i in 0..8) {
            for (j in 0..8) {
                if (board[i][j] != 0) {
                    continue
                }
                for (k in 1..9) {
                    if (isValid(board, i, j, k)) {
                        board[i][j] = k
                        if (helper(board)) {
                            return true
                        }
                        board[i][j] = 0
                    }
                }
                return false
            }
        }
        return true //return true if all cells are checked
    }

    private fun isValid(board: Array<Array<Int?>>, row: Int, col: Int, c: Int): Boolean {
        for (i in 0..8) {
            if (board[i][col] != 0 && board[i][col] === c) {
                return false
            }
            if (board[row][i] != 0 && board[row][i] === c) {
                return false
            }
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] != 0
                    &&
                    board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] === c) {
                return false
            }
        }
        return true
    }
}