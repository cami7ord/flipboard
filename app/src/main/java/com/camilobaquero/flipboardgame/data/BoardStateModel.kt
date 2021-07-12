package com.camilobaquero.flipboardgame.data

data class BoardStateModel(
    val board: Array<IntArray>,
    val largestRectangle: RectangleInBoardModel?
)
