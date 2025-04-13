import java.io.*
import java.math.*
import java.util.*

val BOARD_WIDTH = 3
val BOARD_HEIGHT = 3
val BOARD_SIZE = BOARD_WIDTH * BOARD_HEIGHT

val solutionCache: HashMap<Pair<Int, UInt>, UInt> = HashMap<Pair<Int, UInt>, UInt>()


/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val depth = input.nextInt()

    val initialBoard = Board(UIntArray(BOARD_SIZE) { _ -> input.nextInt().toUInt() })

    System.err.println("Depth is: $depth")
    System.err.println("Initial board: ${initialBoard}")

    val result = initialBoard.solve(depth).mod(1u shl 30)
    println(result)
}

data class Board(val cells: UIntArray) {

    private data class Move(val cell: Int, val captures: List<Int>, val value: UInt)

    private val emptyCells = (0 until cells.size).filter { cells[it] == 0u }

    fun solve(depth: Int): UInt {
        if (emptyCells.size == 0) {
            return hash
        } else {
            val cacheKey = Pair(depth, this.hash)
            val cachedSolution = solutionCache[cacheKey]
            if (cachedSolution != null) {
                return cachedSolution
            } else {
                val result: UInt =
                    emptyCells.fold(0u) { acc, cell ->
                        acc + solveForCell(depth, cell)
                    }
                solutionCache[cacheKey] = result
                return result
            }
        }
    }

    private inline fun solveForCell(depth: Int, cell: Int): UInt {
        return movesForCell(cell).fold(0u) { acc, move ->
            val newBoard = applyMove(move)
            if (depth > 1) acc + newBoard.solve(depth - 1) else (acc + newBoard.hash)
        }
    }

    private inline fun applyMove(move: Move): Board {
        val newCells = cells.copyOf()
        newCells[move.cell] = move.value
        move.captures.forEach {
            newCells[it] = 0u
        }
        return Board(newCells)
    }

    private inline fun movesForCell(cell: Int): List<Move> {
        val candidates = neighbours[cell]!!.filter {
            cells[it] > 0u && cells[it] < 6u
        }
        val capturingMoves = combinations(candidates)
            .map {
                Move(cell, it, it.map{cells[it]}.sum())
            }
            .filter { it.value <= 6u }
        if (capturingMoves.size == 0) {
            return listOf(Move(cell, emptyList(), 1u))
        } else {
            return capturingMoves
        }
    }

    private val hash: UInt = cells.fold(0u) { acc, x -> 10u * acc + x}

    override fun toString(): String = cells.toList().toString()

}

    inline fun neighboursOfCell(cell: Int): List<Int> {
        val result = ArrayList<Int>()
        if (cell >= BOARD_WIDTH ) result.add(cell - BOARD_WIDTH)
        if (cell < BOARD_SIZE - BOARD_WIDTH) result.add(cell + BOARD_WIDTH)
        if (cell % BOARD_WIDTH > 0) result.add(cell - 1)
        if (cell % BOARD_WIDTH < BOARD_WIDTH - 1) result.add(cell + 1)
        return result
    }

    val neighbours: Array<List<Int>> = Array<List<Int>>(BOARD_SIZE) {i -> neighboursOfCell(i)}

    private fun combinations(items: List<Int>): List<List<Int>> {
        return when (items.size) {
            0 -> emptyList()
            1 -> emptyList()
            2 -> listOf(items)
            3 -> {
                listOf(
                    listOf(items[0], items[1]),
                    listOf(items[0], items[2]),
                    listOf(items[1], items[2]),
                    items
                )
            }
            4 -> {
                listOf(
                items,
                listOf(items[0], items[1]),
                listOf(items[0], items[2]),
                listOf(items[0], items[3]),
                listOf(items[1], items[2]),
                listOf(items[1], items[3]),
                listOf(items[2], items[3]),
                listOf(items[0], items[1], items[2]),
                listOf(items[0], items[1], items[3]),
                listOf(items[0], items[2], items[3]),
                listOf(items[1], items[2], items[3])
                )
            }
            else -> throw IllegalStateException("Too many items $items")
        }
    }

