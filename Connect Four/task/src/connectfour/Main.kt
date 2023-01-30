package connectfour

import kotlin.system.exitProcess

const val boardRegex = "\\s*[0-9]{1,9}\\s*[x|X]\\s*[0-9]{1,9}\\s*"
const val numberRegex = "\\s*[0-9]+\\s*"
const val gamesRegex = "\\s*[1-9][0-9]*\\s*"
const val splitterRegex = "[x|X]"

class Player(private val name: String, private val symbol: Char) {
    private var score = 0

    fun addScore(points: Int) {
        score += points
    }

    fun getScore(): Int {
        return score
    }

    fun getSymbol(): Char {
        return symbol
    }

    override fun toString(): String {
        return name
    }
}

class Game(private val player1: Player, private val player2: Player, rows: Int, private val columns: Int) {
    private val board = Array(rows) { CharArray(columns) { ' ' } }

    fun play(firstPlayer: Player) {
        printBoard(board)

        var playerTurn = firstPlayer

        while (true) {
            println("$playerTurn's turn:")
            val column = readln()

            if (column == "end") {
                println("Game over!")
                exitProcess(0)
            }

            if (isNotNumber(column)) continue

            if (isColumnValid(column.toInt(), columns)) continue

            if (isColumnFull(board, column.toInt())) continue

            addMove(board, column.toInt(), playerTurn)
            printBoard(board)

            if (isBoardFull(board)) {
                player1.addScore(1)
                player2.addScore(1)
                break
            }

            if (checkWinner(board)) {
                println("Player $playerTurn won")
                playerTurn.addScore(2)
                break
            }

            playerTurn = if (playerTurn == player1) player2 else player1
        }
    }

    private fun isBoardFull(board: Array<CharArray>): Boolean {
        return if (board.all { it.all { it != ' ' } }) {
            println("It is a draw")
            true
        } else false
    }

    private fun checkWinner(board: Array<CharArray>): Boolean {
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
                        return true
                    }

                    if (row + 3 < rows) {
                        if (board[row][column] == board[row + 1][column] &&
                            board[row][column] == board[row + 2][column] &&
                            board[row][column] == board[row + 3][column]
                        ) {
                            return true
                        }

                        if (column + 3 < columns &&
                            board[row][column] == board[row + 1][column + 1] &&
                            board[row][column] == board[row + 2][column + 2] &&
                            board[row][column] == board[row + 3][column + 3]
                        ) {
                            return true
                        }

                        if (column - 3 >= 0 &&
                            board[row][column] == board[row + 1][column - 1] &&
                            board[row][column] == board[row + 2][column - 2] &&
                            board[row][column] == board[row + 3][column - 3]
                        ) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun isNotNumber(column: String): Boolean {
        if (!column.matches(Regex(numberRegex))) {
            println("Incorrect column number")
            return true
        }
        return false
    }

    private fun addMove(board: Array<CharArray>, column: Int, player: Player) {
        for (row in board.lastIndex downTo 0) {
            if (board[row][column - 1] == ' ') {
                board[row][column - 1] = player.getSymbol()
                break
            }
        }
    }

    private fun printBoard(board: Array<CharArray>) {
        println(" ${1.rangeTo(board[0].size).joinToString(" ")}")
        board.indices.forEach { row ->
            board[row].indices.forEach { column ->
                print("║${board[row][column]}")
            }
            println("║")
        }
        println("╚═${"╩═".repeat(board[0].size - 1)}╝")
    }

    private fun isColumnValid(column: Int, columns: Int): Boolean {
        return if (column !in 1..columns) {
            println("The column number is out of range (1 - $columns)")
            true
        } else false
    }

    private fun isColumnFull(board: Array<CharArray>, column: Int): Boolean {
        return if (board[0][column - 1] != ' ') {
            println("Column $column is full")
            true
        } else false
    }
}

fun validateInput(input: String, regex: Regex): Boolean = input.matches(regex)

fun validateInput(input: String): Boolean {
    return if (validateInput(input, Regex(boardRegex))) {
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
    val player1 = Player(readln(), 'o')

    println("Second player's name:")
    val player2 = Player(readln(), '*')

    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    var input = readln().ifBlank { "6 x 7" }
    while (!validateInput(input)) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        input = readln().ifBlank { "6 x 7" }
    }
    val (rows, columns) = input.split(Regex(splitterRegex)).map { it.trim().toInt() }

    println(
        "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
    )
    var numberOfgames = readln().ifBlank { "1" }
    while (!validateInput(numberOfgames, Regex(gamesRegex))) {
        println("Invalid input")
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )
        numberOfgames = readln().ifBlank { "1" }
    }

    println("$player1 vs $player2")
    println("$rows x $columns board")
    if (numberOfgames.toInt() == 1) {
        println("Single game")
    } else {
        println("Total $numberOfgames games")
    }

    val games = List(numberOfgames.toInt()) { Game(player1, player2, rows, columns) }

    for ((index, game) in games.withIndex()) {
        if (numberOfgames.toInt() > 1) {
            println("Game #${index + 1}")
        }

        game.play(if (index % 2 == 0) player1 else player2)

        println("Score")
        println("$player1: ${player1.getScore()} $player2: ${player2.getScore()}")
    }

    println("Game over!")
}
