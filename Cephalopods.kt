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
//    testNeighboursOfCell()
//    testCombinations()
//    testMovesForCell()

    println(initialBoard.solve(depth))
}

data class Move(val cell: Int, val captures: List<Int>, val value: Int)

data class Board(val cells: IntArray) {

    fun solve(depth: Int): Int {
//        System.err.println("depth $depth, board: $this")
        if (depth <= 0 || isComplete()) {
            return boardHash()
        } else {
            val emptyCells = (0 until cells.size).filter { cells[it] == 0 }
            val result: Int =
                    emptyCells.fold(0) { acc, cell ->
                        addHashes(acc, solveForCell(depth, cell))
                    }
            return result
        }
    }

    fun solveForCell(depth: Int, cell: Int): Int {
        return movesForCell(cell).fold(0) { acc, move ->
            val newBoard = applyMove(move)
            addHashes(acc, newBoard.solve(depth - 1))
        }
    }

    fun applyMove(move: Move): Board {
        val newCells = cells.copyOf()
        newCells[move.cell] = move.value
        move.captures.forEach {
            newCells[it] = 0
        }
        return Board(newCells)
    }


    fun movesForCell(cell: Int): List<Move> {
        val candidates = neighboursOfCell(cell).filter {
            cells[it] > 0 && cells[it] < 6
        }
        val capturingMoves = combinations(candidates)
            .map {
                Move(cell, it, it.map{cells[it]}.sum())
            }
            .filter { it.captures.size > 1 && it.value <= 6 }
        if (capturingMoves.size == 0) {
            return listOf(Move(cell, emptyList(), 1))
        } else {
            return capturingMoves
        }
    }


    fun isComplete(): Boolean = cells.all { it > 0 }

    fun boardHash(): Int = cells.fold(0) { acc, x -> addHashes(10 * acc, x) }

    override fun toString(): String = cells.toList().toString()
}

fun addHashes(h1: Int, h2: Int): Int = (h1 + h2) % (1 shl 30)

fun testMovesForCell() {
    val moves = Board(intArrayOf(0, 1, 0, 2, 0, 3, 0, 4, 0)).movesForCell(4)
    val actualCaptures = moves.map { it.captures.toSet() }.toSet()
   System.err.println("moves $moves")
    val expectedCaptures = setOf(
        setOf(1, 3),
        setOf(1, 5),
        setOf(1, 7),
        setOf(1, 3, 5),
        setOf(3, 5),
        setOf(3, 7)

    )
    val actualMinusExpected = actualCaptures - expectedCaptures
    val expectedMinusActual = expectedCaptures - actualCaptures
    if (actualMinusExpected.size != 0) throw IllegalStateException("capturesForCell actualMinusExpected $actualMinusExpected")
    if (expectedMinusActual.size != 0) throw IllegalStateException("capturesForCell expectedMinusActual $expectedMinusActual")
    val actualValues = moves.map { it.value }.toSet()
    val expectedValues = setOf(3,4,5,6)
    if (!actualValues.equals(expectedValues)) throw IllegalStateException("move values ${actualValues}")
}

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

fun combinations(items: List<Int>): List<List<Int>> {
    if (items.size == 0) {
        return emptyList()
    } else {
        val head: Int = items.first()
        val tail: List<Int> = items.takeLast(items.size - 1)
        val tailCombinations: List<List<Int>> = combinations(tail)
        return listOf(listOf(head))
            .plus(tailCombinations)
            .plus(tailCombinations.map{it.plus(head)})
    }
}


fun testCombinations() {
    val actual: Set<Set<Int>> = combinations(listOf(1,2,3,4)).map{it.toSet()}.toSet()
    val expected: Set<Set<Int>> = setOf(
            setOf(1,2,3,4),
            setOf(1,2,3),
            setOf(1,2,4),
            setOf(1,3,4),
            setOf(2,3,4),
            setOf(1,2),
            setOf(1,3),
            setOf(1,4),
            setOf(2,3),
            setOf(2,4),
            setOf(3,4),
            setOf(1),
            setOf(2),
            setOf(3),
            setOf(4)
            )
 //       System.err.println("actual - expected: ${actual - expected}")
 //       System.err.println("expected: $expected")
        if (actual != expected) throw IllegalStateException("Combos was ${combinations(listOf(1,2,3,4))}")
}

