import kotlin.math.abs
import kotlin.math.sign

fun main() {

    data class Point(val x: Int, val y: Int) {
        fun move(move: Pair<Int, Int>) = Point(x +move.first, y + move.second)

        fun follow(other: Point): Point {
            if (this == other) {
                return this
            }
            var dx = 0
            var dy = 0
            if (abs(this.x - other.x) > 1 || abs(this.y - other.y) > 1) {
                dx = (other.x - this.x).sign
                dy = (other.y - this.y).sign
            }
            return Point(x + dx, y + dy)
        }
    }

    data class Instruction(val direction: Char, val times: Int) {
    }

    val moves = mapOf('U' to Pair(0, 1), 'D' to Pair(0, -1), 'R' to Pair(1, 0), 'L' to Pair(-1, 0))

    fun parseInstructions(input: List<String>) =
        input.map { it.split(" ") }.map { Instruction(it[0][0], it[1].toInt()) }

    fun part1(input: List<String>): Int {
        val instructions = parseInstructions(input)
        var head = Point(0, 0)
        var tail = Point(0, 0)
        val visited = mutableSetOf(tail)

        for (instr in instructions) {
            val move = moves[instr.direction]!!
            repeat(instr.times) {
                head = head.move(move)
                tail = tail.follow(head)
                visited.add(tail)
            }
        }
        return visited.size
    }
    
    fun part2(input: List<String>): Int {
        val instructions = parseInstructions(input)
        val knots = generateSequence { Point(0, 0) }.take(10).toMutableList()
        val visited = mutableSetOf(Point(0, 0))

        for (instr in instructions) {
            val move = moves[instr.direction]!!
            repeat(instr.times) {
                knots.indices.forEach() { i, ->
                    if (i == 0) {
                        knots[0] = knots[0].move(move)
                    } else {
                        knots[i] = knots[i].follow(knots[i - 1])
                    }
                }
                visited.add(knots.last())
            }
        }
        return visited.size
    }

    val testInput = readInput("Day09_test")
    println(part1(testInput))
    check(part1(testInput) == 88)
    check(part2(testInput) == 36)


    val input = readInput("Day09")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
