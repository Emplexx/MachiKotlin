import types.Game

suspend fun buildingPhase(game: Game): Game {
	val buildChoice = ask("\nPlayer ${game.turn}'s turn: Build phase",
		"Build an establishment",
		"Build a landmark",
		"Pass")

	return when (buildChoice) {
		1 -> game.askBuildEstablishment()
		2 -> game.askBuildLandmark()
		else -> game
	}
}

suspend fun Game.askBuildEstablishment(): Game {
	val game = this

	val buildings = game.store.listAvailableFor(game.activePlayer)
		.sortedBy { it.range.first }
		.mapIndexed { i, building -> i + 1 to building }
		.toMap()
	val options = buildings.values
		.map { "¤${it.price} ${it.icon.icon} ${it.name}" }
		.plus("Go back")
		.toTypedArray()
	val selected = ask(
		"What would you like to build? You have ¤${game.activePlayer.money} to spend",
		*options)
		.let { buildings[it] }

	return if (selected == null) buildingPhase(game)
	else doBuildEstablishment(game, selected)
}

suspend fun Game.askBuildLandmark(): Game {
	val game = this

	val landmarks = game.activePlayer.landmarksLeft
		.filter { it.price <= game.activePlayer.money }
		.sortedBy { it.price }
		.mapIndexed { i, landmark -> i + 1 to landmark }
		.toMap()
	val options = landmarks.values
		.map { "¤${it.price} ${it.name}" }
		.plus("Go back")
		.toTypedArray()
	val selected = ask(
		"What would you like to build? You have ¤${game.activePlayer.money} to spend",
		*options)
		.let { landmarks[it] }

	return if (selected == null) buildingPhase(game)
	else doBuildLandmark(game, selected)
}