fun openFile(): List<String> {
    return FileLoader.openFile(dayNumber = 4)
}

fun firstPuzzle() {
    val file = openFile()
    val bingoList = file[0].split(",").map { it.toInt() }
    val boards = BingoBoard.from(src = file.subList(1, file.indices.last))
    runTheBingo(bingoNumbers = bingoList, bingoBoard = boards)
    val result = runTheBingo(bingoNumbers = bingoList, bingoBoard = boards)
    val sumOfNotMarked = result.second.listOfRows
        .flatMap { it.row.filterNot { it2 -> it2.marked } }
        .map { it.number }
        .fold(initial = 0, operation = { acc, number -> acc + number })

    (result.first * sumOfNotMarked).also { println(it) }
}

fun secondPuzzle() {
    val file = openFile()
    val results = runTheSecondBingo(
        bingoNumbers = file[0].split(",").map { it.toInt() },
        bingoBoard = BingoBoard.from(src = file.subList(1, file.indices.last))
    )
    val sumOfNotMarked = results.value.listOfBoards.last().listOfRows.flatMap { it.row.filterNot { it2 -> it2.marked } }
        .map { it.number }
        .fold(initial = 0, operation = { acc, number -> acc + number })
    (sumOfNotMarked * results.key).also { println(it) }
}

tailrec fun runTheBingo(bingoNumbers: List<Int>, index: Int = 0, bingoBoard: BingoBoard): Pair<Int, BingoMatrix> {
    return if (index <= 4) {
        runTheBingo(bingoNumbers = bingoNumbers, index = index + 1, bingoBoard.update(bingoNumbers[index]))
    } else if (bingoBoard.bingo) {
        Pair(bingoNumbers[index - 1], bingoBoard.getBingo())
    } else {
        runTheBingo(bingoNumbers = bingoNumbers, index = index + 1, bingoBoard.update(bingoNumbers[index]))
    }
}

tailrec fun runTheSecondBingo(
    bingoNumbers: List<Int>,
    index: Int = 0,
    bingoBoard: BingoBoard,
    winnerMatrixes: Map<Int, BingoBoard> = linkedMapOf()
): Map.Entry<Int, BingoBoard> {
    return if (index == bingoNumbers.size) winnerMatrixes.filterValues { it.listOfBoards.isNotEmpty() }.entries.last()
    else {
        val updated = bingoBoard.update(bingoNumbers[index])
        runTheSecondBingo(
            bingoNumbers = bingoNumbers,
            index = index + 1,
            bingoBoard = updated.listOfBoards.filterNot { it.bingo }.let { BingoBoard(it) },
            winnerMatrixes = winnerMatrixes.plus(bingoNumbers[index] to updated.listOfBoards.filter { it.bingo }.let { BingoBoard(it) })
        )
    }
}

interface Updatable<T> {
    fun update(number: Int): T
}

interface Transposable<T> {
    fun transposed(): T
}

data class BingoNumber(val number: Int, val marked: Boolean = false) : Updatable<BingoNumber> {
    override fun update(number: Int): BingoNumber {
        return if (this.number == number) BingoNumber(this.number, true) else this
    }
}
data class Row(val row: List<BingoNumber>) : Updatable<Row> {
    val bingo by lazy { row.all { it.marked } }
    override fun update(number: Int): Row {
        return row
            .map { it.update(number) } // update number
            .let { Row(it) }
    }
}
data class BingoMatrix(val listOfRows: List<Row>) : Updatable<BingoMatrix>, Transposable<BingoMatrix> {
    val bingo by lazy { listOfRows.any { it.bingo } || this.transposed().listOfRows.any { it.bingo } }

    override fun update(number: Int): BingoMatrix {
        return BingoMatrix(listOfRows.map { it.update(number) })
    }

    override fun transposed(): BingoMatrix {
        fun transpose(listOfRows: List<Row>, index: Int = 0, acc: List<Row> = listOf()): BingoMatrix {
            return if (index == listOfRows.size) BingoMatrix(acc)
            else {
                transpose(
                    listOfRows = listOfRows,
                    index = index + 1,
                    acc = acc.plus(element = Row(row = listOfRows.map { it.row[index] }))
                )
            }
        }
        return transpose(listOfRows = listOfRows)
    }
}
data class BingoBoard(val listOfBoards: List<BingoMatrix>) : Updatable<BingoBoard>, Transposable<BingoBoard> {
    val bingo by lazy { listOfBoards.any { it.bingo } }

    override fun update(number: Int): BingoBoard {
        return BingoBoard(listOfBoards.map { it.update(number) })
    }
    override fun transposed(): BingoBoard {
        return BingoBoard(listOfBoards.map { it.transposed() })
    }
    fun getBingo(): BingoMatrix {
        return listOfBoards.first { it.bingo }
    }

    companion object {
        fun from(src: List<String>): BingoBoard {
            return src.filter { it != "" }
                .map { line -> line.split(" ").filter { it != "" }.map { BingoNumber(number = it.toInt()) } }
                .chunked(5)
                .map { BingoMatrix(it.map { it2 -> Row(it2) }) }
                .let { BingoBoard(it) }
        }
    }
}

firstPuzzle()
secondPuzzle()
