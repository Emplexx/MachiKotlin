package types

interface Building {
    val icon: Icon
    val name: String
    val color: BuildingColor

    val range: IntRange
    val income: IncomeFormula

    val price: Int
}

sealed class Icon(open val icon: String) {

    /**
     * Correlates to the icon in the game that roughly resembles a piece of toast
     */
    data object Store : Icon("🏪")

    data object Dining : Icon("🍵")
    data object Plant : Icon("🌾")
    data object Animal : Icon("🐮")
    data object Flower : Icon("🌸")
    data object MajorEstablishment : Icon("🗼")

    /**
     * Correlates to any icon in the game that exists for aesthetic purposes and does not convey meaning.
     * Buildings or Landmarks that use this can put any emoji as the icon.
     */
    data class Other(override val icon: String) : Icon(icon)
}
enum class BuildingColor {
    Blue,
    Green,
    Red,
    Purple
}
typealias IncomeFormula = (Player) -> Int



data object WheatField : Building {

    override val icon = Icon.Plant
    override val name: String = "Wheat Field"
    override val color: BuildingColor = BuildingColor.Blue

    override val range: IntRange = 1..1
    override val income: IncomeFormula = { 1 }

    override val price: Int = 1
}

data object Ranch : Building {

    override val icon = Icon.Animal
    override val name: String = "Ranch"
    override val color: BuildingColor = BuildingColor.Blue

    override val range: IntRange = 2..2
    override val income: IncomeFormula = { 1 }

    override val price: Int = 1
}

data object Forest : Building {

    override val icon = Icon.Flower
    override val name: String = "Forest"
    override val color: BuildingColor = BuildingColor.Blue

    override val range: IntRange = 5..5
    override val income: IncomeFormula = { 1 }

    override val price: Int = 3
}

data object Mine : Building {

    override val icon = Icon.Flower
    override val name: String = "Mine"
    override val color: BuildingColor = BuildingColor.Blue

    override val range: IntRange = 9..9
    override val income: IncomeFormula = { 5 }

    override val price: Int = 6
}

data object AppleOrchard : Building {

    override val icon = Icon.Plant
    override val name: String = "Apple Orchard"
    override val color: BuildingColor = BuildingColor.Blue

    override val range: IntRange = 10..10
    override val income: IncomeFormula = { 3 }

    override val price: Int = 3
}



data object Bakery : Building {
    override val icon = Icon.Store
    override val name: String = "Bakery"
    override val color: BuildingColor = BuildingColor.Green

    override val range: IntRange = 2..3
    override val income = shoppingMallFormula { 1 }

    override val price: Int = 1
}

data object ConvenienceStore : Building {
    override val icon = Icon.Store
    override val name: String = "Convenience Store"
    override val color: BuildingColor = BuildingColor.Green

    override val range: IntRange = 4..4
    override val income = shoppingMallFormula { 3 }

    override val price: Int = 2
}

data object CheeseFactory : Building {
    override val icon = Icon.Other("🏭")
    override val name: String = "Cheese Factory"
    override val color: BuildingColor = BuildingColor.Green

    override val range: IntRange = 7..7
    override val income: IncomeFormula = { player ->
        player.city
            .filter { it.icon == Icon.Animal }
            .size * 3
    }

    override val price: Int = 5
}

data object FurnitureFactory : Building {
    override val icon = Icon.Other("🏭")
    override val name: String = "Furniture Factory"
    override val color: BuildingColor = BuildingColor.Green

    override val range: IntRange = 8..8
    override val income: IncomeFormula = { player ->
        player.city
            .filter { it.icon == Icon.Flower }
            .size * 3
    }

    override val price: Int = 3
}

data object Market : Building {
    override val icon = Icon.Other("🍎")
    override val name: String = "Fruit & Vegetable Market"
    override val color: BuildingColor = BuildingColor.Green

    override val range: IntRange = 11..12
    override val income: IncomeFormula = { player ->
        player.city
            .filter { it.icon == Icon.Plant }
            .size * 2
    }

    override val price: Int = 2
}



data object Cafe : Building {
    override val icon: Icon = Icon.Dining
    override val name: String = "Cafe"
    override val color: BuildingColor = BuildingColor.Red

    override val range: IntRange = 3..3
    override val income: IncomeFormula = shoppingMallFormula { 1 }

    override val price: Int = 2
}

data object Restaurant : Building {
    override val icon: Icon = Icon.Dining
    override val name: String = "Restaurant"
    override val color: BuildingColor = BuildingColor.Red

    override val range: IntRange = 9..10
    override val income: IncomeFormula = shoppingMallFormula { 2 }

    override val price: Int = 3
}



sealed interface PurpleBuilding : Building

/**
 * Take 2 coins from all players
 */
data object Stadium : Building, PurpleBuilding {
    override val icon: Icon = Icon.MajorEstablishment
    override val name: String = "Stadium"
    override val color: BuildingColor = BuildingColor.Purple

    override val range: IntRange = 6..6
    override val income: IncomeFormula = { 1 }

    override val price: Int = 6
}

/**
 * Take 5 coins from one player
 */
data object TVStation : Building, PurpleBuilding {
    override val icon: Icon = Icon.MajorEstablishment
    override val name: String = "TV Station"
    override val color: BuildingColor = BuildingColor.Purple

    override val range: IntRange = 6..6
    override val income: IncomeFormula = { 1 }

    override val price: Int = 7
}

/**
 * Trade one establishment with another player
 */
data object BusinessCenter : Building, PurpleBuilding {
    override val icon: Icon = Icon.MajorEstablishment
    override val name: String = "Business Center"
    override val color: BuildingColor = BuildingColor.Purple

    override val range: IntRange = 6..6
    override val income: IncomeFormula = { 1 }

    override val price: Int = 8
}