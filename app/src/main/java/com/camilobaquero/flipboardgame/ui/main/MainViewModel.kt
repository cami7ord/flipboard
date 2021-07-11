package com.camilobaquero.flipboardgame.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camilobaquero.flipboardgame.data.BoardStateModel
import com.camilobaquero.flipboardgame.di.RealUseCase
import com.camilobaquero.flipboardgame.domain.GetLargestRectangleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @RealUseCase private val getLargestRectangleUseCase: GetLargestRectangleUseCase
) : ViewModel() {

    private val rows = 15
    private val cols = 15

    private val board = Array(rows) { IntArray(cols) }

    private val _boardState = MutableLiveData<BoardStateModel>()
    val boardState = _boardState

    private val _maxArea = MutableLiveData<Int>().apply { value = 0 }
    val maxArea = _maxArea

    fun selectField(x: Int, y:Int) {
        // Toggle field
        board[y][x] = if (board[y][x] == 0) 1 else 0
        // Create a new coroutine on the UI thread
        viewModelScope.launch {
            // Make the calculation and suspend execution until it finishes
            val largestRectangle = getLargestRectangleUseCase(board)
            // Display result to the observer
            _boardState.postValue(BoardStateModel(board, largestRectangle))
            _maxArea.postValue(largestRectangle?.area ?: 0)
        }
    }

    fun reset() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[j][i] == 1) board[j][i] = 0
            }
        }
        _boardState.postValue(BoardStateModel(board, null))
        _maxArea.postValue(0)
    }

}
