package dev.jx.pokedex.data

import com.apollographql.apollo.coroutines.await
import dev.jx.pokedex.GetPokemonsQuery
import dev.jx.pokedex.data.remote.PokeServiceApi
import dev.jx.pokedex.data.remote.toModel
import dev.jx.pokedex.model.Err
import dev.jx.pokedex.model.Ok
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.model.Result
import javax.inject.Inject

class PokedexRepositoryImpl @Inject constructor(private val pokeServiceAPI: PokeServiceApi) :
PokedexRepository {

    override suspend fun queryPokemonList(): Result<List<Pokemon>> {
        return try {
            val result = pokeServiceAPI.getApolloClient()
                .query(GetPokemonsQuery())
                .await().data?.pokemon_v2_pokemon

            when (result.isNullOrEmpty()) {
                true -> Err(Throwable("Empty List"))
                false -> Ok(result.map { it.toModel() })
            }
        } catch (error: Throwable) {
            Err(error)
        }

    }

}
