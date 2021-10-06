package dev.jx.pokedex.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.state.ViewState

typealias PokemonMutableLiveData = MutableLiveData<ViewState<Pokemon>>
typealias PokemonLiveData = LiveData<ViewState<Pokemon>>

class PokemonDetailViewModel : ViewModel() {

    var pokemons: List<Pokemon> = listOf()

    private val _pokemonSelected by lazy { PokemonMutableLiveData() }
    val pokemonSelected: PokemonLiveData get() = _pokemonSelected

    fun onPokemonChange(pos: Int) {
        if (pokemons.isEmpty()) return
        _pokemonSelected.value = ViewState.Success(pokemons[pos])
    }

    fun setPokemonList(list: List<Pokemon>){
        pokemons = list
    }

}