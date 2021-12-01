fun main() {
    val input = FileLoader.openFile(dayNumber = 1).map { it.toInt() }
    println(input.countIncreasedDistances())
    println(input.listOfSums().countIncreasedDistances())
}

tailrec fun List<Int>.countIncreasedDistances(currentIndex: Int = 1, previousIndex: Int = 0, acc: Int = 0): Int {
    return if (currentIndex == this.size) acc
    else {
        this.countIncreasedDistances(
            currentIndex = currentIndex + 1,
            previousIndex = currentIndex,
            acc = if (this[currentIndex] > this[previousIndex]) (acc + 1) else acc
        )
    }
}

tailrec fun List<Int>.listOfSums(minIndex: Int = 0, maxIndex: Int = 3, acc: List<Int> = listOf()): List<Int> {
    return if (maxIndex > this.size) acc
    else {
        this.listOfSums(
            minIndex = minIndex + 1,
            maxIndex = maxIndex + 1,
            acc = acc.plus(this.subList(minIndex, maxIndex).sum())
        )
    }
}