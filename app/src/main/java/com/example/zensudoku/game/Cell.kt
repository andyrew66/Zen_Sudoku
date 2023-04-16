package com.example.zensudoku.game

import java.io.Serializable

class Cell(var value: Int) : Serializable {
    var notes: MutableSet<Int> = HashSet()
    var originalValue = false
    var isInvalidValue = false
    var isHintvalue = false

    init {
        if (value != 0) originalValue = true
    }

    fun getNotes(): Set<Int> {
        val empty: Set<Int> = HashSet()
        return if (value != 0) empty else notes
    }

    fun addNotes(value: Int) {
        if (value == 0) return
        notes.add(value)
    }

    fun setHintvalue() {
        isHintvalue = true
    }

    fun removeNotes(value: Int) {
        notes.remove(value)
    }

    fun removeAllNotes() {
        notes = HashSet()
    }
}