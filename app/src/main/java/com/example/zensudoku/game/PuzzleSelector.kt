package com.example.zensudoku.game;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class PuzzleSelector {
    public ArrayList<Integer[][]> games;
    private Context context;
    private String difficulty; //set difficulty for puzzle
    private Integer[][] sudokuGame = new Integer[9][9];
    private HashMap<Integer, Integer[][]> gamesMap = new HashMap<>();

    {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuGame[i][j] = i + j;
            }

        }
    }

    {
        games = new ArrayList<>();
    }


    public PuzzleSelector(Context context, String difficulty) {
        this.difficulty = difficulty;
        this.context = context;


    }

    public void run() throws IOException {
        importP();
    }

    private void importP() throws IOException {
        int c = 0;
        AssetManager AMM = context.getResources().getAssets();
        AMM.getLocales();
        InputStream open = AMM.open(difficulty + ".txt");
        Scanner scanner = new Scanner(open).useDelimiter(",");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner scnr = new Scanner(line);
            scnr.useDelimiter(",");
            sudokuGame = new Integer[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sudokuGame[i][j] = Integer.parseInt((scnr.next()));
                }

            }

            gamesMap.put(c, sudokuGame);
            c++;
        }
    }

    public Integer[][] getGame() {
        Random rand = new Random();

        return gamesMap.get(rand.nextInt(gamesMap.size()));
    }
}