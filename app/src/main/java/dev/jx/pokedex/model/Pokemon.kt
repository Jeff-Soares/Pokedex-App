package dev.jx.pokedex.model

import android.os.Parcelable
import dev.jx.pokedex.ui.color.Color
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val stats: PokemonStats,
    val type: List<PokemonType>,
    val typeWeaknesses: List<PokemonType>,
    val abilities: List<PokemonAbility>?,
    val specie: PokemonSpecie,
    val flavorText: String,
    val formattedId: String = id.toString().padStart(3, '0')
) : Parcelable

@Parcelize
data class PokemonAbility(
    val name: String
) : Parcelable

@Parcelize
data class PokemonType(
    val name: String
) : Parcelable

@Parcelize
data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val espAttack: Int,
    val espDefense: Int,
    val totalPoints: Int = hp + attack + defense + speed + espAttack + espDefense
) : Parcelable

@Parcelize
data class PokemonSpecie(
    val name: String,
    val color: Color,
    val shape: String?,
    val evolutions: List<Evolution>?,
    val eggGroup: List<PokemonEggGroup>?,
    val growthrate: String,
    val habitat: String
) : Parcelable

@Parcelize
data class Evolution(
    val id: Int,
    val name: String,
    val order: Int,
    val pokemonId: Int
) : Parcelable

@Parcelize
data class PokemonEggGroup(
    val name: String
) : Parcelable
