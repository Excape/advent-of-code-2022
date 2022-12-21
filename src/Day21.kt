fun main() {

    data class Expression(var left: Any, var right: Any, val operand: Char) {
        fun evaluate(): ULong {
            assert(left is ULong)
            assert(right is ULong)
            val l = left as ULong
            val r = right as ULong
            val result = when (operand) {
                '+' -> l + r
                '-' -> l - r
                '*' -> l * r
                '/' -> l / r
                else -> throw IllegalStateException("Invalid operand $operand")
            }
            return result
        }

        fun printRecursively(allExpressions: Map<String, Any>): String {
            val leftString = getExpressionString(allExpressions, left)
            val rightString = getExpressionString(allExpressions, right)
            return "($leftString $operand $rightString)"
        }

        private fun getExpressionString(allExpressions: Map<String, Any>, expr: Any) = when (expr) {
            is String -> {
                val job = allExpressions[expr]!!
                if (job is Expression) job.printRecursively(allExpressions) else job.toString()
            }

            else -> expr.toString()
        }
    }

    fun parseExpression(expr: String): Any {
        val numberOrExpressions = expr.split(" ")
        if (numberOrExpressions.size == 1) {
            return numberOrExpressions.first().toULong()
        }
        return Expression(numberOrExpressions[0], numberOrExpressions[2], numberOrExpressions[1].first())
    }

    fun parseInput(input: List<String>) =
        input.map { it.split(":") }.associate { it[0] to parseExpression(it[1].trim()) }

    fun updateExpression(newExpression: Expression, allExpressions: Map<String, Any>) {
        if (newExpression.left is String && allExpressions[newExpression.left] is ULong) {
            newExpression.left = allExpressions[newExpression.left]!!
        }
        if (newExpression.right is String && allExpressions[newExpression.right] is ULong) {
            newExpression.right = allExpressions[newExpression.right]!!
        }
    }

    fun part1(input: List<String>): ULong {
        val expressions = parseInput(input)
        val iterations = generateSequence(expressions) { allExpressions ->
            allExpressions.mapValues { (_, expr) ->
                if (expr is ULong) {
                    return@mapValues expr
                }
                val newExpression = expr as Expression
                updateExpression(newExpression, allExpressions)
                return@mapValues when {
                    newExpression.left is ULong && newExpression.right is ULong -> newExpression.evaluate()
                    else -> newExpression
                }
            }
        }.takeWhile { it["root"] is Expression }.toList()

        val rootExpression: Expression = iterations.last()["root"] as Expression
        return rootExpression.evaluate()
    }

    fun buildEquation(expressions: Map<String, Any>): String {
        val root = expressions["root"]!! as Expression
        val rootWithEquals = root.copy(operand = '=')
        return rootWithEquals.printRecursively(expressions)
    }

    fun part2(input: List<String>): String {
        val inputExpr = parseInput(input).toMutableMap()
        inputExpr["humn"] = "X"

        var changed = true
        var currentExpr = inputExpr.toMap()
        while (changed) {
            changed = false
            currentExpr = currentExpr.mapValues { (_, expr) ->
                if (expr is ULong) {
                    return@mapValues expr
                }
                if (expr == "X") {
                    return@mapValues expr
                }
                val newExpression = (expr as Expression).copy()
                updateExpression(newExpression, currentExpr)
                if (newExpression != expr) {
                    changed = true
                }
                return@mapValues when {
                    newExpression.left is ULong && newExpression.right is ULong -> newExpression.evaluate()
                    else -> newExpression
                }
            }
        }

        return buildEquation(currentExpr.toMap())
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152UL)


    val input = readInput("Day21")
    val part1Answer = part1(input)
    val part2Answer = part2(input)
    println("part 1: $part1Answer")
    println("part 2: Plug this into a solver :) : $part2Answer")

}
