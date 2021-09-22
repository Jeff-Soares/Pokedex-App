package dev.jx.pokedex.model

import dev.jx.pokedex.ui.color.Color

data class Pokemon(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val stats: PokemonStats,
    val type: List<PokemonType>,
    val abilities: List<PokemonAbility>?,
    val specie: PokemonSpecie,
    val flavorText: String,
    val formattedId: String = id.toString().padStart(3, '0')
)

data class PokemonAbility(
    val name: String
)

data class PokemonType(
    val name: String
)

data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val espAttack: Int,
    val espDefense: Int,
    val totalPoints: Int = hp + attack + defense + speed + espAttack + espDefense
)

data class PokemonSpecie(
    val name: String,
    val color: Color,
    val shape: String?,
    val evolutions: List<Evolution>?,
    val eggGroup: List<PokemonEggGroup>?
)

data class Evolution(
    val id: Int,
    val name: String,
    val order: Int
)

data class PokemonEggGroup(
    val name: String
)
