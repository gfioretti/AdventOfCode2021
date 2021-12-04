import java.io.File

object FileLoader {
    fun openFile(dayNumber: Int): List<String> {
        return File(this.javaClass.getResource("day$dayNumber.txt")!!.file).readLines()
    }
}
