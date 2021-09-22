package dev.jx.pokedex.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jx.pokedex.data.PokedexRepository
import dev.jx.pokedex.model.Err
import dev.jx.pokedex.model.Ok
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.state.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias pokemonMutableLiveData = MutableLiveData<ViewState<List<Pokemon>>>
typealias pokemonLiveData = LiveData<ViewState<List<Pokemon>>>

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokedexRepository
) : ViewModel() {

    private val _pokemonList by lazy { pokemonMutableLiveData() }
    val pokemonList: pokemonLiveData = _pokemonList

    fun getPokemonList(){
        _pokemonList.postValue(ViewState.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.queryPokemonList()){
                is Ok -> onSuccess(response.value!!)
                is Err -> onFailure(response.error)
            }
        }
    }

    private fun onSuccess(value: List<Pokemon>) {
        _pokemonList.postValue(ViewState.Success(value))
    }

    private fun onFailure(error: Throwable?) {
        _pokemonList.postValue(ViewState.Error(error?.message))
    }

}