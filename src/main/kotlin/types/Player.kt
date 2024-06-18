package types

import kotlin.math.max
import kotlin.math.min

data class Player(
    val city: List<Building> = listOf(WheatField, Bakery),
    val landmarks: Set<Landmark> = setOf(),
    val money: Int = 3,
) {

    init {
        if (money < 0) error("A player cannot hold less than 0 coins.")
    }

    val canRollTwoDice get() = landmarks.contains(Landmark.Station)
    val canReroll get() = landmarks.contains(Landmark.RadioTower)
    val isGoatedWithTheDoubles get() = landmarks.contains(Landmark.AmusementPark)

    val landmarksLeft = Landmark.entries.filter { it !in landmarks }

    fun findBlueAndGreen(dice: Dice) = city
        .filter { dice.result in it.range }
        .filter { it.color == BuildingColor.Blue || it.color == BuildingColor.Green }

    fun findBlue(roll: Dice) = city
        .filter { roll.result in it.range }
        .filter { it.color == BuildingColor.Blue }

    fun findRed(dice: Dice) = city
        .filter { dice.result in it.range }
        .filter { it.color == BuildingColor.Red }

    fun findPurple(dice: Dice) = city
        .filter { dice.result in it.range }
        .filterIsInstance<PurpleBuilding>()

    /**
     * A copy of this player that has paid the sum according to the game rules
     * (no negative balance and no owing)
     * @return pair of the copy and amount that was actually paid
     */
    fun pay(sum: Int): Pair<Player, Int> {
        return this.copy(money = max(money - sum, 0)) to min(sum, money)
    }

    /**
     * A copy of this player that has gained money
     */
    fun gain(sum: Int): Player = this.copy(money = money + sum)

    fun build(what: Building) = this.copy(city = city + what)
    fun build(what: Landmark) = this.copy(landmarks = landmarks + what)

}