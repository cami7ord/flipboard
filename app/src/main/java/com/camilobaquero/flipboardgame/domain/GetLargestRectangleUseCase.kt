package com.camilobaquero.flipboardgame.domain

import com.camilobaquero.flipboardgame.data.LargestRectangleInHistogramModel
import com.camilobaquero.flipboardgame.data.RectangleInBoardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface GetLargestRectangleUseCase {
    suspend operator fun invoke(board: Array<IntArray>): RectangleInBoardModel?
}

class MockLargestRectangleUseCaseImpl @Inject constructor() :
    GetLargestRectangleUseCase {

    override suspend fun invoke(board: Array<IntArray>): RectangleInBoardModel {
        delay(3000L) // pretend we are doing something useful here
        return RectangleInBoardModel(
            topLeft = 2 to 1,
            bottomRight = 4 to 2,
            area = 6
        )
    }

}

class GetLargestRectangleUseCaseImpl @Inject constructor() :
    GetLargestRectangleUseCase {

    override suspend fun invoke(board: Array<IntArray>): RectangleInBoardModel? =
        withContext(Dispatchers.Default) {
            var maxArea = 0
            var largestRectangleInBoard: RectangleInBoardModel? = null
            val histogram = Array(board[0].size) { 0 }
            for (i in board.indices) {
                for (j in board[i].indices) {
                    histogram[j] = if (board[i][j] != 0) board[i][j] + histogram[j] else 0
                }
                val largestRectangle = findLargestRectangleInHistogram(histogram)
                if (largestRectangle.area > maxArea) {
                    maxArea = largestRectangle.area
                    val top = i - largestRectangle.height + 1
                    largestRectangleInBoard = RectangleInBoardModel(
                        topLeft = largestRectangle.left to top,
                        bottomRight = largestRectangle.right to i,
                        area = largestRectangle.area
                    )
                }
            }
            largestRectangleInBoard
        }

    private fun findLargestRectangleInHistogram(histogram: Array<Int>): LargestRectangleInHistogramModel {

        val stack = Stack<Int>()
        var largestRectangleInHistogram = LargestRectangleInHistogramModel()

        for (i in histogram.indices) {
            while (stack.isNotEmpty() && histogram[stack.peek()] > histogram[i]) {
                largestRectangleInHistogram =
                    calculateMaxArea(stack, histogram, largestRectangleInHistogram, i)
            }
            stack.push(i)
        }
        while (stack.isNotEmpty()) {
            largestRectangleInHistogram =
                calculateMaxArea(stack, histogram, largestRectangleInHistogram, histogram.size)
        }
        return largestRectangleInHistogram
    }

    private fun calculateMaxArea(
        stack: Stack<Int>,
        histogram: Array<Int>,
        largestRectangleInHistogram: LargestRectangleInHistogramModel,
        limit: Int
    ): LargestRectangleInHistogramModel {
        val popped = stack.pop()
        val left = if (stack.isEmpty()) 0 else stack.peek() + 1
        val currentLargestRectangle = LargestRectangleInHistogramModel(
            left = left,
            right = limit - 1,
            height = histogram[popped],
            area = histogram[popped] * (limit - left)
        )

        return if (currentLargestRectangle.area > largestRectangleInHistogram.area) {
            currentLargestRectangle
        } else {
            largestRectangleInHistogram
        }
    }

}
