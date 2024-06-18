@file:Suppress("RedundantSuspendModifier")

import types.Building
import types.Dice
import types.Player

suspend fun ask(q: String, vararg options: String): Int {
	println(q)
	options.forEachIndexed { index, s ->
		println("[${index + 1}] $s")
	}
	print("Your choice -> ")
	val num = readlnOrNull()?.toIntOrNull() ?: 0
	return if (num < 1 || num > options.size) {
		println("Invalid selection.")
		ask(q, *options)
	} else num
}

suspend fun askPlayerCount(): Int {
	println("How many players? (2 to 5)")
	print("Your choice -> ")
	val num = readlnOrNull()?.toIntOrNull() ?: 0
	return if (num !in 2..5) {
		println("Invalid selection.")
		askPlayerCount()
	} else num
}

suspend fun List<Building>.printActivated() {
	this
		.sortedBy { it.range.first }
		.groupBy {
			it::class.simpleName
		}
		.forEach {
			println("${it.value.first()::class.simpleName} x${it.value.size} | ") // ${it.value.sumOf { it.income(player) }}
		}
}

suspend fun rollOneOrTwoDice(activePlayer: Player): Dice =
	when {
		activePlayer.canRollTwoDice -> {
			val howMany = ask(
				"[Station] allows you to roll 2 dice.",
				"Roll 1 die", "Roll 2 dice")

			if (howMany == 2) Dice.throwTwo() else Dice.throwOne()
		}
		else -> Dice.throwOne()
	}

suspend fun keepOrReroll(activePlayer: Player, firstRoll: Dice): Pair<Boolean, Dice> =
	when {
		activePlayer.canReroll -> {
			val reroll = ask(
				"[Radio Tower] allows you to re-roll your dice.",
				"Re-roll",
				"Go with the result"
			)

			when (reroll) {
				1 -> true to rollOneOrTwoDice(activePlayer)
				else -> false to firstRoll
			}
		}
		else -> false to firstRoll
	}
