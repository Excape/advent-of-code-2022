fun main() {

    fun part1(input: List<String>): Int {
        val mapOfResults = mapOf(
            "A X" to 1 + 3,
            "A Y" to 2 + 6,
            "A Z" to 3 + 0,

            "B X" to 1 + 0,
            "B Y" to 2 + 3,
            "B Z" to 3 + 6,

            "C X" to 1 + 6,
            "C Y" to 2 + 0,
            "C Z" to 3 + 3,
        )
        return input.sumOf { mapOfResults[it]!! }
    }

    fun part2(input: List<String>): Int {
        val mapOfResults = mapOf(
            "A X" to 0 + 3,
            "A Y" to 3 + 1,
            "A Z" to 6 + 2,

            "B X" to 0 + 1,
            "B Y" to 3 + 2,
            "B Z" to 6 + 3,

            "C X" to 0 + 2,
            "C Y" to 3 + 3,
            "C Z" to 6 + 1,
        )
        return input.sumOf { mapOfResults[it]!! }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)


    val input = readInput("Day02")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
