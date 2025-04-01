

import java.util.*
import java.io.*
import java.math.*

import Molecule.*

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val projectCount = input.nextInt()
    System.err.println("projectCount: $projectCount")
    for (i in 0 until projectCount) {
        val a = input.nextInt()
        val b = input.nextInt()
        val c = input.nextInt()
        val d = input.nextInt()
        val e = input.nextInt()
        System.err.println("abcd $a $b $c $d $e")
    }

    val ranks = arrayOf(1,1,1)
    var loopNum = 0
    // game loop
    while (true) {
        val robots = ArrayList<Robot>()
        for (i in 0 until 2) {
            val target = input.next()
            val eta = input.nextInt()
            val score = input.nextInt()
            val storage: Map<Molecule, Int> = Molecule.values().map {
                it to input.nextInt()
            }.toMap()

            val expertise: Map<Molecule, Int> = Molecule.values().map {
                it to input.nextInt()
            }.toMap()
        
            robots.add(Robot(target, score, storage, expertise))
        }
        val availability: Map<Molecule, Int> = Molecule.values().map {
            it to input.nextInt()
        }.toMap()
        
        System.err.println("Availabiliy: $availability")
        val sampleCount = input.nextInt()
        val samples = ArrayList<Sample>()
        for (i in 0 until sampleCount) {
            val sampleId = input.nextInt()
            val carriedBy = input.nextInt()
            val rank = input.nextInt()
            val expertiseGain = input.next()
            val health = input.nextInt()
            val cost: Map<Molecule, Int> = Molecule.values().map {
                it to input.nextInt()
            }.toMap()
            samples.add(Sample(sampleId, carriedBy, health, cost))
        }
        System.err.println(samples)

        val myRobot: Robot= robots[0]
        System.err.println("my robot ${myRobot}")
        System.err.println("other robot ${robots[1]}")
        val mySamples: List<Sample> = samples.filter { it.carriedBy == 0}

        if (myRobot.target == "START_POS") {
            println("GOTO SAMPLES")
        } else if (myRobot.target == "SAMPLES") {
            if (mySamples.size < 3) {
                println("CONNECT ${ranks[2 - mySamples.size]}")
            } else {
                println("GOTO DIAGNOSIS")
            }
        } else if (myRobot.target == "DIAGNOSIS") {
            val costlySample = mySamples.find { ! myRobot.canHandle(it) }
            val undiagnosedSample = mySamples.find { ! it.diagnosed }
            if (undiagnosedSample != null) {
                println("CONNECT ${undiagnosedSample.id}")
            } else if (costlySample != null) {
                println("CONNECT ${costlySample.id}")
            } else if (mySamples.isEmpty()) {
                println("GOTO SAMPLES")
            } else {
                println("GOTO MOLECULES")
            }
        } else if (myRobot.target == "MOLECULES") {
            System.err.println("can prov: ${mySamples.filter{myRobot.canProvision(it, availability)}}")

            val target = mySamples.filter{myRobot.canProvision(it, availability)}.firstOrNull()
            System.err.println("target $target")
            if (target != null) {
                fillStorage(myRobot, target, availability)
            } else if (mySamples.size >= 2) {
                println("WAIT")
            } else {
                loopNum = loopNum + 1
                println("GOTO SAMPLES")
            }
        } else {
            if (myRobot.target != "LABORATORY") {
                throw IllegalStateException("${myRobot.target} instead of LABORATORY")
            }
            val preparedSample = mySamples.find { it.satisfiedBy(myRobot) }
            if (preparedSample != null) {
                println("CONNECT ${preparedSample.id}")
            } else if (mySamples.size == 0) {
                ranks[(2 * loopNum) % 3] = Math.min(3, 1 + ranks[(2 * loopNum) % 3])
                ranks[(2 * loopNum + 1) % 3] = Math.min(3, 1 + ranks[(2 * loopNum + 1) % 3])
                loopNum = loopNum + 1
                println("GOTO SAMPLES")
            } else {
                println("GOTO MOLECULES")
            }
        }
    }
}

enum class Molecule {A, B, C, D, E}

data class Sample(val id: Int, val carriedBy: Int, val health: Int, val costs: Map<Molecule, Int>) {

    val diagnosed: Boolean = health >= 0

    fun satisfiedBy(robot: Robot): Boolean {
        for (molecule in Molecule.values()) {
            if (robot.storage[molecule]!! + robot.expertise[molecule]!! < costs[molecule]!!) {
                return false
            }
        }
        return true
    }
}

data class Robot(val target: String, val score: Int, val storage: Map<Molecule, Int>, val expertise: Map<Molecule, Int>) {
    fun canHandle(sample: Sample): Boolean {
        val effectiveCosts: List<Int> = Molecule.values()
            .map { sample.costs[it]!! - expertise[it]!! }
            .map { if (it >= 0) it else 0 }
        return effectiveCosts.sum() <= 10 &&
            effectiveCosts.find { it > 5 } == null
    }

    fun additionalRequired(sample: Sample) : Map<Molecule, Int> {
        return Molecule.values().map {
            it to Math.max(0, sample.costs[it]!! - storage[it]!! - expertise[it]!!)
        }.toMap()
    }

    fun canProvision(sample: Sample, availability: Map<Molecule, Int>): Boolean {
        val req = additionalRequired(sample)
        return req.values.sum() <= 10 - storage.values.sum()
         && Molecule.values().all {
            req[it]!! <= availability[it]!!
         }
    }


}

fun fillStorage(robot: Robot, sample: Sample, availability: Map<Molecule, Int>) {
    var sampleShortage = false
    for (molecule in Molecule.values()) {
        if (robot.storage[molecule]!! < (sample.costs[molecule]!! - robot.expertise[molecule]!!)) {
            if (availability[molecule] == 0) {
                sampleShortage = true
            } else {
                println("CONNECT $molecule")
                return
            }
        }
    }
    if (sampleShortage) {
        println("WAIT")
    } else {
        println("GOTO LABORATORY")
    }
}
