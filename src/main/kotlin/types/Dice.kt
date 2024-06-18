package types

sealed interface Dice {

	data class One(val first: Int) : Dice {
		init {
			require(first in 1..6)
		}
		override fun toString(): String {
			return formatDieFace(this.first) ?: error("This never happens")
		}
	}

	data class Two(
		val first: Int,
		val second: Int
	) : Dice {
		init {
			require(first in 1..6)
			require(second in 1..6)
		}
		override fun toString(): String {
			val first = formatDieFace(first) ?: error("This never happens")
			val second = formatDieFace(second) ?: error("This never happens")
			return "$first $second"
		}
	}

	val result: Int get() =
		when (this) {
			is One -> this.first
			is Two -> this.first + this.second
		}

	val isDouble get() = this is Two && first == second

	companion object {

		private fun formatDieFace(int: Int): String? {
			return when (int) {
				1 -> "⚀"
				2 -> "⚁"
				3 -> "⚂"
				4 -> "⚃"
				5 -> "⚄"
				6 -> "⚅"
				else -> null
			}
		}

		suspend fun throwOne() = One((1..6).random())
		suspend fun throwTwo() = Two((1..6).random(), (1..6).random())
	}
}

