import util.UnweightedGraph

fun main() {
    val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

    fun inBounds(input: List<String>, p: Point) =
        p.x >= 0 && p.y >= 0 && p.y <= input.lastIndex && p.x <= input[p.y].lastIndex

    fun getElevation(input: List<String>, point: Point): Char {
        return when (val elevation = input[point.y][point.x]) {
            'S' -> 'a'
            'E' -> 'z'
            else -> elevation
        }
    }

    fun parseGraph(input: List<String>): UnweightedGraph<Point> {
        val vertices = input.indices.flatMap { y ->
            input[y].indices.map { x -> Point(x, y) }
        }

        val edges = vertices.associateWith { v ->
            val elevation = getElevation(input, v)
            directions
                .map { Point(v.x + it.first, v.y + it.second) }
                .filter { inBounds(input, it) }
                .filter { getElevation(input, it) <= elevation.inc() }.toSet()
        }

        return UnweightedGraph(vertices, edges)
    }


    fun getStartPoint(graph: UnweightedGraph<Point>, input: List<String>) =
        graph.vertices.find { p -> input[p.y][p.x] == 'S' } ?: throw IllegalStateException("start point not found")

    fun getEndPoint(graph: UnweightedGraph<Point>, input: List<String>) =
        graph.vertices.find { p -> input[p.y][p.x] == 'E' } ?: throw IllegalStateException("end point not found")


    fun part1(input: List<String>): Int {
        val graph = parseGraph(input)
        val startPoint = getStartPoint(graph, input)
        val endPoint = getEndPoint(graph, input)
        return graph.findShortestPathLength(startPoint, endPoint)
            ?: throw IllegalStateException("no shortest path found")
    }

    fun part2(input: List<String>): Int {
        val graph = parseGraph(input)
        val endPoint = getEndPoint(graph, input)
        val startPoints = graph.vertices.filter { getElevation(input, it) == 'a' }
        return startPoints.mapNotNull { startPoint -> graph.findShortestPathLength(startPoint, endPoint) }.min()
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)


    val input = readInput("Day12")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
