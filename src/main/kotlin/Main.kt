import types.Game
import kotlin.system.exitProcess

suspend fun main() {
    println("Welcome to Machi Koro!")
    val choice = ask("What do you want to do?", "Play", "Exit")
    if (choice == 2) exitProcess(0)

    val playerCount = askPlayerCount()
    val game = Game.forPlayerCount(playerCount)
    val won = continueGame(game)

    val winner = won.players.indexOfFirst { it.landmarks.size == 4 }
    println("Player $winner won!")
    println("The end")
}

suspend fun continueGame(game: Game): Game {
    return if (game.isWon) game
    else {
        turnStart(game)

        val (afterIncome, canTakeAnotherTurn) = incomePhase(game)
        val afterBuilding = buildingPhase(afterIncome)

        val newGame = if (canTakeAnotherTurn) afterBuilding else afterBuilding.nextTurn()

        if (canTakeAnotherTurn) println("[Amusement Park] Player ${game.turn} rolled doubles and will take another turn")

        continueGame(newGame)
    }
}

suspend fun turnStart(game: Game) {

    val turnOpt = ask("\nPlayer ${game.turn}'s turn",
        "Continue",
        "Game status")

    when (turnOpt) {
        1 -> return
        2 -> {
            println("--- Game status ---")
            game.players.forEachIndexed { i, player ->
                println("P$i: (${player.money}) coins | ${player.city.size} buildings")
                player.city.forEach {
                    println("${it::class.simpleName}")
                }
            }
            println("--- --- --- --- ---")

            turnStart(game)
        }
    }
}
