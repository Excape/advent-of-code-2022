
fun main() {
    val sandDirections = listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1))

    fun createWall(points: List<Point>) = points.windowed(2)
        .flatMap { (first, second) ->
        (if (first.x > second.x) first.x downTo second.x else first.x..second.x).flatMap { x ->
            (if (first.y > second.y) first.y downTo second.y else first.y..second.y).map { y ->
                Point(x, y)
            }
        }
    }.toSet()

    fun prettyPrint(grid: Map<Point, Char>) {
        val points = grid.keys
        (points.minOf { it.y }..points.maxOf { it.y }).forEach { y ->
            (points.minOf { it.x }..points.maxOf { it.x }).forEach { x ->
                print(grid[Point(x, y)] ?: ".")
            }
            print('\n')
        }
    }

    fun simulateSandUnit(
        filled: Map<Point, Char>,
        lowestWall: Int,
        path: ArrayDeque<Point>
    ): Point {
        val sandPath = generateSequence(path.last()) { current ->
            sandDirections.map { (dx, dy) -> Point(current.x + dx, current.y + dy) }
                .firstOrNull { !filled.contains(it) && it.y < lowestWall + 2 }
        }.toList()
        sandPath.dropLast(1).forEach { path.addLast(it) }
        return sandPath.last()
    }

    fun simulateSand(grid: Map<Point, Char>, part1: Boolean = false): Map<Point, Char> {
        val filled = grid.toMutableMap()
        val path = ArrayDeque(listOf(Point(500, 0)))
        val lowestWall = grid.keys.maxOf { it.y }
        while (path.isNotEmpty()) {
            val nextSand = simulateSandUnit(filled, lowestWall, path)
            if (part1 && nextSand.y > lowestWall) {
                return filled
            }
            filled[nextSand] = 'o'
            path.removeLast()
        }
        return filled
    }

    fun parseWalls(input: List<String>) = input.map { line ->
        line.split(" -> ")
            .map { coords -> coords.split(",") }
            .map { Point(it[0].toInt(), it[1].toInt()) }
    }.flatMap { createWall(it) }.associateWith { '#' }

    fun execute(input: List<String>, part1: Boolean): Int {
        val grid = parseWalls(input)
        val filledGrid = simulateSand(grid, part1)
        prettyPrint(filledGrid)
        return filledGrid.values.count {it == 'o'}
    }

    fun part1(input: List<String>) = execute(input, true)
    fun part2(input: List<String>) = execute(input, false)

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
