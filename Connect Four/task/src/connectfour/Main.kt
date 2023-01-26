package connectfour

const val boardRegex = "\\s*[0-9]{1,9}\\s*[x|X]\\s*[0-9]{1,9}\\s*"
const val numberRegex = "\\s*[0-9]+\\s*"
const val splitterRegex = "[x|X]"

fun validateInput(input: String): Boolean {
    return if (input.matches(Regex(boardRegex))) {
        val (rows, columns) = input.split(Regex(splitterRegex)).map { it.trim().toInt() }

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

    val (rows, columns) = input.split(Regex(splitterRegex)).map { it.trim().toInt() }

    println("$player1 vs $player2")
    println("$rows x $columns board")

    val board = Array(rows) { CharArray(columns) { ' ' } }

    printBoard(board)

    var isPlayer1Turn = true
    while (true) {
        println((if (isPlayer1Turn) player1 else player2) + "'s turn")
        val column = readln()

        if (column == "end") {
            println("Game over!")
            break
        }

        if (isNotNumber(column)) continue

        if (isColumnValid(column.toInt(), columns)) continue

        if (isColumnFull(board, column.toInt())) continue

        addMove(board, column.toInt(), isPlayer1Turn)
        isPlayer1Turn = !isPlayer1Turn
        printBoard(board)
    }
}

fun isNotNumber(column: String): Boolean {
    if (!column.matches(Regex(numberRegex))) {
        println("Incorrect column number")
        return true
    }
    return false
}

fun addMove(board: Array<CharArray>, column: Int, player1Turn: Boolean) {
    for (row in board.lastIndex downTo 0) {
        if (board[row][column - 1] == ' ') {
            board[row][column - 1] = if (player1Turn) 'o' else '*'
            break
        }
    }
}

fun printBoard(board: Array<CharArray>) {
    println(" ${1.rangeTo(board[0].size).joinToString(" ")}")
    board.indices.forEach { row ->
        board[row].indices.forEach { column ->
            print("║${board[row][column]}")
        }
        println("║")
    }
    println("╚═${"╩═".repeat(board[0].size - 1)}╝")
}

fun isColumnValid(column: Int, columns: Int): Boolean {
    return if (column !in 1..columns) {
        println("The column number is out of range (1 - $columns)")
        true
    } else false
}

fun isColumnFull(board: Array<CharArray>, column: Int): Boolean {
    return if (board[0][column - 1] != ' ') {
        println("Column $column is full")
        true
    } else false
}
