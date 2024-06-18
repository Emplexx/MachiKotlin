@file:Suppress("RedundantSuspendModifier", "ComplexRedundantLet")

import types.Building
import types.Dice
import types.Game
import types.Game.Companion.arrangedCounterClockwise
import types.Landmark
import kotlin.math.min

data class RedIncome(val playerIndex: Int, val activated: List<Building>, val totalOwes: Int)

fun calcRedIncome(
	game: Game,
	diceRoll: Dice
): List<RedIncome> = game.players
	.mapIndexed { index, player -> index to player }
	.arrangedCounterClockwise(game.turn)
	.toMap()
	.minus(game.turn)
	.mapNotNull { (i, player) ->

		player
			.findRed(diceRoll)
			.let {
				if (it.isEmpty()) null
				else RedIncome(i, it, it.sumOf { it.income(player) })
			}
	}

data class RedPayout(val paidWhom: Int, val owed: Int, val paid: Int)

fun applyRedIncome(
	game: Game,
	income: List<RedIncome>
): Pair<Game, List<RedPayout>> = income
	.fold(game to emptyList()) { (state, payouts), (i, _, owes) ->

		if (owes == 0) return@fold state to payouts

		val (newCurrentPlayer, paid) = state.activePlayer.pay(owes)

		state
			.updatedPlayer(state.turn) { newCurrentPlayer }
			.updatedPlayer(i) { it.gain(paid) } to
				payouts + RedPayout(i, owes, paid)
	}



data class IndustryIncome(val playerIndex: Int, val activated: List<Building>, val total: Int)

fun calcIndustryIncome(
	game: Game,
	diceRoll: Dice
): List<IndustryIncome> = game
	.players
	.mapIndexed { i, player ->

		val isActive = i == game.turn
		val income = if (isActive) player.findBlueAndGreen(diceRoll) else player.findBlue(diceRoll)

		IndustryIncome(i, income, income.sumOf { it.income(player) })
	}

fun applyIndustryIncome(
	game: Game,
	income: List<IndustryIncome>
): Game = income.fold(game) { state, income ->
	state.updatedPlayer(income.playerIndex) {
		it.gain(income.total)
	}
}

fun calculateStadiumIncome(game: Game) = game.nonActivePlayers.sumOf { min(it.money, 2) }
fun applyStadiumIncome(game: Game, income: Int) = game
	.players
	.mapIndexed { index, player ->
		if (index == game.turn) player.gain(income)
		else player.pay(2).first
	}
	.let { game.copy(players = it) }



fun doBuildEstablishment(game: Game, selected: Building): Game = game
	.copy(store = game.store.buy(selected))
	.updatedPlayer(game.turn) {
		it
			.build(selected)
			.pay(selected.price)
			.first
	}

fun doBuildLandmark(game: Game, selected: Landmark): Game = game
	.updatedPlayer(game.turn) {
		it
			.build(selected)
			.pay(selected.price)
			.first
	}
