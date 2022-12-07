class Dir {
    var size = 0
    val children = mutableSetOf<Dir>()

    fun parseLs(input: String) {
        this.size = input.split("\n").drop(1)
            .filterNot { it.startsWith("dir") }
            .fold(0) { totalSize, line -> totalSize + line.split(" ").first().toInt() }
    }
    fun addChild() = Dir().also { children.add(it) }

    val totalSize: Int
        get() = size + children.sumOf { it.totalSize }

}

fun main() {

    fun parseDirectories(input: String): List<Dir> {
        val rootDir = Dir()
        val workingDirs = ArrayDeque<Dir>()
        val dirs = mutableListOf(rootDir)
        workingDirs.addFirst(rootDir)

        input.split("\n$ ").drop(1).forEach { cmd ->
            when {
                cmd.startsWith("ls") -> {
                    workingDirs.first().parseLs(cmd)
                }

                cmd.startsWith("cd") -> {
                    when (cmd.split(" ")[1]) {
                        ".." -> {
                            workingDirs.removeFirst()
                        }
                        else -> {
                            val newDir = workingDirs.first().addChild()
                            dirs.add(newDir)
                            workingDirs.addFirst(newDir)
                        }
                    }
                }
            }
        }
        return dirs
    }

    fun part1(input: String): Int {
        val dirs = parseDirectories(input)
        return dirs.map { it.totalSize }.filter { it <= 100000  }.sum()
    }

    fun part2(input: String): Int {
        val dirs = parseDirectories(input)
        val capacity = 70000000
        val requiredSpace = 30000000
        val rootDir = dirs[0]
        val unusedSize = capacity - rootDir.totalSize
        val minSize = requiredSpace - unusedSize
        return dirs.map { it.totalSize }.sorted().first { it >= minSize }
    }

    val testInput = readInputAsString("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)


    val input = readInputAsString("Day07")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
