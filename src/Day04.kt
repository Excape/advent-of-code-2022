fun main() {

    fun parseRanges(input: List<String>) =
        input.map { it.split(",").map { it.split("-").map { it.toInt() } } }
            .map { Pair((it[0][0]..it[0][1]).toSet(), (it[1][0]..it[1][1]).toSet()) }

    fun part1(input: List<String>) = parseRanges(input)
        .count { (it.first union  it.second).size <= maxOf(it.first.size, it.second.size) }

    fun part2(input: List<String>) = parseRanges(input)
        .map{ it.first intersect it.second}
        .count { it.isNotEmpty() }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)


    val input = readInput("Day04")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
