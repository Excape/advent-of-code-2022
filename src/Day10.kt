import kotlin.math.abs

fun main() {
    fun drawPixel(xReg: Int, cycle: Int) {
        val xPos = (cycle - 1) % 40
        if (abs(xReg - xPos) < 2) print("#") else print(".")
        if (xPos == 39) {
            print("\n")
        }
    }

    fun computeAndRender(input: List<String>): Int {
        var xReg = 1
        var addReg = 0
        val instr = input.listIterator()
        var totalStrength = 0

        generateSequence(1) { it + 1 }.takeWhile { instr.hasNext() }.forEach { i ->
            if (i == 20 ||(i - 20) % 40 == 0) {
                totalStrength += i * xReg
            }
            drawPixel(xReg, i)
            if (addReg != 0) {
                xReg += addReg
                addReg = 0
            } else {
                val next = instr.next()
                if (next.startsWith("addx")) {
                    addReg = next.split(" ")[1].toInt()
                }
            }
        }
        println()
        return totalStrength
    }

    val testInput = readInput("Day10_test")
    check(computeAndRender(testInput) == 13140)


    val input = readInput("Day10")
    val part1Answer = computeAndRender(input)
    println("part 1: $part1Answer")

}
