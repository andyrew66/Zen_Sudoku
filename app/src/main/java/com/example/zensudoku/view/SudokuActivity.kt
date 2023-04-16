package com.example.zensudoku.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimatedVectorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.zensudoku.MusicPlayer
import com.example.zensudoku.R
import com.example.zensudoku.SettingsActivity
import com.example.zensudoku.Timer
import com.example.zensudoku.game.Board
import com.example.zensudoku.game.LevelComplete
import com.example.zensudoku.game.PauseMenu
import com.example.zensudoku.game.PuzzleSelector
import com.example.zensudoku.view.custom.SudokuBoardView
import com.example.zensudoku.view.custom.SudokuBoardView.onTouchListener
import com.example.zensudoku.viewmodel.PlaySudokuViewModel
import com.google.gson.Gson
import com.startapp.sdk.adsbase.StartAppAd
import java.io.IOException

class SudokuActivity : AppCompatActivity(), onTouchListener {
    val takingNotes: Observer<Boolean> = object : Observer<Boolean?> {
        override fun onChanged(aBoolean: Boolean) {
            for (bttn in buttons!!) {
                bttn!!.setImageResource(R.drawable.ic_key_transp)
                if (aBoolean) {
                    bttn.setBackgroundResource(noteButtonImages[buttons!!.indexOf(bttn)])
                    try {
                        val button = bttn.background as AnimatedVectorDrawable
                        button.start()
                    } catch (e: Exception) {
                    }
                } else {
                    bttn.setBackgroundResource(regularButtonImages[buttons!!.indexOf(bttn)])
                    try {
                        val button = bttn.background as AnimatedVectorDrawable
                        button.start()
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
    var button1: ImageButton? = null
    var hintCounter = 0
    var difficulty: String? = null
    val updateNotes: Observer<Set<Int>> = object : Observer<Set<Int?>?> {
        override fun onChanged(notes: Set<Int?>) {
            for (bttn in buttons!!) {
                val i = buttons!!.indexOf(bttn) + 1
                if (notes.contains(i)) {
                }
                if (!notes.contains(i)) {
                }
            }
        }
    }
    var timer = Timer()
    val completedBoard = Observer { aBoolean: Boolean ->
        if (aBoolean) {
            timer.stop()
            val fm = supportFragmentManager
            val lc = LevelComplete(difficulty, timer.timer)
            lc.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_NoActionBar_Fullscreen)
            lc.show(fm, "LevelComplete")
        }
    }

    private fun releaseMediaPlayers() {
        if (popNoise != null) {
            popNoise!!.release()
            popNoise = null
        }
        if (hintNoise != null) {
            hintNoise!!.release()
            hintNoise = null
        }
    }

    var hintNoise: MediaPlayer? = null
    var popNoise: MediaPlayer? = null
    var tappedButtonImages: Array<Int>
    var view: PlaySudokuViewModel? = null
    var buttons: MutableList<ImageButton?>? = null
    var sudokuBoardView: SudokuBoardView? = null
    var mPrefs: SharedPreferences? = null
    var bkgrd: MusicPlayer? = null
    var button2: ImageButton? = null
    var button3: ImageButton? = null
    var button4: ImageButton? = null
    var button5: ImageButton? = null
    var button6: ImageButton? = null
    var button7: ImageButton? = null
    var button8: ImageButton? = null
    var button9: ImageButton? = null
    var delete: ImageButton? = null
    var notes: ImageButton? = null
    var hint: ImageButton? = null
    var noteButtonImages: Array<Int>
    var regularButtonImages: Array<Int>
    val cellObserver: Observer<Board> = object : Observer<Board?> {
        override fun onChanged(board: Board) {
            sudokuBoardView!!.updateCells(view!!.sudokuGame.cellsLiveData)
        }
    }
    val updateCell: Observer<Pair<Int, Int>> = object : Observer<Pair<Int?, Int?>?> {
        override fun onChanged(value: Pair<Int?, Int?>) {
            sudokuBoardView!!.updateSelectedCellUI(value.first!!, value.second!!)
        }
    }

    init {
        hintCounter = 3
    }

    init {
        buttons = ArrayList()
        noteButtonImages = arrayOf(R.drawable.ic_key1_to_smol1, R.drawable.ic_key2_to_smol2, R.drawable.ic_key3_to_smol3, R.drawable.ic_key4_to_smol4, R.drawable.ic_key5_to_smol5, R.drawable.ic_key6_to_smol6, R.drawable.ic_key7_to_smol7, R.drawable.ic_key8_to_smol8, R.drawable.ic_key9_to_smol9)
        regularButtonImages = arrayOf(R.drawable.ic_key1_to_big1, R.drawable.ic_key2_to_big2, R.drawable.ic_key3_to_big3, R.drawable.ic_key4_to_big4, R.drawable.ic_key5_to_big5, R.drawable.ic_key6_to_big6, R.drawable.ic_key7_to_big7, R.drawable.ic_key8_to_big8, R.drawable.ic_key9_to_big9)
        tappedButtonImages = arrayOf(R.drawable.ic_key1_tap, R.drawable.ic_key2_tap, R.drawable.ic_key3_tap, R.drawable.ic_key4_tap, R.drawable.ic_key5_tap, R.drawable.ic_key6_tap, R.drawable.ic_key7_tap, R.drawable.ic_key8_tap, R.drawable.ic_key9_tap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StartAppAd.showAd(this)
        mPrefs = getSharedPreferences("game", MODE_PRIVATE)
        timer.start()
        difficulty = intent.getStringExtra("Difficulty")
        bkgrd = MusicPlayer(applicationContext)
        popNoise = MediaPlayer.create(applicationContext, R.raw.pop1)
        hintNoise = MediaPlayer.create(applicationContext, R.raw.hintnoise)
        var ps: PuzzleSelector? = null
        if (!intent.hasExtra("Resume")) {
            ps = PuzzleSelector(this, intent.getStringExtra("Difficulty"))
            try {
                ps.run()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        // Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        view = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        view!!.sudokuGame.selectedCellLiveData.observe(this, updateCell)
        view!!.sudokuGame.cellsLiveData.observe(this, cellObserver)
        view!!.sudokuGame.notesLiveData.observe(this, updateNotes)
        view!!.sudokuGame.isTakingNotes.observe(this, takingNotes)
        view!!.sudokuGame.isComplete.observe(this, completedBoard)
        sudokuBoardView = findViewById(R.id.sudokuBoardView)
        sudokuBoardView.registerListener(this)
        if (ps != null) view!!.sudokuGame.setGame(ps.game)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)
        delete = findViewById(R.id.delete)
        notes = findViewById(R.id.notes)
        val avd = notes.getBackground() as AnimatedVectorDrawable
        avd.start()
        hint = findViewById(R.id.hint)
        val pause = findViewById<ImageButton>(R.id.pauseButton)
        pause.setOnClickListener { v: View? -> pauseMenu() }
        buttons!!.add(button1)
        buttons!!.add(button2)
        buttons!!.add(button3)
        buttons!!.add(button4)
        buttons!!.add(button5)
        buttons!!.add(button6)
        buttons!!.add(button7)
        buttons!!.add(button8)
        buttons!!.add(button9)
        delete.setOnClickListener(View.OnClickListener { v: View? ->
            val delBttn = delete.getBackground() as AnimatedVectorDrawable
            delBttn.start()
            view!!.sudokuGame.delete()
            playPop(true)
        })
        notes.setOnClickListener(View.OnClickListener { v: View? ->
            view!!.sudokuGame.setNotesMode()
            if (view!!.sudokuGame.isTakingNotes.value!!) {
                notes.setBackgroundResource(R.drawable.ic_notes_off)
            } else {
                notes.setBackgroundResource(R.drawable.ic_notes_on)
            }
            val noteB = notes.getBackground() as AnimatedVectorDrawable
            noteB.start()
            playPop(true)
        })
        hint.setOnClickListener(View.OnClickListener { v: View? ->
            val hintBttn = hint.getBackground() as AnimatedVectorDrawable
            hintBttn.start()
            if (hintCounter > 0) {
                view!!.sudokuGame.hint
                hintCounter--
            } else {
                StartAppAd.showAd(this)
                hintCounter = 3
            }
            playPop(false)
        })
        for (bttn in buttons) {
            val i = buttons!!.indexOf(bttn)
            bttn!!.setOnClickListener { v: View? ->
                if (!view!!.sudokuGame.isTakingNotes.value!!) {
                    bttn!!.setBackgroundResource(tappedButtonImages[i])
                    val numBttn = bttn!!.getBackground() as AnimatedVectorDrawable
                    numBttn.start()
                }
                view!!.sudokuGame.handleInput(i + 1)
                playPop(true)
            }
        }
    }

    private fun pauseMenu() {
        timer.stop()
        val fm = supportFragmentManager
        val pauseMenu = PauseMenu()
        pauseMenu.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_NoActionBar_Fullscreen)
        pauseMenu.show(fm, "LevelComplete")
    }

    private fun playPop(soundType: Boolean) {
        val sb = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (sb.getBoolean("sfx", true)) {
            if (soundType) {
                popNoise!!.start()
            } else {
                hintNoise!!.start()
            }
        }
    }

    override fun onStart() {
        val activity: Activity = this
        super.onStart()
        if (intent.hasExtra("Resume")) {
            loadBoard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //super.onBackPressed();
        pauseMenu()
    }

    override fun onCellTouched(row: Int, col: Int) {
        playPop(true)
        view!!.sudokuGame.updateSelectedCellLiveData(row, col)
    }

    override fun onStop() {
        super.onStop()
        saveGame()
        bkgrd!!.stop()
    }

    fun saveGame() {
        val pre = mPrefs!!.edit()
        val gson = Gson()
        val jsonString = gson.toJson(view!!.sudokuGame.cellsLiveData.value)
        pre.putString("boardObj", jsonString)
        pre.putString("difficulty", intent.getStringExtra("difficulty"))
        pre.commit()
    }

    fun loadBoard() {
        val gson = Gson()
        val jsonString = mPrefs!!.getString("boardObj", "")
        difficulty = mPrefs!!.getString("difficulty", "Medium")
        val board = gson.fromJson(jsonString, Board::class.java)
        view!!.sudokuGame.setGame(board.origBoard)
        view!!.sudokuGame.setBoard(board)
        view!!.sudokuGame.cellsLiveData.value = board
    }

    override fun onResume() {
        super.onResume()
        bkgrd!!.musicChecker()
    }
}