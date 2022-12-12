package util

class UnweightedGraph<T>(
    val vertices: List<T>,
    val edges: Map<T, Set<T>>,
) {
    fun findShortestPathLength(start: T, end: T): Int? {
        val previousMap = bfs(start, end)
        var size = 0
        var current = end
        while (current != start) {
            current = previousMap[current] ?: return null
            size += 1
        }
        return size
    }

    private fun bfs(start: T, end: T): Map<T, T> {
        val visited = mutableSetOf<T>()
        val queue = ArrayDeque(listOf(start))
        val previous = mutableMapOf<T, T>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val neighbors = edges[current]!!
            neighbors.forEach { n ->
                if (!visited.contains(n)) {
                    visited.add(n)
                    previous[n] = current
                    queue.addLast(n)
                    if (n == end) {
                        return previous
                    }
                }
            }
        }
        return previous
    }
}