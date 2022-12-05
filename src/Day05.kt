data class Instruction(val amount: Int, val source: Int, val dest: Int)

class CrateMover(private val stacks: List<ArrayDeque<String>>) {

    fun moveSingleStacks(instruction: Instruction) {
        val source = stacks[instruction.source]
        val dest = stacks[instruction.dest]

        repeat(instruction.amount) {
            dest.addFirst(source.removeFirst())
        }
    }

    fun moveMultipleStacks(instruction: Instruction) {
        val source = stacks[instruction.source]
        val dest = stacks[instruction.dest]

        val crates = (1..instruction.amount).fold(listOf<String>()) { crates, _ ->
            crates + source.removeFirst()
        }
        dest.addAll(0, crates)
    }

    fun getTopOfStacks(): String {
        return stacks.joinToString("") { it.first() }
    }

}

fun main() {

    fun parseDrawing(input: List<String>): List<ArrayDeque<String>> {
        val drawingLines = input.takeWhile { it[1] != '1' }
        return drawingLines.fold(mutableListOf()) { stacks, line ->
            line.chunked(4).forEachIndexed { i, elem ->
                if (stacks.size < i + 1) {
                    stacks.add(ArrayDeque())
                }
                val match = Regex("""\[(\w)\]""").find(elem)
                if (match != null) {
                    val crate = match.groupValues[1]
                    stacks[i].addLast(crate)
                }
            }
            return@fold stacks
        }
    }

    fun parseInstructions(input: List<String>): List<Instruction> {
        return input
            .filter { it.startsWith("move") }
            .map { line ->
                val match = Regex("""move (\d+) from (\d+) to (\d+)""").find(line)
                val (amount, source, dest) = match?.destructured ?: throw IllegalStateException("Invalid input")
                Instruction(amount.toInt(), source.toInt() - 1, dest.toInt() - 1)
            }
    }

    fun part1(input: List<String>): String {
        val drawing = parseDrawing(input)
        val instructions = parseInstructions(input)
        val crateMover = CrateMover(drawing)
        instructions.forEach { crateMover.moveSingleStacks(it) }
        return crateMover.getTopOfStacks()
    }

    fun part2(input: List<String>): String {
        val drawing = parseDrawing(input)
        val instructions = parseInstructions(input)
        val crateMover = CrateMover(drawing)
        instructions.forEach { crateMover.moveMultipleStacks(it) }
        return crateMover.getTopOfStacks()
    }

    val testInput = readInput("Day05_test")
    part1(testInput)
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")


    val input = readInput("Day05")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
