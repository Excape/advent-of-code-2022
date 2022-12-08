data class Coords(val y: Int, val x: Int) {
    fun pair() = Pair(y, x)
}

fun main() {

    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun isInBounds(grid: List<List<Int>>, coords: Coords) =
        coords.x >= 0 && coords.y >= 0 && coords.y < grid.size && coords.x < grid[coords.y].size

    fun applyDirection(coords: Coords, direction: Pair<Int, Int>) =
        Coords(coords.y + direction.first, coords.x + direction.second)

    fun isVisibleInDirection(coords: Coords, grid: List<List<Int>>, direction: Pair<Int, Int>): Boolean {
        val height = grid[coords.y][coords.x]
        var current = applyDirection(coords, direction)
        while (isInBounds(grid, current)) {
            if (grid[current.y][current.x] >= height)
                return false
            current = applyDirection(current, direction)
        }
        return true
    }

    fun isVisible(coords: Coords, grid: List<List<Int>>): Boolean {
        return directions.any { dir -> isVisibleInDirection(coords, grid, dir) }
    }

    fun mapInputToGrid(input: List<String>) = input.map { it.map { it.digitToInt() } }

    fun part1(input: List<String>): Int {
        val grid = mapInputToGrid(input)
        val boundX = grid[0].size - 1
        val boundY = grid.size - 1
        val treesOnBorder = boundX * 2 + boundY * 2

        val visibleTrees = (1 until boundY).flatMap { y ->
            (1 until boundX).map { x ->
                isVisible(Coords(y, x), grid)
            }
        }.count { it }
        return treesOnBorder + visibleTrees
    }


    fun getViewingDistance(coords: Coords, grid: List<List<Int>>, direction: Pair<Int, Int>): Int {
        val height = grid[coords.y][coords.x]
        var current = applyDirection(coords, direction)
        var viewingDistance = 0
        while (isInBounds(grid, current)) {
            if (grid[current.y][current.x] >= height)
                return viewingDistance + 1
            viewingDistance++
            current = applyDirection(current, direction)
        }
        return viewingDistance
    }


    fun getScenicScore(coords: Coords, grid: List<List<Int>>): Int {
        return directions.map { dir -> getViewingDistance(coords, grid, dir) }.reduce { acc, n -> acc * n }
    }

    fun part2(input: List<String>): Int {
        val grid = mapInputToGrid(input)
        val boundX = grid[0].size - 1
        val boundY = grid.size - 1

        return (1 until boundY).flatMap { y ->
            (1 until boundX).map { x ->
                getScenicScore(Coords(y, x), grid)
            }
        }.max()
    }

    val testInput = readInput("Day08_test")
    println(part1(testInput))
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)


    val input = readInput("Day08")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
