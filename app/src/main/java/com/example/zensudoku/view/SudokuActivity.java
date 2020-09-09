package com.example.zensudoku.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.example.zensudoku.MusicPlayer;
import com.example.zensudoku.R;
import com.example.zensudoku.SettingsActivity;
import com.example.zensudoku.Timer;
import com.example.zensudoku.game.Board;
import com.example.zensudoku.game.LevelComplete;
import com.example.zensudoku.game.PauseMenu;
import com.example.zensudoku.game.PuzzleSelector;
import com.example.zensudoku.view.custom.SudokuBoardView;
import com.example.zensudoku.viewmodel.PlaySudokuViewModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SudokuActivity extends AppCompatActivity implements SudokuBoardView.onTouchListener {
    public final Observer<Boolean> takingNotes = new Observer<Boolean>() {
        @Override
        public void onChanged(@NotNull Boolean aBoolean) {
            for (ImageButton bttn : buttons) {
                bttn.setImageResource(R.drawable.ic_key_transp);
                if (aBoolean) {
                    bttn.setBackgroundResource(noteButtonImages[buttons.indexOf(bttn)]);
                    try {
                        AnimatedVectorDrawable button = (AnimatedVectorDrawable) bttn.getBackground();
                        button.start();
                    } catch (Exception e) {

                    }

                } else {
                    bttn.setBackgroundResource(regularButtonImages[buttons.indexOf(bttn)]);
                    try {
                        AnimatedVectorDrawable button = (AnimatedVectorDrawable) bttn.getBackground();
                        button.start();
                    } catch (Exception e) {

                    }
                }
            }


        }
    };
    ImageButton button1;
    int hintCounter;
    String difficulty;
    public final Observer<Set<Integer>> updateNotes = new Observer<Set<Integer>>() {
        @Override
        public void onChanged(Set<Integer> notes) {
            for (ImageButton bttn : buttons) {
                int i = buttons.indexOf(bttn) + 1;
                if (notes.contains(i)) {

                }
                if (!notes.contains(i)) {
                }
            }
        }
    };
    Timer timer = new Timer();
    public final Observer<Boolean> completedBoard = aBoolean -> {
        if (aBoolean) {
            timer.stop();
            FragmentManager fm = getSupportFragmentManager();
            LevelComplete lc = new LevelComplete(difficulty, timer.getTimer());
            lc.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_NoActionBar_Fullscreen);


            lc.show(fm, "LevelComplete");


        }

    };
    MediaPlayer hintNoise;
    MediaPlayer popNoise;
    Integer[] tappedButtonImages;
    PlaySudokuViewModel view;


    final List<ImageButton> buttons;
    SudokuBoardView sudokuBoardView;
    SharedPreferences mPrefs;
    MusicPlayer bkgrd;
    ImageButton button2;
    ImageButton button3;
    ImageButton button4;
    ImageButton button5;
    ImageButton button6;
    ImageButton button7;
    ImageButton button8;
    ImageButton button9;
    ImageButton delete;
    ImageButton notes;
    ImageButton hint;
    Integer[] noteButtonImages;
    Integer[] regularButtonImages;

    public final Observer<Board> cellObserver = new Observer<Board>() {
        @Override
        public void onChanged(Board board) {

            sudokuBoardView.updateCells(view.sudokuGame.cellsLiveData);
        }
    };
    public final Observer<Pair<Integer, Integer>> updateCell = new Observer<Pair<Integer, Integer>>() {
        @Override
        public void onChanged(Pair<Integer, Integer> value) {
            sudokuBoardView.updateSelectedCellUI(value.first, value.second);
        }

    };

    {
        hintCounter = 9999;
    }

    {
        buttons = new ArrayList<>();
        noteButtonImages = new Integer[]{R.drawable.ic_key1_to_smol1, R.drawable.ic_key2_to_smol2, R.drawable.ic_key3_to_smol3, R.drawable.ic_key4_to_smol4, R.drawable.ic_key5_to_smol5, R.drawable.ic_key6_to_smol6, R.drawable.ic_key7_to_smol7, R.drawable.ic_key8_to_smol8, R.drawable.ic_key9_to_smol9};
        regularButtonImages = new Integer[]{R.drawable.ic_key1_to_big1, R.drawable.ic_key2_to_big2, R.drawable.ic_key3_to_big3, R.drawable.ic_key4_to_big4, R.drawable.ic_key5_to_big5, R.drawable.ic_key6_to_big6, R.drawable.ic_key7_to_big7, R.drawable.ic_key8_to_big8, R.drawable.ic_key9_to_big9};
        tappedButtonImages = new Integer[]{R.drawable.ic_key1_tap, R.drawable.ic_key2_tap, R.drawable.ic_key3_tap, R.drawable.ic_key4_tap, R.drawable.ic_key5_tap, R.drawable.ic_key6_tap, R.drawable.ic_key7_tap, R.drawable.ic_key8_tap, R.drawable.ic_key9_tap};
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mPrefs = getPreferences(MODE_PRIVATE);

        timer.start();
        difficulty = getIntent().getStringExtra("Difficulty");

        bkgrd = new MusicPlayer(getApplicationContext());
        popNoise = MediaPlayer.create(getApplicationContext(), R.raw.pop1);
        hintNoise = MediaPlayer.create(getApplicationContext(), R.raw.hintnoise);

        PuzzleSelector ps = null;
        if (!getIntent().hasExtra("Resume")) {
            ps = new PuzzleSelector(this, getIntent().getStringExtra("Difficulty"));

            try {
                ps.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);


        view = ViewModelProviders.of(this).get(PlaySudokuViewModel.class);
        view.sudokuGame.selectedCellLiveData.observe(this, updateCell);
        view.sudokuGame.cellsLiveData.observe(this, cellObserver);
        view.sudokuGame.notesLiveData.observe(this, updateNotes);
        view.sudokuGame.isTakingNotes.observe(this, takingNotes);
        view.sudokuGame.isComplete.observe(this, completedBoard);
        sudokuBoardView = findViewById(R.id.sudokuBoardView);
        sudokuBoardView.registerListener(this);

        if (ps != null) view.sudokuGame.setGame(ps.getGame());
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        delete = findViewById(R.id.delete);
        notes = findViewById(R.id.notes);
        AnimatedVectorDrawable avd = (AnimatedVectorDrawable) notes.getBackground();
        avd.start();
        hint = findViewById(R.id.hint);
        ImageButton pause = (ImageButton) findViewById(R.id.pauseButton);

        pause.setOnClickListener(v -> {
            pauseMenu();
        });
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);
        delete.setOnClickListener(v -> {
            AnimatedVectorDrawable delBttn = (AnimatedVectorDrawable) delete.getBackground();
            delBttn.start();
            view.sudokuGame.delete();
            playPop(true);
        });
        notes.setOnClickListener(v -> {
            view.sudokuGame.setNotesMode();
            if (view.sudokuGame.isTakingNotes.getValue()) {
                notes.setBackgroundResource(R.drawable.ic_notes_off);
            } else {
                notes.setBackgroundResource(R.drawable.ic_notes_on);
            }
            AnimatedVectorDrawable noteB = (AnimatedVectorDrawable) notes.getBackground();
            noteB.start();

            playPop(true);
        });
        hint.setOnClickListener(v -> {
            AnimatedVectorDrawable hintBttn = (AnimatedVectorDrawable) hint.getBackground();

            hintBttn.start();

            if (hintCounter > 0) {
                view.sudokuGame.getHint();
                hintCounter--;
            }
            playPop(false);
        });

        for (final ImageButton bttn : buttons) {
            int i = buttons.indexOf(bttn);


            bttn.setOnClickListener(v -> {
                if (!view.sudokuGame.isTakingNotes.getValue()) {
                    bttn.setBackgroundResource(tappedButtonImages[i]);
                    AnimatedVectorDrawable numBttn = (AnimatedVectorDrawable) bttn.getBackground();
                    numBttn.start();
                }

                view.sudokuGame.handleInput(i + 1);
                playPop(true);
            });
        }
    }

    private void pauseMenu() {

        timer.stop();
        FragmentManager fm = getSupportFragmentManager();
        PauseMenu pauseMenu = new PauseMenu();
        pauseMenu.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_NoActionBar_Fullscreen);
        pauseMenu.show(fm, "LevelComplete");
    }

    private void playPop(Boolean soundType) {
        SharedPreferences sb = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sb.getBoolean("sfx", true)) {
            if (soundType) {
                popNoise.start();
            } else {
                hintNoise.start();

            }
        }
    }

    @Override
    protected void onStart() {
        Activity activity = this;
        super.onStart();
        if (getIntent().hasExtra("Resume")) {
            loadBoard();
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        pauseMenu();

    }

    @Override
    public void onCellTouched(int row, int col) {
        playPop(true);
        view.sudokuGame.updateSelectedCellLiveData(row, col);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGame();
        bkgrd.stop();
    }


    public void saveGame() {
        SharedPreferences.Editor pre = mPrefs.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(view.sudokuGame.cellsLiveData.getValue());
        pre.putString("boardObj", jsonString);
        pre.putString("difficulty", getIntent().getStringExtra("difficulty"));
        pre.commit();
    }

    public void loadBoard() {
        Gson gson = new Gson();
        String jsonString = mPrefs.getString("boardObj", "");
        difficulty = mPrefs.getString("difficulty", "Medium");
        Board board = gson.fromJson(jsonString, Board.class);
        view.sudokuGame.setGame(board.getOrigBoard());
        view.sudokuGame.setBoard(board);
        view.sudokuGame.cellsLiveData.setValue(board);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bkgrd.musicChecker();
    }


}