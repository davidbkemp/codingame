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
    testNeighboursOfCell()

    println(initialBoard.solve(depth))
}

data class Board(val cells: IntArray) {

    fun solve(depth: Int): Int {
        if (depth <= 0 || isComplete()) {
            return boardHash()
        } else {
            val emptyCells = (0 until cells.size).filter { cells[it] == 0 }
            val result: Int =
                    emptyCells.fold(0) { acc, cell ->
                        addHashes(acc, solveForCell(depth - 1, cell))
                    }
            return result
        }
    }

    fun solveForCell(depth: Int, cell: Int): Int {
        val newCells = cells.copyOf()
        newCells[cell] = 1
        return Board(newCells).solve(depth - 1)
    }

/** 
    fun capturesForCell(cell: Int): List<List<Int>> {
        val canditates = neighboursOfCell(cell).filter {
            it > 0 && it < 6
        }
        val combos = combinations(candidates)
    }
*/

    fun isComplete(): Boolean = cells.all { it > 0 }

    fun boardHash(): Int = cells.fold(0) { acc, x -> addHashes(10 * acc, x) }

    override fun toString(): String = cells.toList().toString()
}

fun addHashes(h1: Int, h2: Int): Int = (h1 + h2) % (1 shl 30)

fun neighboursOfCell(cell: Int): List<Int> {
    val result = ArrayList<Int>()
    if (cell >= BOARD_WIDTH ) result.add(cell - BOARD_WIDTH)
    if (cell < BOARD_SIZE - BOARD_WIDTH) result.add(cell + BOARD_WIDTH)
    if (cell % BOARD_WIDTH > 0) result.add(cell - 1)
    if (cell % BOARD_WIDTH < BOARD_WIDTH - 1) result.add(cell + 1)
    return result
}

fun testNeighboursOfCell() {
    if (neighboursOfCell(0).toSet() != listOf(1, 3).toSet()) throw IllegalStateException("neighboursOfCell(0) was ${neighboursOfCell(0)}")
    if (neighboursOfCell(1).toSet()  != listOf(0, 2, 4).toSet() ) throw IllegalStateException("neighboursOfCell(1) was ${neighboursOfCell(1)}")
    if (neighboursOfCell(2).toSet()  != listOf(1, 5).toSet() ) throw IllegalStateException("neighboursOfCell(2) was ${neighboursOfCell(2)}")
    if (neighboursOfCell(4).toSet()  != listOf(1, 3, 5, 7).toSet() ) throw IllegalStateException("neighboursOfCell(4) was ${neighboursOfCell(4)}")
    if (neighboursOfCell(8).toSet()  != listOf(7, 5).toSet() ) throw IllegalStateException("neighboursOfCell(8) was ${neighboursOfCell(8)}")
}
