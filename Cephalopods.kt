import java.util.*
import java.io.*
import java.math.*


val BOARD_WIDTH  = 3
val BOARD_HEIGHT = 3
val BOARD_SIZE = BOARD_WIDTH * BOARD_HEIGHT

fun boardToString(board: IntArray): String {
    return board.toList().toString()
}


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val depth = input.nextInt()
    /**
    val initialBoard = ArrayList<List<Int>>(3)
    
    for (i in 0 until 3) {
        val row = ArrayList<Int>(3)
        initialBoard.add(row)
        for (j in 0 until 3) {
            row.add(input.nextInt())
        }
    }
    */
    val initialBoard = IntArray(BOARD_SIZE){_ -> input.nextInt()}
    
    System.err.println("Depth is: $depth")
    System.err.println("Initial board: ${boardToString(initialBoard)}")


    // Write an action using println()
    // To debug: System.err.println("Debug messages...");

    println(solve(depth, initialBoard))
}


fun solve(depth: Int, board: IntArray): Int {
    if (depth <=0 || isComplete(board) ) {
        return boardHash(board)
    } else {
        val emptySlots = (0 until board.size).filter { board[it] == 0}
        val result: Int = emptySlots.fold(0) { acc, target ->
            val newBoard = board.copyOf()
            newBoard[target] = 1
            (acc + solve(depth - 1, newBoard)) % (1 shl 30)
        }
        return result
    }
}



 fun isComplete(board: IntArray): Boolean = board.all {it > 0}

 fun boardHash(board: IntArray): Int = board.fold(0) { acc, x -> (10 * acc + x) % (1 shl 30)}
