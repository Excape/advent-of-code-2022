fun main() {
    fun getSumOfCalories(input: List<String>): List<Int> = input.fold(mutableListOf(0)) { acc, line ->
        when {
            line.isBlank() -> acc.add(0)
            else -> acc[acc.lastIndex] += line.toInt()
        }
        return@fold acc
    }

    fun part1(input: List<String>): Int = getSumOfCalories(input).max()

    fun part2(input: List<String>): Int = getSumOfCalories(input).sortedDescending().take(3).sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)


    val input = readInput("Day01")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
