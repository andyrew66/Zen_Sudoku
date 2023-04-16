package com.example.zensudoku.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.zensudoku.game.Board

class SudokuBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var listener: OnTouchListener? = null


    var paint = Paint()
    var textPaint = Paint()
    var notePaint = Paint()
    var invalidPaint = Paint()
    var startingCellTextPaint = Paint()
    var startingCell = Paint()
    var hintPaint = Paint()
    var boardSize = 9f
    var sqrt = 3f
    private var cellSizePixels = 0f
    private var noteSizePixels = 0f

    init {
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.color = Color.BLACK
        startingCellTextPaint.style = Paint.Style.FILL_AND_STROKE
        startingCellTextPaint.color = Color.BLACK
        startingCell.style = Paint.Style.FILL
        startingCell.color = Color.rgb(218, 218, 218)
        startingCell.typeface = Typeface.DEFAULT_BOLD
        invalidPaint.style = Paint.Style.FILL
        invalidPaint.color = Color.rgb(233, 0, 0)
        notePaint.style = Paint.Style.FILL_AND_STROKE
        notePaint.color = Color.BLACK
        hintPaint.style = Paint.Style.FILL_AND_STROKE
        hintPaint.color = Color.rgb(255, 174, 201)
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun setPaint(paint: Paint) {
        this.paint = paint
        paint.color = Color.rgb(0, 0, 0)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
    }

    fun setThickPaint(paint: Paint) {
        this.paint = paint
        paint.color = Color.rgb(0, 0, 0)
        paint.alpha = 150
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 12f
    }

    fun selectedCellPaint(paint: Paint) {
        this.paint = paint
        paint.color = Color.rgb(192, 192, 192)
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    fun conflictingCellPaint(paint: Paint) {
        this.paint = paint
        paint.color = Color.rgb(225, 192, 192)
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = measuredWidth.toFloat() / boardSize
        noteSizePixels = measuredWidth.toFloat() / boardSize
        updateMeasurements(width.toFloat())
        fillCells(canvas)
        drawDuplicates(canvas)
        drawLines(canvas)
    }

    private fun updateMeasurements(width: Float) {
        cellSizePixels = width / boardSize
        noteSizePixels = cellSizePixels / sqrt
        notePaint.textSize = cellSizePixels / sqrt
        notePaint.textSize = notePaint.textSize * 0.80f
        textPaint.textSize = cellSizePixels / 1.5f
        startingCellTextPaint.textSize = cellSizePixels / 1.5f
    }

    private fun drawDuplicates(canvas: Canvas) {
        if (selectedCol == -1 || selectedRow == -1) return
        val row = selectedRow
        val col = selectedCol
        if (board!!.getInvalidCell(row, col)) canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels, (row + 1) * cellSizePixels, invalidPaint)
    }

    private fun fillCells(canvas: Canvas) {
        for (row in 0 until boardSize.toInt()) {
            for (col in 0 until boardSize.toInt()) {
                val blockRow = (row / sqrt).toInt()
                val blockCol = (col / sqrt).toInt()

                if (board!!.getOriginal(row, col)) {
                    paintCell(canvas, row, col, startingCell)
                    drawText(row, col, canvas, startingCellTextPaint)
                } else if (board!!.getHint(row, col)) {
                    paintCell(canvas, row, col, hintPaint)
                } else if (selectedRow == -1 && selectedCol == -1) {
                } else if (row == selectedRow && col == selectedCol) {
                    selectedCellPaint(paint)
                    paintCell(canvas, row, col, paint)
                } else if (row == selectedRow || col == selectedCol ||
                        (selectedRow / sqrt).toInt() == blockRow && (selectedCol / sqrt).toInt() == blockCol) {
                    conflictingCellPaint(paint)
                    paintCell(canvas, row, col, paint)
                }

                drawText(row, col, canvas, textPaint)
                drawNotes(row, col, canvas)
            }
        }
    }

    private fun fillText(canvas: Canvas) {
        var i = 0
        while (i < boardSize) {
            var j = 0
            while (j < boardSize) {
                drawText(i, j, canvas, textPaint)
                drawNotes(i, j, canvas)
                j++
            }
            fillCells(canvas)
            i++
        }
    }

    private fun drawText(row: Int, col: Int, canvas: Canvas, paint: Paint) {
        val cellString = StringBuilder().append(board!!.getCell(row, col))
        val valueString: String
        valueString = if (cellString.toString() == "0") {
            " "
        } else {
            cellString.toString()
        }
        val textBounds = Rect()
        textPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
        val textWidth = textPaint.measureText(valueString)
        val textHeight = textBounds.height().toFloat()
        canvas.drawText(valueString,
                col * cellSizePixels + cellSizePixels / 2 - textWidth / 2,
                row * cellSizePixels + cellSizePixels / 2 + textHeight / 2,
                paint)
    }

    private fun paintCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col + 1) * cellSizePixels, (row + 1) * cellSizePixels - 2, paint)
    }

    private fun drawNotes(row: Int, col: Int, canvas: Canvas) {
        val cellNotes = board!!.getCellNotes(row, col)
        cellNotes.forEach { note: Any? ->
            val textBounds = Rect()
            val notes = note as Int
            val text = "" + note
            val textWidth = notePaint.measureText(text)
            val textHeight = textBounds.height().toFloat()
            val rowInCell = ((notes - 1) / sqrt).toInt()
            val colInCell = ((notes - 1) % sqrt).toInt()
            notePaint.getTextBounds(text, 0, text.length, textBounds)
            canvas.drawText(text,
                    col * cellSizePixels + colInCell * noteSizePixels + noteSizePixels / 2f - textWidth / 2f,
                    row * cellSizePixels + rowInCell * noteSizePixels + noteSizePixels / 1.4f + textHeight / 2,
                    notePaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        setPaint(paint)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        for (i in 0..9) {
            if (i % sqrt == 0f) {
                setThickPaint(paint)
            } else {
                setPaint(paint)
            }
            canvas.drawLine(i * cellSizePixels, 0f, i * cellSizePixels, measuredHeight.toFloat(), paint)
            canvas.drawLine(i * cellSizePixels, 0f, i * cellSizePixels, measuredHeight.toFloat(), paint)
        }
        for (i in 0..9) {
            if (i % sqrt == 0f) {
                setThickPaint(paint)
            } else {
                setPaint(paint)
            }
            canvas.drawLine(0f, i * cellSizePixels, measuredWidth.toFloat(), i * cellSizePixels, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        return if (event.action == action) {
            handleTouchEvent(event.x, event.y)
            true
        } else {
            false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possSelectedRow = (y / cellSizePixels).toInt()
        val possSelectedCol = (x / cellSizePixels).toInt()
        if (!board!!.getOriginal(possSelectedRow, possSelectedCol)) {
            listener?.onCellTouched(possSelectedRow, possSelectedCol)
        }
        invalidate()
    }


    fun updateCells(cellsLiveData: MutableLiveData<Board?>) {
        board = cellsLiveData.value
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
        invalidate()
    }


    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }

    companion object {
        var board: Board? = Board()
        private var selectedRow = -1
        private var selectedCol = -1
    }
}