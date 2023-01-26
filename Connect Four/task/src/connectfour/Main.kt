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
            break
        }

        if (isNotNumber(column)) continue

        if (isColumnValid(column.toInt(), columns)) continue

        if (isColumnFull(board, column.toInt())) continue

        addMove(board, column.toInt(), isPlayer1Turn)
        printBoard(board)

        if (isBoardFull(board)) break

        if (checkWinner(board, player1, player2)) break

        isPlayer1Turn = !isPlayer1Turn
    }

    println("Game over!")
}

fun isBoardFull(board: Array<CharArray>): Boolean {
    return if (board.all { it.all { it != ' ' } }) {
        println("It is a draw")
        true
    } else false
}

fun checkWinner(board: Array<CharArray>, player1: String, player2: String): Boolean {
    val rows = board.size
    val columns = board[0].size

    for (row in 0 until rows) {
        for (column in 0 until columns) {
            if (board[row][column] != ' ') {
                if (column + 3 < columns &&
                    board[row][column] == board[row][column + 1] &&
                    board[row][column] == board[row][column + 2] &&
                    board[row][column] == board[row][column + 3]
                ) {
                    println("Player ${if (board[row][column] == 'o') player1 else player2} won")
                    return true
                }

                if (row + 3 < rows) {
                    if (board[row][column] == board[row + 1][column] &&
                        board[row][column] == board[row + 2][column] &&
                        board[row][column] == board[row + 3][column]
                    ) {
                        println("Player ${if (board[row][column] == 'o') player1 else player2} won")
                        return true
                    }

                    if (column + 3 < columns &&
                        board[row][column] == board[row + 1][column + 1] &&
                        board[row][column] == board[row + 2][column + 2] &&
                        board[row][column] == board[row + 3][column + 3]
                    ) {
                        println("Player ${if (board[row][column] == 'o') player1 else player2} won")
                        return true
                    }

                    if (column - 3 >= 0 &&
                        board[row][column] == board[row + 1][column - 1] &&
                        board[row][column] == board[row + 2][column - 2] &&
                        board[row][column] == board[row + 3][column - 3]
                    ) {
                        println("Player ${if (board[row][column] == 'o') player1 else player2} won")
                        return true
                    }
                }
            }
        }
    }

    return false
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
