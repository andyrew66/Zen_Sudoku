package com.example.zensudoku.game

import java.io.Serializable
import java.util.Random

class Board(val size: Int = 9) : Serializable {
    var boardComplete = false
    var board: Array<Array<Cell?>>
    var origBoard: Array<Array<Int?>>
    var cell: Cell? = null
    var answer: Array<Array<Cell?>>

    init {
        board = Array(size) { arrayOfNulls<Cell>(size) }
        origBoard = Array(size) { arrayOfNulls<Int>(size) }
        answer = Array(size) { arrayOfNulls<Cell>(size) }
    }

    constructor(size: Int, board: Array<Array<Int?>>) : this(size) {
        origBoard = board.clone()
        this.board = setCells(board)
        val ss = SudokuSolver(origBoard)
        answer = setCells(ss.answerBoard)
    }

    private fun setCells(boardLayout: Array<Array<Int?>>): Array<Array<Cell?>> {
        val boards = Array(size) { arrayOfNulls<Cell>(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                cell = Cell(boardLayout[i][j]!!)
                boards[i][j] = cell
            }
        }
        return boards
    }

    fun getCell(r: Int, c: Int): Int {
        return board[r][c]!!.value
    }

    fun getOriginal(r: Int, c: Int): Boolean {
        return board[r][c]!!.originalValue
    }

    fun setCell(r: Int, c: Int, value: Int) {
        board[r][c]!!.value = value
        checkBoard()
    }

    fun addNotes(r: Int, c: Int, value: Int) {
        board[r][c]!!.addNotes(value)
    }

    fun getInvalidCell(r: Int, c: Int): Boolean {
        return board[r][c]!!.isInvalidValue
    }

    fun getCellNotes(row: Int, col: Int): Set<Int?> {
        return if (row == -1 && col == -1) {
            HashSet<Int>()
        } else board[row][col]!!.getNotes()
    }


    fun removeNotes(row: Int, col: Int, value: Int) {
        board[row][col]!!.removeNotes(value)
    }

    fun removeAllNotes(row: Int, col: Int) {
        board[row][col]!!.removeAllNotes()
    }

    private fun checkBoard(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j]!!.value != answer[i][j]!!.value) return false
            }
        }
        return true
    }

    fun getBoardComplete(): Boolean {
        boardComplete = checkBoard()
        return boardComplete
    }

    fun getHint(row: Int, col: Int): Boolean {
        return board[row][col]!!.isHintvalue
    }

    val hint: Unit
        get() {
            if (getBoardComplete()) {
                return
            }
            val max = 9
            val min = 0
            var origCell = true
            var hintCell = false
            val r = Random()
            r.nextInt(max - min)
            var rowPos = 1
            var colPos = 1
            while (origCell && !hintCell) {
                rowPos = r.nextInt(max - min - min)
                colPos = r.nextInt(max - min - min)
                origCell = board[rowPos][colPos]!!.originalValue
                if (origCell) {
                    hintCell = board[rowPos][colPos]!!.isHintvalue
                }
            }
            setCell(rowPos, colPos, answer[rowPos][colPos]!!.value)
            board[rowPos][colPos]!!.setHintvalue()
        }

    companion object {
        var size = 0
    }
}