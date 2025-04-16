import java.util.*
import java.io.*
import java.math.*


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
        if (runLengthA.count == runLengthB.count) {
            result += runLengthA.count * runLengthA.value * runLengthB.value
            runLengthA = iterA.nextOrNull()
            runLengthB = iterB.nextOrNull()
        } else if (runLengthA.count < runLengthB.count) {
            result += runLengthA.count * runLengthA.value * runLengthB.value
            runLengthB = runLengthB.copy(count = runLengthB.count - runLengthA.count)
            runLengthA = iterA.nextOrNull()
        } else {
            result += runLengthB.count * runLengthA.value * runLengthB.value
            runLengthA = runLengthA.copy(count = runLengthA.count - runLengthB.count)
            runLengthB = iterB.nextOrNull()
        }
    }

    println(result)
}

data class RunLengthBlock(val value: Long, val count: Long)

fun parseVector(str: String): List<RunLengthBlock> {
    val tokenIter = str.split(' ').map { it.toLong() }.iterator()
    val result = ArrayList<RunLengthBlock>()
    while (tokenIter.hasNext()) {
        val count = tokenIter.next()
        val value = tokenIter.next()
        result.add(RunLengthBlock(value = value, count = count))
    }
    return result
}

fun <T> Iterator<T>.nextOrNull(): T? = 
    if (this.hasNext()) this.next() else null

