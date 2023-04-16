package com.example.zensudoku.game

import android.content.Context
import android.content.SharedPreferences
import android.util.Pair
import androidx.lifecycle.MutableLiveData

class SudokuGame(val size: Int) {
    var isComplete = MutableLiveData<Boolean>()
    var cellsLiveData = MutableLiveData<Board?>()
    var notesLiveData = MutableLiveData<Set<Int?>>()
    var isTakingNotes: MutableLiveData<Boolean>? = MutableLiveData()
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var context: Context? = null
    var board: Board? = null
    var mPrefs: SharedPreferences? = null
    var savedBoard: Board? = null
    var game: Array<Array<Int?>>
    private var selectedRow = -1
    private var selectedCol = -1
    private var takingNotes = false

    init {
        game = Array(size) { arrayOfNulls<Int>(size) }
        isTakingNotes!!.value = false
        isComplete.value = false
        selectedCellLiveData.postValue(Pair.create(selectedRow, selectedCol))
    }

    fun handleInput(value: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val returnValues: MutableSet<Int?> = HashSet()
        if (isTakingNotes == null) return
        if (!isTakingNotes!!.value!!) {
            board!!.setCell(selectedRow, selectedCol, value)
        } else {
            if (board!!.getCellNotes(selectedRow, selectedCol).contains(value)) {
                board!!.removeNotes(selectedRow, selectedCol, value)
            } else {
                board!!.addNotes(selectedRow, selectedCol, value)
            }
            returnValues.clear()
            returnValues.addAll(board!!.getCellNotes(selectedRow, selectedCol))
        }
        cellsLiveData.postValue(board)
        notesLiveData.postValue(returnValues)
        isComplete.postValue(board!!.getBoardComplete())
    }


    fun setNotesMode() {
        takingNotes = !takingNotes
        isTakingNotes!!.postValue(takingNotes)
        if (takingNotes) notesLiveData.postValue(board!!.getCellNotes(selectedRow, selectedCol)) else notesLiveData.postValue(HashSet())
    }

    val hint: Unit
        get() {
            board!!.hint
            cellsLiveData.postValue(board)
            isComplete.postValue(board!!.getBoardComplete())
        }

    fun setContext(context: Context) {
        this.context = context
        context.getSharedPreferences("savedData", Context.MODE_PRIVATE)
    }

    fun updateSelectedCellLiveData(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        if (board!!.getCell(row, col) != 0) {
            val empty: Set<Int?> = HashSet()
            notesLiveData.postValue(empty)
        }
        assert(isTakingNotes != null)
        if (isTakingNotes!!.value!!) {
            notesLiveData.postValue(board!!.getCellNotes(row, col))
        }
        isComplete.postValue(board!!.getBoardComplete())
    }

    fun setGame(game: Array<Array<Int?>>) {
        this.game = game
        board = Board(9, game)
        cellsLiveData.value = board
        selectedCellLiveData.postValue(Pair.create(selectedRow, selectedCol))
    }

    fun delete() {
        if (selectedCol == -1 || selectedRow == -1 || isTakingNotes == null) return
        if (isTakingNotes!!.value!!) {
            board!!.removeAllNotes(selectedRow, selectedCol)
            notesLiveData.postValue(HashSet())
        }
        handleInput(0)
    }

    fun setBoard(board: Board?) {
        this.board = board
    }
}