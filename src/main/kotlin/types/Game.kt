package types

data class Game(
	val players: List<Player>,
	val store: Store = Store(),
	val turn: Int = 0,
) {

	init {
		require(players.size in 2..5)
		require(turn in players.indices)
	}

	val activePlayer = players[turn]
	val nonActivePlayers = players.filterIndexed { i, _ -> i != turn }

	val isWon = players.any { it.landmarks.size == 4 }

	fun nextTurn(): Game {
		val next = if (turn == players.lastIndex) 0 else turn + 1
		return this.copy(turn = next)
	}

	fun updatedPlayer(i: Int, update: (Player) -> Player): Game {
		return if (i !in players.indices) this else this.copy(
			players = players.toMutableList().apply {
				this[i] = update(this[i])
			}
		)
	}

	companion object {

		fun forPlayerCount(count: Int) = Game(
			players = List(count) { Player() }
		)

		fun <T> List<T>.arrangedCounterClockwise(from: Int) =
			subList(0, from).reversed() + subList(from, this.size).reversed()
	}
}
