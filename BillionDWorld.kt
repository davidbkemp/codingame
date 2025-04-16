import java.util.*
import java.io.*
import java.math.*

data class RunLengthBlock(val length: Long, val value: Long)

fun <T> Iterator<T>.nextOrNull(): T? = 
    if (this.hasNext()) this.next() else null


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val a = input.nextLine()
    val b = input.nextLine()

    System.err.println(a)
    System.err.println(b)

    val vectorA = parseVector(a)
    val vectorB = parseVector(b)

    val iterA = vectorA.iterator()
    val iterB = vectorB.iterator()

    var runLengthA = iterA.nextOrNull()
    var runLengthB = iterB.nextOrNull()

    var result: Long = 0L

    while (runLengthA != null && runLengthB != null) {
        if (runLengthA.length == runLengthB.length) {
            result += runLengthA.length * runLengthA.value * runLengthB.value
            runLengthA = iterA.nextOrNull()
            runLengthB = iterB.nextOrNull()
        } else if (runLengthA.length < runLengthB.length) {
            result += runLengthA.length * runLengthA.value * runLengthB.value
            runLengthB = runLengthB.copy(length = runLengthB.length - runLengthA.length)
            runLengthA = iterA.nextOrNull()
        } else {
            result += runLengthB.length * runLengthA.value * runLengthB.value
            runLengthA = runLengthA.copy(length = runLengthA.length - runLengthB.length)
            runLengthB = iterB.nextOrNull()
        }
    }

    println(result)
}

fun parseVector(str: String): List<RunLengthBlock> =
    str.split(' ')
        .map { it.toLong() }
        .chunked(2)
        .map { RunLengthBlock(length = it[0], value = it[1])}



