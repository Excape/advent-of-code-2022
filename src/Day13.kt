fun main() {
    fun findMatchingBracket(input: String): Int {
        var counter = 1
        input.substring(1)
            .forEachIndexed { i, c ->
            when (c) {
                '[' -> counter++
                ']' -> counter --
            }
            if (counter == 0) return i + 1
        }
        throw IllegalStateException("unbalanced brackets")
    }

    fun parseRecursively(input: String, list: MutableList<Any>) {
        if (input.isBlank()) {
            return
        }
        if (input.startsWith("[")) {
            val subList = mutableListOf<Any>()
            list.add(subList)
            val listEnd = findMatchingBracket(input)
            val content = input.substring(1 until listEnd)
            parseRecursively(content, subList)
            if (listEnd < input.lastIndex) {
                parseRecursively(input.substring(listEnd + 2), list)
            }
        } else if (input[0].isDigit()) {
            val nextComma = input.indexOf(',')
            val end = if (nextComma == -1) input.length else nextComma
            val number = input.substring(0 until end).toInt()
            list.add(number)
            if (nextComma == -1) {
                return
            }
            parseRecursively(input.substring(nextComma + 1), list)
        }
    }

    fun parsePackage(input: String): Any {
        val rootList = mutableListOf<Any>()
        parseRecursively(input, rootList)
        return rootList[0]
    }

    fun comparePackage(first: Any, second: Any): Int {
        return when {
            first is Int && second is Int -> first - second

            first is List<*> && second is List<*> ->
                (first zip second).map { comparePackage(it.first!!, it.second!!) }
                    .firstOrNull { it != 0 }
                    ?: (first.size - second.size)

            else -> if (first is Int) comparePackage(listOf(first), second)
            else comparePackage(first, listOf(second))
        }
    }


    fun part1(input: String): Int {
        val pairs = input
            .split("\n\n")
            .map { it.split("\n").map { line -> parsePackage(line) } }
        return pairs
            .map { comparePackage(it[0], it[1]) }
            .map { it < 0 }
            .foldIndexed(0) { i, acc, isOrdered -> if (isOrdered) acc + i + 1 else acc }
    }

    fun part2(input: String): Int {
        val allInputPackages = input.split("\n")
            .filter { it.isNotBlank() }
            .map { parsePackage(it) }

        val divider1 = listOf(listOf(2))
        val divider2 = listOf(listOf(6))
        val allPackages = allInputPackages + listOf(divider1) + listOf(divider2)

        val sortedPackages = allPackages.sortedWith(::comparePackage)

        return (sortedPackages.indexOf(divider1) + 1) * (sortedPackages.indexOf(divider2) + 1)
    }

    val testInput = readInputAsString("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)


    val input = readInputAsString("Day13")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
