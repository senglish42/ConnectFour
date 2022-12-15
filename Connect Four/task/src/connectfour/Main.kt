package connectfour

class Game(private val name: Array<String>, numArr: List<Int?>, private val amount: Int) {
    init { println("${name[0]} VS ${name[1]}\n${numArr[0]} X ${numArr[1]} board\n${ if (amount > 1) "Total $amount games" else "" }") }
    private val height: Int = numArr[0]!!
    private val width: Int = numArr[1]!!
    private val square: Int = height * width
    private val fieldMap = mapOf('o' to name[0], '*' to name[1])
    private val scoreMap = mutableMapOf(name[0] to 0, name[1] to 0)
    private var field = emptyArray<MutableList<Char>>()
    private var isFinished = false
    private var row = 0
    private var turn = emptyMap<Char, String>()
    fun run() {
        Loop@for (i in 1 .. amount) {
            isFinished = false
            field = Array(height) { MutableList(width) {' '} }
            println(if (amount > 1) "Game #$i" else "Single game")
            show()
            var count = 0
            while (!isFinished && count < square) {
                turn = if (count % 2 == 0) fieldMap.filter { it.key == if (i % 2 == 1) 'o' else '*' }
                        else fieldMap.filter { it.key == if (i % 2 == 1) '*' else 'o' }
                println("${turn.values.toList()[0]}'s turn")
                val str = readln()
                if (str == "end") break@Loop
                val x = str.toIntOrNull()
                if (x == null) { println("Incorrect column number") ; continue }
                if (x !in 1..width) { println("The column number is out of range (1 - $width)") ; continue }
                if (field[0][x - 1] != ' ') { println("Column $x is full") ; continue }
                ++count
                for (y in height downTo 1) {
                    if (field[y - 1][x - 1] == ' ') {
                        field[y - 1][x - 1] = turn.keys.toList()[0]
                        if (count > 6) checkRow(y, x, turn.keys.toList()[0])
                        break
                    }
                }
                show()
            }
            if (isFinished) {
                println("Player ${turn.values.toList()[0]} won")
                scoreMap[turn.values.toList()[0]] = scoreMap[turn.values.toList()[0]]!! + 2
            }
            if (count == square) {
                println("It is a draw")
                scoreMap[fieldMap.values.toList()[0]] = scoreMap[fieldMap.values.toList()[0]]!! + 1
                scoreMap[fieldMap.values.toList()[1]] = scoreMap[fieldMap.values.toList()[1]]!! + 1
            }
            println("Score")
            for (num in 0..1) print("${scoreMap.keys.toList()[num]}: ${scoreMap.values.toList()[num]} ")
            println()
        }
        println("Game over!")
    }

    private fun checkRow(y: Int, x: Int, ch: Char) {
        row = 0
        for (a in height downTo 1) {
            if (field[a - 1][x - 1] == ch) ++row
            else row = 0
            if (row == 4) { isFinished = true ; return }
        }
        row = 0
        for (b in width downTo 1) {
            if (field[y - 1][b - 1] == ch) ++row
            else row = 0
            if (row == 4) { isFinished = true ; return }
        }
        row = 0
        var a = y
        var b = x
        while (a < height && b < width) { ++a; ++b }
        while (a > 0 && b > 0) {
            if (field[a-- - 1][b-- - 1] == ch) ++row
            else row = 0
            if (row == 4) { isFinished = true ; return }
        }
        row = 0
        a = y
        b = x
        while (a > 1 && b < width) { --a; ++b }
        while (a <= height && b > 0) {
            if (field[a++ - 1][b-- - 1] == ch) ++row
            else row = 0
            if (row == 4) { isFinished = true ; return }
        }
    }

    private fun show() {
        for (i in 1..width) print(" $i")
        println()
        for (a in 1..height) {
            for (b in 1..width)
                print("║${field[a - 1][b - 1]}")
            println("║")
        }
        for (i in 1..width) {
            if (i == 1) print("╚═")
            else print("╩═")
        }
        println("╝")
    }
}

fun setNumArr(): List<Int?> {
    var field: String
    var numArr: List<Int?> = arrayListOf(6, 7)
    val reg = "[5-9][xX][5-9]".toRegex()
    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        field = readln().filterNot { it.isWhitespace() }
        if (field.isEmpty()) break
        if (field.length < 3) { println("Invalid input"); continue }
        val ch = field.filter { it.isLetter()}
        numArr = field.split(field.filter { it == ch[0] }).map { it.toIntOrNull() }
        if (field.matches(reg)) break
        if (ch.length > 1 || ch.matches("[^xX]".toRegex())) { println("Invalid input"); continue }
        if (numArr.size != 2) println("Invalid input")
        val fieldArr = listOf("row", "column")
        for (i in 0..1) { if (numArr[i] !in 5..9) println("Board ${fieldArr[i]}s should be from 5 to 9") }
    }
    return numArr
}

fun setName(): Array<String> {
    println("Connect Four")
    val pos = listOf("First", "Second")
    val name: Array<String> = Array(2){""}
    for (i in 0..1) {
        println("${pos[i]} player's name:")
        name[i] = readln()
    }
    return name
}

fun setAmount(): Int {
    println("Do you want to play single or multiple games?\n" +
            "For a single game, input 1 or press Enter\n" +
            "Input a number of games:")
    val str = readln()
    if (str.isEmpty()) return 1
    val amount = str.toIntOrNull()
    if (amount == null || amount < 1 ) { println("Invalid input"); return setAmount() }
    return amount
}

fun main() = Game(setName(), setNumArr(), setAmount()).run()