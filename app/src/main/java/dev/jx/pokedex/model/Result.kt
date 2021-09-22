package dev.jx.pokedex.model

import android.util.Log
import com.apollographql.apollo.api.Response
import dev.jx.pokedex.GetPokemonsQuery

sealed class Result<out V>(val value: V? = null, val error: Throwable? = null)

class Ok<out V>(value: V) : Result<V>(value)
class Err(error: Throwable) : Result<Nothing>(error = error)

suspend fun <T> getResult(request: suspend () -> T): Result<T> {
    runCatching {
        request()
    }.onSuccess {
        return Ok(it)
    }.onFailure {
        return Err(it)
    }
    return Err(Throwable("Request error in function 'getResult'"))
}
