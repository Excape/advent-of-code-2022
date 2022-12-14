
fun main() {
    fun createWall(points: List<Point>): Set<Point> {
        val wall = points.windowed(2).flatMap { (first, second) ->
            (if (first.x > second.x) first.x downTo second.x else first.x..second.x).flatMap { x ->
                (if (first.y > second.y) first.y downTo second.y else first.y..second.y).map { y ->
                    Point(x, y)
                }
            }
        }.toSet()
        return wall
    }

    fun prettyPrint(walls: Set<Point>, sand: Set<Point>) {
        (sand.minOf { it.y }..sand.maxOf { it.y }).forEach { y ->
            (sand.minOf { it.x }..sand.maxOf { it.x }).forEach { x ->
                val char = when (Point(x, y)) {
                    in walls -> '#'
                    in sand -> 'o'
                    else -> '.'
                }
                print(char)
            }
            print('\n')
        }
    }

    val sandDirections = listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1))

    fun simulateSandUnit(
        walls: Set<Point>,
        sandPoints: Set<Point>,
        lowestWall: Int,
        path: ArrayDeque<Point>
    ): Point {
        val filledPoints = walls union sandPoints

        val sandPath = generateSequence(path.last()) { current ->
            sandDirections.map { (dx, dy) -> Point(current.x + dx, current.y + dy) }
                .firstOrNull { !filledPoints.contains(it) && it.y < lowestWall + 2 }
        }.toList()
        sandPath.dropLast(1).forEach { path.addLast(it) }
        return sandPath.last()
    }

    fun simulateSand(walls: Set<Point>, part1: Boolean = false): Set<Point> {
        val sand = mutableSetOf<Point>()
        val path = ArrayDeque(listOf(Point(500, 0)))
        val lowestWall = walls.maxOf { it.y }
        while (path.isNotEmpty()) {
            val nextSand = simulateSandUnit(walls, sand, lowestWall, path)
            if (part1 && nextSand.y > lowestWall) {
                return sand
            }
            sand.add(nextSand)
            path.removeLast()
        }
        return sand
    }

    fun parseWalls(input: List<String>) = input.map { line ->
        line.split(" -> ")
            .map { coords -> coords.split(",") }
            .map { Point(it[0].toInt(), it[1].toInt()) }
    }.flatMap { createWall(it) }.toSet()

    fun execute(input: List<String>, part1: Boolean): Int {
        val walls = parseWalls(input)
        val sand = simulateSand(walls, part1)

        prettyPrint(walls, sand)

        return sand.size
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
