package com.camilobaquero.flipboardgame.data

data class RectangleInBoardModel(
    val topLeft: Pair<Int,Int>,
    val bottomRight: Pair<Int,Int>,
    val area: Int
)
