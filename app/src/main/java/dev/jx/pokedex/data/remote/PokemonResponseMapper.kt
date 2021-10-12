package dev.jx.pokedex.data.remote

import dev.jx.pokedex.GetPokemonsQuery.*
import dev.jx.pokedex.model.*
import dev.jx.pokedex.ui.color.Color
import dev.jx.pokedex.ui.color.Color.*
import java.lang.StringBuilder
import java.util.*

fun Pokemon_v2_pokemon.toModel(): Pokemon {
    return Pokemon(
        id = id,
        name = name.formatName(),
        baseExperience = base_experience ?: 0,
        height = height ?: 0,
        weight = weight ?: 0,
        stats = pokemon_v2_pokemonstats.toModel(),
        type = pokemon_v2_pokemontypes.toModel(),
        typeWeaknesses = pokemon_v2_pokemontypes.toTypeWeakness(),
        abilities = pokemon_v2_pokemonabilities.toModel(),
        specie = pokemon_v2_pokemonspecy?.toModel()!!,
        flavorText = pokemon_v2_pokemonspecy.getFlavorText() ?: ""
    )
}

fun String.capitalize() = replaceFirstChar { it.uppercaseChar() }

fun String.formatName(): String {
    val str = StringBuilder()
    split("-")
        .joinTo(str, " ") { name ->
            name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
    return str.toString()
}

@JvmName("toModelPokemon_v2_pokemontype")
fun List<Pokemon_v2_pokemontype>.toModel(): List<PokemonType> =
    map { it.pokemon_v2_type?.toModel() ?: return listOf(PokemonType("No type")) }

fun Pokemon_v2_type.toModel() = PokemonType(name = name.capitalize())

@JvmName("toModelPokemon_v2_pokemontypeWeakness")
private fun List<Pokemon_v2_pokemontype>.toTypeWeakness(): List<PokemonType> {
    val pokeTypes = map { it.pokemon_v2_type?.name }

    val typeEfficacies = flatMap {
        it.pokemon_v2_type?.pokemonV2TypeefficaciesByTargetTypeId ?: listOf()
    }
    val weaks = typeEfficacies
        .filter { it.damage_factor > 100 }
        .filterNot { pokeTypes.contains(it.pokemon_v2_type?.name) }
    val strengths = typeEfficacies
        .filter { it.damage_factor < 100 }
        .filterNot { pokeTypes.contains(it.pokemon_v2_type?.name) }

    val weaknesses = (weaks + strengths)
        .groupBy { it.pokemon_v2_type?.name }
        .filter {
            val dmg = it.value.fold(0) { acc, dam -> acc + dam.damage_factor }
            dmg == 200 || dmg == 400
        }
        .keys
        .map { PokemonType(it!!.capitalize()) }
    return weaknesses
}

@JvmName("toModelPokemon_v2_pokemonability")
fun List<Pokemon_v2_pokemonability>.toModel(): List<PokemonAbility>? =
    map { it.pokemon_v2_ability?.toModel() ?: return null }

fun Pokemon_v2_ability.toModel() = PokemonAbility(name = name.capitalize())

@JvmName("toModelPokemon_v2_pokemonstat")
fun List<Pokemon_v2_pokemonstat>.toModel(): PokemonStats {
    val stats = this.map {
        it.pokemon_v2_stat?.name to it.base_stat
    }.toMap()

    return PokemonStats(
        hp = stats["hp"] ?: 0,
        attack = stats["attack"] ?: 0,
        defense = stats["defense"] ?: 0,
        speed = stats["speed"] ?: 0,
        espAttack = stats["special-attack"] ?: 0,
        espDefense = stats["special-defense"] ?: 0
    )
}

fun Pokemon_v2_pokemonspecy.toModel() =
    PokemonSpecie(
        name = name.formatName(),
        color = pokemon_v2_pokemoncolor?.name?.colorFormat() ?: WHITE,
        shape = pokemon_v2_pokemonshape?.name?.formatName(),
        evolutions = pokemon_v2_evolutionchain?.toModel(),
        eggGroup = pokemon_v2_pokemonegggroups.toModel(),
        growthrate = pokemon_v2_growthrate?.name?.formatName() ?: "",
        habitat = pokemon_v2_pokemonhabitat?.name?.capitalize() ?: ""
    )

fun String.colorFormat(): Color {
    return when (this) {
        "black" -> BLACK
        "blue" -> BLUE
        "brown" -> BROWN
        "gray" -> GRAY
        "green" -> GREEN
        "pink" -> PINK
        "purple" -> PURPLE
        "red" -> RED
        "white" -> WHITE
        "yellow" -> YELLOW
        else -> BLACK
    }
}

fun Pokemon_v2_evolutionchain.toModel(): List<Evolution>? {
    return pokemon_v2_pokemonspecies.map {
        Evolution(
            id = it.evolution_chain_id ?: return null,
            name = it.name.formatName(),
            order = it.order ?: return null,
            pokemonId = it.pokemon_v2_pokemons.first().id
        )
    }
}

@JvmName("toModelPokemon_v2_pokemonegggroup")
fun List<Pokemon_v2_pokemonegggroup>.toModel(): List<PokemonEggGroup>? {
    return map {
        PokemonEggGroup(name = it.pokemon_v2_egggroup?.name?.capitalize() ?: return null)
    }
}

fun Pokemon_v2_pokemonspecy.getFlavorText(): String? {
    return pokemon_v2_pokemonspecies.firstOrNull()
        ?.pokemon_v2_pokemonspeciesflavortexts?.firstOrNull()
        ?.flavor_text?.replace("(?! |\\.)\\W".toRegex(), " ")
}
