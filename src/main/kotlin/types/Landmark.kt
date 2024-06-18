package types

enum class Landmark(val price: Int) {
	/**
	 * You can choose to throw 2 dice
	 */
	Station(4),
	/**
	 * All [Icon.Store] and [Icon.Dining] buildings bring 1 more coin when activated
	 */
	ShoppingMall(10),
	/**
	 * If you throw 2 dice and get a double, you take another turn after the end of this one.
	 * Doubles that are re-rolled with [RadioTower] do not activate this bonus.
	 * Yes, this stacks infinitely! If you're lucky.
	 */
	AmusementPark(16),
	/**
	 * You can re-roll the dice once
	 */
	RadioTower(22)
}

fun shoppingMallFormula(formula: IncomeFormula): IncomeFormula {
	return {
		formula(it) + if (it.landmarks.contains(Landmark.ShoppingMall)) 1 else 0
	}
}