fun main() {

    fun Char.toPriority() = if (this.isUpperCase())
        this.code - 65 + 27
    else
        this.code - 96

    fun part1(input: List<String>): Int {
        return input.map { it.chunked(it.length / 2).map {it.toSet()} }
            .map { (it[0] intersect it[1]).first() }
            .sumOf { it.toPriority() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSet() }
            .chunked(3)
            .map { it.reduce { i, r -> i intersect r } }
            .sumOf { it.first().toPriority() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)


    val input = readInput("Day03")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
