package dev.jx.pokedex.model

data class FilterOptions(
    var orderBy: OrderBy,
    var types: List<Type>,
    var height: IntRange,
    var weight: IntRange,
    var weakness: List<Type>
) {
    enum class Type(val type: String) {
        NORMAL("Normal"),
        FIGHTING("Fighting"),
        FLYING("Flying"),
        POISON("Poison"),
        GROUND("Ground"),
        ROCK("Rock"),
        BUG("Bug"),
        GHOST("Ghost"),
        STEEL("Steel"),
        FIRE("Fire"),
        WATER("Water"),
        GRASS("Grass"),
        ELECTRIC("Electric"),
        PSYCHIC("Psychic"),
        DARK("Dark"),
        ICE("Ice"),
        DRAGON("Dragon"),
        FAIRY("Fairy");

        companion object {
            fun getType(str: String) = values().first { it.type == str }
        }
    }

    enum class OrderBy(val value: String, val comparator: Comparator<Pokemon>) {
        NUMBER_ASC("Number", compareBy<Pokemon> { it.id }),
        NUMBER_DESC("Number - Desc", compareByDescending<Pokemon> { it.id }),
        A_Z("Name - A-Z", compareBy<Pokemon> { it.name }),
        Z_A("Name - Z-A", compareByDescending<Pokemon> { it.name }),
        HEIGHT("Height", compareBy<Pokemon> { it.height }),
        HEIGHT_DESC("Height - Desc", compareByDescending<Pokemon> { it.height }),
        WEIGHT("Weight", compareBy<Pokemon> { it.weight }),
        WEIGHT_DESC("Weight - Desc", compareByDescending<Pokemon> { it.weight }),
        STATS_ASC("Total stats", compareBy<Pokemon> { it.stats.totalPoints }),
        STATS_DESC("Total stats - Desc", compareByDescending<Pokemon> { it.stats.totalPoints });

        override fun toString() = value
    }
}
