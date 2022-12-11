fun main() {

    val monkeyRegex =
        Regex("""Starting items:\s(?<startingItems>.+)\n.+old\s(?<op>.)\s(?<rhs>.+)\n.+by\s(?<mod>\d+)\n.+monkey\s(?<m1>\d+)\n.+monkey\s(?<m2>\d+)""")

    class Monkey(input: String, val part2: Boolean) {
        val items: ArrayDeque<Long>
        private val operation: (old: Long) -> Long
        val modTest: Int
        private val ifTrueMonkeyIndex: Int
        private val ifFalseMonkeyIndex: Int
        private var ifTrueMonkey: Monkey? = null
        private var ifFalseMonkey: Monkey? = null
        var inspected: Int = 0

        init {
            val match = monkeyRegex.find(input) ?: throw IllegalStateException("Regex doesn't match input")
            val (startingItems, op, rhs, mod, m1, m2) = match.destructured
            items = ArrayDeque(startingItems.split(", ").map { it.toLong() })
            modTest = mod.toInt()
            operation = parseOperation(op, rhs, modTest, part2)
            ifTrueMonkeyIndex = m1.toInt()
            ifFalseMonkeyIndex = m2.toInt()
        }

        override fun toString(): String {
            return items.toString()
        }

        fun takeTurn(part2: Boolean = false) {
            inspected += items.size
            while (items.isNotEmpty()) {
                val item = items.removeFirst()
                val level = operation(item) / 3
                val target = if (level % modTest == 0L) ifTrueMonkey else ifFalseMonkey
                target!!.items.addLast(level)
            }
        }

        fun takeTurnPart2(lcm: Int) {
            inspected += items.size
            while (items.isNotEmpty()) {
                val item = items.removeFirst()
                val level = operation(item) % lcm
                val target = if (level % modTest == 0L) ifTrueMonkey else ifFalseMonkey
                target!!.items.addLast(level)
            }
        }

        fun linkWithOthers(monkeys: List<Monkey>) {
            ifTrueMonkey = monkeys[ifTrueMonkeyIndex]
            ifFalseMonkey = monkeys[ifFalseMonkeyIndex]
        }

        private fun parseOperation(op: String, rhs: String, mod: Int, part2: Boolean): (old: Long) -> Long {
            val operator: Long.(Long) -> Long = when (op) {
                "+" -> Long::plus
                "*" -> Long::times
                else -> throw IllegalArgumentException("invalid operator $op")
            }
            return when (rhs) {
                "old" -> {
                    { old -> operator(old, old) }
                }

                else -> {
                    { old -> operator(old, rhs.toLong()) }
                }
            }

        }
    }

    fun initMonkeys(input: String, part2: Boolean): List<Monkey> {
        val monkeys = input.split("\n\n").map { Monkey(it, part2) }
        monkeys.forEach { it.linkWithOthers(monkeys) }
        return monkeys
    }

    fun getMonkeyBusiness(monkeys: List<Monkey>) =
        monkeys.map { it.inspected.toLong() }.sortedDescending().subList(0, 2).reduce(Long::times)

    fun part1(input: String): Long {
        val monkeys = initMonkeys(input, false)
        repeat(20) { round ->
            println("Play round $round")
            monkeys.forEach { it.takeTurn() }
        }
        return getMonkeyBusiness(monkeys)
    }


    fun part2(input: String): Long {
        val monkeys = initMonkeys(input, true)
        // since the input modTest only contain prime numbers, their LCM is the product of all numbers
        val lcm = monkeys.map { it.modTest }.reduce(Int::times)
        repeat(10000) { round ->
            println("Play round $round")
            monkeys.forEach { it.takeTurnPart2(lcm) }
        }
        return getMonkeyBusiness(monkeys)
    }

    val testInput = readInputAsString("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)


    val input = readInputAsString("Day11")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: $part2Answer")
}
