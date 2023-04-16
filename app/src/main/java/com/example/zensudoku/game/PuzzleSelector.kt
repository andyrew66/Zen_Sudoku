package com.example.zensudoku.game

import android.content.Context
import java.io.IOException
import java.util.Random
import java.util.Scanner

class PuzzleSelector(context: Context, difficulty: String) {
    var games: ArrayList<Array<Array<Int>>>? = null
    private val context: Context
    private val difficulty //set difficulty for puzzle
            : String
    private var sudokuGame = Array(9) { arrayOfNulls<Int>(9) }
    private val gamesMap = HashMap<Int, Array<Array<Int?>>>()

    init {
        for (i in 0..8) {
            for (j in 0..8) {
                sudokuGame[i][j] = i + j
            }
        }
    }

    init {
        games = ArrayList()
    }

    init {
        this.difficulty = difficulty
        this.context = context
    }

    @Throws(IOException::class)
    fun run() {
        importP()
    }

    @Throws(IOException::class)
    private fun importP() {
        var c = 0
        val AMM = context.resources.assets
        AMM.locales
        val open = AMM.open("$difficulty.txt")
        val scanner = Scanner(open).useDelimiter(",")
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val scnr = Scanner(line)
            scnr.useDelimiter(",")
            sudokuGame = Array(9) { arrayOfNulls(9) }
            for (i in 0..8) {
                for (j in 0..8) {
                    sudokuGame[i][j] = scnr.next().toInt()
                }
            }
            gamesMap[c] = sudokuGame
            c++
        }
    }

    val game: Array<Array<Int?>>
        get() {
            val rand = Random()
            return gamesMap[rand.nextInt(gamesMap.size)]!!
        }
}