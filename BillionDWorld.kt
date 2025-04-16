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

    // Write an answer using println()
    // To debug: System.err.println("Debug messages...");

    System.err.println(a)
    System.err.println(b)

    val vectorA = parseVector(a)
    val vectorB = parseVector(b)

    System.err.println(vectorA)
    System.err.println(vectorB)

    val iterA = vectorA.iterator()
    val iterB = vectorB.iterator()
    var seqA = nextOrNull(iterA)
    var seqB = nextOrNull(iterB)

    var result: Long = 0L

    while (seqA != null && seqB != null) {
        if (seqA.freq == seqB.freq) {
            result += seqA.freq * seqA.value * seqB.value
            seqA = nextOrNull(iterA)
            seqB = nextOrNull(iterB)
        } else if (seqA.freq < seqB.freq) {
            result += seqA.freq * seqA.value * seqB.value
            seqB = seqB.copy(freq = seqB.freq - seqA.freq)
            seqA = nextOrNull(iterA)
        } else {
            result += seqB.freq * seqA.value * seqB.value
            seqA = seqA.copy(freq = seqA.freq - seqB.freq)
            seqB = nextOrNull(iterB)
        }
    }

    println(result)
}

data class NumSequence(val value: Long, val freq: Long)

fun parseVector(str: String): List<NumSequence> {
    val tokenIter = str.split(' ').map { it.toLong() }.iterator()
    val result = ArrayList<NumSequence>()
    while (tokenIter.hasNext()) {
        val frequency = tokenIter.next()
        val value = tokenIter.next()
        result.add(NumSequence(value = value, freq = frequency))
    }
    return result
}

fun <T>nextOrNull(iterator: Iterator<T>): T? = 
    if (iterator.hasNext()) iterator.next() else null
