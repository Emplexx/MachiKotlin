package types

data class Store(
	val pool: Map<Building, Int> = baseGamePool
) {

	/**
	 * Returns a copy of this store with the bought building no longer in the pool
	 */
	fun buy(what: Building) =
		copy(
			pool = pool
				.mapValues { (building, left) ->
					if (building == what) left - 1 else left
				}
				.filterValues { it > 0 }
		)

	/**
	 * Returns a list of all the buildings a player is able to build
	 */
	fun listAvailableFor(player: Player) = pool.keys.toList()
		.filter { it.price <= player.money }
		.filter {
			it.color != BuildingColor.Purple || player.city.contains(it).not()
		}

	companion object {
		val baseGamePool = mapOf(
			WheatField to 6,
			Ranch to 6,
			Forest to 6,
			Mine to 6,
			AppleOrchard to 6,
			Bakery to 6,
			ConvenienceStore to 6,
			CheeseFactory to 6,
			FurnitureFactory to 6,
			Market to 6,
			Cafe to 6,
			Restaurant to 6,
			Stadium to 5,
			TVStation to 5,
//			BusinessCenter to 5,
		)
	}
}