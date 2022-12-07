fun main() {

fun findFirstDistinct(input: String, n: Int) = input.windowed(n).indexOfFirst { it.toSet().size == n } + n

fun part1(input: String) = findFirstDistinct(input, 4)

fun part2(input: String) = findFirstDistinct(input, 14)

    println(part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))

    check(part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)


    val input = readInputAsString("Day06")
    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
