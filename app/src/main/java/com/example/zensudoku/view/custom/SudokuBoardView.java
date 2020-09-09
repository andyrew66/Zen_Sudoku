package com.example.zensudoku.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.example.zensudoku.game.Board;

import java.util.Set;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.graphics.Paint.Style.STROKE;

public class SudokuBoardView extends View {
    static Board board = new Board();
    static boolean firstRun = true;
    private static Integer selectedRow = -1;
    private static Integer selectedCol = -1;
    public SudokuBoardView.onTouchListener listener = (row, col) -> {
        selectedRow = row;
        selectedCol = col;
    };

    Paint paint = new Paint();
    Paint textPaint = new Paint();
    Paint notePaint = new Paint();
    Paint invalidPaint = new Paint();
    Paint startingCellTextPaint = new Paint();
    Paint startingCell = new Paint();
    Paint hintPaint = new Paint();
    float boardSize = 9;
    float sqrt = 3;
    private float cellSizePixels;
    private float noteSizePixels;

    {
        textPaint.setStyle(FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);

        startingCellTextPaint.setStyle(FILL_AND_STROKE);
        startingCellTextPaint.setColor(Color.BLACK);


        startingCell.setStyle(FILL);
        startingCell.setColor(Color.rgb(218, 218, 218));
        startingCell.setTypeface(Typeface.DEFAULT_BOLD);

        invalidPaint.setStyle(FILL);
        invalidPaint.setColor(Color.rgb(233, 0, 0));

        notePaint.setStyle(FILL_AND_STROKE);
        notePaint.setColor(Color.BLACK);

        hintPaint.setStyle(FILL_AND_STROKE);
        hintPaint.setColor(Color.rgb(255, 174, 201));

    }


    public SudokuBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateSelectedCellUI(Integer row, Integer col) {
        selectedRow = row;
        selectedCol = col;
        invalidate();
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        paint.setColor(Color.rgb(0, 0, 0));

        paint.setStyle(STROKE);
        paint.setStrokeWidth(3);
    }

    public void setThickPaint(Paint paint) {
        this.paint = paint;
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setAlpha(150);
        paint.setStyle(STROKE);
        paint.setStrokeWidth(12);
    }

    public void selectedCellPaint(Paint paint) {
        this.paint = paint;
        paint.setColor(Color.rgb(192, 192, 192));
        paint.setStyle(FILL_AND_STROKE);
    }

    public void conflictingCellPaint(Paint paint) {
        this.paint = paint;
        paint.setColor(Color.rgb(225, 192, 192));
        paint.setStyle(FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixels, sizePixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        cellSizePixels = (float) getMeasuredWidth() / boardSize;
        noteSizePixels = (float) getMeasuredWidth() / boardSize;
        updateMeasurements(getWidth());

        fillText(canvas);

        drawDuplicates(canvas);
        drawLines(canvas);

    }

    private void updateMeasurements(float width) {

        cellSizePixels = width / boardSize;
        noteSizePixels = cellSizePixels / sqrt;
        notePaint.setTextSize(cellSizePixels / sqrt);
        notePaint.setTextSize(notePaint.getTextSize() * 0.80f);
        textPaint.setTextSize(cellSizePixels / 1.5F);
        startingCellTextPaint.setTextSize(cellSizePixels / 1.5F);
    }

    private void drawDuplicates(Canvas canvas) {
        if (selectedCol == -1 || selectedRow == -1) return;
        int row = selectedRow;
        int col = selectedCol;

        if (board.getInvalidCell(row, col))
            canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels, (row + 1) * cellSizePixels, invalidPaint);
    }

    private void fillCells(Canvas canvas) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                //
                int blockRow = (int) (row / sqrt);
                int blockCol = (int) (col / sqrt);
                if (board.getOriginal(row, col)) {
                    paintCell(canvas, row, col, startingCell);
                    drawText(row, col, canvas, startingCellTextPaint);

                } else if ((board.getHint(row, col))) {
                    paintCell(canvas, row, col, hintPaint);
                } else if (selectedRow == -1 && selectedCol == -1) {

                } else if (row == selectedRow && col == selectedCol) {
                    selectedCellPaint(paint);
                    paintCell(canvas, row, col, paint);
                } else if (row == selectedRow && col != selectedCol) {
                    conflictingCellPaint(paint);
                    paintCell(canvas, row, col, paint);
                } else if (col == selectedCol) {
                    conflictingCellPaint(paint);
                    paintCell(canvas, row, col, paint);
                } else if ((int) (selectedRow / sqrt) == blockRow && (int) (selectedCol / sqrt) == blockCol) {
                    conflictingCellPaint(paint);
                    paintCell(canvas, row, col, paint);
                }
                {
                    drawText(row, col, canvas, textPaint);
                    drawNotes(row, col, canvas);
                }

            }
        }
        firstRun = false;

    }

    private void fillText(Canvas canvas) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                drawText(i, j, canvas, textPaint);
                drawNotes(i, j, canvas);

            }
            fillCells(canvas);
        }
    }

    private void drawText(int row, int col, Canvas canvas, Paint paint) {
        StringBuilder cellString = new StringBuilder().append(board.getCell(row, col));
        String valueString;
        if (cellString.toString().equals("0")) {
            valueString = " ";
        } else {
            valueString = cellString.toString();
        }

        Rect textBounds = new Rect();
        textPaint.getTextBounds(valueString, 0, valueString.length(), textBounds);

        float textWidth = textPaint.measureText(valueString);
        float textHeight = textBounds.height();

        canvas.drawText(valueString,
                (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                (row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                paint);
    }

    private void paintCell(Canvas canvas, int row, int col, Paint paint) {

        canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels, (row + 1) * cellSizePixels - 2, paint);
    }

    private void drawNotes(int row, int col, Canvas canvas) {

        Set cellNotes = board.getCellNotes(row, col);

        cellNotes.forEach((note) -> {

            Rect textBounds = new Rect();

            Integer notes = (Integer) note;
            String text = "" + note;
            float textWidth = notePaint.measureText(text);
            float textHeight = textBounds.height();


            int rowInCell = (int) ((notes - 1) / sqrt);
            int colInCell = (int) ((notes - 1) % sqrt);
            notePaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text,
                    (col * cellSizePixels) + (colInCell * noteSizePixels) + noteSizePixels / 2f - textWidth / 2f,
                    (row * cellSizePixels) + (rowInCell * noteSizePixels) + noteSizePixels / 1.4f + textHeight / 2,
                    notePaint);

        });


    }

    private void drawLines(Canvas canvas) {
        setPaint(paint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        for (int i = 0; i < 10; i++) {
            if (i % sqrt == 0) {
                setThickPaint(paint);
            } else {
                setPaint(paint);
            }
            canvas.drawLine(i * cellSizePixels, 0f, i * cellSizePixels, getMeasuredHeight(), paint);
            canvas.drawLine(i * cellSizePixels, 0f, i * cellSizePixels, getMeasuredHeight(), paint);
        }

        for (int i = 0; i < 10; i++) {
            if (i % sqrt == 0) {
                setThickPaint(paint);
            } else {
                setPaint(paint);
            }
            canvas.drawLine(0, i * cellSizePixels, getMeasuredWidth(), i * cellSizePixels, paint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (event.getAction() == action) {

            handleTouchEvent(event.getX(), event.getY());
            return true;
        } else {
            return false;
        }


    }

    private void handleTouchEvent(float x, float y) {

        int possSelectedRow = (int) (y / cellSizePixels);
        int possSelectedCol = (int) (x / cellSizePixels);
        if (!board.getOriginal(possSelectedRow, possSelectedCol)) {
            listener.onCellTouched(possSelectedRow, possSelectedCol);
        }
        invalidate();

    }

    public void updateCells(MutableLiveData<Board> cellsLiveData) {
        board = cellsLiveData.getValue();
        invalidate();
    }

    public void registerListener(SudokuBoardView.onTouchListener listener) {
        this.listener = listener;
        invalidate();
    }


    public interface onTouchListener {
        void onCellTouched(int row, int col);
    }
}
