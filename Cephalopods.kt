import java.io.*
import java.math.*
import java.util.*

val BOARD_WIDTH = 3
val BOARD_HEIGHT = 3
val BOARD_SIZE = BOARD_WIDTH * BOARD_HEIGHT

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val depth = input.nextInt()

    val initialBoard = Board(IntArray(BOARD_SIZE) { _ -> input.nextInt() })

    System.err.println("Depth is: $depth")
    System.err.println("Initial board: ${initialBoard}")

    println(initialBoard.solve(depth))
}

data class Board(val cells: IntArray) {

    fun solve(depth: Int): Int {
        if (depth <= 0 || isComplete()) {
            return boardHash()
        } else {
            val emptySlots = (0 until cells.size).filter { cells[it] == 0 }
            val result: Int =
                    emptySlots.fold(0) { acc, target ->
                        addHashes(acc, solveForSlot(depth - 1, target))
                    }
            return result
        }
    }

    fun solveForSlot(depth: Int, target: Int): Int {
        val newCells = cells.copyOf()
        newCells[target] = 1
        return Board(newCells).solve(depth - 1)
    }

    fun isComplete(): Boolean = cells.all { it > 0 }

    fun boardHash(): Int = cells.fold(0) { acc, x -> addHashes(10 * acc, x) }

    override fun toString(): String = cells.toList().toString()
}

fun addHashes(h1: Int, h2: Int): Int = (h1 + h2) % (1 shl 30)
