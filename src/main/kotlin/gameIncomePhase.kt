@file:Suppress("ComplexRedundantLet")

import types.*
import kotlin.math.min

data class GameWithTurnDecision(val game: Game, val canTakeAnotherTurn: Boolean)

// Earning income (from rulebook):
//	• It is possible that multiple types of Establishments are activated by the same
//	  die roll, in this case the Establishments are activated in the following order:
//	1) Restaurants (Red)
//	2) Secondary Industry (Green) and Primary Industry (Blue)
//	3) Major Establishments (Purple)
suspend fun incomePhase(
	gameState: Game
): GameWithTurnDecision {

	println("\nPlayer ${gameState.turn}'s turn: Income phase")
	val activePlayer = gameState.activePlayer

	val roll = rollOneOrTwoDice(activePlayer)
		.let {
			println("--- Player ${gameState.turn} rolls [${it.result}] ($it) ---")
			keepOrReroll(activePlayer, it)
		}
		.let { (didReroll, it) ->
			if (didReroll) println("--- Player ${gameState.turn} re-rolls [${it.result}] ($it) ---")
			it
		}

	val canTakeAnotherTurn = activePlayer.isGoatedWithTheDoubles && roll.isDouble

	val newGameState = gameState
		.let { game ->
			val income = calcRedIncome(game, roll).onEach { it.activated.printActivated() }
			applyRedIncome(game, income)
				.also { (_, payouts) ->
					payouts.onEach { println("P${game.turn} pays P${it.paidWhom} ${it.paid}/${it.owed} coins.") }
				}
				.first
		}
		.let { game ->
			val income = calcIndustryIncome(game, roll).onEach { it.activated.printActivated() }
			applyIndustryIncome(game, income)
		}
		.let { game ->
			tryPurpleBuildings(game, roll)
		}

	return GameWithTurnDecision(newGameState, canTakeAnotherTurn)
}

suspend fun tryPurpleBuildings(
	game: Game,
	diceRoll: Dice
): Game {
	return game.activePlayer
		.findPurple(diceRoll)
		.fold(game) { state, building ->
			when (building) {
				Stadium -> askStadium(state)
				TVStation -> askTvStation(state)
				BusinessCenter -> askBusinessCenter(state)
			}
		}
}

suspend fun askStadium(game: Game): Game {
	val willGain = calculateStadiumIncome(game)
	val choice = ask(
		"[${Stadium.name}] allows you to take ¤2 from each player.",
		"Take (Gain ¤$willGain)", "Pass"
	)
	return when (choice) {
		1 -> applyStadiumIncome(game, willGain)
		else -> game
	}
}

suspend fun askTvStation(game: Game): Game {
	val choice = ask(
		"[${TVStation.name}] allows you to take ¤5 from a single player.",
		"Take ¤5", "Pass"
	)
	if (choice == 2) return game

	val takeFrom = game.players
		.mapIndexed { index, player ->
			if (index == game.turn) null
			else index to "P$index - Balance: ¤${player.money} - Can take: ¤${min(player.money, 5)}"
		}
		.filterNotNull()
		.mapIndexed { index, pair -> index + 1 to pair }
		.toMap()

	val options = takeFrom.values.map { it.second }.toTypedArray()

	val choice2 = ask("Who to take from?", *options)
	val playerIndex = takeFrom[choice2]!!.first
	val gain = game.players[playerIndex].pay(5).second

	val newPlayers = game.players.mapIndexed { index, player ->
		when (index) {
			game.turn -> player.gain(gain)
			playerIndex -> player.pay(5).first
			else -> player
		}
	}

	return game.copy(players = newPlayers)
}

suspend fun askBusinessCenter(game: Game): Game {
	val choice = ask(
		"[${BusinessCenter.name}] allows you to exchange an establishment with another player.",
		"Start exchange", "Pass"
	)
	if (choice == 2) return game

	TODO()
}
