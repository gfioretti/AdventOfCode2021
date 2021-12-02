fun main() {
    val navigationInstructions = FileLoader
        .openFile(dayNumber = 2)
        .map {
            it.split(" ").run {
                if (this[0] == "forward") Pair(this[1].toInt(), 0)
                else if (this[0] == "down") Pair(0, this[1].toInt())
                else Pair(0, this[1].toInt() * -1)
            }
        }

    // Puzzle #1
    val (position, depth) = navigationInstructions.reduce {
            (accPosition, accDepth), (position, depth) -> accPosition + position to accDepth + depth
    }
    println(position * depth)

    // Puzzle #2
    val (finalHorizontalPosition, _, finalDepth) = navigationInstructions.fold(
        initial = Triple(0, 0, 0),
        operation = { (accHorizontalPosition, accAim, accDepth), (position, depth) ->
            Triple(
                first = accHorizontalPosition + position, second = accAim + depth, third = accDepth + (position * accAim)
            )
        }
    )
    println(finalHorizontalPosition * finalDepth)
}
