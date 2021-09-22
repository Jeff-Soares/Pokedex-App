package dev.jx.pokedex.data

import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.model.Result

interface PokedexRepository {

    suspend fun queryPokemonList(): Result<List<Pokemon>>

}