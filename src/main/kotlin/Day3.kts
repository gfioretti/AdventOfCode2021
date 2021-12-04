fun openReport(function: List<String>.() -> Unit): Unit = FileLoader.openFile(dayNumber = 3).function()

fun firstPuzzle() {
    openReport {
        map { it.toCharArray() }
            .countMostCommonChar()
            .run { toDecimalInt() * map { it.invert() }.toDecimalInt() }
            .also { println(it) }
    }
}

tailrec fun List<CharArray>.countMostCommonChar(index: Int = 0, acc: List<Int> = listOf()): List<Int> {
    return if (index == this[0].size) acc
    else countMostCommonChar(
        index = index.inc(),
        acc = acc.plus(this.map { it[index] }.count { it == '0' }.let { if ((this.size - it) < this.size / 2) 0 else 1})
    )
}

fun Int.invert(): Int {
    return when (this) {
        0 -> 1
        1 -> 0
        else -> throw Exception("$this is not a binary number")
    }
}

fun List<Int>.toDecimalInt(): Int {
    return this
        .map { it.toString() }
        .reduce { acc, s -> acc + s }
        .toInt(radix = 2)
}

firstPuzzle()

fun secondPuzzle() {
    openReport {
        val oxygenGeneratorRating = filterUntilMax(bitCriteria = mostRecurrent).toInt(radix = 2)
        val co2ScrubberRating = filterUntilMax(bitCriteria = leastRecurrent).toInt(radix = 2)
        (oxygenGeneratorRating * co2ScrubberRating).also { println(it) }
    }
}

val mostRecurrent: (Int, Int) -> Int = { zeros, ones -> if (zeros <= ones) 1 else 0 }
val leastRecurrent: (Int, Int) -> Int = { zeros, ones -> if (zeros <= ones) 0 else 1 }

tailrec fun List<String>.filterUntilMax(maxSize: Int = 1, index: Int = 0, bitCriteria: (Int, Int) -> Int): String {
    return if (this.size == maxSize) this[0]
    else this.run {
        val common = map { it.toCharArray() }.map { it[index] }.run {
            val zeros = filter { it == '0' }.size
            val ones = filter { it == '1' }.size
            bitCriteria(zeros, ones)
        }
        filter { it[index].digitToInt() == common }
    }.filterUntilMax(index = index.inc(), bitCriteria = bitCriteria)
}
secondPuzzle()