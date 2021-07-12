package com.camilobaquero.flipboardgame.ui.playground

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.camilobaquero.flipboardgame.R
import com.camilobaquero.flipboardgame.data.BoardStateModel
import com.camilobaquero.flipboardgame.data.RectangleInBoardModel
import kotlin.math.floor
import kotlin.math.min

class PlaygroundView : View {

    private var rows: Int = DEFAULT_ROWS_COUNT
    private var columns: Int = DEFAULT_COLUMNS_COUNT
    private var fieldColorOn: Int = DEFAULT_FIELD_ON_COLOR
    private var fieldColorOff: Int = DEFAULT_FIELD_OFF_COLOR
    private var fieldColorActive: Int = DEFAULT_FIELD_ACTIVE_COLOR

    private var fieldSize: Float = 0f
    private var boardBottom: Float = 0f
    private var boardRight: Float = 0f

    //Click events
    private lateinit var clickedCoordinates: PointF
    private var downTouch = false

    //Public properties

    var fieldSelectedListener: FieldSelectedListener? = null
    var state: BoardStateModel? = null
        set(value) {
            field = value
            invalidate()
        }


    private val paint = Paint()

    constructor(context: Context) : super(context) {
        initialize(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        isClickable = true
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaygroundView)
            try {
                rows = ta.getInt(R.styleable.PlaygroundView_rows, DEFAULT_ROWS_COUNT)
                columns = ta.getInt(R.styleable.PlaygroundView_columns, DEFAULT_COLUMNS_COUNT)
                fieldColorOn = ta.getColor(
                    R.styleable.PlaygroundView_fieldColorOn,
                    DEFAULT_FIELD_ON_COLOR
                )
                fieldColorOff = ta.getColor(
                    R.styleable.PlaygroundView_fieldColorOff,
                    DEFAULT_FIELD_OFF_COLOR
                )
                fieldColorActive = ta.getColor(
                    R.styleable.PlaygroundView_fieldColorActive,
                    DEFAULT_FIELD_ACTIVE_COLOR
                )
            } finally {
                ta.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        fieldSize = (size / columns).toFloat()
        boardBottom = size.toFloat()
        boardRight = boardBottom
        setMeasuredDimension(size, size + 10)// Some padding bottom
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawFields(it)
            drawGrid(it)
        }
    }

    private fun drawGrid(canvas: Canvas) {

        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.isAntiAlias = true

        for (i in 1 until columns) {
            canvas.drawLine(fieldSize * i, 0f, fieldSize * i, boardBottom, paint)
        }
        for (i in 1..rows) {
            canvas.drawLine(0f, fieldSize * i, boardRight, fieldSize * i, paint)
        }
    }

    private fun drawFields(canvas: Canvas) {
        state?.let { state ->
            for (i in state.board.indices) {
                for (j in state.board[i].indices) {
                    when {
                        state.largestRectangle?.let { isFieldInTheLargestRectangle(j, i, it) } == true -> {
                            drawField(j, i, DEFAULT_FIELD_ACTIVE_COLOR, canvas)
                        }
                        state.board[i][j] == 1 -> {
                            drawField(j, i, DEFAULT_FIELD_ON_COLOR, canvas)
                        }
                        state.board[i][j] == 0 -> {
                            drawField(j, i, DEFAULT_FIELD_OFF_COLOR, canvas)
                        }
                    }
                }
            }
        }
    }

    private fun drawField(coordinateX: Int, coordinateY: Int, color: Int, canvas: Canvas) {
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(coordinatesToRectF(coordinateX, coordinateY), paint)
    }

    private fun isFieldInTheLargestRectangle(x: Int, y: Int, largestRectangle: RectangleInBoardModel): Boolean {
        return x in largestRectangle.topLeft.first..largestRectangle.bottomRight.first &&
                y in largestRectangle.topLeft.second..largestRectangle.bottomRight.second
    }

    private fun coordinatesToRectF(x: Int, y: Int): RectF {
        return RectF(x * fieldSize, y * fieldSize, (x+1) * fieldSize , (y+1) * fieldSize)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        // Listening for the down and up touch events
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downTouch = true
                true
            }

            MotionEvent.ACTION_UP -> if (downTouch) {
                downTouch = false
                clickedCoordinates = PointF(event.x, event.y)
                true
            } else {
                false
            }

            else -> false  // Return false for other touch events
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        val clickedField =
            Pair(floor(clickedCoordinates.x / fieldSize), floor(clickedCoordinates.y / fieldSize))
        fieldSelectedListener?.onFieldSelected(
            clickedField.first.toInt(),
            clickedField.second.toInt()
        )
        return true
    }

    interface FieldSelectedListener {
        fun onFieldSelected(x: Int, y: Int)
    }

    companion object {
        private const val DEFAULT_ROWS_COUNT = 15
        private const val DEFAULT_COLUMNS_COUNT = 15
        private const val DEFAULT_FIELD_ON_COLOR = Color.GRAY
        private const val DEFAULT_FIELD_OFF_COLOR = Color.WHITE
        private const val DEFAULT_FIELD_ACTIVE_COLOR = Color.RED
    }

}
