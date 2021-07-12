package com.camilobaquero.flipboardgame.domain

import com.camilobaquero.flipboardgame.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetLargestRectangleUseCaseImplTest {

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private lateinit var useCaseImpl: GetLargestRectangleUseCaseImpl

    @Before
    fun setUp() {
        useCaseImpl = GetLargestRectangleUseCaseImpl()
    }

    @Test
    fun `case 1 - no active rectangle returns null`() = runBlocking {
        val board = arrayOf(
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0)
        )
        val result = useCaseImpl(board)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `case 2 - 5 by 5 board with a rectangle of area 1`() = runBlocking {
        val board = arrayOf(
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0)
        )
        val result = useCaseImpl(board)
        Assert.assertEquals(2 to 2, result?.topLeft)
        Assert.assertEquals(2 to 2, result?.bottomRight)
        Assert.assertEquals(1, result?.area)
    }

    @Test
    fun `case 3 - 5 by 5 board with a rectangle of area 9`() = runBlocking {
        val board = arrayOf(
            intArrayOf(1, 0, 1, 0, 0),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1),
            intArrayOf(1, 0, 1, 1, 1)
        )
        val result = useCaseImpl(board)
        Assert.assertEquals(2 to 1, result?.topLeft)
        Assert.assertEquals(4 to 3, result?.bottomRight)
        Assert.assertEquals(9, result?.area)
    }

    @Test
    fun `case 4 - 15 by 15 board with a rectangle of area 20`() = runBlocking {
        val board = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1),
            intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1),
            intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
        val result = useCaseImpl(board)
        Assert.assertEquals(4 to 5, result?.topLeft)
        Assert.assertEquals(8 to 8, result?.bottomRight)
        Assert.assertEquals(20, result?.area)
    }

}
