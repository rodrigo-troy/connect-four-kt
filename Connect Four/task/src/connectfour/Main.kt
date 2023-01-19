package connectfour

const val pattern = "\\s*[0-9]{1,9}\\s*[x|X]\\s*[0-9]{1,9}\\s*"
const val splitter = "[x|X]"

fun validateInput(input: String): Boolean {
    return if (input.matches(Regex(pattern))) {
        val (rows, columns) = input.split(Regex(splitter)).map { it.trim().toInt() }

        if (rows in 5..9 && columns in 5..9) {
            return true
        } else if (rows !in 5..9) {
            println("Board rows should be from 5 to 9")
            return false
        } else {
            println("Board columns should be from 5 to 9")
            return false
        }
    } else {
        println("Invalid input")
        false
    }
}

fun main() {
    println("Connect Four")

    println("First player's name:")
    val player1 = readln()

    println("Second player's name:")
    val player2 = readln()


    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    var input = readln().ifBlank { "6 x 7" }

    while (!validateInput(input)) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        input = readln().ifBlank { "6 x 7" }
    }

    val (rows, columns) = input.split(Regex(splitter)).map { it.trim().toInt() }

    println("$player1 vs $player2")
    println("$rows x $columns board")
}
